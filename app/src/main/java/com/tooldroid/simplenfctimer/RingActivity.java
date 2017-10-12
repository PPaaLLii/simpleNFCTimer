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

    private SharedPreferences sp;
    private Ringtone defaultRingtone;
    private int tagNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        Log.e(RingActivity.class.toString(), "onCreate");

        cancelNotification();

        Intent intent = getIntent();
        tagNumber = -1;
        if (intent != null) {
            Bundle extras = intent.getExtras();
            tagNumber = extras.getInt("tagNumber", -1);
            if (tagNumber == -1) {
                Toast.makeText(this, "Invalid Tag Number!", Toast.LENGTH_SHORT).show();
                Log.e(RingActivity.class.toString(), "invalid tag number");
            }

            sp = PreferenceManager.getDefaultSharedPreferences(this);

            // get tag type - vibrate or ring
            int tagType = sp.getInt("" + tagNumber + "type", -1);
            if (tagType == -1) {
                Toast.makeText(this, "Invalid Tag Type!", Toast.LENGTH_SHORT).show();
            }

            if (tagType == 0) {
                vibrate();
            }

            if (tagType == 1) {
                ring();
            }
        } else {
            Toast.makeText(this, "Unknown error", Toast.LENGTH_SHORT).show();
            Log.e(RingActivity.class.toString(), "intent is null!");
        }
    }

    private void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        Log.e(RingActivity.class.toString(), "notification cancelled");
    }

    private void ring() {
        defaultRingtone = RingtoneManager.getRingtone(this, Settings.System.DEFAULT_RINGTONE_URI);
        SharedPreferences.Editor ringingEditor = sp.edit();
        ringingEditor.putBoolean("isRinging", true);
        ringingEditor.apply();
        try {
            defaultRingtone.play();
        } catch (Exception e) {
            Toast.makeText(this, "Cannot play ringtone!", Toast.LENGTH_SHORT);
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000 * 4);
        Log.e(RingActivity.class.toString(), "vibrating");
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
