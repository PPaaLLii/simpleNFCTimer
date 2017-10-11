package com.tooldroid.simplenfctimer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by P on 22/07/2017.
 */

public class AlarmSchedule {

    private static PendingIntent pendingIntent;

    public static void schedule(Context context, int hour, int minute, int tagNumber) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, RingActivity.class);
        intent.putExtra("tagNumber", tagNumber);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*60*minute + 1000*60*60*hour, pendingIntent);
        Toast.makeText(context, "Setting alarm for " + hour + " hours and " + minute + " minutes!" , Toast.LENGTH_SHORT).show();
    }

    public static void unSchedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
