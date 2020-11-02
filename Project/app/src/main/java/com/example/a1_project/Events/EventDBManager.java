package com.example.a1_project.Events;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class EventDBManager extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "event.db";
    // Table Names
    private static final String TABLE_EVENT = "eventtable";
    // Common column names
    private static final String KEY_EVENTID = "id";
    private static final String KEY_EVENTNAME = "eventname";
    private static final String KEY_DATETIME = "datetime";
    private static final String KEY_LOCATION = "location";

    // Table Create Statements
    // todo table create statement
    String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_EVENT + " ("
            + KEY_EVENTID + " INTEGER PRIMARY KEY,"
            + KEY_EVENTNAME + " TEXT,"
            + KEY_DATETIME + " TEXT,"
            + KEY_LOCATION + " TEXT" + ")";

    // shared preference values
    String sortingOrder;

    public EventDBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // read the Preferences
        SharedPreferences prefs = context.getSharedPreferences("com.example.event", Context.MODE_PRIVATE);

        sortingOrder=prefs.getString("sortingOrder","");
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TODO_TABLE);
    }

    // Upgrade DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        // create new tables
        onCreate(db);
    }

    public boolean addToDo (Event todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EVENTNAME, todo.getEvent());
        values.put(KEY_DATETIME, todo.getDateTime());
        values.put(KEY_LOCATION, todo.getLocation());
        long result = db.insert(TABLE_EVENT, null, values);
        db.close();
        return result != -1;
    }

    public int updateToDo(Event todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        int records= db.update(TABLE_EVENT, values, KEY_EVENTID + " = ?",
                new String[] {String.valueOf(todo.getId())});
        db.close();
        return records;
    }

    public ArrayList<Event> getAllToDosAL() {
        ArrayList<Event> todoList = new ArrayList<Event>();
        String selectQuery = "SELECT * FROM " + TABLE_EVENT;
        switch (sortingOrder) {
            case "Ascend By Date":
                selectQuery = selectQuery + " ORDER BY " + KEY_DATETIME + " ASC;";
                break;
            case "Descend By Date":
                selectQuery = selectQuery + " ORDER BY " + KEY_DATETIME + " DESC;";
                break;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                Event todo = new Event();
                todo.setId(Integer.parseInt(c.getString(0)));
                todo.setEvent(c.getString(1));
                todo.setDateTime(c.getString(2));
                todo.setLocation(c.getString(3));
                todoList.add(todo);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return todoList;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EVENT;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemID (String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_EVENTID + " FROM " + TABLE_EVENT + " WHERE " + KEY_EVENTNAME + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public int editToDo(Event newToDo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EVENTNAME, newToDo.getEvent());
        values.put(KEY_DATETIME, newToDo.getDateTime());
        values.put(KEY_LOCATION, newToDo.getLocation());
        int record= db.update(TABLE_EVENT, values, KEY_EVENTID + " = ?",
                new String[] {String.valueOf(newToDo.getId())});
        db.close();
        return record;
    }

    public void deleteToDo(int id){ //delete todo task by id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENT, KEY_EVENTID+"=?", new String[]{String.valueOf(id)});
        db.close();

    }

    public Event getToDo (int id){ // find todo task by id
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cur=db.query(TABLE_EVENT,
                new String[]{KEY_EVENTID, KEY_EVENTNAME, KEY_DATETIME, KEY_LOCATION},
                KEY_EVENTID+"=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cur!=null){
            cur.moveToFirst();
            Event todo= new Event(Integer.parseInt(cur.getString(0)),
                    cur.getString(1),
                    cur.getString(2),
                    cur.getString(3));
            cur.close();
            db.close();
            return todo;

        }
        cur.close();
        db.close();
        return null;
    }

    public int deleteToDoList (){ //delete all todolist
        SQLiteDatabase db= this.getWritableDatabase();
        int deletedRecords=db.delete(TABLE_EVENT, "1", null);
        db.close();
        return deletedRecords;
    }
}

