package com.example.louyulin.down.db;

import com.example.louyulin.down.bean.ThreadInfo;

import java.util.List;

/**
 * Created by louyulin on 2019/1/30.
 */

public interface ThreadDao {
    //插入线程信息
    void insertThread(ThreadInfo threadInfo);

    //删除线程
    void deleteThread(String url, int thread_id);

    //更新线程信息
    void updateThread(String url, int thread_id,int state);


    //根据url查询文件的线程信息
    ThreadInfo getThreadsByUrl(String url);

    List<ThreadInfo> getAllThreads();

    //线程信息是否存在
    boolean isExists(String url, int thread_id);
}
