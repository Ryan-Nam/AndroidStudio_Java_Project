package com.example.a1_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.a1_project.Events.Event_View;
import com.example.a1_project.Friends.View_Friends;
import com.example.a1_project.Tasks.Task_View;

public class MainActivity extends AppCompatActivity {

    //Buttons for leading to manupulate pages
    Button btn_view_friends, btn_mani_task, btn_mani_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_view_friends = (Button)findViewById(R.id.btn_view_friends);
        btn_mani_task = (Button)findViewById(R.id.btn_mani_task);
        btn_mani_event = (Button)findViewById(R.id.btn_mani_event);

        btn_view_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, View_Friends.class);
                startActivity(intent);
            }
        });

        btn_mani_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoTaskview = new Intent (MainActivity.this, Task_View.class);
                startActivity(gotoTaskview);
            }
        });

        btn_mani_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoEventView = new Intent (MainActivity.this, Event_View.class);
                startActivity(gotoEventView);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friends:
                Intent i = new Intent(MainActivity.this, View_Friends.class);
                startActivity(i);
        }

        switch (item.getItemId()) {
            case R.id.tasks:
                Intent i = new Intent(MainActivity.this, Task_View.class);
                startActivity(i);
        }

        switch (item.getItemId()) {
            case R.id.events:
                Intent i = new Intent(MainActivity.this, Event_View.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}