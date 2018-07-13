package com.lazymining.mobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
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
import com.lazymining.mobile.Utility.Utility;

import org.ipify.Ipify;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by doanngocduc on 3/8/18.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {
    private TextView tvSignin, tvErrEmail, tvErrPw, tvErrRp;
    private EditText etEmail, etPw, etRp;
    private Button btnSignUp;
    private LinearLayout lnMain;
    private Boolean succEmail = false;
    private Boolean succPw = false;
    private Boolean succRp = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Utility.hideTilteBar(this);
        initUI();
        Utility.setUnderlineText("ĐĂNG NHẬP", tvSignin);

    }

    private void initUI() {
        tvSignin = (TextView) findViewById(R.id.tv_sign_in);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPw = (EditText) findViewById(R.id.et_pw);
        etRp = (EditText) findViewById(R.id.et_rp);
        tvErrEmail = (TextView) findViewById(R.id.tv_err_email);
        tvErrPw = (TextView) findViewById(R.id.tv_err_pw);
        tvErrRp = (TextView) findViewById(R.id.tv_err_rp);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);
        lnMain = (LinearLayout) findViewById(R.id.ln_main);
        lnMain.setOnClickListener(this);
        etEmail.addTextChangedListener(this);
        etPw.addTextChangedListener(this);
        etRp.addTextChangedListener(this);
        tvSignin.setOnClickListener(this);
    }

    private void checkEmail() {
        if (etEmail.getText().length() == 0 || !Utility.isEmailValid(etEmail.getText().toString())) {
            tvErrEmail.setVisibility(View.VISIBLE);
            btnSignUp.setEnabled(false);
        } else {
            succEmail = true;
            tvErrEmail.setVisibility(View.GONE);
            if (succPw && succRp) {
                btnSignUp.setEnabled(true);
            } else {
                btnSignUp.setEnabled(false);
            }
        }
    }

    private void checkRepeat() {
        if (etRp.getText().length() == 0) {
            succRp = false;
            tvErrRp.setVisibility(View.VISIBLE);
            tvErrRp.setText("Mật khẩu nhập lại không được trống");
            btnSignUp.setEnabled(false);
        } else if (!etRp.getText().toString().equals(etPw.getText().toString())) {
            succRp = false;
            tvErrRp.setVisibility(View.VISIBLE);
            tvErrRp.setText("Mật khẩu nhập lại không trùng khớp");
            btnSignUp.setEnabled(false);
        } else {
            succRp = true;
            tvErrRp.setVisibility(View.GONE);
            if (succEmail && succPw) {
                btnSignUp.setEnabled(true);
            } else {
                btnSignUp.setEnabled(false);
            }
        }
    }

    private void checkPassword() {
        if (etPw.getText().length() == 0) {
            tvErrPw.setVisibility(View.VISIBLE);
            btnSignUp.setEnabled(false);
        } else {
            succPw = true;
            tvErrPw.setVisibility(View.GONE);
            if (succEmail && succRp) {
                btnSignUp.setEnabled(true);
            } else {
                btnSignUp.setEnabled(false);
            }
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
        if (etEmail.isFocused()) {
            checkEmail();
        } else if (etPw.isFocused()) {
            checkPassword();
        } else {
            checkRepeat();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ln_main:
                Utility.hideKeyboard(this);
                checkEmail();
                checkPassword();
                checkRepeat();
                break;
            case R.id.btn_sign_up:
                signup();
                break;
            case R.id.tv_sign_in:
                goToSignIn();
                break;
        }
    }

    private void signup() {
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
                            ApiService.signup(email, pw, appVersion, platform, model, version, uuid, ip, new ApiService.Callback() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    if (response != null) {
                                        try {
                                            if (response.getString("response_code").equals(ApiService.SUCC_SIGNUP)) {
                                                JSONObject obj = response.getJSONObject("data");
                                                SharePreference.saveAuth(SignUpActivity.this, obj);
                                                goToMainpage();
                                            } else if (response.getString("response_code").equals(ApiService.ERRO_ACCOUNT_EXISTING)) {
                                                Utility.showAlert(SignUpActivity.this, "Lỗi", "Email đã đăng ký rồi. Vui lòng điền email khác");
                                            }
                                        } catch (JSONException e) {
                                            Utility.showAlert(SignUpActivity.this, "Lỗi", "Không thể xử kết quả trả về");
                                        }
                                    } else {
                                        Utility.showAlert(SignUpActivity.this, "Lỗi", "Không thể kết nối server");
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                    Utility.showAlert(SignUpActivity.this, "Lỗi", "Không thể kết nối server");
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

    private void goToMainpage() {
        Intent newIntent = new Intent(this, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }

    private void goToSignIn() {
        Intent newIntent = new Intent(this, LoginActivity.class);
        startActivity(newIntent);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (etEmail.isFocused()) {
            checkEmail();
        } else if (etPw.isFocused()) {
            checkPassword();
        } else {
            checkRepeat();
        }
    }
}
