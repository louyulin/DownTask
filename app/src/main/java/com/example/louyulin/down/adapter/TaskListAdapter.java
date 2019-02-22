package com.example.louyulin.down.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.louyulin.down.R;
import com.example.louyulin.down.TaskListActivity;
import com.example.louyulin.down.bean.FileInfo;
import com.example.louyulin.down.bean.ThreadInfo;
import com.example.louyulin.down.service.DownloadService;

import java.io.File;
import java.util.List;

/**
 * Created by louyulin on 2019/1/31.
 */

public class TaskListAdapter extends BaseAdapter {
    private Context context;
    private List<ThreadInfo> threadInfos;
    private ItemButtonListener itemButtonListener;
    private List<Integer> progressList;


    public void setFileInfoList(Context context, List<ThreadInfo> threadInfos,List<Integer> progressList ,ItemButtonListener itemButtonListener) {
        this.context = context;
        this.threadInfos = threadInfos;
        this.itemButtonListener = itemButtonListener;
        this.progressList = progressList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (threadInfos == null) {
            return 0;
        } else {
            return threadInfos.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return threadInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        context = parent.getContext();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ThreadInfo threadInfo = threadInfos.get(position);
        holder.textView.setText(threadInfo.getTitle());
        holder.progressBar.setProgress(progressList.get(position));
        if (threadInfo.getState() == 1) {
            holder.startOrPause.setText("暂停");
        } else if (threadInfo.getState() == 2) {
            holder.startOrPause.setText("继续");
        } else {
            holder.startOrPause.setText("下载完成");
        }

        holder.startOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.startOrPause.getText().toString().equals("暂停")) {
                    itemButtonListener.onListButtonPause(threadInfo);
                    threadInfos.get(position).setState(2);
                } else if (holder.startOrPause.getText().toString().equals("继续")) {
                    itemButtonListener.onListButtonStart(threadInfo);
                    threadInfos.get(position).setState(1);
                } else {
                    Toast.makeText(context, "已经下载完成", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });

        holder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemButtonListener.onLongClick(position);
                return true;
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ProgressBar progressBar;
        Button startOrPause;
        LinearLayout item;

        public ViewHolder(View view) {
            textView = view.findViewById(R.id.title_tv);
            progressBar = view.findViewById(R.id.progressBar);
            startOrPause = view.findViewById(R.id.startOrPause);
            item = view.findViewById(R.id.item);
        }
    }

    //更新列表行中的进度条
    public void updateProgress(int id, int progress) {
        for (int i = 0; i < threadInfos.size(); i++) {
            if (threadInfos.get(i).getId() == id) {
                progressList.set(i,progress);
                if (progress == 100) {
                    threadInfos.get(i).setState(3);
                }
                notifyDataSetChanged();
            }
        }
    }

    public interface ItemButtonListener {
        void onListButtonStart(ThreadInfo fileInfo);

        void onListButtonPause(ThreadInfo fileInfo);

        void onLongClick(int position);
    }
}
