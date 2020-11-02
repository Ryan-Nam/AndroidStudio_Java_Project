package com.example.a1_project.Tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a1_project.Friends.View_Friends;
import com.example.a1_project.MainActivity;
import com.example.a1_project.R;

import java.util.ArrayList;

public class Task_View extends AppCompatActivity {

    public final static String stringToActAdd = "message"; // pass this message to ActivityAddRecord
    // set shared Preferences
    public boolean showCompleted;
    public boolean showReminder;
    public String sortingOrder;

    private RelativeLayout mRelativeLayout;

    private ListView lv;
    private static ListAdapter adapter ;
    DatabaseManager dm;

    Button btn_add_me, btn_delete_me, btn_setting_me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_view);
        lv = findViewById(R.id.listview_todo);
        registerForContextMenu(lv);
        dm = new DatabaseManager(this);
        btn_add_me = findViewById(R.id.btn_add_me);
        btn_delete_me = findViewById(R.id.btn_delete_me);
        btn_setting_me = findViewById(R.id.btn_setting_me);
        btn_setting_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setting();
            }
        });
        updateUI();
    }

    private void updateUI() {
        lv = findViewById(R.id.listview_todo);
        DatabaseManager dm = new DatabaseManager(this);
        ArrayList<ToDoTask> todoList = dm.getAllToDosAL();
        adapter = new ListAdapter(getApplicationContext(),todoList);
        lv.setAdapter(adapter);
    }

    // go to ActivityAddRecord, add record
    public void addRecord() {
        Intent intent = new Intent(this, Add_Task.class);
        intent.putExtra(stringToActAdd, "ADD");
        startActivityForResult(intent,10);
    }

    // go to ActivityAddRecord, edit record
    public void editRecord(int id) {
        Intent intent = new Intent(this, Add_Task.class);
        intent.putExtra(stringToActAdd, "EDIT");
        intent.putExtra(stringToActAdd+"_id", id);
        startActivityForResult(intent,20);
    }

    // go to ActivitySettings, change custom settings
    public void setting() {
        Intent intent = new Intent(this, Task_Setting.class);
        startActivityForResult(intent,30);
    }

    // delete all records in todo list, show the AlertDialog
    public void deleteList() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("DELETE");
        alert.setMessage("Delete all ToDo records ?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // click Yes to delete
                int deletedRecords=dm.deleteToDoList();
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

    // click fab to add record
    public void btnAdd(View view) {
        addRecord();
    }

    public void btnDelete(View view){
        deleteList();
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
                Intent i = new Intent(Task_View.this, MainActivity.class);
                startActivity(i);
        }

        switch (item.getItemId()) {
            case R.id.home:
                Intent i = new Intent(Task_View.this, MainActivity.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
