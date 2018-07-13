package com.lazymining.mobile.Activity;

import android.content.Intent;
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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private String token = null;
    private EditText etPassword,etRepeat;
    private Button btnChangePw;
    private LinearLayout lnMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Utility.hideTilteBar(this);
        token = getIntent().getStringExtra("token");
        if(token == null || token.length() == 0){
            goToLogIn();
        }
        initUI();
    }
    private void initUI(){
        etPassword = findViewById(R.id.et_pw);
        etRepeat = findViewById(R.id.et_rp);
        btnChangePw = findViewById(R.id.btn_change_pw);
        lnMain = (LinearLayout) findViewById(R.id.ln_main);
        lnMain.setOnClickListener(this);
        btnChangePw.setOnClickListener(this);
    }
    private void goToLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ln_main:
                Utility.hideKeyboard(ChangePasswordActivity.this);
                break;
            case R.id.btn_change_pw:
                if (isPasswordEmpty()) {
                    Utility.showAlert(ChangePasswordActivity.this, "Lỗi", "mật khẩu không được để trống");
                    return;
                }
                if (isRepeatEmpty()){
                    Utility.showAlert(ChangePasswordActivity.this,"Lỗi","nhập lại mật khẩu không được để trống");
                    return;
                }

                if (isDiffence()){
                    Utility.showAlert(ChangePasswordActivity.this,"Lỗi","mật khẩu nhập lại không trùng");
                    return;
                }

                ApiService.changePassword(token, etPassword.getText().toString(), new ApiService.Callback() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if(response.getString("response_code").equals(Enum.SUCC_UPDATE_PASSWORD)){
                                Utility.showToast(ChangePasswordActivity.this,"mật khẩu đã thay đổi thành công");
                                goToLogIn();
                            }else {
                                Utility.showAlert(ChangePasswordActivity.this,"Lỗi",response.getString("description"));
                            }
                        }catch (JSONException e){
                            Utility.showToast(ChangePasswordActivity.this,"không thể parse json");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

                    }
                });

                break;
        }
    }

    private Boolean isPasswordEmpty(){
        if(etPassword.getText().toString().length() == 0){
            return true;
        }
        return false;
    }

    private Boolean isRepeatEmpty(){
        if(etRepeat.getText().toString().length() == 0){
            return true;
        }
        return false;
    }

    private Boolean isDiffence(){
        if(!etRepeat.getText().toString().equals(etPassword.getText().toString())){
            return true;
        }
        return false;
    }
}
