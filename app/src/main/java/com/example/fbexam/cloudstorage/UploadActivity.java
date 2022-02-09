package com.example.fbexam.cloudstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.fbexam.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URL;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener
{
    private final int REQ_CODE_SELECT_IMAGE = 1000;

    private String mImgPath = null;
    private String mImgTitle = null;
    private String mImgOrient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Button uploadbtn = (Button)findViewById(R.id.imguploadbtn);
        uploadbtn.setOnClickListener(this);

        getGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 선택된 사진을 받아 서버에 업로드합니다.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                getImageNameToUri(uri);


                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    ImageView img = (ImageView) findViewById(R.id.showimg);
                    img.setImageBitmap(bm);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imguploadbtn:
                uploadFile(mImgPath);
                break;
            default:
                break;
        }
    }

    /**
     * 사진 선택을 위해 갤러리를 호출합니다.
     */
    private void getGallery()
    {
        Intent intent = null;

        // 안드로이드 KitKat(level 19)부터는 ACTION_PICK 이용
        if(Build.VERSION.SDK_INT >= 19)
        {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("image/*");
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }


    /**
     * URI 정보를 이용하여 사진 정보 가져옴
     */
    private void getImageNameToUri(Uri data)
    {
        String[] proj =
                {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.ORIENTATION
                };

        @SuppressLint("Recycle") Cursor cursor = this.getContentResolver().query(data, proj, null, null, null);
        cursor.moveToFirst();

        int column_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int column_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
        int column_orientation = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);

        mImgPath = cursor.getString(column_data);
        mImgTitle = cursor.getString(column_title);
        mImgOrient = cursor.getString(column_orientation);

        Log.d("jang", "mImgPath = " + mImgPath);
        Log.d("jang", "mImgTitle = " + mImgTitle);
        Log.d("jang", "mImgOrient = " + mImgOrient);
    }

    /**
     *
     * Firebase Cloud Storage 파일 업로드
     *
     */
    private void uploadFile(String aFilePath)
    {
        Uri file = Uri.fromFile(new File(aFilePath));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        // 참조 만들기 및 파일 업로드
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d("jang","path: "+aFilePath);
        StorageReference storageRef = storage.getReference();
        UploadTask  uploadTask = storageRef.child("storage/"+file.getLastPathSegment()).putFile(file, metadata);


        // 업로드 상태 받기
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Toast.makeText(UploadActivity.this, "Upload is " + progress + "% done", Toast.LENGTH_SHORT).show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onPaused(@NonNull UploadTask.TaskSnapshot taskSnapshot)
            {
                Log.d("jang", "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                // Handle unsuccessful uploads
                int errorCode = ((StorageException) exception).getErrorCode();
                String errorMessage = exception.getMessage();
                Log.d("jang", "Upload Exception!!");
                //Log.d("jang", (string)errorCode);
                Log.d("jang", "Exception : "+errorMessage);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                // Handle successful uploads on complete
                Toast.makeText(UploadActivity.this, "업로드가 완료되었습니다.!!", Toast.LENGTH_SHORT).show();

                String name = storageRef.getName();
                String path = storageRef.getPath();

                Log.d("jang", "name = " + name);
                Log.d("jang", "path = " + path);

                // 실시간 데이터베이스 업데이트 합니다.
                //writeNewImageInfoToDB(name, path);
            }
        });
    }

//    private void writeNewImageInfoToDB(String name, String path)
//    {
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference("images");
//
//        UploadInfo info = new UploadInfo();
//        info.setName(name);
//        info.setPath(path);
//
//        String key = databaseReference.push().getKey();
//        databaseReference.child(key).setValue(info);
//    }
}