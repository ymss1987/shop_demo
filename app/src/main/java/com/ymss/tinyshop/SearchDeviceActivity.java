package com.ymss.tinyshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ymss.iccard.BerICCardOs;
import com.ymss.view.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchDeviceActivity extends BaseActivity {

    private ListView listDevices;
    private LinearLayout emptyDesc;
    private TextView research_device;

    private BerICCardOs mICCardOs;

    SimpleAdapter adapter;
    List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private ProgressDialog mPDialog;
    private Map<String, Object> mConnectMap;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// handler接收到消息后就会执行此方法
            switch(msg.what) {
                case 0:   //搜索
                    mPDialog.dismiss();// 关闭ProgressDialog
                    if (msg.arg1 == 0) {   //搜索成功
                        research_device.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }else {
                        new AlertDialog.Builder(SearchDeviceActivity.this).setTitle("搜索设备提示").setMessage(msg.obj.toString()).setPositiveButton("确定", null);
                        if (listData.size() == 0) {
                            listDevices.setVisibility(View.GONE);
                            emptyDesc.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case 1:   //连接
                    mPDialog.dismiss();// 关闭ProgressDialog
                    if (msg.arg1 == 0){   //连接成功，跳转
                       // BaseActivity.finishAllActivity();
                        Intent intent = new Intent(SearchDeviceActivity.this,CardReaderMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        new AlertDialog.Builder(SearchDeviceActivity.this).setTitle("连接设备提示").setMessage(msg.obj.toString()).setPositiveButton("确定",null);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);

        listDevices = (ListView)findViewById(R.id.listview);
        emptyDesc = (LinearLayout)findViewById(R.id.empty);
        research_device = (TextView) findViewById(R.id.research_device);
        research_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDevices();
            }
        });

        LinearLayout mBack = (LinearLayout)findViewById(R.id.title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        listData.clear();
        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode",0);
        mICCardOs = BerICCardOs.getInstance(this.getApplicationContext());
        mICCardOs.setICCardConnectMode(mode);

        String title = intent.getStringExtra("name");
        if (title!=null) {
            String mac = intent.getStringExtra("mac");
            mConnectMap = new HashMap<String, Object>();
            mConnectMap.put("name", title);
            mConnectMap.put("mac", mac);
            mConnectMap.put("status", "已连接");
        }else{
        }

        adapter = new SimpleAdapter(this,listData,R.layout.search_device_view,
                new String[]{"name","mac","status"},
                new int[]{R.id.blue_name,R.id.blue_mac,R.id.connect_status});
        listDevices.setAdapter(adapter);

        mPDialog = new ProgressDialog(this);

        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listData.size()>position){
                    Map<String, Object> map = listData.get(position);
                    String status = (String)map.get("status");
                    if (!status.isEmpty()&& status.equals("已连接")){
                        //已连接，什么都不做
                        Toast.makeText(SearchDeviceActivity.this,"设备 "+map.get("name")+" 已连接",Toast.LENGTH_SHORT).show();
                    }else{
                        //mPDialog.setTitle("连接设备");
                        mPDialog.setMessage("设备正在连接中，请等待...");
                        mPDialog.show();
                        stopSearchDevices(map.get("mac").toString());
                    }
                }
            }
        });

        searchDevices();

    }

    private void searchDevices(){
        //mPDialog.setTitle("搜索设备");
        listData.clear();
        if (mConnectMap!=null) {
            listData.add(mConnectMap);
        }
        mPDialog.setMessage("设备搜索中，请等待...");
        mPDialog.show();
        research_device.setVisibility(View.GONE);

        mICCardOs.BerICCardStopSearchDevice(new BerICCardOs.StopSearchDeviceCallback() {
            @Override
            public void onReceiveBerbonStopSearchDevice(int status, String desc) {
                mICCardOs.BerICCardSearchDevice(20*1000, new BerICCardOs.SearchDeviceCallback() {
                    @Override
                    public void onReceiveBerbonSearchDevice(int status, String desc, String addr, String name) {
                        if (status == 0){
                            addDeviceToList(name, addr, "未连接");
                        }
                        Message msg = new Message();
                        msg.what=0;
                        msg.arg1 = status;
                        msg.obj = desc;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }

    private void stopSearchDevices(final String selectMac){
        mICCardOs.BerICCardStopSearchDevice(new BerICCardOs.StopSearchDeviceCallback() {
            @Override
            public void onReceiveBerbonStopSearchDevice(int status, String desc) {
                String address = mICCardOs.getConnectDeviceAddress();
                if (address!=null){
                    disconnectDevice(address, selectMac);
                }else{
                    connectDevice(selectMac);
                }
            }
        });
    }

    private void disconnectDevice(String oldAddr, final String newAddr){
        mICCardOs.BerICCardDisconnectDevice(oldAddr, new BerICCardOs.DisConnectDeviceCallback() {
            @Override
            public void onReceiveBerbonDisconnectDevice(int status, String desc) {
                connectDevice(newAddr);
            }
        });
    }

    private void connectDevice(String address){
        mICCardOs.BerICCardConnectDevice(10 * 1000, address, new BerICCardOs.ConnectDeviceCallback() {
            @Override
            public void onReceiveBerbonConnectDevice(int status, String desc) {
                Message msg = new Message();
                msg.what=1;
                msg.arg1 = status;
                msg.obj = desc;
                handler.sendMessage(msg);
            }
        });
    }

    public void addDeviceToList(String name, String mac, String status){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("mac", mac);
        map.put("status", status);
        listData.add(map);
    }


}
