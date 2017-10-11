package com.time.time;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by guoweijie on 2017/10/11.
 */

public class NoteDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME_NOTES = "note"; //数据表名
    public static final String COLUMN_NAME_ID = "_id";    //列ID
    public static final String COLUMN_NAME_NOTE_TITLE = "title";    //列标题
    public static final String COLUMN_NAME_NOTE_CONTENT = "content"; //内容
    public static final String COLUMN_NAME_NOTE_DATE = "date";  //日期

    private Context mcontext;
    public NoteDB(Context context, String name , SQLiteDatabase.CursorFactory factory, int version ){
        super(context,name,factory,version);
        mcontext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_NAME_NOTES
                + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                //+ "conunt" +"INTEGER NOT NULL DEFAULT"
                + COLUMN_NAME_NOTE_TITLE +" TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_NOTE_CONTENT + " TEXT NOT NULL DEFAULT\"\","
                + COLUMN_NAME_NOTE_DATE + " TEXT NOT NULL DEFAULT\"\""
                + ")";
        //格式：ID  标题 内容 日期
        Log.d("SQL",sql);
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion ,int newVersion){
        db.execSQL("drop table if exists note");
        onCreate(db);

    }

}