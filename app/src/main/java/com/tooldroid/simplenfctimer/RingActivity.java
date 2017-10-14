package com.tooldroid.simplenfctimer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Logger;

public class RingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        Log.e(RingActivity.class.toString(), "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(RingActivity.class.toString(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(RingActivity.class.toString(), "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(RingActivity.class.toString(), "onRestart");
    }
}
