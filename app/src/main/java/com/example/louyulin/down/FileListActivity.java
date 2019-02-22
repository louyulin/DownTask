package com.example.louyulin.down;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.louyulin.down.adapter.FileListAdapter;
import com.example.louyulin.down.bean.FileInfo;
import com.example.louyulin.down.bean.ThreadInfo;
import com.example.louyulin.down.db.ThreadDao;
import com.example.louyulin.down.db.ThreadDaoImpl;

import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends AppCompatActivity {

    private ListView lv;
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private List<Integer> selectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        setAndroidNativeLightStatusBar(this, true);
        initData();
        initView();
    }

    private void initData() {
        FileInfo fileInfo1 = new FileInfo(0,
                "http://cdn.xiaoxiongyouhao.com/apps/androilas.apk",
                "小熊.apk", 0, 0);
        FileInfo fileInfo2 = new FileInfo(1,
                "http://appdl.hicloud.com/dl/appdl/application/apk/3f/3fc7e360842f44baa541f2e35e7baf3d/com.sina.weibo.1901311518.apk?sign=portal@portal1549717710273&source=portalsite&uid=null",
                "微博.apk", 0, 0);
        FileInfo fileInfo3 = new FileInfo(2,
                "http://appdl.hicloud.com/dl/appdl/application/apk/64/647e95152dc447c288176cfd727982cc/com.sohu.tv.1809222117.apk?sign=portal@portal1548923679242&source=portalsite&uid=null",
                "搜狐视频.apk", 0, 0);
        FileInfo fileInfo4 = new FileInfo(3,
                "http://appdl.hicloud.com/dl/appdl/application/apk/7c/7ce70b42d53441cb85a980399681ddc3/com.ss.android.ugc.live.1901251402.apk?sign=portal@portal1548923679260&source=portalsite&uid=null",
                "火山小视频.apk", 0, 0);
        FileInfo fileInfo5 = new FileInfo(4,
                "http://appdl.hicloud.com/dl/appdl/application/apk/4a/4a0be6b98d5e4c16922cacc4d37def8f/com.imangi.templerun2.1901221827.apk?sign=portal@portal1548923679549&source=portalsite&uid=null",
                "神庙逃亡.apk", 0, 0);
        fileInfoList.add(fileInfo1);
        fileInfoList.add(fileInfo2);
        fileInfoList.add(fileInfo3);
        fileInfoList.add(fileInfo4);
        fileInfoList.add(fileInfo5);

        selectId = new ArrayList<>();
        ThreadDao dao = new ThreadDaoImpl(this);
        List<ThreadInfo> allThreads = dao.getAllThreads();
        if (allThreads != null || allThreads.size() > 0) {
            for (int i = 0; i < allThreads.size(); i++) {
                int id = allThreads.get(i).getId();
                selectId.add(id);
            }
        }else {
            selectId.add( -1 );
        }

    }

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    private void initView() {
        lv = (ListView) findViewById(R.id.lv);
        FileListAdapter fileListAdapter = new FileListAdapter();
        fileListAdapter.setFileInfoList(fileInfoList, selectId);
        lv.setAdapter(fileListAdapter);
    }
}
