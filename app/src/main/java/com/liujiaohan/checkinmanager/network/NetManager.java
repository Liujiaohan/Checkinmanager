package com.liujiaohan.checkinmanager.network;

import android.util.Log;

import com.liujiaohan.checkinmanager.Message;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Liu jiaohan on 2017-07-01.
 */

public class NetManager {
    public static void sendMessage(final Message message, final SendMessageCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody body=new FormBody.Builder()
                        .add("numbering",message.getNumbering())
                        .add("location",message.getLocation())
                        .add("forest",message.getForest())
                        .add("time",message.getTime())
                        .add("longitude",message.getLongitude())
                        .add("latitude",message.getLatitude())
                        .add("altitude",message.getAltitude())
                        .add("maleCount",message.getMaleCount())
                        .add("femaleCount",message.getFemaleCount())
                        .add("total",message.getTotal()).build();
                Request request =new Request.Builder()
                        .url("http://checkinmanager.hustonline.net/api/v1/records/")
                        .post(body)
                        .build();
                OkHttpClient okHttpClient=new OkHttpClient();
                try {
                    int code=okHttpClient.newCall(request).execute().code();
                    Log.i("TAG", "run: "+code);
                    callback.onSended();
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError();
                }
            }
        }).start();
    }
    public interface SendMessageCallback{
        void onSended();
        void onError();
    }
}
