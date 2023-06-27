package com.example.vidplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class videoPlayerActivity extends AppCompatActivity {
    TextView videoTitle,time;
    ImageButton forward,backward,play;
    SeekBar seekBar;
    String videoName,videoPath;
    VideoView videoView;
    RelativeLayout controlsRL,videoRL;
    boolean isOpen=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoTitle=findViewById(R.id.VideoTitle);
        time=findViewById(R.id.timeseek);
        forward=findViewById(R.id.forward);
        backward=findViewById(R.id.backward);
        play=findViewById(R.id.play);
        seekBar=findViewById(R.id.seek);
        videoView=findViewById(R.id.video);
        videoName=getIntent().getStringExtra("videoName");
        videoPath=getIntent().getStringExtra("videoPath");
        videoRL=findViewById(R.id.idRLVideo);
        controlsRL=findViewById(R.id.idVideoControls);

        videoView.setVideoURI(Uri.parse(videoPath));
        Toast.makeText(this, videoPath, Toast.LENGTH_LONG).show();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(videoView.getDuration());
                videoView.start();
            }
        });

        videoTitle.setText(videoName);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.seekTo(videoView.getCurrentPosition()-10000);
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.seekTo(videoView.getCurrentPosition()+10000);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView.isPlaying()){
                    videoView.pause();
                    play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));

                }
                else{
                    videoView.start();
                    play.setImageDrawable(getResources().getDrawable(R.drawable.baseline_pause_circle_24));
                }
            }
        });
        videoRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    hideControls();
                    isOpen=false;}
                else{
                    isOpen=true;
                    showControls();
                }
            }
        });
        setHandler();
        initializeSeekBar();

    }

    private void initializeSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(seekBar.getId()==R.id.seek){
                    if(b){
                        videoView.seekTo(i);
                        videoView.start();
                        int curPos=videoView.getCurrentPosition();
                        time.setText(""+convertTime(videoView.getDuration()-curPos));

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setHandler(){
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if (videoView.getDuration() > 0) {
                    int curPos = videoView.getCurrentPosition();
                    seekBar.setProgress(curPos);
                    time.setText("" + convertTime(videoView.getDuration() - curPos));

                }
                handler.postDelayed(this, 0);
            }
            };
            handler.postDelayed(runnable,500);

    }

    private String convertTime(int ms) {
        String time;
        int x,sec,min,hrs;
        x=ms/1000;
        sec=x%60;
        x/=60;
        min=x%60;
        x/=60;
        hrs=x%24;
        if(hrs!=0){
            time=String.format("%02d",hrs)+":"+String.format("%02d",min)+":"+String.format("%02d",
                sec);
        }
        else{
            time=String.format("%02d",min)+":"+String.format("%02d",
                    sec);
        }
        return time;
    }

    private void showControls() {
        controlsRL.setVisibility(View.VISIBLE);

        final Window window=this.getWindow();
        if(window==null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView=window.getDecorView();
        if(decorView!=null){
            int uiOption=decorView.getSystemUiVisibility();
            if(Build.VERSION.SDK_INT>=14){
                uiOption&=~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT>=16){
                uiOption&=~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT>=14){
                uiOption&=~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }

    private void hideControls() {
        controlsRL.setVisibility(View.GONE);

        final Window window=this.getWindow();
        if(window==null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        View decorView=window.getDecorView();
        if(decorView!=null){
            int uiOption=decorView.getSystemUiVisibility();
            if(Build.VERSION.SDK_INT>=14){
                uiOption|=View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT>=16){
                uiOption|=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT>=14){
                uiOption|=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            decorView.setSystemUiVisibility(uiOption);
        }
    }
}