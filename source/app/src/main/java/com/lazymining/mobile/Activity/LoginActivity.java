package com.lazymining.mobile.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lazymining.mobile.LocalData.SharePreference;
import com.lazymining.mobile.R;
import com.lazymining.mobile.Service.ApiService;
import com.lazymining.mobile.Utility.Enum;
import com.lazymining.mobile.Utility.Utility;

import org.ipify.Ipify;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,TextWatcher {
    private TextView tvSignup;
    private TextView tvForgetPw;
    private TextView tvErrEmail;
    private TextView tvErrPw;
    private LinearLayout lnMain;
    private Button btnLogin;
    private EditText etEmail;
    private EditText etPw;
    private Boolean succEmail = false;
    private Boolean succPw = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utility.hideTilteBar(this);
        Intent intent = getIntent();
        if(intent != null && intent.getData() != null){
            Uri uri = intent.getData();
            String token = String.valueOf(uri.getQueryParameters("token"));
            token = token.replace("[","");
            token = token.replace("]","");
            if(token.length() > 0){
                goToChangePassword(token);
            }
        }

        initUi();
        Utility.setUnderlineText("TẠO TÀI KHOẢN MỚI", tvSignup);
        Utility.setUnderlineText("Quên mật khẩu?", tvForgetPw);
        checkAlreadyLogin();

    }

    private void goToChangePassword(String token){
        Intent intent = new Intent(getBaseContext(), ChangePasswordActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    private void initUi(){
        tvSignup = (TextView) findViewById(R.id.tv_sign_up);
        tvErrEmail = (TextView) findViewById(R.id.tv_err_email);
        tvErrPw = (TextView) findViewById(R.id.tv_err_pw);
        lnMain = (LinearLayout) findViewById(R.id.ln_main);
        btnLogin = (Button) findViewById(R.id.btn_log_in);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPw = (EditText) findViewById(R.id.et_pw);
        tvForgetPw = (TextView) findViewById(R.id.tv_forget_pw);
//        etEmail.setOnFocusChangeListener(this);
        lnMain.setOnClickListener(this);
        tvForgetPw.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        etEmail.requestFocus();
        etEmail.addTextChangedListener(this);
        etPw.addTextChangedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void signUp(){
        Intent newIntent = new Intent(this,SignUpActivity.class);
        startActivity(newIntent);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.ln_main:
                Utility.hideKeyboard(this);
                checkEmail();
                checkPassword();
                break;
            case R.id.btn_log_in:
                logIn();
                break;
            case R.id.tv_sign_up:
                signUp();
                break;
            case R.id.tv_forget_pw:
                gotoForgetPw();
                break;
        }
    }

    private void gotoForgetPw(){
        Intent newIntent = new Intent(this,ForgetPasswordActivity.class);
        startActivity(newIntent);
    }

    private void checkAlreadyLogin(){
        String authString = SharePreference.getAuth(this);
        if(authString != null && authString.length() > 0){
            goToMainpage();
        }
    }
    private void logIn(){
        final String email = etEmail.getText().toString();
        final String pw = etPw.getText().toString();
        final String appVersion = Utility.getAppVersionName(this);
        final String platform = Utility.PLATFORM_NAME;
        final String model = Utility.getDeviceManufacturerAndModel();
        final String version = Utility.getSDKVersionNumber();
        final String uuid = Utility.getDeviceId(this);


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    final String ip = Ipify.getPublicIp();

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            ApiService.login(email,pw,appVersion,platform,model,version,uuid,ip,new ApiService.Callback() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                    try {
                                        String responseCode = response.getString("response_code");
                                        if(responseCode.equals(Enum.SUCC_LOGIN)){
                                            JSONObject obj = response.getJSONObject("data");
                                            String token = obj.getString("token");
                                            String email = obj.getString("email");
                                            SharePreference.saveAuth(LoginActivity.this,obj);
                                            goToMainpage();
                                        }else {
                                            Utility.showToast(LoginActivity.this,"Sai tài khoản hoặc mật khẩu");
                                        }

                                    }catch (JSONException e){
                                        Utility.showToast(LoginActivity.this,"Không thể parse Json");
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                    Utility.showToast(LoginActivity.this,"Lỗi mạng");
                                }
                            });
                        }
                    };
                    mainHandler.post(myRunnable);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private void goToMainpage(){
        Intent newIntent = new Intent(this,MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }

    private void checkEmail(){
        if(etEmail.getText().length() == 0 || !Utility.isEmailValid(etEmail.getText().toString())){
            tvErrEmail.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        }else {
            succEmail = true;
            tvErrEmail.setVisibility(View.GONE);
            if(succPw){
                btnLogin.setEnabled(true);
            }else {
                btnLogin.setEnabled(false);
            }
        }
    }
    private void checkPassword(){



        if(etPw.getText().length() == 0 ){
            tvErrPw.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        }else {
            succPw = true;
            tvErrPw.setVisibility(View.GONE);
            if(succEmail){
                btnLogin.setEnabled(true);
            }else {
                btnLogin.setEnabled(false);
            }
        }
    }
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch(view.getId()) {
            case R.id.et_email:
                if(!hasFocus){
                    checkEmail();
                }

                break;
            case R.id.et_pw:
                if(!hasFocus) {
                    checkPassword();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(etEmail.isFocused()){
            checkEmail();
        }else if(etPw.isFocused()){
            checkPassword();
        }
    }
}
