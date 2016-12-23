package com.ymss.tinyshop;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymss.iccard.BerICCardOs;
import com.ymss.iccard.BerIcNfcOs;
import com.ymss.view.BaseActivity;

import java.util.HashMap;
import java.util.Map;

public class GetCardInfoActivity extends BaseActivity {

    private BerICCardOs mICCardOs;
    private boolean mStop = false;
    private LinearLayout mBack;
    private TextView mTtatus_tip;
    private TextView mConnect;
    private TextView mChangeDevice;
    private int mConnectStatus = 0;
    private int mMode = 0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 0:
                    String csn = ((Map) msg.obj).get("csn").toString();
                    String ats = ((Map) msg.obj).get("ats").toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(GetCardInfoActivity.this);
                    builder.setTitle("寻卡成功");
                    String text = "CSN：" + csn + "\n" + "ATS：" + ats;
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(text);
                    text += "\n已将数据复制到剪贴板";
                    builder.setMessage(text);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getCardInfo();
                        }
                    });
                    builder.show();
                    break;
                case 1:
                    csn = (String) msg.obj.toString();
                    builder = new AlertDialog.Builder(GetCardInfoActivity.this);
                    builder.setTitle("寻卡成功");
                    text = "CSN：" + csn + "\n";
                    cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(text);
                    text += "\n已将数据复制到剪贴板";
                    builder.setMessage(text);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    break;
            }
        }
    };
    private BerICCardOs.ConnectCardCallback cardCallback = new BerICCardOs.ConnectCardCallback() {
        @Override
        public void onReceiveBerbonConnectCard(int status, String desc, String csn, String ats) {
            if (status == 0){
                Log.i("ymss","CSN："+csn+"\nATS："+ats);
                Message msg  =new Message();
                msg.what = 0;
                Map map = new HashMap();
                map.put("csn",csn);
                map.put("ats",ats);
                msg.obj = map;
                mHandler.sendMessage(msg);

            }else {
                getCardInfo();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_card_info);
        mTtatus_tip = (TextView)findViewById(R.id.status_tip);
        mConnect = (TextView)findViewById(R.id.connect);
        mBack = (LinearLayout)findViewById(R.id.title_back);
        mChangeDevice = (TextView)findViewById(R.id.change_device);
        mChangeDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStop = true;
                finishAllActivity();
                Intent intent  = new Intent(GetCardInfoActivity.this,SelectConnectActivity.class);
                startActivity(intent);
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStop = true;
                setResult(mConnectStatus);
                finish();
            }
        });

        mICCardOs = BerICCardOs.getInstance(this.getApplicationContext());
        Intent intent = getIntent();
        if (intent!=null) {
            mMode = intent.getIntExtra("mode", 0);
        }
        if (BerIcNfcOs.checkIntentIncludeTag(intent)){  //如果包含tag，则将mICCardOs模式设置为NFC
            mICCardOs.setICCardConnectMode(BerICCardOs.MODE_NFC);
            mMode = BerICCardOs.MODE_NFC;
        }
        if (mMode == BerICCardOs.MODE_NFC){
            mICCardOs.setNFCNewTagListener(new BerIcNfcOs.NewTagListenerCallback() {
                @Override
                public void newTagCallback(String tag) {
                    Message msg  =new Message();
                    msg.what = 1;
                    msg.obj = tag;
                    mHandler.sendMessage(msg);
                }
            });
            mICCardOs.checkNfcNewTag(intent);
        }else {
            getCardInfo();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMode == BerICCardOs.MODE_NFC){
            mICCardOs.setNFCEnableReadMode(this,GetCardInfoActivity.class);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMode == BerICCardOs.MODE_NFC){
            mICCardOs.setNFCDisableReadMode(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mMode == BerICCardOs.MODE_NFC){
            mICCardOs.checkNfcNewTag(intent);
        }
    }

    private void getCardInfo(){
        if (mStop == false) {
            mICCardOs.BerICCardConnectCard(cardCallback);
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
        if (status == 1){
            mConnectStatus = 1;
            mTtatus_tip.setText("读卡器已断开，刷卡无效，请重新连接！");
            mTtatus_tip.setTextColor(Color.parseColor("#ff0000"));
            mConnect.setText("设备已断开");
            Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mStop = true;
        setResult(mConnectStatus);
        finish();
    }
}
