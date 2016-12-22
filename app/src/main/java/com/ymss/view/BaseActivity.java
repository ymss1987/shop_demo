package com.ymss.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.ymss.config.Constants;
import com.ymss.iccard.BerICCardOs;
import com.ymss.iccard.BerbonCardReader;
import com.ymss.tinyshop.CardReaderMainActivity;
import com.ymss.tinyshop.SelectConnectActivity;

public class BaseActivity extends AppCompatActivity {

    private BerbonCardReaderReceiver myReceiver = new BerbonCardReaderReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            builder.setTitle("异常提示").setMessage("设备已断开,是否重新连接?").setNegativeButton("取消", null);
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent  = new Intent(BaseActivity.this,SelectConnectActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.show();
        }
    }
}
