package com.example.fbexam.cloudmessaging;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fbexam.R;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;



public class CloudMessagingActivity extends AppCompatActivity implements View.OnClickListener
{
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);


        Button tokenbtn = (Button)findViewById(R.id.tokenbtn);
        tokenbtn.setOnClickListener(this);
        String registrationToken = "diPcCDDLS0C1VFDF_La3Fy:APA91bGbciIssqm4GCk72abCQAg60E3ZzQ5dA66ghS6vlkYRuYfZYPU0V7TFHz4icQfrxoUX6AflBgqTJ91DknNA1NaKJoT9oqGri7CNtVapB8qhqhit_zABQhjmIBDZOuAtMz_idoCg";


//        Message message = Message.builder()
//                .putData("score", "850")
//                .putData("time", "2:45")
//                .setToken(registrationToken)
//                .build();


//        FirebaseMessaging.getInstance().subscribeToTopic("확진")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg ="구독성공";
//                        if (!task.isSuccessful()) {
//                            msg = "구독실패";
//                        }
//                        Log.d("jang", msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tokenbtn:
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.d("jang", "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                token = task.getResult();

                                // Log and toast
                                String msg = "InstanceID Token: " + token;
                                Log.d("jang", msg);
                                Toast.makeText(CloudMessagingActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                break;
            default:
                break;
        }

    }




}