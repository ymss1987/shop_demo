package com.ymss.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ymss.config.Constants;
import com.ymss.tinyshop.SelectConnectActivity;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private BerbonCardReaderReceiver myReceiver = new BerbonCardReaderReceiver();
    private static List<Activity> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList.add(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter dynamic_filter = new IntentFilter();
        dynamic_filter.addAction(Constants.AUTODISCONNECT_ACTION);			//添加动态广播的Action
        registerReceiver(myReceiver, dynamic_filter);	// 注册自定义动态广播消息
    }

    private class BerbonCardReaderReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setTitle("异常提示").setMessage("设备已断开,是否重新连接?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onChangeConnectStatus(1, "读卡器已断开连接");
                }
            });
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finishAllActivity();
                            Intent intent  = new Intent(BaseActivity.this,SelectConnectActivity.class);
                            startActivity(intent);
                            //finish();
                        }
                    });
            builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.remove(this);
    }

    public static void finishAllActivity(){
        for(Activity activity:mList){
            activity.finish();
        }
    }

    /**
     * @Title: onChangeConnectStatus
     * @Description: 父类在用户状态发生改变时调用此方法，子类复写该方法实现父类与子类的通讯
     * @param @param data 需要传递给子类的数据
     * @return void
     * @author york
     */
    protected void onChangeConnectStatus(int status, final String data) {

    }

}
