package com.tooldroid.simplenfctimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManageNFCTags extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String SAVED_TAG_COUNT = "savedTagCount";

    private SharedPreferences sp;
    private List<Integer> tagNumberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_nfctags);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        List<String> tagList = new ArrayList();
        tagNumberList = new ArrayList();
        int tagCount = sp.getInt(SAVED_TAG_COUNT, -1);
        for (int i = 0; i < tagCount + 1; i++) {
            if (sp.getBoolean("" + i + "valid", false)) {
                tagList.add(sp.getString("" + i + "name", "Blank Tag Name!"));
                tagNumberList.add(i);
            }
        }

        ListView tags = (ListView) findViewById(R.id.NfcTagsListView);
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tagList);
        tags.setOnItemClickListener(this);
        tags.setAdapter(tagAdapter);
    }

    private void configureTag(int tagNumber) {
        Intent myIntent = new Intent(ManageNFCTags.this, EditTagActivity.class);
        myIntent.putExtra("tagNumber", tagNumber);
        ManageNFCTags.this.startActivity(myIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        configureTag(tagNumberList.get(position));
    }
}
