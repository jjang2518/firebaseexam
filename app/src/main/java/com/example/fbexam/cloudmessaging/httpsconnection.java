package com.example.fbexam.cloudmessaging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fbexam.R;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.MediaType;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.net.HttpURLConnection;

import java.net.URL;

import java.util.List;


import android.content.ContentValues;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class httpsconnection extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httpsconnection);

        //메시지 가공
        JsonObject jsonObj = new JsonObject();

        //token
        Gson gson = new Gson();
        JsonArray registtoken = new JsonArray();

        JsonElement jsonElement = gson.toJsonTree("diPcCDDLS0C1VFDF_La3Fy:APA91bGbciIssqm4GCk72abCQAg60E3ZzQ5dA66ghS6vlkYRuYfZYPU0V7TFHz4icQfrxoUX6AflBgqTJ91DknNA1NaKJoT9oqGri7CNtVapB8qhqhit_zABQhjmIBDZOuAtMz_idoCg");
        registtoken.add("diPcCDDLS0C1VFDF_La3Fy:APA91bGbciIssqm4GCk72abCQAg60E3ZzQ5dA66ghS6vlkYRuYfZYPU0V7TFHz4icQfrxoUX6AflBgqTJ91DknNA1NaKJoT9oqGri7CNtVapB8qhqhit_zABQhjmIBDZOuAtMz_idoCg");

        //jsonObj.add("to", jsonElement);
        jsonObj.add("registration_ids", registtoken);


        //Notification
        JsonObject notification = new JsonObject();
        notification.addProperty("title", "타이틀HTTP");
        notification.addProperty("message", "바디HTTP");
        jsonObj.add("data", notification);

        /*발송*/
        class BackgroundThread extends Thread {
            public void run() {
                final MediaType mediaType = MediaType.parse("application/json");
                OkHttpClient httpClient = new OkHttpClient();
                try {
                    Request request = new Request.Builder().url("https://fcm.googleapis.com/fcm/send")
                            .addHeader("Content-Type", "application/json; UTF-8")
                            .addHeader("Authorization", "Key=" + "AAAAKA0piQQ:APA91bEVerDnCBuiJWJSbWO5eF9cOFymhU5w7SUUxcNg5nvLeYC3leDbaXI5aVHhNNvyACVJLyDnh0-qFhBQ_csAIVQ8Q23sq3Ce7TFazYPLB0hEMjN8iT0s9bwr2iz8cK1FWmuAsHsW")
                            .post(RequestBody.create(mediaType, jsonObj.toString())).build();
                    Response response = httpClient.newCall(request).execute();
                    String res = response.body().string();
                    Log.d("jang",jsonObj.toString());
                    Log.d("jang","notification response " + res);
                } catch (IOException e) {
                    Log.d("jang","Error in sending message to FCM server " + e);
                }
                }
            }
        BackgroundThread thread = new BackgroundThread();
        thread.start();
        }

}