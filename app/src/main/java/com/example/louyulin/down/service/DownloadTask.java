package com.example.louyulin.down.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.louyulin.down.NetUtil;
import com.example.louyulin.down.bean.FileInfo;
import com.example.louyulin.down.bean.ThreadInfo;
import com.example.louyulin.down.db.ThreadDao;
import com.example.louyulin.down.db.ThreadDaoImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by louyulin on 2019/1/30.
 */
//下载任务类
public class DownloadTask {
    private Context context = null;
    private FileInfo fileInfo = null;
    private ThreadDao dao = null;
    public boolean isPause = false;

    public DownloadTask(Context context, FileInfo fileInfo) {
        this.context = context;
        this.fileInfo = fileInfo;
        dao = new ThreadDaoImpl(context);
    }

    public void download() {
        //读取数据库的线程任务信息
        ThreadInfo threadInfo = dao.getThreadsByUrl(fileInfo.getUrl());
        if (threadInfo == null) {
            //如果是空的初始化线程任务对象
            threadInfo = new ThreadInfo(fileInfo.getId(), fileInfo.getUrl(),  fileInfo.getLength(), 1, fileInfo.getFileName());
        }
        //创建子线程开始下载
        new DownloadThread(threadInfo).start();
    }

    //下载线程
    class DownloadThread extends Thread {
        ThreadInfo threadInfo = null;
        private File file;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            super.run();
            //向数据库插入线程任务信息
            if (!dao.isExists(threadInfo.getUrl(), threadInfo.getId())) {
                //之前不存在这个线程任务就插入到数据库
                dao.insertThread(threadInfo);
            }
            HttpURLConnection httpURLConnection = null;
            RandomAccessFile raf = null;
            InputStream inputStream = null;
            try {
                Intent intent = new Intent(DownloadService.ACTION_UPDATA);

                //设置文件写入位置 同时判断手机中是否已经有下载的相同文件
                file = new File(DownloadService.DOWNLOAD_PATH, fileInfo.getFileName());

                if (file.length() == fileInfo.getLength()){
                    //已经有这个文件了
                    //下载完成之后发送广播
                    intent.putExtra("finished", 100 + "");
                    intent.putExtra("fileId", fileInfo.getId());
                    context.sendBroadcast(intent);
                    //下载完成之后更新线程任务信息
                    dao.updateThread(threadInfo.getUrl(), threadInfo.getId(), 3);
                    return;
                }

                raf = new RandomAccessFile(file, "rwd");
                raf.seek(file.length());

                //开始下载
                URL url = new URL(threadInfo.getUrl());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("RANGE", "bytes=" + file.length() + "-" );
                inputStream = httpURLConnection.getInputStream();
                byte[] bytes = new byte[1024 * 10];
                int len;
                long time = System.currentTimeMillis();

                while ((len = inputStream.read(bytes)) != -1) {
                    //写入文件
                    raf.write(bytes, 0, len);
                    //下载进度发送广播给activity
                    if (System.currentTimeMillis() - time > 300) {
                        time = System.currentTimeMillis();
                        intent.putExtra("finished", file.length() * 100 / fileInfo.getLength() + "");
                        intent.putExtra("fileId", fileInfo.getId());
                        context.sendBroadcast(intent);
                    }
                    //下载暂停保存下载进度
                    if (isPause) {
                        dao.updateThread(threadInfo.getUrl(), threadInfo.getId() , 2);
                        return;
                    }
                }
                //下载完成之后发送广播
                intent.putExtra("finished", 100 + "");
                intent.putExtra("fileId", fileInfo.getId());
                context.sendBroadcast(intent);
                //下载完成之后更新线程任务信息
                dao.updateThread(threadInfo.getUrl(), threadInfo.getId(), 3);
            } catch (Exception e) {
                e.printStackTrace();
                isPause = true;
                dao.updateThread(threadInfo.getUrl(), threadInfo.getId(), 2);
                Intent intent = new Intent(DownloadService.ACTION_ERRO);
                intent.putExtra("fileId", fileInfo.getId());
                context.sendBroadcast(intent);
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }
}
