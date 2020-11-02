package com.example.a1_project.Friends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "freind.db";
    private static final String TABLE_CONTACTS = "contacts1";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_AGE = "age";
    private static final String KEY_ADDRESS = "address";

    private final ArrayList<Contact> contact_list = new ArrayList<Contact>();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " BLOB,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_AGE + " TEXT,"
                + KEY_ADDRESS + " TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void Add_Contact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_IMAGE, contact.getImage());
        values.put(KEY_LASTNAME, contact.getLname());
        values.put(KEY_GENDER, contact.getGender());
        values.put(KEY_AGE, contact.getAge());
        values.put(KEY_ADDRESS, contact.getAddress());
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    Contact Get_Contact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,KEY_NAME,KEY_IMAGE, KEY_LASTNAME, KEY_GENDER, KEY_AGE, KEY_ADDRESS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getBlob(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        cursor.close();
        db.close();
        return contact;
    }

    //Display All Friends Record
    public ArrayList<Contact> Get_Contacts() {
        try {
            contact_list.clear();
            String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setImage(cursor.getBlob(2));
                    contact.setLname(cursor.getString(3));
                    contact.setGender(cursor.getString(4));
                    contact.setAge(cursor.getString(5));
                    contact.setAddress(cursor.getString(6));
                    contact_list.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return contact_list;
        } catch (Exception e) {
            Log.e("all_contact", "" + e);
        }
        return contact_list;
    }

    //Update Friends Record
    public int Update_Contact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_IMAGE, contact.getImage());
        values.put(KEY_LASTNAME, contact.getLname());
        values.put(KEY_GENDER, contact.getGender());
        values.put(KEY_AGE, contact.getAge());
        values.put(KEY_ADDRESS, contact.getAddress());
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    //Delete Friends Record
    public void Delete_Contact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}
