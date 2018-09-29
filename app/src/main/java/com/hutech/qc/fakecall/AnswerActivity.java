package com.hutech.qc.fakecall;

import android.annotation.SuppressLint;
import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AnswerActivity extends AppCompatActivity {
    Extent_Main extent_main = new Extent_Main();
    Button bt_deny;
    TextView tv_name,tv_couter;
    ImageView img_avt;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Anhxa();
        Counter();
        Getinfo();
        bt_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if( extent_main.choose_sound != 0){
            mediaPlayer.stop();
        }
        super.onBackPressed();
    }

    private void Getinfo() {
        extent_main.sharedPreferences = getSharedPreferences(extent_main.fake_call,MODE_PRIVATE);
        String uri_img = extent_main.sharedPreferences.getString(extent_main.uri_img,"");
        String name = extent_main.sharedPreferences.getString(extent_main.name_call,"");
        int choose_sound = Integer.parseInt(extent_main.sharedPreferences.getString(extent_main.choose_voice,"0"));
        if (!uri_img.equalsIgnoreCase("")){
            img_avt.setImageURI(Uri.parse(uri_img));
        }
        if(!name.equalsIgnoreCase("")){
            tv_name.setText(name);
        }
        if (choose_sound == 5) {
            String voice = extent_main.sharedPreferences.getString(extent_main.uri_voice,"");
            if (!voice.equalsIgnoreCase("")){
                mediaPlayer = MediaPlayer.create(AnswerActivity.this,Uri.parse(voice));
                mediaPlayer.start();
            }
        }
        if (choose_sound == 1){
            mediaPlayer=MediaPlayer.create(AnswerActivity.this,R.raw.alo_chao_chi);
            mediaPlayer.start();
        }
        if (choose_sound == 2){
            mediaPlayer=MediaPlayer.create(AnswerActivity.this,R.raw.children);
            mediaPlayer.start();
        }
        if (choose_sound == 3){
            mediaPlayer=MediaPlayer.create(AnswerActivity.this,R.raw.hello_are_u_busy);
            mediaPlayer.start();
        }
        if (choose_sound == 4){
            mediaPlayer=MediaPlayer.create(AnswerActivity.this,R.raw.man_telephone);
            mediaPlayer.start();
        }
    }

    private void Counter() {
        final int[] count = {0};
        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run()
                    {
                        Date date = new Date(count[0] * 1000); //260000 milliseconds
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                        tv_couter.setText(""+sdf.format(date));
                        count[0]++;
                    }
                });
            }
        }, 1000, 1000);

    }

    private void Anhxa() {
        tv_couter = findViewById(R.id.txt_counter);
        tv_name = findViewById(R.id.tv_name);
        img_avt = findViewById(R.id.img_avt);
        bt_deny = findViewById(R.id.bt_answer_deny);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }
}
@SuppressLint("Registered")
class Extent_Main extends MainActivity{
}
