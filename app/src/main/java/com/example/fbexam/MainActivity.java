package com.example.fbexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fbexam.authentication.AuthActivity;
import com.example.fbexam.cloudstorage.CloudStorageActivity;
import com.example.fbexam.firestore.FirestoreActivity;
import com.example.fbexam.firestore.register;
import com.example.fbexam.realtimedb.MemoActivity;
import com.example.fbexam.cloudmessaging.CloudMessagingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registerbtn = (Button) findViewById(R.id.registerbtn);
        registerbtn.setOnClickListener((this));

        Button firebaseauthbtn = (Button) findViewById(R.id.firebaseauthbtn);
        firebaseauthbtn.setOnClickListener((this));

        Button firebaserealdbbtn = (Button) findViewById(R.id.firebaserealtimedbbtn);
        firebaserealdbbtn.setOnClickListener(this);

        Button firebasecloudfirestorebtn = (Button) findViewById(R.id.firebasecloudfirestorebtn);
        firebasecloudfirestorebtn.setOnClickListener(this);

        Button firebasecloudstoragebtn = (Button)findViewById(R.id.firebasecloudstoragebtn);
        firebasecloudstoragebtn.setOnClickListener(this);

        Button firebasecloudmessagingbtn = (Button)findViewById(R.id.firebasecloudmessagingbtn);
        firebasecloudmessagingbtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.registerbtn:
                Intent i=new Intent(this, register.class);
                startActivity(i);
                break;
            case R.id.firebaseauthbtn:
                i = new Intent(this, AuthActivity.class);
                startActivity(i);
                break;
            case R.id.firebaserealtimedbbtn:
                i = new Intent(this, MemoActivity.class);
                startActivity(i);
                break;
            case R.id.firebasecloudfirestorebtn:
                i = new Intent(this, FirestoreActivity.class);
                startActivity(i);
                break;
            case R.id.firebasecloudstoragebtn:
                i = new Intent(this, CloudStorageActivity.class);
                startActivity(i);
            case R.id.firebasecloudmessagingbtn:
                i=new Intent(this, CloudMessagingActivity.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }


}