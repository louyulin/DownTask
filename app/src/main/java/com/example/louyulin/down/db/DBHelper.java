package com.example.louyulin.down.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by louyulin on 2019/1/30.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "download.db";
    private static final int VERSION = 1;
    //state 1 是正在下载 2 是暂停  3是已经完成
    //任务id(与文件id相同) 下载url 文件总长度 下载状态 文件标题
    private static final String SQL_CREATE = "create table if not exists thread_info(_id integer," +
            "thread_id integer,url text,filelength integer,state integer,title text)";

    private static final String SQL_DROP = "drop table if exists thread_info";



    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}
