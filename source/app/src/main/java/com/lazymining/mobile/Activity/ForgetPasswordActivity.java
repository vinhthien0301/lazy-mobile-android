package com.lazymining.mobile.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lazymining.mobile.R;
import com.lazymining.mobile.Service.ApiService;
import com.lazymining.mobile.Utility.Enum;
import com.lazymining.mobile.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by doanngocduc on 3/16/18.
 */

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private Button btnForgotPw;
    private LinearLayout lnMain;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.hideTilteBar(this);
        setContentView(R.layout.activity_forget_password);
        initUI();
    }

    private void initUI(){
        etEmail = (EditText) findViewById(R.id.et_email);
        btnForgotPw = (Button) findViewById(R.id.btn_forgot_pw);
        lnMain = (LinearLayout) findViewById(R.id.ln_main);
        lnMain.setOnClickListener(this);
        btnForgotPw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.ln_main:
                Utility.hideKeyboard(ForgetPasswordActivity.this);
                break;
            case R.id.btn_forgot_pw:
                String email = etEmail.getText().toString();
                if (email.length() == 0){
                    Utility.showAlert(ForgetPasswordActivity.this,"Lỗi","Email không được bỏ trống");
                    return;
                }


                String link = "www.lazymining.com";
                ApiService.forgetPassword(email,link, new ApiService.Callback() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if(response.getString("response_code").equals(Enum.SUCC_SEND_RESET_EMAIL)){
                                Utility.showAlert(ForgetPasswordActivity.this,"Thông báo","Hãy kiểm tra email để nhận thông tin thay đổi mật khẩu");
                            }else {
                                Utility.showAlert(ForgetPasswordActivity.this,"Lỗi",response.getString("description"));
                            }
                        }catch (JSONException e){
                            Utility.showToast(ForgetPasswordActivity.this,"không thể parse json");
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                        Utility.showAlert(ForgetPasswordActivity.this,"Lỗi","Lỗi mạng");
                    }
                });
              break;
        }
    }
}
