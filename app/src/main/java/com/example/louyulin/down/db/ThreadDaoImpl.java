package com.example.louyulin.down.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.louyulin.down.bean.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louyulin on 2019/1/30.
 */

public class ThreadDaoImpl implements ThreadDao {
    private DBHelper mHelper = null;

    public ThreadDaoImpl(Context context) {
        mHelper = new DBHelper(context);
    }

    @Override
    public synchronized void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id,url,filelength,state,title) values (?,?,?,?,?)",
                new Object[]{threadInfo.getId(), threadInfo.getUrl(), threadInfo.getFilelength(), threadInfo.getState(), threadInfo.getTitle()});
        db.close();
    }

    @Override
    public synchronized void deleteThread(String url, int thread_id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where thread_id = ? and url = ?",
                new Object[]{thread_id, url});
        db.close();
    }

    @Override
    public synchronized void updateThread(String url, int thread_id, int state) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("update thread_info set  state = ? where thread_id = ? and url = ?",
                new Object[]{state,thread_id, url});
        db.close();
    }

    @Override
    public synchronized ThreadInfo getThreadsByUrl(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url = ?", new String[]{url});
        ThreadInfo threadInfo = null;
        while (cursor.moveToNext()) {
            threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setFilelength(cursor.getInt(cursor.getColumnIndex("filelength")));
            threadInfo.setState(cursor.getInt(cursor.getColumnIndex("state")));
            threadInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        }
        cursor.close();
        db.close();
        return threadInfo;
    }

    @Override
    public List<ThreadInfo> getAllThreads() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info", new String[]{});
        ThreadInfo threadInfo = null;
        List<ThreadInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setFilelength(cursor.getInt(cursor.getColumnIndex("filelength")));
            threadInfo.setState(cursor.getInt(cursor.getColumnIndex("state")));
            threadInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            list.add(threadInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public List<ThreadInfo> getAllContinueThreads() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where state = ?", new String[]{"1"});
        ThreadInfo threadInfo = null;
        List<ThreadInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setFilelength(cursor.getInt(cursor.getColumnIndex("filelength")));
            threadInfo.setState(cursor.getInt(cursor.getColumnIndex("state")));
            threadInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            list.add(threadInfo);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public synchronized boolean isExists(String url, int thread_id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url = ? and thread_id = ?",
                new String[]{url, thread_id + ""});
        while (cursor.moveToNext()) {
            cursor.close();
            db.close();
            return true;
        }
        return false;
    }
}
