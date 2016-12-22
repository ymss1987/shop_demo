package com.ymss.tinyshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ymss.iccard.BerICCardOs;

public class SelectConnectActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout mConnectNFC;
    private LinearLayout mConnectDinghe;
    private LinearLayout mConnectHuada;
    private BerICCardOs mICCardOs;
    private LinearLayout mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_connect);

        mConnectNFC = (LinearLayout)findViewById(R.id.connect_nfc);
        mConnectDinghe = (LinearLayout)findViewById(R.id.connect_dinghe);
        mConnectHuada = (LinearLayout)findViewById(R.id.connect_huada);
        mBack = (LinearLayout)findViewById(R.id.title_back);
        mBack.setOnClickListener(this);
        mConnectNFC.setOnClickListener(this);
        mConnectDinghe.setOnClickListener(this);
        mConnectHuada.setOnClickListener(this);
        mICCardOs = BerICCardOs.getInstance(this.getApplicationContext());
    }

    public void onClick(View v){

        switch(v.getId()){
            case R.id.connect_nfc:
                break;
            case R.id.connect_dinghe:
                mICCardOs.setICCardConnectMode(mICCardOs.MODE_DINGHE);
                if (mICCardOs.BerICCardDeviceIsSupport()) {
                    Intent intent = new Intent(SelectConnectActivity.this, SearchDeviceActivity.class);
                    intent.putExtra("mode", 0);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"手机不支持鼎和读卡器连接",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.connect_huada:
                mICCardOs.setICCardConnectMode(mICCardOs.MODE_HUADA);
                if (mICCardOs.BerICCardDeviceIsSupport()) {
                    Intent intent1 = new Intent(SelectConnectActivity.this, SearchDeviceActivity.class);
                    intent1.putExtra("mode", 2);
                    startActivity(intent1);
                }else{
                    Toast.makeText(this,"手机不支持华大读卡器连接",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.title_back:
                finish();
                break;
        }

    }
}
