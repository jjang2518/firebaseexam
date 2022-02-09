package com.example.fbexam.realtimedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.example.fbexam.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener, MemoViewListener {
    private ArrayList<MemoItem> memoItems = null;
    private MemoAdapter memoAdapter = null;
    private String username = null;
    private String uid = null;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        init();
        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        addChildEvent();
        //addValueEventListener();
    }

    @Override
    public void onItemClick(int position, View view) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.memobtn:
                regMemo();
                break;
            case R.id.reguser:
                writeNewUser();
                break;
        }
    }

    private void init() {
        memoItems = new ArrayList<>();

        username = "user_" + new Random().nextInt(1000);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            this.uid = user.getUid();
//        }
    }

    private void initView() {
        Button regbtn = (Button) findViewById(R.id.memobtn);
        regbtn.setOnClickListener(this);

        Button userbtn = (Button) findViewById(R.id.reguser);
        userbtn.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.memolist);
        memoAdapter = new MemoAdapter(memoItems, this, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(memoAdapter);
    }

    private void writeNewUser()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();

            Log.d("jang", "name = " + name);
            Log.d("jang", "email = " + email);
            Log.d("jang", "uid = " + uid);

            UserInfo userInfo = new UserInfo();
            userInfo.setUserpwd("1234");
            userInfo.setUsername(name);
            userInfo.setEmailaddr(email);

            databaseReference.child("users").child(uid).setValue(userInfo);
        }
        else
        {
            // No user is signed in
            Log.d("jang", "user null");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void regMemo() {
//        if (uid == null) {
//            Toast.makeText(this,
//                    "메모를 추가하기 위해서는 Firebase 인증이 되어야합니다. Firebase 인증 후 다시 진행해주세요.",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }

        EditText titleedit = (EditText) findViewById(R.id.memotitle);
        EditText contentsedit = (EditText) findViewById(R.id.memocontents);

        if (titleedit.getText().toString().length() == 0 ||
                contentsedit.getText().toString().length() == 0) {
            Toast.makeText(this,
                    "메모 제목 또는 메모 내용이 입력되지 않았습니다. 입력 후 다시 시작해주세요.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        MemoItem item = new MemoItem();
        item.setUser(this.username);
        item.setMemotitle(titleedit.getText().toString());
        item.setMemocontents(contentsedit.getText().toString());

        memoItems.add(item);
        memoAdapter.notifyDataSetChanged();

        databaseReference.child("memo").push().setValue(item);
    }
    private void addChildEvent()
    {
        databaseReference.child("memo").addChildEventListener(new ChildEventListener()
        {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s)
            {
                Log.d("jang", "addChildEvent in");
                MemoItem item = dataSnapshot.getValue(MemoItem.class);

                memoItems.add(item);
                memoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void addValueEventListener()
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d("jang", "Value = " + Objects.requireNonNull(dataSnapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
