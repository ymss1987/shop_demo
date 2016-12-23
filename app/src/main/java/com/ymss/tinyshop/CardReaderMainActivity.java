package com.ymss.tinyshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.siodata.bleSdk.HexAscByteUtil;
import com.ymss.iccard.BerICCardOs;
import com.ymss.iccard.BerbonCardReaderInfo;
import com.ymss.view.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CardReaderMainActivity extends BaseActivity {

    public static final int GETCARDINFO_CODE = 1000;
    private ListView mListFunction;
    private TextView mShownMessage;
    private ScrollView mScrollView;
    private TextView mClearScreen;
    private SimpleAdapter mAdapter;
    List<Map<String,Object>> mListData = new ArrayList<>();
    private BerICCardOs mICCardOs;
    private String mMac;
    private int mConnect;

    private Handler mHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String desc;
            switch(msg.what){
                case 0:
                    shownMessage(0,msg.arg1, (String)msg.obj);
                    mConnect = 0;
                    Map map = mListData.get(0);
                    map.put("fun", "重新连接");
                    map.put("desc", "重新连接之前连接成功的读卡器,，连接成功后可以对读卡器进行操作");
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    shownMessage(1,msg.arg1, (String)msg.obj);
                    mConnect = 1;
                    Map map1 = mListData.get(0);
                    map1.put("fun", "断开连接");
                    map1.put("desc", "断开手机和读卡器设备之间的连接，断开后，将无法对读卡器进行操作");
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    desc = ((Map)msg.obj).get("desc").toString();
                    shownMessage(2,msg.arg1, (String)desc);
                    if (msg.arg1 == 0) {
                        BerbonCardReaderInfo info = (BerbonCardReaderInfo) ((Map) msg.obj).get("info");
                        String text = "设备名：" + info.getBtName() + "\n设备mac地址：" + info.getBtAddr() + " \n" + "硬件版本号：" + info.getProdHwVer() + "\n软件版本号：" + info.getProdSwVer() + " \n" + "ChipUid：" + info.getChipUid() + "\nProdModel：" + info.getProdModel() + " \n" + "电量百分比：" + info.getVoltPercent() + "% \n";
                        mShownMessage.append(text);
                    }
                    break;
                case 3:
                    shownMessage(3,msg.arg1, (String)msg.obj);
                    break;
                case 4:
                    desc = ((Map)msg.obj).get("desc").toString();
                    shownMessage(4,msg.arg1, (String)desc);
                    if (msg.arg1 == 0) {
                        String csn = (String) ((Map) msg.obj).get("csn");
                        String ats = (String) ((Map) msg.obj).get("ats");
                        String text = "CSN：" + csn + "\nATS：" + ats + " \n";
                        mShownMessage.append(text);
                    }
                    break;
                case 5:
                    shownMessage(5,msg.arg1, (String)msg.obj);
                    break;
                case 6:
                    mShownMessage.append((String)msg.obj);
                    break;
                case 7:
                    desc = ((Map)msg.obj).get("desc").toString();
                    shownMessage(7,msg.arg1, (String)desc);
                    if (msg.arg1 == 0) {
                        String data = (String) ((Map) msg.obj).get("data");
                        String text = "卡片回复：" + data + " \n";
                        mShownMessage.append(text);
                    }
                    break;
                case 8:
                    shownMessage(8,msg.arg1, (String)msg.obj);
                    break;
                case 9:
                    shownMessage(9,msg.arg1, (String)msg.obj);
                    break;
                case 10:
                    desc = ((Map)msg.obj).get("desc").toString();
                    shownMessage(10,msg.arg1, (String)desc);
                    if (msg.arg1 == 0) {
                        String data = (String) ((Map) msg.obj).get("password");
                        String text = "密码：" + data + " \n";
                        mShownMessage.append(text);
                    }
                    break;
            }
            scroll2Bottom(mScrollView, mShownMessage);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mICCardOs.BerICCardReleaseDevice();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case GETCARDINFO_CODE:
                if (resultCode == 1){
                    mConnect = 0;
                    Map map = mListData.get(0);
                    map.put("fun", "重新连接");
                    map.put("desc", "重新连接之前连接成功的读卡器,，连接成功后可以对读卡器进行操作");
                    mAdapter.notifyDataSetChanged();
                }
                break;
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
            mConnect = 0;
            Map map = mListData.get(0);
            map.put("fun", "重新连接");
            map.put("desc", "重新连接之前连接成功的读卡器,，连接成功后可以对读卡器进行操作");
            mAdapter.notifyDataSetChanged();
            Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reader_main);
        mListFunction = (ListView)findViewById(R.id.list_function);
        mListFunction.setClickable(true);
        mShownMessage = (TextView)findViewById(R.id.shown_message);
        mShownMessage.setMovementMethod(new ScrollingMovementMethod());
        mScrollView = (ScrollView) findViewById(R.id.sv_show);
        mClearScreen = (TextView)findViewById(R.id.clear_screen);
        mClearScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShownMessage.setText("");
            }
        });

        mICCardOs = BerICCardOs.getInstance(this.getApplicationContext());
        mMac = mICCardOs.getConnectDeviceAddress();
        mConnect = 1;

        LinearLayout mBack = (LinearLayout)findViewById(R.id.title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initData();
        mAdapter = new SimpleAdapter(this,mListData,R.layout.cardreader_main_view,new String[]{"order","fun","desc"},new int[]{R.id.order,R.id.func,R.id.desc});
        mListFunction.setAdapter(mAdapter);
        mListFunction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:
                        execFunction0();
                        break;
                    case 1:
                        execFunction1();
                        break;
                    case 2:
                        execFunction2();
                        break;
                    case 3:
                        execFunction3();
                        break;
                    case 4:
                        execFunction4();
                        break;
                    case 5:
                        execFunction5();
                        break;
                    case 6:
                        execFunction6();
                        break;
                    case 7:
                        execFunction7();
                        break;
                    case 8:
                        execFunction8();
                        break;
                    case 9:
                        execFunction9();
                        break;
                    case 10:
                        execFunction10();
                        break;
                }
            }
        });

        mICCardOs.BerICCardSetDeviceAutoDisconnectIsCall(true);

    }

    private void shownMessage(int type, int status, String desc){
        String text = "";
        switch(type)
        {
            case 0:
                text="断开连接:\n";
                break;
            case 1:
                text="卡片数据提取:\n";
                break;
            case 2:
                text="获取读卡器信息:\n";
                break;
            case 3:
                text="修改设备名:\n";
                break;
            case 4:
                text="连接卡片:\n";
                break;
            case 5:
                text="断开连接卡片:\n";
                break;
            case 6:
                text="检测倍棒卡:\n";
                break;
            case 7:
                text="发送apdu指令:\n";
                break;
            case 8:
                text="显示文字:\n";
                break;
            case 9:
                text="清除设备显示:\n";
                break;
            case 10:
                text="密码输入:\n";
                break;
        }
        text+="status："+status+"；desc："+desc+" \n";
        mShownMessage.append(text);
    }

    private void execFunction0(){
        if (mConnect == 1) {
            mICCardOs.BerICCardDisconnectDevice(mICCardOs.getConnectDeviceAddress(), new BerICCardOs.DisConnectDeviceCallback() {
                @Override
                public void onReceiveBerbonDisconnectDevice(int status, String desc) {
                    Message msg  =new Message();
                    msg.what = 0;
                    msg.arg1 = status;
                    msg.obj = desc;
                    mHandler.sendMessage(msg);
                }
            });
        }else {
            mICCardOs.BerICCardConnectDevice(20 * 1000, mMac, new BerICCardOs.ConnectDeviceCallback() {
                @Override
                public void onReceiveBerbonConnectDevice(int status, String desc) {
                    Message msg  =new Message();
                    msg.what = 1;
                    msg.arg1 = status;
                    msg.obj = desc;
                    mHandler.sendMessage(msg);
                }
            });
        }
    }

    private void execFunction1(){
        if (mConnect == 0){
            mShownMessage.append("连接已断开，请重新连接后再操作\n");
            return;
        }
        Intent intent = new Intent(CardReaderMainActivity.this,GetCardInfoActivity.class);
        startActivityForResult(intent,GETCARDINFO_CODE);
    }

    private void execFunction2(){
        mICCardOs.BerICCardGetDeviceStatus(new BerICCardOs.GetDeviceStatusCallback() {
            @Override
            public void onReceiveBerbonGetDeviceStatus(int status, String desc, BerbonCardReaderInfo info) {
                Message msg  =new Message();
                msg.what = 2;
                msg.arg1 = status;
                Map map = new HashMap();
                map.put("desc",desc);
                map.put("info",info);
                msg.obj = map;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void execFunction3(){
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);
        if (mICCardOs.getICCardConnectMode() == 0){
            inputServer.setHint("例如：BerBon001");
        }else {
            inputServer.setHint("例如：HD001");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入设备名").setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        if (!TextUtils.isEmpty(inputName)){
                            mICCardOs.BerICCardSetDeviceBtName(inputName, new BerICCardOs.SetDeviceBtNameCallback() {
                                @Override
                                public void onReceiveBerbonSetDeviceBtName(int status, String desc) {
                                    Message msg  =new Message();
                                    msg.what = 3;
                                    msg.arg1 = status;
                                    msg.obj = desc;
                                    mHandler.sendMessage(msg);
                                }
                            });
                        }else{
                            //Toast.makeText(CardReaderMainActivity.this,"设备蓝牙名称不能为空",Toast.LENGTH_SHORT).show();
                            mShownMessage.append("设备蓝牙名称不能为空！\n");
                        }
                    }
                });
        builder.show();
    }

    private void execFunction4(){
        mICCardOs.BerICCardConnectCard(new BerICCardOs.ConnectCardCallback() {
            @Override
            public void onReceiveBerbonConnectCard(int status, String desc, String csn, String ats) {
                Message msg  =new Message();
                msg.what = 4;
                msg.arg1 = status;
                Map map = new HashMap();
                map.put("desc",desc);
                map.put("csn",csn);
                map.put("ats",ats);
                msg.obj = map;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void execFunction5(){
        mICCardOs.BerICCardDisconnectCard(new BerICCardOs.DisconnectCardCallback() {
            @Override
            public void onReceiveBerbonDisconnectCard(int status, String desc) {
                Message msg  =new Message();
                msg.what = 5;
                msg.arg1 = status;
                msg.obj = desc;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void execFunction6(){
        checkIsBerbonCard();
//        mICCardOs.BerICCardGetCardStatus(new BerICCardOs.GetCardStatusCallback() {
//            @Override
//            public void onReceiveBerbonGetCardStatus(int status, String desc, String csn, String ats) {
//                Message msg  =new Message();
//                msg.what = 6;
//                msg.arg1 = status;
//                Map map = new HashMap();
//                map.put("desc",desc);
//                map.put("csn",csn);
//                map.put("ats",ats);
//                msg.obj = map;
//                mHandler.sendMessage(msg);
//            }
//        });
    }

    private void execFunction7(){
        final EditText inputServer = new EditText(this);
        String selectMF = "00A40000023F00";
        inputServer.setText(selectMF);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入APDU指令").setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputApdu = inputServer.getText().toString();
                        if (!TextUtils.isEmpty(inputApdu)){
                            mICCardOs.BerICCardSendAPDU(inputApdu, new BerICCardOs.SendAPDUCallback() {
                                @Override
                                public void onReceiveBerbonSendAPDU(int status, String desc, String data) {
                                    Message msg  =new Message();
                                    msg.what = 7;
                                    msg.arg1 = status;
                                    Map map = new HashMap();
                                    map.put("desc",desc);
                                    map.put("data",data);
                                    msg.obj = map;
                                    mHandler.sendMessage(msg);
                                }
                            });
                        }else{
                            //Toast.makeText(CardReaderMainActivity.this,"发送的命令不能为空",Toast.LENGTH_SHORT).show();
                            mShownMessage.append("发送的命令不能为空！\n");
                        }
                    }
                });
        builder.show();
    }

    private void execFunction8(){
        final EditText inputServer = new EditText(this);
        inputServer.setHint("文字内容不能太长");
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入需要显示的文字").setView(inputServer).setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String text = inputServer.getText().toString();
                        if (!TextUtils.isEmpty(text)){
                            mICCardOs.BerShowMessage(text,1,0,new BerICCardOs.ShowMessageCallback() {
                                @Override
                                public void onReceiveBerbonShowMessage(int status, String desc) {
                                    Message msg  =new Message();
                                    msg.what = 8;
                                    msg.arg1 = status;
                                    msg.obj = desc;
                                    mHandler.sendMessage(msg);
                                }
                            });
                        }else{
                            //Toast.makeText(CardReaderMainActivity.this,"显示的文字不能为空",Toast.LENGTH_SHORT).show();
                            mShownMessage.append("显示的文字不能为空！\n");
                        }
                    }
                });
        builder.show();
    }

    private int clearType = 1;
    private void execFunction9(){
        final String[] type = {"清除读卡器显示文字", "读卡器复位"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择清屏的类型").setNegativeButton("取消", null);
        clearType = 1;
        builder.setSingleChoiceItems(type, 1, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                clearType = which;
                //Toast.makeText(CardReaderMainActivity.this, "性别为：" + type[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mICCardOs.BerClearScreen(clearType,new BerICCardOs.ClearScreenCallback() {
                            @Override
                            public void onReceiveBerbonClearScreen(int status, String desc) {
                                Message msg  =new Message();
                                msg.what = 9;
                                msg.arg1 = status;
                                msg.obj = desc;
                                mHandler.sendMessage(msg);
                            }
                        });

                    }
                });
        builder.show();
    }

    private void execFunction10(){
        mICCardOs.getPlainTextPassword(new BerICCardOs.PlainTextPasswordCallback() {
            @Override
            public void onReceiveBerbonPlainTextPassword(int status, String desc, String password) {
                Message msg  =new Message();
                msg.what = 10;
                msg.arg1 = status;
                Map map = new HashMap();
                map.put("desc",desc);
                map.put("password",password);
                msg.obj = map;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void initData(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order", "1");
        map.put("fun", "断开连接");
        map.put("desc", "断开手机和读卡器设备之间的连接，断开后，将无法对读卡器进行操作");
        mListData.add(map);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("order", "2");
        map1.put("fun", "卡片数据提取");
        map1.put("desc", "获取卡片CSN和ATS等数据，可以用于生成《倍棒卡数据导入表》。");
        mListData.add(map1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("order", "3");
        map2.put("fun", "获取设备信息");
        map2.put("desc", "获取读卡器设备信息，包括读卡器设备名和mac地址等");
        mListData.add(map2);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("order", "4");
        map3.put("fun", "修改设备名");
        map3.put("desc", "修改读卡器设备名，修改之后重新搜索连接即可看到效果");
        mListData.add(map3);

        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("order", "5");
        map4.put("fun", "连接卡片");
        map4.put("desc", "用于给卡片上电，上电之后可以等到卡片CSN和ATS");
        mListData.add(map4);

        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("order", "6");
        map5.put("fun", "断开卡片连接");
        map5.put("desc", "用于断开和卡片的连接");
        mListData.add(map5);

        Map<String, Object> map6 = new HashMap<String, Object>();
        map6.put("order", "7");
        map6.put("fun", "检测倍棒卡");
        map6.put("desc", "检测当前卡片是否是倍棒卡，请将卡片先放置在读卡器上");
        mListData.add(map6);

        Map<String, Object> map7 = new HashMap<String, Object>();
        map7.put("order", "8");
        map7.put("fun", "发送APDU指令");
        map7.put("desc", "给卡片发送指令，可以得到卡片的回复");
        mListData.add(map7);

        Map<String, Object> map8 = new HashMap<String, Object>();
        map8.put("order", "9");
        map8.put("fun", "显示文字");
        map8.put("desc", "在读卡器上显示指定文字，华大读卡器有效，鼎和读卡器无效");
        mListData.add(map8);

        Map<String, Object> map9 = new HashMap<String, Object>();
        map9.put("order", "10");
        map9.put("fun", "清除设备显示");
        map9.put("desc", "清除读卡器屏幕的文字显示，华大读卡器有效，鼎和读卡器无效");
        mListData.add(map9);

        Map<String, Object> map10 = new HashMap<String, Object>();
        map10.put("order", "11");
        map10.put("fun", "密码输入");
        map10.put("desc", "使用读卡器输入密码，华大读卡器有效，鼎和读卡器无效");
        mListData.add(map10);
    }


    //检测是否是倍棒卡
    /*
    判断是否是倍棒卡的步骤
    1、发送APDU命令(选择Applet)
    \x00\xA4\x04\x00\x06\xD1\x56\x00\x00\x00\x02
    2、发送APDU命令(选择MF)
    \x00\xA4\x00\x00\x02\x3F\x00
    3、发送APDU命令(选择倍棒钱包)
    \x00\xA4\x00\x00\x02\xBB\xDF
    第一个命令无所谓执行是否成功，只要第三个命令执行成功(状态代码是\x90\x00)，则是倍棒卡
     */
    private void checkIsBerbonCard(){
        mICCardOs.BerICCardConnectCard(new BerICCardOs.ConnectCardCallback() {
            @Override
            public void onReceiveBerbonConnectCard(int status, String desc, String csn, String ats) {
                if (status == 0 && csn !=null){
                    seleceApplet();
                }else{
                    checkIsBerbonCardCallback(3,"ConnectCard",status, desc, null);
                }
            }
        });

    }

    //0倍棒卡，1非倍棒卡，其它值检测失败
    private void checkIsBerbonCardCallback(int success, String step, int status, String desc,String data){
        String text="检测倍棒卡：\n";
        if (success == 0){
            text+="当前卡片是倍棒卡\n";
        }else if (success == 1){
            text+="当前卡片是未知卡\n";
        }else{
            text+="检测失败，失败步骤："+step+"。原因 status:"+status+",desc:"+desc+",data:"+data+"\n";
        }
        Message msg  =new Message();
        msg.what = 6;
        msg.arg1 = status;
        msg.obj = text;
        mHandler.sendMessage(msg);
    }

    private void seleceApplet(){
        //首先选择applet
        byte[] seleceApplet = new byte[11];
        //CLA
        seleceApplet[0] = (byte)0x00;
        //INS
        seleceApplet[1] = (byte)0xA4;
        //P1
        //  =’00’:按文件标识符(FID)选择 MF ,DF 或 EF
        //  =’04’:按文件名(AID)选择应用
        seleceApplet[2] = (byte)0x04;
        //P2
        //明华卡
        //  =’00’:第一个或仅有的一个
        //  =’02’:下一个
        //捷德卡
        //‘00’ 表示要求返回FCI；
        //‘0C’表示不返回 FCI。
        seleceApplet[3] = (byte)0x00;
        //Lc
        seleceApplet[4] = 6;
        System.arraycopy(HexAscByteUtil.hexStr2ByteArr("D15600000002"), 0, seleceApplet, 5, 6);

        mICCardOs.BerICCardSendAPDU(HexAscByteUtil.byteArr2HexStr(seleceApplet), new BerICCardOs.SendAPDUCallback() {
            @Override
            public void onReceiveBerbonSendAPDU(int status, String desc, String data) {
                if (status == 0) {
                    seleceMF();
                } else {
                    checkIsBerbonCardCallback(2,"seleceApplet",status, desc, data);
                }
            }
        });
    }

    private void seleceMF(){
        // 再执行选择MF
        byte[] seleceMF = new byte[7];
        seleceMF[0] = (byte) 0x00;    //CLA
        seleceMF[1] = (byte) 0xA4;    //INS
        seleceMF[2] = (byte) 0x00;    //P1 =’00’:按文件标识符(FID)选择 MF ,DF 或 EF
        seleceMF[3] = (byte) 0x00;      //=’00’:第一个或仅有的一个
        seleceMF[4] = (byte) 2;     //Lc
        System.arraycopy(HexAscByteUtil.hexStr2ByteArr("3F00"), 0, seleceMF, 5, 2);
        mICCardOs.BerICCardSendAPDU(HexAscByteUtil.byteArr2HexStr(seleceMF), new BerICCardOs.SendAPDUCallback() {
            @Override
            public void onReceiveBerbonSendAPDU(int status, String desc, String data)  {
                if (status == 0 && data.length()>=4) {
                    String code = data.substring(data.length()-4,data.length());
                    if (code.equals("9000")){
                        seleceBerbonADF();
                    }else{
                        checkIsBerbonCardCallback(1,"selectMF",status, desc, data);
                    }
                } else {
                    checkIsBerbonCardCallback(2,"selectMF",status, desc, data);
                }
            }
        });
    }

    private void seleceBerbonADF(){
        // 再执行选择MF
        byte[] seleceMF = new byte[7];
        seleceMF[0] = (byte) 0x00;    //CLA
        seleceMF[1] = (byte) 0xA4;    //INS
        seleceMF[2] = (byte) 0x00;    //P1 =’00’:按文件标识符(FID)选择 MF ,DF 或 EF
        seleceMF[3] = (byte) 0x00;      //=’00’:第一个或仅有的一个
        seleceMF[4] = (byte) 2;     //Lc
        System.arraycopy(HexAscByteUtil.hexStr2ByteArr("BBDF"), 0, seleceMF, 5, 2);
        mICCardOs.BerICCardSendAPDU(HexAscByteUtil.byteArr2HexStr(seleceMF), new BerICCardOs.SendAPDUCallback() {
            @Override
            public void onReceiveBerbonSendAPDU(int status, String desc, String data)  {
                if (status == 0 && data.length()>=4) {
                    String code = data.substring(data.length()-4,data.length());
                    if (code.equals("9000")){
                        checkIsBerbonCardCallback(0,"seleceBerbonADF",status, desc, data);
                    }else{
                        checkIsBerbonCardCallback(1,"seleceBerbonADF",status, desc, data);
                    }
                } else {
                    checkIsBerbonCardCallback(2,"seleceBerbonADF",status, desc, data);
                }
            }
        });
    }

    private void scroll2Bottom(final ScrollView scroll, final View inner) {
        //Handler handler = new Handler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (scroll == null || inner == null) {
                    return;
                }
                // 内层高度超过外层
                int offset = inner.getMeasuredHeight()
                        - scroll.getMeasuredHeight();
                if (offset < 0) {
                    System.out.println("定位...");
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }


}
