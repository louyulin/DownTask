package com.example.louyulin.down;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btn_filelilst;
    private Button btn_tasklilst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void initView() {
        btn_filelilst = (Button) findViewById(R.id.btn_filelilst);
        btn_tasklilst = (Button) findViewById(R.id.btn_tasklilst);

        btn_filelilst.setOnClickListener(this);
        btn_tasklilst.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_filelilst:
                intent.setClass(this,FileListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_tasklilst:
                intent.setClass(this,TaskListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
