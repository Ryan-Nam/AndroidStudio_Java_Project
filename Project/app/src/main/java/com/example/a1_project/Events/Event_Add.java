package com.example.a1_project.Events;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.a1_project.MainActivity;
import com.example.a1_project.R;
import com.example.a1_project.Tasks.Task_View;

import java.util.Calendar;

public class Event_Add extends AppCompatActivity {

    static EditText event_et_name;
    static EditText event_et_date;
    static EditText event_et_location;

    static final Calendar c = Calendar.getInstance();
    static int year = c.get(Calendar.YEAR);
    static int month = c.get(Calendar.MONTH);
    static int day = c.get(Calendar.DAY_OF_MONTH);
    static int hour = c.get(Calendar.HOUR_OF_DAY);
    static int minute = c.get(Calendar.MINUTE);

    String event_message;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        event_message = intent.getStringExtra(Event_View.stringToActAdd);

        TextView title = (TextView) findViewById(R.id.event_tv_add_title);
        event_et_name = (EditText) findViewById(R.id.event_et_name);
        event_et_date = (EditText) findViewById(R.id.event_et_date);
        event_et_location = (EditText) findViewById(R.id.event_et_location);

        Button btnAdd = (Button) findViewById(R.id.event_btn_add);

        if (event_message.equals("ADD")) {
            title.setText("Add Record");
            editDateText(year,month,day);
            editTimeText(hour,minute);
            btnAdd.setText("Add");
        }

        if (event_message.equals("EDIT")) {
            title.setText("Edit Record");
            btnAdd.setText("Edit");
            id = intent.getExtras().getInt(Event_View.stringToActAdd+"_id");
            EventDBManager DB = new EventDBManager(this);
            Event todo = DB.getToDo(id);
            if (todo!=null) {
                event_et_name.setText(todo.getEvent());
                event_et_date.setText(todo.getDateTime());
                event_et_location.setText(todo.getLocation());
            }
        }

        // EditText listener for picking Date and Time
        event_et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
                showDatePickerDialog(v);
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new Event_Add.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            editDateText(year, month, day);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new Event_Add.TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            editTimeText(hourOfDay,minute);
        }
    }

    public static void editDateText (int year, int month, int day) {
        String monthText;
        String dayText=String.valueOf(day);
        if (month<9){
            monthText="0"+ (month + 1);
        } else {
            monthText= String.valueOf(month + 1);
        }

        if (day<10) {
            dayText ="0"+day;
        }
        event_et_date.setText(year + "-" + monthText + "-" + dayText);
    }

    public static void editTimeText (int hour, int minute) { //format the time
        String hourText = String.valueOf(hour);
        String minuteText = String.valueOf(minute);
        if (hour<10){
            hourText="0"+ hour;
        }
        if (minute<10) {
            minuteText ="0"+minute;
        }
        event_et_date.setText(event_et_date.getText() + " " + hourText + ":" + minuteText);
    }


    public void onCancel (View view){
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED,intent);
        finish();
    }

    public void onAdd (View view) {
        if (!event_et_name.getText().toString().equals("") && !event_et_location.getText().toString().equals("") && !event_et_date.getText().toString().equals("")) {
            Event t = new Event();
            t.setEvent(event_et_name.getText().toString());
            t.setDateTime(event_et_date.getText().toString());
            t.setLocation(event_et_location.getText().toString());

            if (event_et_date.getText().toString().equals("")) {
                t.setDateTime("Unknown");
            }
            EventDBManager DB = new EventDBManager(this);
            if (event_message.equals("ADD")) {
                if (DB.addToDo(t)) {

                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Add not successful", Toast.LENGTH_LONG).show();
                }
            }

            if (event_message.equals("EDIT")) {
                t.setId(id);
                if (DB.editToDo(t)==1) {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Edit not successful", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please input task", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friends_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                Intent i = new Intent(Event_Add.this, Event_View.class);
                startActivity(i);
        }

        switch (item.getItemId()) {
            case R.id.home:
                Intent i = new Intent(Event_Add.this, MainActivity.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}

