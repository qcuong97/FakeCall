package com.hutech.qc.fakecall;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button bt_demo,bt_select,bt_setTime,bt_addSound,bt_done,bt_cancel,bt_as_select,bt_as_oke,bt_as_cancel;
    ImageView img_avt;
    EditText edit_name,edit_phone,edit_reply_number,edit_times;
    TextView tv_alo,tv_children,tv_hello,tv_man;
    Switch swith_repeat;
    Dialog dialog;
    private static final int FILE_SELECT_CODE = 0;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Calendar myCalender;
    SharedPreferences sharedPreferences;
    String fake_call = "Fake_Call";
    String name_call = "Name_Call";
    String phone_call = "Phone_Call";
    String uri_img = "Uri_img";
    String uri_voice = "Uri_voice";
    int check_select = 1;
    int choose_sound = 0;
    String uri_img_data = "";
    String uri_voice_data ="";
    String choose_voice = "choose_voice";
    String repeat_minute = "Repeat_Time";
    String times = "Times";
    private AdView mAdView;
    private static InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        Button_fakecall();
        Request_Permission();
        Admob();
    }
    @Override
    public void onBackPressed() {
        Alert();
    }
    private void Alert() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.rate_it)
                .setTitle(R.string.rate_us);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                }
                Uri uri = Uri.parse("market://details?id=" + appPackageName);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                System.exit(0);

            }
        });
// 3. Get the AlertDialog from create()
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void Admob() {
        MobileAds.initialize(this, "ca-app-pub-6212624214413563~5702523408");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Load_ADS","Load Thanh CÔng");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Load_ADS","LOad That Bai " + errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Load_ADS","Quang Cao Da MO");
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6212624214413563/7113584097");
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener()
        {

            @Override
            public void onAdClosed()
            {
                //reload interstitial
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }
        });
    }
    public static void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    private void Request_Permission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    //Permisson don't granted
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(MainActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                    }
                    // Permisson don't granted and dont show dialog again.
                    else {
                        Toast.makeText(MainActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                    }
                    //Register permission
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }
    }

    private void Button_fakecall() {
        bt_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save_data_call();
                Intent intent = new Intent(MainActivity.this,CallActivity.class);
                intent.putExtra("Demo_ads","1");
                startActivity(intent);
            }
        });
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_select = 1;
                showFileChooser();
            }
        });
        bt_setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showhourPicker();
            }
        });
        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCalender != null) {
                    Save_data_call();
                    final Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
                    pendingIntent = PendingIntent.getBroadcast(
                            MainActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);
                    Toast.makeText(MainActivity.this, R.string.toast_done , Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"You have not set a  timer",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_avt.setImageDrawable(getResources().getDrawable(R.drawable.avt_default));
                uri_img_data = "";
            }
        });
        bt_addSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddvoice();
            }
        });
        swith_repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edit_reply_number.setEnabled(true);
                    edit_times.setEnabled(true);
                }
                else {
                    edit_reply_number.setEnabled(false);
                    edit_times.setEnabled(false);
                }
            }
        });
    }

    private void DialogAddvoice() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_custom);
        //---------------
         tv_alo = dialog.findViewById(R.id.txt_alo_chao_chi);
         tv_children = dialog.findViewById(R.id.txt_Children);
         tv_hello = dialog.findViewById(R.id.txt_are_u_busy);
         tv_man = dialog.findViewById(R.id.txt_man);
         bt_as_select = dialog.findViewById(R.id.bt_select_voice);
         bt_as_oke = dialog.findViewById(R.id.bt_as_ok);
         bt_as_cancel = dialog.findViewById(R.id.bt_as_cancel);
        //---------------
        dialog.show();
        Button_addsound();
    }
    private void Button_addsound() {
        bt_as_oke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        bt_as_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                choose_sound = 0;
            }
        });
        bt_as_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_select = 2;
                choose_sound = 5;
                showFileChooser();
            }
        });
        tv_alo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                choose_sound = 1;
                tv_alo.setBackgroundColor(R.color.silver);
                tv_children.setBackgroundColor(0);
                tv_hello.setBackgroundColor(0);
                tv_man.setBackgroundColor(0);
                MediaPlayer mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.alo_chao_chi);
                mediaPlayer.start();
            }
        });
        tv_children.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                choose_sound = 2;
                tv_children.setBackgroundColor(R.color.white);
                tv_alo.setBackgroundColor(0);
                tv_hello.setBackgroundColor(0);
                tv_man.setBackgroundColor(0);
                MediaPlayer mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.children);
                mediaPlayer.start();
            }
        });
        tv_hello.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                choose_sound = 3;
                tv_hello.setBackgroundColor(R.color.white);
                tv_children.setBackgroundColor(0);
                tv_alo.setBackgroundColor(0);
                tv_man.setBackgroundColor(0);
                MediaPlayer mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.hello_are_u_busy);
                mediaPlayer.start();
            }
        });
        tv_man.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                choose_sound = 4;
                tv_man.setBackgroundColor(R.color.white);
                tv_children.setBackgroundColor(0);
                tv_hello.setBackgroundColor(0);
                tv_alo.setBackgroundColor(0);
                MediaPlayer mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.man_telephone);
                mediaPlayer.start();
            }
        });
    }

    private void Save_data_call() {
        SharedPreferences.Editor editor_shared = sharedPreferences.edit();
        String name = edit_name.getText().toString();
        String phone = edit_phone.getText().toString();
        editor_shared.putString(name_call, name);
        editor_shared.putString(phone_call,phone);
        editor_shared.putString(uri_img,uri_img_data);
        editor_shared.putString(uri_voice,uri_voice_data);
        editor_shared.putString(choose_voice,""+choose_sound);
        if (edit_reply_number.getText() != null){
            editor_shared.putString(repeat_minute,""+edit_reply_number.getText());
        }
        if (edit_times.getText() != null){
            editor_shared.putString(times,""+edit_times.getText());
        }
        editor_shared.apply();
    }

    private void showhourPicker() {
        myCalender = Calendar.getInstance();
        final int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        final int minute_call = myCalender.get(Calendar.MINUTE);
        final TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    Toast.makeText(MainActivity.this,getString(R.string.toast_fakecall)+" " +toast_timeout(hourOfDay,hour,minute,minute_call),Toast.LENGTH_SHORT).show();
                }
            }

            private String toast_timeout(int hourOfDay, int hour, int minute, int minute_call) {
                int hour_call = -1;
                if ( hour != hourOfDay){
                    for ( int i = hour ; i < 30;i++){
                        if ( i == 24) {
                            i = 1;
                        }
                        hour_call++;
                        if (i == hourOfDay) {
                            i = 29;
                        }
                    }
                    minute_call = minute - minute_call;
                    if ( minute_call < 0) minute_call *=-1;
                }
                else {
                    hour_call = hourOfDay - hour;
                    if( hour_call < 0) hour_call *= -1;
                    minute_call = minute - minute_call;
                    if ( minute_call < 0) minute_call *=-1;
                }
                String toast_timeout = " "+ hour_call + " "+ getString(R.string.hour_call) + " "+ minute_call + " " + getString(R.string.minute_call);
                return toast_timeout;
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute_call, true);
        timePickerDialog.setTitle(getString(R.string.choose_hour));
        Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }
    private void Anhxa() {
        bt_demo = findViewById(R.id.bt_demo);
        img_avt = findViewById(R.id.img_avt);
        bt_select = findViewById(R.id.bt_selectImage);
        bt_setTime = findViewById(R.id.bt_setTime);
        bt_done = findViewById(R.id.bt_Ok);
        bt_cancel = findViewById(R.id.bt_cancel);
        bt_addSound = findViewById(R.id.bt_addVoice);
        edit_name = findViewById(R.id.auto_txt);
        edit_phone = findViewById(R.id.edit_phone);
        edit_reply_number = findViewById(R.id.ed_replycall);
        edit_times = findViewById(R.id.ed_times);
        swith_repeat = findViewById(R.id.switch_repeat);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        sharedPreferences = getSharedPreferences(fake_call,MODE_PRIVATE);
    }
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if ( check_select == 1){
            intent.setType("image/*");
        }
        else {
            intent.setType("audio/*");
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    if (check_select == 1){
                        img_avt.setImageURI(data.getData());
                        uri_img_data = data.getDataString();
                    }
                    else {
                        uri_voice_data = data.getDataString();
                        Toast.makeText(MainActivity.this,"You have chosen to succeed",Toast.LENGTH_LONG).show();
                    }
                    }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
