package com.ymss.tinyshop;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymss.iccard.BerICCardOs;

import java.util.HashMap;
import java.util.Map;

public class GetCardInfoActivity extends AppCompatActivity {

    private BerICCardOs mICCardOs;
    private boolean mStop = false;
    private LinearLayout mBack;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String csn = ((Map)msg.obj).get("csn").toString();
            String ats = ((Map)msg.obj).get("ats").toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(GetCardInfoActivity.this);
            builder.setTitle("寻卡成功");
            String text = "CSN："+csn+"\n"+"ATS："+ats;
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
        mICCardOs = BerICCardOs.getInstance(this.getApplicationContext());
        mBack = (LinearLayout)findViewById(R.id.title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStop = true;
                finish();
            }
        });

        getCardInfo();

    }

    private void getCardInfo(){
        if (mStop == false) {
            mICCardOs.BerICCardConnectCard(cardCallback);
        }
    }
}
