package com.example.vidplayer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public RecyclerView videoList;
    public ArrayList<videoModel> videoModels;
    private MyAdapter adapter;
    private static final int STORAGE_PERMISSION=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoList=findViewById(R.id.listVideos);
        videoModels= new ArrayList<>();
        adapter=new MyAdapter(videoModels, this, this::onVideoClick);
        videoList.setLayoutManager(new GridLayoutManager(this,2));
        videoList.setAdapter(adapter);
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION);

        }
        else{
            getVideos();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_PERMISSION){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getVideos();
            }
            else{
                Toast.makeText(this,"error",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void getVideos() {
        ContentResolver contentResolver=getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            cursor = contentResolver.query(uri,null,null,null);
        }
        if(cursor!=null && cursor.moveToFirst()){
            do{
                String videoTitle=
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String videoPath=
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Bitmap videoThumb= ThumbnailUtils.createVideoThumbnail(videoPath,
                        MediaStore.Images.Thumbnails.MINI_KIND);
                videoModels.add(new videoModel(videoTitle,videoPath,videoThumb));



            }while (cursor.moveToNext());
        }
        adapter.notifyDataSetChanged();
    }

    private void onVideoClick(int i) {
        Intent intent=new Intent(MainActivity.this,videoPlayerActivity.class);
        intent.putExtra("videoName",videoModels.get(i).getVideoName());
        intent.putExtra("videoPath",videoModels.get(i).getVideoPath());
        startActivity(intent);
    }
}