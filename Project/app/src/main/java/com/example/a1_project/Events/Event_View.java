package com.example.a1_project.Events;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a1_project.MainActivity;
import com.example.a1_project.R;
import com.example.a1_project.Tasks.Add_Task;
import com.example.a1_project.Tasks.Task_View;

import java.util.ArrayList;

public class Event_View extends AppCompatActivity {

    public final static String stringToActAdd = "event_message"; // pass this message to ActivityAddRecord
    public String sortingOrder;
    private ListView listview;
    private static Event_ListAdapter custome_adapter ;
    EventDBManager DB;
    Button eventbtn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);
        // get the Preferences
        SharedPreferences prefs = this.getSharedPreferences("com.example.event", Context.MODE_PRIVATE);
        if (prefs.getString("sortingOrder",sortingOrder)==null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            sortingOrder = "Ascend By Date";
            editor.putString("sortingOrder", sortingOrder);
            editor.commit();
        } else {
            sortingOrder = prefs.getString("sortingOrder", sortingOrder);
        }

        listview = findViewById(R.id.listview_event); // list view
        registerForContextMenu(listview); // register list view for context menu
        DB = new EventDBManager(this);
        eventbtn_add = findViewById(R.id.eventbtn_add);
        updateUI();
    }

    public void updateUI() {
        listview = findViewById(R.id.listview_event);
        EventDBManager DB = new EventDBManager(this);
        ArrayList<Event> eventList = DB.getAllToDosAL();
        custome_adapter = new Event_ListAdapter(getApplicationContext(),eventList);
        listview.setAdapter(custome_adapter);
    }

    public void addRecord() {
        Intent intent = new Intent(this, Event_Add.class);
        intent.putExtra(stringToActAdd, "ADD");
        startActivityForResult(intent,10);
    }

    public void editRecord(int id) {
        Intent intent = new Intent(this, Event_Add.class);
        intent.putExtra(stringToActAdd, "EDIT");
        intent.putExtra(stringToActAdd+"_id", id);
        startActivityForResult(intent,20);
    }

    public void deleteList() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("DELETE");
        alert.setMessage("Delete all ToDo records ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // click Yes to delete
                int deletedRecords = DB.deleteToDoList();
                updateUI();
                toastMsg("Deleted "+deletedRecords+" Records.");

                dialog.dismiss();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==10){ // add record result
            if (resultCode == Activity.RESULT_OK) {
                toastMsg("Add Record");
            }
            if (resultCode == Activity.RESULT_CANCELED){
                toastMsg("User Cancelled");
            }
        }

        if (requestCode==20){ // edit record result
            if (resultCode == Activity.RESULT_OK) {
                toastMsg("Edit Record");
            }
            if (resultCode == Activity.RESULT_CANCELED){
                toastMsg("user cancelled");
            }
        }

        if (requestCode==30){ // change settings result
            if (resultCode == Activity.RESULT_OK) {
                toastMsg("Set settings");
            }
        }
        updateUI();
    }

    private void toastMsg(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    // Use onClick
    public void btnAdd(View view) {
        addRecord();
    }

    // click fab to add record
    public void btnDelete(View view) {
        deleteList();
    }
    // click fab to add record
    public void btnEdit(View view) {
        //editRecord();
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
                Intent i = new Intent(Event_View.this, MainActivity.class);
                startActivity(i);
        }

        switch (item.getItemId()) {
            case R.id.home:
                Intent i = new Intent(Event_View.this, MainActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}
