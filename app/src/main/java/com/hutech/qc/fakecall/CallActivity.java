package com.hutech.qc.fakecall;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class CallActivity extends AppCompatActivity {

    Button bt_deny,bt_answer;
    Uri defaultRintoneUri;
    Ringtone defaultRingtone;
    Extent_main extent_main = new Extent_main();
    ImageView img_call;
    TextView tv_name,tv_phone;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    int  number_time;
    int times;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        MainActivity.showInterstitial();
        Anhxa();
        Animation_call();
        TurnonRingtone();
        Button_Call();
        Getinfo();
    }

    private void Button_Call() {
        bt_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultRingtone.play();
                defaultRingtone.stop();
                Intent intent = new Intent(CallActivity.this,AnswerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (number_time >0 && times >0){
                        final Intent intent1 = new Intent(CallActivity.this, AlarmReceiver.class);
                        extent_main.myCalender = Calendar.getInstance();
                        extent_main.myCalender.set(Calendar.MILLISECOND,60000);
                        pendingIntent = PendingIntent.getBroadcast(
                                CallActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, extent_main.myCalender.getTimeInMillis(), pendingIntent);
                        times--;
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = extent_main.sharedPreferences.edit();
                        editor.putString(extent_main.times,""+times);
                        editor.apply();
                        finish();
                    }
                    else {
                        finish();
                    }
                defaultRingtone.stop();
            }
        });
    }

    protected void Getinfo() {
        extent_main.sharedPreferences = getSharedPreferences(extent_main.fake_call,MODE_PRIVATE);
        String uri_img = extent_main.sharedPreferences.getString(extent_main.uri_img,"");
        String name = extent_main.sharedPreferences.getString(extent_main.name_call,"");
        String phone = extent_main.sharedPreferences.getString(extent_main.phone_call,"");
        if (extent_main.sharedPreferences.getString(extent_main.repeat_minute,null) != null){
            number_time = Integer.parseInt(extent_main.sharedPreferences.getString(extent_main.repeat_minute,""+0));
        }
        if ( extent_main.sharedPreferences.getString(extent_main.times,null) != null){
            times = Integer.parseInt(extent_main.sharedPreferences.getString(extent_main.times,""+0));
        }
        if (!uri_img.equalsIgnoreCase("")){
            img_call.setVisibility(View.VISIBLE);
            img_call.setImageURI(Uri.parse(uri_img));
        }
        if(!name.equalsIgnoreCase("")){
            tv_name.setText(name);
        }
        if(!phone.equalsIgnoreCase("")){
            tv_phone.setText(phone);
        }

    }

    private void Animation_call() {
        TranslateAnimation translateYAnimation = new TranslateAnimation(0f, 0f, 0f, 30f);
        translateYAnimation.setDuration(501);
        translateYAnimation.setRepeatCount(Animation.INFINITE);
        translateYAnimation.setRepeatMode(Animation.REVERSE);
        bt_deny.setAnimation(translateYAnimation);
        bt_answer.setAnimation(translateYAnimation);
    }

    private void TurnonRingtone() {
        defaultRingtone.play();
    }
    private void Anhxa() {
        bt_deny = findViewById(R.id.bt_deny);
        bt_answer = findViewById(R.id.bt_answer);
        tv_name = findViewById(R.id.txt_namecall);
        tv_phone = findViewById(R.id.txt_numbercall);
        img_call = findViewById(R.id.img_call);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        defaultRingtone = RingtoneManager.getRingtone(getApplicationContext(), defaultRintoneUri);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
        defaultRingtone.stop();
        super.onBackPressed();
    }
    @SuppressLint("Registered")
    class Extent_main extends MainActivity{
    }
}
