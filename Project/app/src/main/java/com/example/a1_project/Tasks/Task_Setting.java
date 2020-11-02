package com.example.a1_project.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.a1_project.R;

public class Task_Setting extends AppCompatActivity {
    boolean showCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_setting);

        // read the Preferences
        SharedPreferences prefs = this.getSharedPreferences("com.example1.a1_project", Context.MODE_PRIVATE);
        showCompleted=prefs.getBoolean("showCompleted",showCompleted);


        // set the switch for showing completed task
        Switch swCompleted = findViewById(R.id.switch_show_completed);
        swCompleted.setChecked(showCompleted);
        // switch listener
        swCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                showCompleted = isChecked;
            }

        });
    }

    public void onClose (View view){
        savePreference();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
    // save the custom settings as shared preference.
    public void savePreference(){
        SharedPreferences prefs = this.getSharedPreferences("com.example1.a1_project", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("showCompleted",showCompleted);
        editor.commit();
    }
}
