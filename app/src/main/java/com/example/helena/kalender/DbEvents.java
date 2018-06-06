package com.example.helena.kalender;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbEvents {

    Context context;
    SQLiteDatabase db;
    DbEvents.DbHelper dbHelper;

    public DbEvents(Context s){
        context = s;
    }
    public DbEvents open(){
        try {
            dbHelper = new DbHelper(context);
            db = dbHelper.getWritableDatabase();
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            return this;
        }
    }

    public void close (){db.close();}
    public void insert (String name, String location, String description, String start_datetime, String end_datetime){
        ContentValues values = new ContentValues();
        values.put(dbHelper.NAME, name);
        values.put(dbHelper.LOCATION, location);
        values.put(dbHelper.DESCRIPTION, description);
        values.put(dbHelper.START_DATETIME, start_datetime);
        values.put(dbHelper.END_DATETIME, end_datetime);
        db.insert(dbHelper.TABLE_NAME, null, values);
    }
    public Cursor show (){
        String[] columns = new String [] {dbHelper.ID, dbHelper.NAME, dbHelper.LOCATION, dbHelper.DESCRIPTION, dbHelper.START_DATETIME, dbHelper.END_DATETIME};
        Cursor s = db.query(dbHelper.TABLE_NAME, columns, null, null, null, null, dbHelper.ID + " desc");
        if (s != null){
            s.moveToFirst();
        }
        return s;
    }
    public Cursor getById(long id){
        String[] columns = new String [] {dbHelper.ID, dbHelper.NAME, dbHelper.LOCATION, dbHelper.DESCRIPTION, dbHelper.START_DATETIME, dbHelper.END_DATETIME};
        Cursor s = db.query(dbHelper.TABLE_NAME, columns, dbHelper.ID + "=" + id, null, null, null, null);
        if (s != null){
            s.moveToFirst();
        }
        return s;

    }
    public void delete (long id){
        open();
        db.delete(dbHelper.TABLE_NAME, dbHelper.ID + "=" + id, null);
        close();
    }
    public void update (long id, String name, String location, String description, String start_datetime, String end_datetime){
        open();
        ContentValues values = new ContentValues();
        values.put(dbHelper.NAME, name);
        values.put(dbHelper.LOCATION, location);
        values.put(dbHelper.DESCRIPTION, description);
        values.put(dbHelper.START_DATETIME, start_datetime);
        values.put(dbHelper.END_DATETIME, end_datetime);
        db.update(dbHelper.TABLE_NAME,values ,dbHelper.ID + "=" + id, null);
        close();


    }

    public class DbHelper extends SQLiteOpenHelper {
        public static final String DB_NAME = "kalender.db";
        public static final String TABLE_NAME = "events";
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String LOCATION = "location";
        public static final String DESCRIPTION = "description";
        public static final String START_DATETIME = "start_datetime";
        public static final String END_DATETIME = "end_datetime";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + LOCATION + " TEXT, " + DESCRIPTION + " STRING, " + START_DATETIME + " DATETIME, " + END_DATETIME + " DATETIME);";
        public static final int VERSION = 1;

        public DbHelper (Context context){
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            onCreate(db);

        }
    }

}
