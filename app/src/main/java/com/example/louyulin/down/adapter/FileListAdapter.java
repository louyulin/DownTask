package com.example.louyulin.down.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.louyulin.down.R;
import com.example.louyulin.down.bean.FileInfo;
import com.example.louyulin.down.bean.ThreadInfo;
import com.example.louyulin.down.service.DownloadService;

import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends BaseAdapter {
    List<FileInfo> fileInfoList;
    List<Integer> hasInTaskFileIdList;
    Context context;

    public void setFileInfoList(List<FileInfo> fileInfoList,List<Integer> hasInTaskFileIdList) {
        this.fileInfoList = fileInfoList;
        this.hasInTaskFileIdList = hasInTaskFileIdList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (fileInfoList == null){
            return 0;
        }else {
        return fileInfoList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder;
        context = viewGroup.getContext();
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_filelist,viewGroup,false);
            holder = new Holder(view);
            view.setTag(holder);
            for (int j = 0; j < hasInTaskFileIdList.size() ; j++) {
                if (fileInfoList.get(i).getId() == hasInTaskFileIdList.get(j) ){
                    holder.addTask.setText("已在队列");
                }
            }
        }else {
            holder = (Holder) view.getTag();
        }
        holder.title_tv.setText(fileInfoList.get(i).getFileName());


        holder.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.addTask.getText().toString().equals("加入队列")){
                    Intent intent = new Intent(context, DownloadService.class);
                    intent.putExtra("fileInfo", fileInfoList.get(i));
                    intent.setAction(DownloadService.ACTION_START);
                    context.startService(intent);
                    holder.addTask.setText("已在队列");
                    hasInTaskFileIdList.add(fileInfoList.get(i).getId());
                }else {
                    Toast.makeText(context, "已经在下载队列中", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    class Holder {
        TextView title_tv;
        Button addTask;
        public Holder(View view) {
        title_tv = view.findViewById(R.id.title_tv);
            addTask = view.findViewById(R.id.addTask);
        }
    }
}
