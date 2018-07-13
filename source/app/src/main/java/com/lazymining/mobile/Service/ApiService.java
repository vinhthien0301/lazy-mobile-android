package com.lazymining.mobile.Service;

import android.content.res.Resources;

import com.lazymining.mobile.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by doanngocduc on 1/23/18.
 */

public class ApiService {

    public static String BASE_URL = Resources.getSystem().getString(R.string.api_url);

    public static String GET_MINER_URL = BASE_URL + "/miners";
    public static String LOGIN_URL =  BASE_URL + "/login";
    public static String DELETE_MINER =  BASE_URL + "/delete_miner";
    public static String RESTART_MINER =  BASE_URL + "/restart-miner";
    public static String LOGOUT_URL =  BASE_URL + "/logout";
    public static String FORGET_PASSWORD_URL =  BASE_URL + "/resetPasswordUser/send";
    public static String CHANGE_PASSWORD_URL =  BASE_URL + "/resetPasswordUser/update";
    public static String SIGNUP_URL =  BASE_URL + "/signup";


    //Response

    public static String SUCC_SIGNUP = "SUCC_SIGNUP";
    public static String ERRO_ACCOUNT_EXISTING = "ERRO_ACCOUNT_EXISTING";





    public interface Callback {
        void onSuccess(int statusCode, Header[] headers, JSONObject response);
        void onFailure(int statusCode, Header[] headers, String res, Throwable t);
    }

    public static void login(String email, String password,String appVersion,String devicePlatform,String deviceModel,String deviceVersion,String deviceUUID,String ip,final Callback callback){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);
        params.put("app_version", appVersion);
        params.put("device_platform", devicePlatform);
        params.put("device_model", deviceModel);
        params.put("device_version", deviceVersion);
        params.put("device_uuid", deviceUUID);
        params.put("ip", ip);
        client.post(LOGIN_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                callback.onSuccess(statusCode,headers,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.onFailure(statusCode,headers,res,t);
            }
        });
    }

    public static void load(String email,String token,final Callback callback){
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("token", token);
        client.post(GET_MINER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                callback.onSuccess(statusCode,headers,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.onFailure(statusCode,headers,res,t);
            }
        });
    }

    public static void delete(String email,String machineId,final Callback callback){
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("machine_id", machineId);
        client.post(DELETE_MINER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                callback.onSuccess(statusCode,headers,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.onFailure(statusCode,headers,res,t);
            }
        });
    }

    public static void restart(String email,String name,final Callback callback){
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("name", name);
        client.post(RESTART_MINER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                callback.onSuccess(statusCode,headers,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.onFailure(statusCode,headers,res,t);
            }
        });
    }



    public static void logout(String token,final Callback callback){
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("token", token);
        client.post(LOGOUT_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                callback.onSuccess(statusCode,headers,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.onFailure(statusCode,headers,res,t);
            }
        });
    }


    public static void forgetPassword(String email,String appLink,final Callback callback){
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("action", "grenade_token");

        client.post(FORGET_PASSWORD_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                callback.onSuccess(statusCode,headers,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.onFailure(statusCode,headers,res,t);
            }
        });
    }


    public static void changePassword(String token,String password,final Callback callback){
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("action", "update_user_password");
        params.put("token", token);
        params.put("password", password);

        client.post(CHANGE_PASSWORD_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                callback.onSuccess(statusCode,headers,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.onFailure(statusCode,headers,res,t);
            }
        });
    }


    public static void signup(String email,String pw,String appVersion,String devicePlatform,String deviceModel,String deviceVersion,String deviceUUID,String ip,final Callback callback){
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", pw);
        params.put("app_version", appVersion);
        params.put("device_platform", devicePlatform);
        params.put("device_model", deviceModel);
        params.put("device_version", deviceVersion);
        params.put("device_uuid", deviceUUID);
        params.put("ip", ip);
        client.post(SIGNUP_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                callback.onSuccess(statusCode,headers,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                callback.onFailure(statusCode,headers,res,t);
            }
        });
    }
}
