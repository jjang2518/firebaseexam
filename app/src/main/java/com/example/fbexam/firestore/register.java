package com.example.fbexam.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fbexam.R;
import com.example.fbexam.realtimedb.MemoAdapter;
import com.example.fbexam.realtimedb.MemoItem;
import com.example.fbexam.realtimedb.MemoViewListener;
import com.example.fbexam.realtimedb.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class register extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //init();

        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        //addChildEvent();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regplacebtn:
                regplace();
                break;
            case R.id.registeruser:
                writeUser();
                break;
        }
    }



    private void initView() {
        Button regbtn = (Button) findViewById(R.id.regplacebtn);
        regbtn.setOnClickListener(this);

        Button userbtn = (Button) findViewById(R.id.registeruser);
        userbtn.setOnClickListener(this);

    }

    private void writeUser()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText nameedit = (EditText) findViewById(R.id.name);

        LocalTime nowtime = LocalTime.now();
        System.out.println(nowtime); // 06:20:57.008731300
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        String formatedNowtime = nowtime.format(formatter);

        Map<String, Object> member = new HashMap<>();
        member.put("Make time",formatedNowtime);

        LocalDate nowdate = LocalDate.now();

        db.collection(nameedit.toString()).document(nowdate.toString()).set(formatedNowtime);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void regplace() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("222")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("jang", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("jang", "Error getting documents: ", task.getException());
                        }
                    }
                });


        EditText nameedit = (EditText) findViewById(R.id.name);
        EditText phoneedit = (EditText) findViewById(R.id.phonenum);

        if (nameedit.getText().toString().length() == 0 ||
                phoneedit.getText().toString().length() == 0) {
            Toast.makeText(this,
                    "메모 제목 또는 메모 내용이 입력되지 않았습니다. 입력 후 다시 시작해주세요.",
                    Toast.LENGTH_LONG).show();
            return;
        }

       // FirebaseFirestore db = FirebaseFirestore.getInstance();


        LocalTime nowtime = LocalTime.now();
        System.out.println(nowtime); // 06:20:57.008731300
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        String formatedNowtime = nowtime.format(formatter);

        LocalDate nowdate = LocalDate.now();
        Map<String, Object> member = new HashMap<>();
        member.put("생성 시간",formatedNowtime);
        member.put("pwd",phoneedit.getText().toString());




        db.collection(nameedit.getText().toString()).document("생성일 : "+nowdate.toString())
                .set(member, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("jang", "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("jang", "Error writing document", e);
                    }
                });


    }
}

