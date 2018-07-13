package com.lazymining.mobile.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jaredrummler.android.device.DeviceName;
import com.lazymining.mobile.Fragment.ContactFragment;
import com.lazymining.mobile.Fragment.ManageFragment;
import com.lazymining.mobile.LocalData.SharePreference;
import com.lazymining.mobile.R;
import com.lazymining.mobile.Socket.InitSocket;
import com.lazymining.mobile.Utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";
    private Socket mSocket;

    private Boolean isConnected = true;
    private Fragment currentFragment;
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
//            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            mSelectedItem = 2;
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
        initSocket();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(1);
        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(MenuItem item) {
        // init corresponding fragment
        if(item == null){
            return;
        }
        switch (item.getItemId()) {
            case R.id.menu_manage:
                currentFragment = ManageFragment.newInstance();
                break;
            case R.id.menu_contact:
                currentFragment = ContactFragment.newInstance();
                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            int a = menuItem.getItemId();
            int b = item.getItemId();
            if(a == b){
                menuItem.setChecked(true);
            }

        }

        updateToolbarText(item.getTitle());

        if (currentFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, currentFragment, currentFragment.getTag());
            ft.commit();
        }
    }


    private void submitDeviceInfo(final String socketId){
        Utility.getDeviceInfo(this, new Utility.DeviceInfoCallback() {
            @Override
            public void onFinished(DeviceName.DeviceInfo info, Exception error) {

                try {
                    JSONObject authObj = new JSONObject(SharePreference.getAuth(MainActivity.this));
                    String email = authObj.getString("email");
                    String deviceId = Utility.getDeviceId(MainActivity.this);
                    String model = info.model;
                    String platform = "Android";
                    String version  = Utility.getAndroidVersion();
                    String manufacturer = info.manufacturer;
                    int isVirtual = Utility.isEmulator() ?1 :0;
                    String serial = Utility.getDeviceSerialNumber();
                    String cordova = " ";
                    mSocket.emit("socket-mobile-id",socketId,email,deviceId,cordova,model,platform,version,manufacturer,isVirtual,serial);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void signout(){
        SharePreference.removeAuth(this);
        Intent newIntent = new Intent(this,LoginActivity.class);
        startActivity(newIntent);
        finish();
    }
    private void initSocket(){
        InitSocket app = (InitSocket) getApplication();
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("notification", onNotification);
        mSocket.connect();
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   isConnected = true;
                   submitDeviceInfo(mSocket.id());
                   Utility.showToast(MainActivity.this,"đã kết nối socket");
                }
            });
        }
    };


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Utility.showToast(MainActivity.this,"đã ngắt kết nối socket");
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Utility.showToast(MainActivity.this,"lỗi mạng");
                }
            });
        }
    };

    private Emitter.Listener onNotification = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String title = (String) args[0];
                    String content = (String) args[1];
                    Utility.showAlert(MainActivity.this,title,content);
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    String message;
//                    try {
//                        username = data.getString("username");
//                        message = data.getString("message");
//                    } catch (JSONException e) {
//                        return;
//                    }
                }
            });
        }
    };


    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
//        if (actionBar != null) {
//            actionBar.setTitle(text);
//        }
    }


}
