package com.tooldroid.simplenfctimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.Toast;

public class NFCActivity extends AppCompatActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        // Check if the smartPhone has NFC
        if (nfcAdpt == null) {
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_LONG).show();
            finish();
        }

        Intent intent = getIntent();
        if (intent != null) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) ||
                    NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()) ||
                    NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                if (tag.getId() == null)
                    Toast.makeText(this, "Id je null!", Toast.LENGTH_LONG).show();

                String tag_id = Base64.encodeToString(tag.getId(), Base64.NO_WRAP);

                int tagNumber = sp.getInt(tag_id, -1);
                if (tagNumber == -1) {
                    // brand new tag
                    tagNumber = registerTag(tag_id);
                    configureTag(tagNumber);
                } else {
                    // tag is configured
                    // check if tag was not removed
                    if (sp.getBoolean(getValid(tagNumber), false)) {
                        int hour = sp.getInt("" + tagNumber + "hour", -1);
                        int minute = sp.getInt("" + tagNumber + "minute", -1);
                        AlarmSchedule.schedule(this, hour, minute, tagNumber);
                        this.finish();
                    } else {
                        // removed tag
                        configureTag(tagNumber);
                    }
                }
            }
        }
        finish();
    }

    private int registerTag(String tag_id) {
        int newNumber = sp.getInt(ManageNFCTags.SAVED_TAG_COUNT, -1) + 1;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(ManageNFCTags.SAVED_TAG_COUNT, newNumber);
        editor.putInt(tag_id, newNumber);
        editor.putBoolean("" + newNumber + "valid", true);
        editor.putInt("" + newNumber + "hour", 0);
        editor.putInt("" + newNumber + "minute", 5);
        editor.putString("" + newNumber + "name", "Edit name");
        editor.putInt("" + newNumber + "type", 0);
        editor.apply();
        return newNumber;
    }

    private void configureTag(int tagNumber) {
        Intent myIntent = new Intent(this, EditTagActivity.class);
        myIntent.putExtra("tagNumber", tagNumber);
        this.startActivity(myIntent);
    }

    private String getValid(int tagNumber) {
        return "" + tagNumber + "valid";
    }

}
