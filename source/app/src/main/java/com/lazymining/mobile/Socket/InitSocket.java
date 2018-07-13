package com.lazymining.mobile.Socket;

import android.app.Application;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

import com.lazymining.mobile.Utility.Constant;

public class InitSocket extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constant.BASE_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
