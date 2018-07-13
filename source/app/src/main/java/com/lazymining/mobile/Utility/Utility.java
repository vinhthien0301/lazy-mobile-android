package com.lazymining.mobile.Utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.device.DeviceName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by doanngocduc on 1/23/18.
 */

public class Utility {

    public static String PLATFORM_NAME = "ANDROID";
    public interface DeviceInfoCallback {
        void onFinished(DeviceName.DeviceInfo info, Exception error);
    }
    public static void hideTilteBar(AppCompatActivity activity){
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.hide();
    }
    public static void setUnderlineText(String text,TextView tv){
        String mystring = new String(text);
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        tv.setText(content);
    }
    public static void hideKeyboard(AppCompatActivity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static void showToast(Activity activity,String text){
        Toast.makeText(activity, text,
                Toast.LENGTH_LONG).show();
    }

    public static int getColorFromResource(Context mContext,int id){
        int backgroundColor = ContextCompat.getColor(mContext, id);
        return backgroundColor;
    }

    public static String getDeviceId(Activity activity){
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return telephonyManager.getDeviceId();
    }



    public static void getDeviceInfo(Activity activity, final DeviceInfoCallback callback ){
        DeviceName.with(activity).request(new DeviceName.Callback() {

            @Override public void onFinished(DeviceName.DeviceInfo info, Exception error) {

                callback.onFinished(info,error);
//                String manufacturer = info.manufacturer;  // "Samsung"
//                String name = info.marketName;            // "Galaxy S7 Edge"
//                String model = info.model;                // "SAMSUNG-SM-G935A"
//                String codename = info.codename;          // "hero2lte"
//                String deviceName = info.getName();       // "Galaxy S7 Edge"
                // FYI: We are on the UI thread.
            }
        });
    }

    public static String getAndroidVersion(){
            String release = Build.VERSION.RELEASE;
            return release;
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static String getDeviceSerialNumber(){
        return Build.SERIAL;
    }

    public static String getSDKVersionNumber(){
        return android.os.Build.VERSION.SDK;
    }

    public static String getDeviceManufacturerAndModel(){
        return android.os.Build.MANUFACTURER + " | " + android.os.Build.MODEL;
    }

    public static String getAppVersionName(Activity activity){
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void showAlert(Activity activity,String title,String content){
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
