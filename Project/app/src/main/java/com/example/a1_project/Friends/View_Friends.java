package com.example.a1_project.Friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1_project.Events.Event_View;
import com.example.a1_project.MainActivity;
import com.example.a1_project.R;
import com.example.a1_project.Tasks.Task_View;

import java.util.ArrayList;

public class View_Friends extends AppCompatActivity {

    Button add_btn;
    ListView Contact_listview;
    ArrayList<Contact> contact_data = new ArrayList<Contact>();
    Contact_Adapter cAdapter;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_friends);
        try {
            Contact_listview = (ListView) findViewById(R.id.list);
            Contact_listview.setItemsCanFocus(false);
            add_btn = (Button) findViewById(R.id.add_btn);
            Set_Referash_Data();
        } catch (Exception e) {
            Log.e("some error", "" + e);
        }
        add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent add_user = new Intent(View_Friends.this,
                        Add_Update_Friends.class);
                add_user.putExtra("called", "add");
                add_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(add_user);
                finish();
            }
        });
    }

    public void Set_Referash_Data() {
        contact_data.clear();
        db = new DatabaseHandler(this);
        ArrayList<Contact> contact_array_from_db = db.Get_Contacts();
        for (int i = 0; i < contact_array_from_db.size(); i++) {
            int tidno = contact_array_from_db.get(i).getID();
            String name = contact_array_from_db.get(i).getName();
            byte[] image = contact_array_from_db.get(i).getImage();
            String lname = contact_array_from_db.get(i).getLname();
            String gender = contact_array_from_db.get(i).getGender();
            String age = contact_array_from_db.get(i).getAge();
            String address = contact_array_from_db.get(i).getAddress();
            Contact cnt = new Contact();
            cnt.setID(tidno);
            cnt.setName(name);
            cnt.setImage(image);
            cnt.setLname(lname);
            cnt.setGender(gender);
            cnt.setAge(age);
            cnt.setAddress(address);
            contact_data.add(cnt);
        }
        db.close();
        cAdapter = new Contact_Adapter(View_Friends.this, R.layout.listview_row,
                contact_data);
        Contact_listview.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Set_Referash_Data();
    }

    public class Contact_Adapter extends ArrayAdapter<Contact> {
        Activity activity;
        int layoutResourceId;
        Contact user;
        ArrayList<Contact> data = new ArrayList<Contact>();

        public Contact_Adapter(Activity act, int layoutResourceId,
                               ArrayList<Contact> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            UserHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.name = (TextView) row.findViewById(R.id.user_name_txt);
                holder.lname = (TextView) row.findViewById(R.id.user_lname_txt);
                holder.gender = (TextView) row.findViewById(R.id.user_gender_txt);
                holder.age = (TextView) row.findViewById(R.id.user_age_txt);
                holder.address = (TextView) row.findViewById(R.id.user_adres_txt);
                holder.imageView = (ImageView) row.findViewById(R.id.user_image);
                holder.edit = (Button) row.findViewById(R.id.btn_update);
                holder.delete = (Button) row.findViewById(R.id.btn_delete);
                holder.btn_location = (Button) row.findViewById(R.id.btn_location);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            user = data.get(position);
            holder.edit.setTag(user.getID());
            holder.delete.setTag(user.getID());
            holder.btn_location.setTag(user.getID());
            holder.name.setText(user.getName());
            holder.lname.setText(user.getLname());
            holder.gender.setText(user.getGender());
            holder.age.setText(user.getAge());
            holder.address.setText(user.getAddress());
            holder.imageView.setImageBitmap(convertToBitmap(user.getImage()));

            holder.edit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("Edit Button Clicked", "**********");
                    Intent update_user = new Intent(activity,
                            Add_Update_Friends.class);
                    update_user.putExtra("called", "update");
                    update_user.putExtra("USER_ID", v.getTag().toString());
                    activity.startActivity(update_user);

                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete ");
                    final int user_id = Integer.parseInt(v.getTag().toString());
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    DatabaseHandler dBHandler = new DatabaseHandler(
                                            activity.getApplicationContext());
                                    dBHandler.Delete_Contact(user_id);
                                    View_Friends.this.onResume();
                                }
                            });
                    adb.show();
                }

            });

            holder.btn_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent i = new Intent(activity, Maps_Friends.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        i.putExtra("Address",user.getAddress());
                        activity.startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(activity,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return row;
        }

        class UserHolder {
            ImageView imageView;
            TextView name;
            TextView lname;
            TextView gender;
            TextView age;
            TextView address;
            Button edit;
            Button delete;
            Button btn_location;
        }
    }

    public Bitmap convertToBitmap(byte[] b){
        return BitmapFactory.decodeByteArray(b, 0, b.length);
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
                Intent i = new Intent(View_Friends.this, MainActivity.class);
                startActivity(i);
        }
        switch (item.getItemId()) {
            case R.id.home:
                Intent i = new Intent(View_Friends.this, MainActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}