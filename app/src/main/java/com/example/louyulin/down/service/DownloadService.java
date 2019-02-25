package com.example.louyulin.down.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.louyulin.down.bean.FileInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DownloadService extends Service {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATA = "ACTION_UPDATA";
    public static final String ACTION_ERRO = "ACTION_ERRO";
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ADEMO";
    public static final int MSG_INIT = 0;
    private DownloadTask downloadTask;
    //下载任务的集合
    public static Map<Integer,DownloadTask> mTasks = new LinkedHashMap<>();


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    Log.d("DownloadService", "MSG_INIT:" + fileInfo);
                    //启动下载任务
                    downloadTask = new DownloadTask(DownloadService.this,fileInfo);
                    downloadTask.download();
                    mTasks.put(fileInfo.getId(),downloadTask);
                    break;
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获得Activity的参数
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            new InitThread(fileInfo).start();
        } else if (ACTION_STOP.equals(intent.getAction())){
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            //从集合中取出下载任务
            DownloadTask downloadTask = mTasks.get(fileInfo.getId());
            if (downloadTask != null){
                downloadTask.isPause = true;
                Log.d("DownloadS", "123");
            }else {
                Log.d("DownloadS", "124");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class InitThread extends Thread {
        private FileInfo mFileInfo = null;

        public InitThread(FileInfo mFileInfo) {
            this.mFileInfo = mFileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection httpURLConnection = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mFileInfo.getUrl());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setRequestMethod("GET");
                int length = -1;
                if (httpURLConnection.getResponseCode() == 200) {
                    length = httpURLConnection.getContentLength();
                }
                if (length <= 0) {
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                mFileInfo.setLength(length);
                Message message = handler.obtainMessage(MSG_INIT, mFileInfo);
                message.sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
