package com.example.a1_project.Tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a1_project.MainActivity;
import com.example.a1_project.R;

public class Add_Task extends AppCompatActivity {

    static EditText et_todo;
    static EditText et_location;
    String message;
    int id;
    String priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        Intent intent = getIntent();
        message = intent.getStringExtra(Task_View.stringToActAdd);

        et_todo = (EditText) findViewById(R.id.et_todo);
        et_location = (EditText) findViewById(R.id.et_location);

        Button btnAdd = (Button) findViewById(R.id.btn_add);

        if (message.equals("ADD")) {
            btnAdd.setText("Add");
        }

        if (message.equals("EDIT")) {
            btnAdd.setText("Edit");
            id = intent.getExtras().getInt(Task_View.stringToActAdd+"_id");
            DatabaseManager dm = new DatabaseManager(this);
            ToDoTask todo = dm.getToDo(id);
            if (todo!=null) {
                et_todo.setText(todo.getTodo());
                et_location.setText(todo.getLocation());
            }
        }
    }

    public void onCancel (View view){
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED,intent);
        finish();
    }

    public void onAdd (View view) {
        if (!et_todo.getText().toString().equals("") && !et_location.getText().toString().equals("")) {
            ToDoTask t = new ToDoTask();
            t.setTodo(et_todo.getText().toString());
            t.setLocation(et_location.getText().toString());
            t.setCompleted(0);
            DatabaseManager dm = new DatabaseManager(this);
            if (message.equals("ADD")) {
                if (dm.addToDo(t)) {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Add not successful", Toast.LENGTH_LONG).show();
                }
            }

            if (message.equals("EDIT")) {
                t.setId(id);
                if (dm.editToDo(t)==1) {
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
                Intent i = new Intent(Add_Task.this, Task_View.class);
                startActivity(i);
        }

        switch (item.getItemId()) {
            case R.id.home:
                Intent i = new Intent(Add_Task.this, MainActivity.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
