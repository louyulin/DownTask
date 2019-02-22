package com.example.louyulin.down;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.louyulin.down.adapter.TaskListAdapter;
import com.example.louyulin.down.bean.FileInfo;
import com.example.louyulin.down.bean.ThreadInfo;
import com.example.louyulin.down.db.ThreadDao;
import com.example.louyulin.down.db.ThreadDaoImpl;
import com.example.louyulin.down.service.DownloadService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements TaskListAdapter.ItemButtonListener {

    private ListView listView;
    private TaskListAdapter taskListAdapter;
    private List<ThreadInfo> allThreads;
    private List<Integer> progressList;
    private ThreadDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initData();
        initView();
        setAndroidNativeLightStatusBar(this, true);
    }


    //设置状态栏字体颜色
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    private void initData() {
        dao = new ThreadDaoImpl(this);
        allThreads = dao.getAllThreads();
        progressList = new ArrayList<>();
    }

    private void initView() {
        listView = findViewById(R.id.lv);
        taskListAdapter = new TaskListAdapter();
        for (int i = 0; i < allThreads.size(); i++) {
            File file = new File(DownloadService.DOWNLOAD_PATH,  allThreads.get(i).getTitle());
            long currentlength = file.length();
            int currentProgress = Integer.valueOf(currentlength * 100 / allThreads.get(i).getFilelength() + "");
            progressList.add(currentProgress);
        }
        taskListAdapter.setFileInfoList(this, allThreads, progressList,this);
        listView.setAdapter(taskListAdapter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_UPDATA);
        intentFilter.addAction(DownloadService.ACTION_ERRO);
        registerReceiver(mReciver, intentFilter);
    }

    BroadcastReceiver mReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATA.equals(intent.getAction())) {
                String finishedstr = intent.getStringExtra("finished");
                int fileId = intent.getIntExtra("fileId", 0);
                taskListAdapter.updateProgress(fileId, Integer.valueOf(finishedstr));
            } else if (DownloadService.ACTION_ERRO.equals(intent.getAction())) {
                Log.d("TaskListActivity", "456");
                int fileId = intent.getIntExtra("fileId", 0);
                for (int i = 0; i < allThreads.size(); i++) {
                    if (allThreads.get(i).getId() == fileId) {
                        //异常了
                        allThreads.get(i).setState(2);
                        taskListAdapter.setFileInfoList(TaskListActivity.this, allThreads, progressList,TaskListActivity.this);
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReciver);
    }

    @Override
    public void onListButtonStart(ThreadInfo threadInfo) {
        Intent intent = new Intent(this, DownloadService.class);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(threadInfo.getTitle());
        fileInfo.setId(threadInfo.getId());
        fileInfo.setUrl(threadInfo.getUrl());
        intent.putExtra("fileInfo", fileInfo);
        intent.setAction(DownloadService.ACTION_START);
        startService(intent);
    }

    @Override
    public void onListButtonPause(ThreadInfo threadInfo) {
        Intent intent = new Intent(this, DownloadService.class);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(threadInfo.getId());
        intent.putExtra("fileInfo", fileInfo);
        intent.setAction(DownloadService.ACTION_STOP);
        startService(intent);
    }

    @Override
    public void onLongClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);
        builder.setTitle("确定要删除").setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dao.deleteThread(allThreads.get(position).getUrl(), allThreads.get(position).getId());
                File file = new File(DownloadService.DOWNLOAD_PATH, allThreads.get(position).getTitle());
                if (file.exists()) {
                    file.delete();
                }
                allThreads.remove(position);
                progressList.remove(position);
                taskListAdapter.setFileInfoList(TaskListActivity.this, allThreads, progressList,TaskListActivity.this);
            }
        }).show();
    }


}
