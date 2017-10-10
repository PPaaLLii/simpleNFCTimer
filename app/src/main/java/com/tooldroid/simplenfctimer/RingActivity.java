package com.tooldroid.simplenfctimer;

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
import android.widget.Toast;

public class RingActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private Ringtone defaultRingtone;
    private PendingIntent nfcPendingIntent;
    private IntentFilter[] intentFiltersArray;
    private NfcAdapter nfcAdapter;
    private int tagNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);

//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        Intent nfcIntent = new Intent(this, getClass());
//        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        nfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
//        IntentFilter tagIntentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        try {
//            intentFiltersArray = new IntentFilter[]{tagIntentFilter};
//        }
//        catch (Throwable t) {
//            t.printStackTrace();
//        }

        Intent intent = getIntent();
        tagNumber = -1;
        if (intent != null) {
            Bundle extras = intent.getExtras();
             tagNumber = extras.getInt("tagNumber", -1);
            if (tagNumber == -1) {
                Toast.makeText(this, "Invalid Tag Number!", Toast.LENGTH_SHORT).show();
            }
        }
        sp = PreferenceManager.getDefaultSharedPreferences(this);


        int tagType = sp.getInt("" + tagNumber + "type", -1);
        if (tagType == -1) {
            Toast.makeText(this, "Invalid Tag Type!", Toast.LENGTH_SHORT).show();
        }

        if (tagType == 0) {
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000 * 5);
        }

        if (tagType == 1) {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
//        nfcAdapter.enableForegroundDispatch(
//                this,
//                nfcPendingIntent,
//                intentFiltersArray,
//                null);
//        handleIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

//    private void handleIntent(Intent intent){
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        if (tag.getId() == null)
//            Toast.makeText(this, "Id je null!", Toast.LENGTH_LONG).show();
//
//        String tag_id = Base64.encodeToString(tag.getId(), Base64.NO_WRAP);
//
//        int tagNumber = sp.getInt(tag_id, -1);
//        if (this.tagNumber == tagNumber) {
//            defaultRingtone.stop();
//        }
//    }
}
