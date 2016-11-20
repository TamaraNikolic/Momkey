package com.momkey.alen.momkey.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class Database{

    //TABLE NAME
    private static final String TABLE_NAME ="CVTable";
    //CV TABLE column name
    private static final String ID ="id";
    private static final String NAME="name";
    private static final String PATH="path";
    private static final String TIME="time";
    private static final String SIZE="size";
    private static final String DATE="date";
    private static final String[] CV_COLUMN= {ID,NAME,PATH, DATE,SIZE,TIME};


    private Context mContext;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //constructor
    public Database(Context context){
        this.mContext=context;
        this.mDbHelper=new DatabaseHelper(context);
        this.mDb=mDbHelper.getWritableDatabase();

    }
    //closing
    public void closeDataBase(){
        if(mDb!=null){
            mDb.close();
        }
    }
//insert new record
    public void insertRecord(Record itemRecord){
        ContentValues values=new ContentValues();
        values.put(NAME, itemRecord.getName());
        values.put(PATH,itemRecord.getPath());
        values.put(TIME, itemRecord.getTime());
        values.put( DATE,itemRecord.getDate());
        values.put(SIZE,itemRecord.getSize());

        mDb.insert(TABLE_NAME, null, values);

    }

//get all list
    public ArrayList<Record> getList_Id(){
        ArrayList<Record>list =new ArrayList<>();

        String[]projection={ID,NAME,PATH, DATE,SIZE, TIME};

        Cursor c=mDb.query(TABLE_NAME,projection,null,null,null,null, ID + " ASC");

        if(c!=null) {

            if (c.moveToFirst()) {
                c.moveToFirst();
                do {
                    Record dataCv = new Record();
                    dataCv.setName(c.getString(c.getColumnIndex(NAME)));
                    dataCv.setPath(c.getString(c.getColumnIndex(PATH)));
                    dataCv.setDate(c.getString(c.getColumnIndex( DATE)));
                    dataCv.setSize(c.getString(c.getColumnIndex(SIZE)));
                    dataCv.setTime(c.getLong(c.getColumnIndex(TIME)));
                    dataCv.setId(c.getLong(c.getColumnIndex(ID)));

                    list.add(dataCv);
                }
                while (c.moveToNext());
            }
        }
        return list;
    }
    // delete record
    public boolean delete(Record dataItem){
        int rowCount=0;

        String arg[]={String.valueOf(dataItem.getId())};
        rowCount=mDb.delete(TABLE_NAME, ID +" =?",arg);

        return rowCount>0;
    }

    public boolean updatedetails(long rowId, String name)
    {
        ContentValues args = new ContentValues();
        args.put(ID, rowId);
        args.put(NAME, name);
        int i= mDb.update(TABLE_NAME, args, ID + "=" + rowId, null);
        return i > 0;
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

        // databse schema

        private static final String DATABASE_NAME = "DatabaseMomkey.db";
        private static final int DATABASE_VERSION = 23;

        private String CREATE_TABLE_CV_SQL = "CREATE TABLE " + TABLE_NAME +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                NAME + " TEXT," +
                PATH + " TEXT," +
                SIZE + " TEXT," +
                 DATE + " TEXT, " +
                TIME + " LONG);";
        private String DELETE_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE_CV_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DELETE_TABLE_SQL);
            onCreate(db);

        }



    }
}