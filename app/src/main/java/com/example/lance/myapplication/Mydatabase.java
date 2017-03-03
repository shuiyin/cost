package com.example.lance.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lance on 2017/3/2.
 */

public class Mydatabase extends SQLiteOpenHelper {
    public Mydatabase(Context context) {
        super(context, "mycost.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists cost("+
                "id integer primary key,"+
                "title varchar,"+
                "money varchar,"+
                "date varchar)");
    }
    public void insertData(CostItem costItem){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",costItem.getCostTitle());
        values.put("money",costItem.getCostMoney());
        values.put("date",costItem.getCostDate());
        database.insert("cost",null,values);


    }
    public Cursor selectData(){
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query("cost",null,null,null,null,null,null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}