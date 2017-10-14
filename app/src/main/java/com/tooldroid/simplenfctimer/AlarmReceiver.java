package com.tooldroid.simplenfctimer;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        cancelNotification(context);

        int tagNumber;
        if (intent != null) {
            Bundle extras = intent.getExtras();
            tagNumber = extras.getInt("tagNumber", -1);
            if (tagNumber == -1) {
                Toast.makeText(context, "Invalid Tag Number!", Toast.LENGTH_SHORT).show();
                Log.e(RingActivity.class.toString(), "invalid tag number");
            }

            sp = PreferenceManager.getDefaultSharedPreferences(context);

            // get tag type - vibrate or ring
            int tagType = sp.getInt("" + tagNumber + "type", -1);
            if (tagType == -1) {
                Toast.makeText(context, "Invalid Tag Type!", Toast.LENGTH_SHORT).show();
            }

            if (tagType == 0) {
                vibrate(context);
            }

            if (tagType == 1) {
                ring(context);
            }
        } else {
            Toast.makeText(context, "Unknown error", Toast.LENGTH_SHORT).show();
            Log.e(RingActivity.class.toString(), "intent is null!");
        }
    }

    private void cancelNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        Log.e(RingActivity.class.toString(), "notification cancelled");
    }

    private void ring(Context context) {
        Ringtone defaultRingtone = RingtoneManager.getRingtone(context, Settings.System.DEFAULT_RINGTONE_URI);
        SharedPreferences.Editor ringingEditor = sp.edit();
        ringingEditor.putBoolean("isRinging", true);
        ringingEditor.apply();
        try {
            defaultRingtone.play();
        } catch (Exception e) {
            Toast.makeText(context, "Cannot play ringtone!", Toast.LENGTH_SHORT);
        }
    }

    private void vibrate(Context context) {
        Log.e(RingActivity.class.toString(), "vibrating");
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire(4000);
        v.vibrate(1000 * 4);
        //wakeLock.release();
    }
}
