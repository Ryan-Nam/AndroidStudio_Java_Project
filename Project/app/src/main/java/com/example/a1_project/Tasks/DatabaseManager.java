package com.example.a1_project.Tasks;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "task.db";
    // Table Names
    private static final String TABLE_TODO = "todo";

    //name
    //location
    //status

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_TODO = "todo";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_LOCATION = "location";

    // Table Create Statements
    // todo table create statement
    String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO + " TEXT,"
            + KEY_LOCATION + " TEXT,"
            + KEY_COMPLETED + " INT" + ")";

    boolean showCompleted;
    String sortingOrder;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // read the Preferences
        SharedPreferences prefs = context.getSharedPreferences("com.example1.a1_project", Context.MODE_PRIVATE);
        showCompleted=prefs.getBoolean("showCompleted",showCompleted);
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);

        // create new tables
        onCreate(db);
    }

    public boolean addToDo(ToDoTask todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TODO, todo.getTodo());
        values.put(KEY_LOCATION, todo.getLocation());
        values.put(KEY_COMPLETED, todo.getCompleted());

        long result = db.insert(TABLE_TODO, null, values);
        db.close();

        if (result==-1) {
            return false;
        }else {
            return true;
        }
    }

    public int updateToDo(ToDoTask todo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_COMPLETED, todo.getCompleted());

        int records= db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] {String.valueOf(todo.getId())});
        db.close();
        return records;

    }


    /**
     * find all tasks in todo table ordering by the sorting order
     * @return
     */
    public ArrayList<ToDoTask> getAllToDosAL() {



        ArrayList<ToDoTask> todoList = new ArrayList<ToDoTask>();

        String selectQuery = "SELECT * FROM " + TABLE_TODO;

        if (showCompleted == false)
        {
            selectQuery = selectQuery + " WHERE completed = 0";
        }



        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                ToDoTask todo = new ToDoTask();
                todo.setId(Integer.parseInt(c.getString(0)));
                todo.setTodo(c.getString(1));
                todo.setLocation(c.getString(2));
                todo.setCompleted(c.getInt(3));

                todoList.add(todo);

            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return todoList;
    }

    /**
     * Return all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TODO;
        Cursor data = db.rawQuery(query, null);
        return data;

    }

    /**
     * Return only the ID that maches the name passed in
     * @param name
     * @return
     */
    public Cursor getItemID (String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_TODO + " WHERE " + KEY_TODO + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public int editToDo(ToDoTask newToDo){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, newToDo.getTodo());
        values.put(KEY_LOCATION, newToDo.getLocation());

        int record= db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] {String.valueOf(newToDo.getId())});
        db.close();
        return record;
    }

    public void deleteToDo(int id){ //delete todo task by id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID+"=?", new String[]{String.valueOf(id)});
        db.close();

    }

    public ToDoTask getToDo (int id){ // find todo task by id
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cur=db.query(TABLE_TODO,
                new String[]{KEY_ID, KEY_TODO, KEY_LOCATION, KEY_COMPLETED},
                KEY_ID+"=?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cur!=null){
            cur.moveToFirst();
            ToDoTask todo= new ToDoTask(Integer.parseInt(cur.getString(0)),
                    cur.getString(1),
                    cur.getString(2),
                    (Integer.parseInt(cur.getString(3))));
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
        int deletedRecords=db.delete(TABLE_TODO, "1", null);
        db.close();
        return deletedRecords;
    }

    public void Delete_Contact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


}
