package com.lazymining.mobile.Fragment;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lazymining.mobile.Activity.LoginActivity;
import com.lazymining.mobile.Activity.MainActivity;
import com.lazymining.mobile.LocalData.SharePreference;
import com.lazymining.mobile.R;
import com.lazymining.mobile.Service.ApiService;
import com.lazymining.mobile.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Fragment class for each nav menu item
 */
public class ContactFragment extends Fragment implements View.OnClickListener {

    private TextView tvEmail;
    private TextView tvFb;
    private TextView tvVersion;
    private TextView tvHello;
    private Button btnSignOut;
    public static Fragment newInstance() {
        Fragment frag = new ContactFragment();
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setUnderlineText("vinh.thien0301@gmail.com",tvEmail);
        setUnderlineText("Lazy Mining",tvFb);
        String version = Utility.getAppVersionName(getActivity());
        tvVersion.setText(tvVersion.getText().toString() + version);
        try {
            JSONObject obj = new JSONObject(SharePreference.getAuth(getActivity()));
            String email = obj.getString("email");
            tvHello.setText("Chào "+email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initUI(View view){
        tvEmail = view.findViewById(R.id.tv_email);
        tvEmail.setOnClickListener(this);
        tvFb = view.findViewById(R.id.tv_facebook);
        tvFb.setOnClickListener(this);
        tvVersion = view.findViewById(R.id.tv_version);
        tvHello = view.findViewById(R.id.tv_hello);
        btnSignOut = view.findViewById(R.id.btn_signout);
        btnSignOut.setOnClickListener(this);
    }

    private void sendMail(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"vinh.thien0301@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Lazy Mining | Suggestion");
        i.putExtra(Intent.EXTRA_TEXT   , "Send from my Android");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    private void setUnderlineText(String text,TextView tv){
        String mystring=new String(text);
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        tv.setText(content);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_signout:
                signout();
                break;
            case R.id.tv_email:
                sendMail();
                break;
            case R.id.tv_facebook:
                openFaceBook();
                break;
        }
    }

    private String getFacebookUrl(){
        String FACEBOOK_URL = "https://www.facebook.com/Lazy-Mining-797049757131819";
        String FACEBOOK_PAGE_ID = "Lazy Mining";
        PackageManager packageManager = getActivity().getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    private void openFaceBook(){
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookUrl();
        facebookIntent.setData(Uri.parse(facebookUrl));
        getActivity().startActivity(facebookIntent);
    }
    private void signout(){
        String authString = SharePreference.getAuth(getActivity());
        try {
            JSONObject obj = new JSONObject(authString);
            String token = obj.getString("token");
            if(token!= null){
                ApiService.logout(token, new ApiService.Callback() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        SharePreference.removeAuth(getActivity());
                        MainActivity activity = (MainActivity) getActivity();
                        activity.signout();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        Utility.showToast(getActivity(),"Lỗi mạng");
                    }
                });
            }
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON");
        }
    }
    private void goToLogin(){
        Intent newIntent = new Intent(getActivity(),LoginActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }
}
