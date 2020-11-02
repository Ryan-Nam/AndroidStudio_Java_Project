package com.example.a1_project.Friends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a1_project.MainActivity;
import com.example.a1_project.R;

import java.io.ByteArrayOutputStream;

public class Add_Update_Friends extends AppCompatActivity {

    EditText add_name,add_lname, add_address;
    Button add_save_btn, add_view_all, update_btn, update_view_all;
    LinearLayout add_view, update_view;
    String valid_name = null, valid_lname = null, valid_gender = null, valid_age = null, valid_address = null;
    String Toast_msg = null, valid_user_id = "";
    int USER_ID;

    DatabaseHandler dbHandler = new DatabaseHandler(this);

    // Image variables
    ImageView mImageView;
    byte[] valid_image =null;
    Bitmap bp;

    //age variables
    NumberPicker np;
    TextView tv;
    String age_result ="";

    // Gender variable
    RadioGroup rg;
    RadioButton rb_male, rb_female, rb_other;
    String str_result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_update_friends);

        // set screen
        Set_Add_Update_Screen();

        // Gender
        getGender();

        // Age
        getAge();

        String called_from = getIntent().getStringExtra("called");
        //When i Click Add Button.
        if (called_from.equalsIgnoreCase("add")) {
            add_view.setVisibility(View.VISIBLE);
            update_view.setVisibility(View.GONE);
            //When i Click Edit Button
        } else {
            update_view.setVisibility(View.VISIBLE);
            add_view.setVisibility(View.GONE);
            USER_ID = Integer.parseInt(getIntent().getStringExtra("USER_ID"));
            Contact c = dbHandler.Get_Contact(USER_ID);
            add_name.setText(c.getName());
            add_lname.setText(c.getLname());
            tv.setText(c.getAge());
            add_address.setText(c.getAddress());
            dbHandler.close();
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            }
        });

        add_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValues();
                if (valid_name.length() != 0
                        && valid_lname.length() != 0
                        && valid_address.length() != 0) {
                    dbHandler.Add_Contact(new Contact(valid_name, valid_image, valid_lname, valid_gender, valid_age, valid_address));
                    dbHandler.close();
                    Toast_msg = "Data inserted successfully";
                    Show_Toast(Toast_msg);
                    Reset_Text();
                } else {
                    Toast_msg = "Sorry Some Fields are missing.\nPlease Fill up all.";
                    Show_Toast(Toast_msg);
                }
            }
        });

        add_view_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent view_user = new Intent(Add_Update_Friends.this,
                        View_Friends.class);
                view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(view_user);
                finish();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getValues();
                dbHandler.Update_Contact(new Contact(USER_ID, valid_name, valid_image, valid_lname, valid_gender, valid_age, valid_address));
                dbHandler.close();
                Toast_msg = "Data Update successfully";
                Show_Toast(Toast_msg);
                Reset_Text();
            }
        });
        update_view_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent view_user = new Intent(Add_Update_Friends.this,
                        View_Friends.class);
                view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(view_user);
                finish();
            }
        });
    }

    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //After Adding data, all the inputs that user wrote are removed just incase, user may add another data.
    public void Reset_Text() {
        add_name.getText().clear();
        add_lname.getText().clear();
        clearRadioButtons();
        tv.setText("Age");
        add_address.getText().clear();
        mImageView.setImageResource(R.drawable.addphoto);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 2:
                if(resultCode == RESULT_OK){
                    Uri choosenImage = data.getData();

                    if(choosenImage !=null){

                        bp=decodeUri(choosenImage, 400);
                        mImageView.setImageBitmap(bp);
                    }
                }
        }
    }

    //Convert and resize our image
    public Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public byte[] profileImage(Bitmap b){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public void getValues(){
        valid_name = add_name.getText().toString();
        valid_lname = add_lname.getText().toString();
        valid_gender = str_result;
        valid_age = age_result;
        valid_address = add_address.getText().toString();
        valid_image = profileImage(bp);
    }


    public void getGender(){
        rg = (RadioGroup)findViewById(R.id.rg);
        rb_male = (RadioButton)findViewById(R.id.rd_male);
        rb_female = (RadioButton)findViewById(R.id.rd_female);
        rb_other = (RadioButton)findViewById(R.id.rd_other);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rd_male){
                    str_result = rb_male.getText().toString();
                }
                if(i == R.id.rd_female){
                    str_result = rb_female.getText().toString();
                }
                if(i == R.id.rd_other){
                    str_result = rb_other.getText().toString();
                }
            }
        });
    }

    //after Add user button, this function make radio input removed.
    public void clearRadioButtons() {
        rg.clearCheck();
    }

    public void getAge(){
        //age value from 0 - 116, cuz 116 years old is maximum age at the moment.
        np = (NumberPicker)findViewById(R.id.np);
        tv = (TextView)findViewById(R.id.tv);
        np.setMinValue(1);
        np.setMaxValue(120);
        np.setWrapSelectorWheel(true);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                tv.setText(String.valueOf(i1));
                age_result = String.valueOf(i1);
                tv.setText(age_result);
            }
        });
    }


    public void Set_Add_Update_Screen(){
        add_name = (EditText) findViewById(R.id.add_name);
        add_lname = (EditText) findViewById(R.id.add_lname);
        add_address = (EditText) findViewById(R.id.add_address);
        mImageView = (ImageView) findViewById(R.id.add_photo);
        add_save_btn = (Button) findViewById(R.id.add_save_btn);
        update_btn = (Button) findViewById(R.id.update_btn);
        add_view_all = (Button) findViewById(R.id.add_view_all);
        update_view_all = (Button) findViewById(R.id.update_view_all);
        add_view = (LinearLayout) findViewById(R.id.add_view);
        update_view = (LinearLayout) findViewById(R.id.update_view);
        add_view.setVisibility(View.GONE);
        update_view.setVisibility(View.GONE);
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
                Intent i = new Intent(Add_Update_Friends.this, View_Friends.class);
                startActivity(i);
        }
        switch (item.getItemId()) {
            case R.id.home:
                Intent i = new Intent(Add_Update_Friends.this, MainActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}

