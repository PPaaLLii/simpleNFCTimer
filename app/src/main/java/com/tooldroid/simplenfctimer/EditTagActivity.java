package com.tooldroid.simplenfctimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditTagActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {

    private int tagNumber = -1;
    private SharedPreferences sp;
    private int tagType = 0;
    private int tagHours = 0;
    private int tagMinutes = 0;
    private String tagName = "";

    private EditText nameEditText;
    private Spinner typeSpinner;
    private Spinner hourSpinner;
    private Spinner minuteSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        nameEditText.addTextChangedListener(this);

        hourSpinner = (Spinner) findViewById(R.id.hourSpinner);
        hourSpinner.setOnItemSelectedListener(this);
        List<Integer> hoursList= new ArrayList<>();
        for (int i=0; i<24; i++) {
            hoursList.add(i);
        }
        ArrayAdapter<Integer> hourAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, hoursList);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        minuteSpinner = (Spinner) findViewById(R.id.minuteSpinner);
        minuteSpinner.setOnItemSelectedListener(this);
        List<Integer> minutesList= new ArrayList<>();
        for (int i=0; i<60; i++) {
            minutesList.add(i);
        }
        ArrayAdapter<Integer> minuteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, minutesList);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        typeSpinner.setOnItemSelectedListener(this);
        List<String> typeList = new ArrayList<>();
        typeList.add("vibrate");
        typeList.add("ring");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeList);
        typeSpinner.setAdapter(typeAdapter);

        Intent intent = getIntent();
        tagNumber = intent.getExtras().getInt("tagNumber", -1);
        if (tagNumber == -1) {
            Toast.makeText(this,"Tag id was not provided!",Toast.LENGTH_SHORT).show();
        }
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        fillValues();
    }

    private void fillValues() {
        boolean isTagValid = sp.getBoolean("" + tagNumber + "valid", false);
        if (isTagValid){
            tagName = sp.getString("" + tagNumber + "name", "");
            tagHours = sp.getInt("" + tagNumber + "hour", -1);
            tagMinutes = sp.getInt("" + tagNumber + "minute",-1);
            tagType = sp.getInt("" + tagNumber + "type", -1);

            nameEditText.setText(tagName);
            hourSpinner.setSelection(tagHours);
            minuteSpinner.setSelection(tagMinutes);
            typeSpinner.setSelection(tagType);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.hourSpinner:
                tagHours = position;
                if (isTagValid()) {
                    SharedPreferences.Editor hourEditor = sp.edit();
                    hourEditor.putInt("" + tagNumber + "hour", position);
                    hourEditor.putInt("" + tagNumber + "minute", tagMinutes);
                    hourEditor.apply();
                } else {
                    Toast.makeText(this, "Invalid values:\n0 hours 0 minutes!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.minuteSpinner:
                tagMinutes = position;
                if (isTagValid()) {
                    SharedPreferences.Editor minuteEditor = sp.edit();
                    minuteEditor.putInt("" + tagNumber + "minute", position);
                    minuteEditor.putInt("" + tagNumber + "hour", tagHours);
                    minuteEditor.apply();
                } else {
                    Toast.makeText(this, "Invalid values:\n0 hours 0 minutes!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.typeSpinner:
                tagType = position;
                if (isTagValid()) {
                    SharedPreferences.Editor typeEditor = sp.edit();
                    typeEditor.putInt("" + tagNumber + "type", position);
                    //Toast.makeText(this, "position: " + position, Toast.LENGTH_SHORT).show();
                    typeEditor.apply();
                }
        }
    }

    private boolean isTagValid() {
        if (!"".equals(tagName.toString())) {
            if (tagHours != 0 || tagMinutes != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        tagName = s.toString();
        if (!"".equals(tagName)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("" + tagNumber + "name", s.toString());
            editor.apply();
        } else {
            Toast.makeText(this, "Tag name must not be empty!", Toast.LENGTH_SHORT).show();
        }
    }
}
