package com.ymss.iccard;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;

import com.huada.bluetoothcommunication.BlueToothCommunication;
import com.siodata.bleSdk.HexAscByteUtil;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by adminstrator on 2016/11/25.
 */

public class HuadaCardReader extends BerbonCardReader {
    private static final String LOGTAG = HuadaCardReader.class.getSimpleName();
    private static final String BER_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private Context mContext;

    public BluetoothAdapter mBluetoothAdapter;
    public HashMap<BluetoothDevice,Integer> searchedDeviceMap; //防止设备被重复搜索到

    Timer scanTimeOutTimer=null;
    Timer connectDeviceTimer=null;
    int timeoutMS=20*1000;
    int connectDeviceMs=18*1000;
    boolean isOpenBLEStartScaned=false;
    public boolean autoDisableIsCall=false;

    private CardReaderCallback mCallback;
    private BluetoothSocket btSocket;
    private BlueToothCommunication blueToothMinipay;
    private BluetoothDevice connectedDevice;


    public HuadaCardReader(Context context, CardReaderCallback callback){
        BluetoothManager bluetoothManager =(BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if(mBluetoothAdapter == null){
            Log.e(LOGTAG,"HuadaCardReader mBluetoothAdapter == null");
            return;
        }
        mContext = context;
        mCallback = callback;
        searchedDeviceMap=new HashMap<BluetoothDevice, Integer>();

        mContext.registerReceiver(bluetoothState,new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        mContext.registerReceiver(bluetoothState,new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        mContext.registerReceiver(bluetoothState,new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));

        mContext.registerReceiver(bluetoothState,new IntentFilter(BluetoothDevice.ACTION_FOUND));    //add by fxm for huada 20160422
        mContext.registerReceiver(bluetoothState,new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
    }

    private boolean checkHuaDaDeviceIsConnect(){
        if(mBluetoothAdapter == null || mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF
                || !mBluetoothAdapter.isEnabled()
                ||btSocket==null || blueToothMinipay == null || connectedDevice== null){
            return false;
        }
        return true;
    }


    BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch(state) {
                case BluetoothAdapter.STATE_OFF:
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.e(LOGTAG, "bolth turn on");
                    if(isOpenBLEStartScaned){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                isOpenBLEStartScaned=false;
                                scanHuaDaDeviceStart();
                            }
                        }, 1500);
                    }
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    //isBLEcloseing=true;
                    if (connectedDevice!=null){
                        //execDeviceStatusNotify(false);
                        if (autoDisableIsCall) {
                            mCallback.onReceiveBerbonDeviceStatusNotify(0, "成功", 0);
                        }
                    }
                    connectedDevice=null;
                    break;
            }
            if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())){
                BluetoothDevice device = (BluetoothDevice)intent.getExtras().get(BluetoothDevice.EXTRA_DEVICE);
                if (connectedDevice!=null && device!=null && connectedDevice.getAddress().equals(device.getAddress()) == true) {
                    //execDeviceStatusNotify(false);
                    if (autoDisableIsCall) {
                        mCallback.onReceiveBerbonDeviceStatusNotify(0, "成功", 0);
                    }
                    connectedDevice=null;
                }
                return;
            }
            //if (connectedDevice!=null){   //华大读卡器
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = (BluetoothDevice)intent
                            .getExtras().get(BluetoothDevice.EXTRA_DEVICE);
                    // If it's already paired, skip it, because it's been listed
                    // already
                    Integer index=searchedDeviceMap.get(device);
                    if(index==null){
                        searchedDeviceMap.put(device, new Integer(1));
                        String name;
                        //if (device.getName() == null || device.getName().equals("Feasycom") == false) {
                        if (device.getName() == null) {
                            name = "未知";    //将其它无关设备通过名称过滤掉
                        } else {
                            name = device.getName();
                        }
                        Log.i(LOGTAG, "search huada " + device.getName());
                        //execSearchCallBack(0, "成功", device.getAddress(), name);
                        mCallback.onReceiveBerbonSearchDevice(0, "成功",device.getAddress(),name);
                    }
                    // When discovery is finished, change the Activity title
                }
                else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
                {
                    if (searchedDeviceMap.size() == 0){   //如果没有搜索到任何结果，则认为超时,否则不做任何处理
                        //execSearchCallBack(1, "没有搜索到设备",null,null);
                        mCallback.onReceiveBerbonSearchDevice(1, "没有搜索到设备",null,null);
                    }
                    else{
                        Log.i(LOGTAG, "discovery is finished");
                    }
                }
           // }
        }

    };

    private void stopScanTimeOutTimer(){
        if(scanTimeOutTimer!=null){
            scanTimeOutTimer.cancel();
            scanTimeOutTimer=null;
        }
    }

    private void stopConnectDeviceTimer(){
        if(connectDeviceTimer!=null){
            connectDeviceTimer.cancel();
            connectDeviceTimer=null;
        }
    }

    private void startConnectDeviceTimer(int timeoutMS){
        connectDeviceTimer=new Timer(true);
        connectDeviceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Log.e(LOGTAG, "BerICCardConnectDevice time out");
                try{
                    if (btSocket!=null){
                        btSocket.close();
                        btSocket = null;
                    }
                    if (blueToothMinipay!=null){
                        blueToothMinipay.closeDevice();
                        blueToothMinipay = null;
                    }
                } catch (IOException e) {
                    Log.e(LOGTAG, e.toString());
                }
                connectedDevice = null;
                execConnectedSuccessBack(10, "连接超时");
               // mCallback.onReceiveBerbonConnectDevice(10, "连接超时");
            }
        }, timeoutMS);
    }

    private void scanHuaDaDeviceStart(){
        stopScanTimeOutTimer();
        //deviceArray.clear();
        searchedDeviceMap.clear();
        mBluetoothAdapter.startDiscovery();
        scanTimeOutTimer=new Timer(true);
        scanTimeOutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mBluetoothAdapter != null)
                {
                    mBluetoothAdapter.cancelDiscovery();
                }
                scanTimeOutTimer=null;
                //execSearchCallBack(1,"搜索超时", null,null);
                mCallback.onReceiveBerbonSearchDevice(2, "搜索超时",null,null);
                Log.i(LOGTAG, "Bluetooth Scan time out");
            }
        }, timeoutMS);
    }

    //退出销毁函数
    public void releaseDevice(){
        try {
            if ((btSocket != null) && (blueToothMinipay != null)) {
                btSocket.close();
                blueToothMinipay.closeDevice();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mContext.unregisterReceiver(bluetoothState);
    }

    /*描述：查询系统是否支持ICCard功能
参数：
返回：bool 是否支持*/
    public boolean isDeviceSupportICCard(){
        return true;
    }

    /* 描述：搜索读卡器设备
参数：timeout:int 超时时间(毫秒)，超出设置时间未找到设备就会回调
返回：void*/
    public void searchDevice(int timeout){
        if(timeout>5000 && timeout <60*1000){
            timeoutMS=timeout;
        }
        if(!mBluetoothAdapter.isEnabled()){
            isOpenBLEStartScaned=true;
            mBluetoothAdapter.enable();
            Log.i(LOGTAG, "Bluetooth not turn on");
            return;
        }
        scanHuaDaDeviceStart();

    }

    /*描述：描述：停止搜索读卡器设备
返回：void*/
    public void stopSearchDevice(){
        stopScanTimeOutTimer();
        if (mBluetoothAdapter != null)
        {
            mBluetoothAdapter.cancelDiscovery();
        }
        mCallback.onReceiveBerbonStopSearchDevice(0, "成功");
    }

    /*描述：描述：连接蓝牙设备
参数：addr:string 蓝牙地址/addr
返回：void*/
    public void connectDevice(int timeout, String address){
        connectDeviceMs = timeout;
        if(!mBluetoothAdapter.isEnabled()){
            //execConnectedSuccessBack(10, "连接失败，蓝牙未打开");
            mCallback.onReceiveBerbonConnectDevice(10, "连接失败，蓝牙未打开");
            return;
        }
        stopConnectDeviceTimer();
        final BluetoothDevice device=mBluetoothAdapter.getRemoteDevice(address);
        if( device!=null){
                // String address = device.getAddress();
                UUID uuid = UUID.fromString(BER_UUID);
                try {
                    if (btSocket != null) {
                        btSocket.close();
                        btSocket = null;
                    }
                    if (blueToothMinipay != null) {
                        blueToothMinipay.closeDevice();
                        blueToothMinipay = null;
                    }
                    btSocket = device.createRfcommSocketToServiceRecord(uuid);
                    startConnectDeviceTimer(timeout);
                    new Thread(){
                        public void run(){
                            try {
                                Log.i(LOGTAG, "connect device");
                                btSocket.connect();
                                blueToothMinipay = new BlueToothCommunication(
                                        btSocket.getInputStream(),
                                        btSocket.getOutputStream());
                                Log.i(LOGTAG, "connect device success");
                                connectedDevice=device;
                                execConnectedSuccessBack(0, "连接成功");
                            } catch (IOException e) {
                                Log.e(LOGTAG, e.toString());
                                try {
                                    //if (connectedSuccessBack!= null) {   //此处如果不等于null,则说明还未超时，继续重试连接
                                        Log.e(LOGTAG, "connect fail,but trying fallback...");
                                        btSocket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                                        btSocket.connect();
                                        blueToothMinipay = new BlueToothCommunication(
                                                btSocket.getInputStream(),
                                                btSocket.getOutputStream());
                                        Log.i(LOGTAG, "connect device createRfcommSocket success");
                                        connectedDevice = device;
                                        execConnectedSuccessBack(0, "连接成功");
                                   // }
                                }
                                catch (Exception e2) {
                                    Log.e(LOGTAG, e2.toString());
                                    execConnectedSuccessBack(100, "连接失败("+e2.toString()+")");
                                }
                            }
                        }
                    }.start();

                } catch (IOException e) {
                    //e.printStackTrace();
                    Log.i(LOGTAG, e.toString());
                    //connecedCallBack(connectedSuccessBack,connectedSuccessBackParam,null,null);
                    execConnectedSuccessBack(2, "连接失败("+e.toString()+")");
                }
        }else{
            //connecedCallBack(connectedSuccessBack,connectedSuccessBackParam,null,null);
            execConnectedSuccessBack(4, "非法的uuid");
        }
    }

    private void execConnectedSuccessBack(int status,String desc){
        stopConnectDeviceTimer();
        mCallback.onReceiveBerbonConnectDevice(status, desc);
    }

    /*描述： 描述：断开蓝牙设备
参数：addr:string 蓝牙地址/addr
返回：void*/
    public void disconnectDevice(String addr){
        try{
            Log.i(LOGTAG, "disconnect device success");
            connectedDevice = null;
            if (btSocket!=null){
                btSocket.close();
                btSocket = null;
            }
            if (blueToothMinipay!=null){
                blueToothMinipay.closeDevice();
                blueToothMinipay = null;
            }
            //execDisconnectedSuccessBack(0, "断开成功");
            mCallback.onReceiveBerbonDisconnectDevice(0,"断开成功");
        } catch (IOException e) {
            Log.e(LOGTAG, e.toString());
            //execDisconnectedSuccessBack(0, "断开成功");
            mCallback.onReceiveBerbonDisconnectDevice(1,"断开失败");
        }
    }

    /*描述：描述：获取设备详细信息
返回：void*/
    public void getDeviceStatus(){
        new Thread(){
            public void run(){
                Log.i(LOGTAG, "get device status");
                if(checkHuaDaDeviceIsConnect() == false){
                    //execStatusCallBack(1,"还未连接读卡器",null,null,null,null,null,null,0);
                    mCallback.onReceiveBerbonGetDeviceStatus(1,"还未连接读卡器",null);
                    return;
                }
                if (connectedDevice!=null){
                    Log.i(LOGTAG, "get device status success");
                    byte[] versionInformation = new byte[200];
                    Arrays.fill(versionInformation, (byte) 0x00);
                    int re = blueToothMinipay.readVersionInformation(versionInformation);
                    if (re > 0){
                        try {
                            String sv = null;
                            String hv = null;
                            XmlPullParser parser = Xml.newPullParser(); //由android.util.Xml创建一个XmlPullParser实例
                            ByteArrayInputStream is = new ByteArrayInputStream(versionInformation);
                            parser.setInput(is, "UTF-8");               //设置输入流 并指明编码方式
                            int eventType = parser.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;
                                    case XmlPullParser.START_TAG:
                                        if (parser.getName().equals("SV")) {
                                            parser.next();
                                            sv=parser.getText();
                                        } else if (parser.getName().equals("HV")) {
                                            parser.next();
                                            hv=parser.getText();
                                        }
                                        break;
                                    case XmlPullParser.END_TAG:
                                        break;
                                }
                                eventType = parser.next();
                            }
                            //execStatusCallBack(0,"获取成功",null,hv,sv,null,connectedDevice.getName(),connectedDevice.getAddress(),0);
                            BerbonCardReaderInfo info = new BerbonCardReaderInfo();
                            info.setBtName(connectedDevice.getName());
                            info.setBtAddr(connectedDevice.getAddress());
                            //info.setProdModel(arg0.getStrProdModel());
                            info.setProdHwVer(hv);
                            info.setProdSwVer(sv);
                            //info.setVoltPercent(arg0.getBytVoltPercent());
                            //info.setChipUid(HexAscByteUtil.byteArr2HexStr(arg0.getBytChipUid()));
                            mCallback.onReceiveBerbonGetDeviceStatus(0,"获取成功",info);
                        }catch (Exception e){
                            e.printStackTrace();
                            //execStatusCallBack(101,"获取失败("+e.getMessage()+")",null,null,null,null,null,null,0);
                            mCallback.onReceiveBerbonGetDeviceStatus(101,"获取失败("+e.getMessage()+")",null);
                        }
                    }else{
                        //execStatusCallBack(1,"获取失败",null,null,null,null,null,null,0);
                        mCallback.onReceiveBerbonGetDeviceStatus(1,"获取失败",null);
                    }
                }else{
                    //execStatusCallBack(2,"还未连接读卡器",null,null,null,null,null,null,0);
                    mCallback.onReceiveBerbonGetDeviceStatus(2,"还未连接读卡器",null);
                }
            }
        }.start();
    }

    /* 描述：描述：连接卡片
返回：void*/
    public void connectCard(){
        execConnectCard(false);
    }

    private void execConnectCard(final boolean isGetStauts){
        new Thread(){
            public void run(){
                if(checkHuaDaDeviceIsConnect() == false){
                    //execConnectCardCallBack(6, "未连接设备",null, null);
                    if (isGetStauts){
                        mCallback.onReceiveBerbonGetCardStatus(6, "未连接设备",null, null);
                    }else{
                        mCallback.onReceiveBerbonConnectCard(6, "未连接设备",null, null);
                    }
                    return;
                }
                Log.i(LOGTAG, "connect card");
                byte[] response = new byte[256];
                Arrays.fill(response, (byte) 0x00);
                int re = blueToothMinipay.PICC_Reader_pre_PowerOn(response);
                try {
                    if (re > 2){
                        int snLen=response[1];
                        byte[] sn = new byte[snLen];
                        System.arraycopy(response, 2, sn, 0, snLen);
                        int atsLen=response[1+snLen+2];
                        if (snLen > 0 && atsLen > 0) {
                            byte[] ats = new byte[atsLen];
                            System.arraycopy(response, 2 + snLen + 2, ats, 0, atsLen);
                            //connectedCardSN = HexAscByteUtil.byteArr2HexStr(sn);
                            //connectedCardATS = HexAscByteUtil.byteArr2HexStr(ats);
                            //Log.i(LOGTAG, "connect card success");
                            //execConnectCardCallBack(0, "成功", connectedCardSN, connectedCardATS);
                            if (isGetStauts){
                                mCallback.onReceiveBerbonGetCardStatus(0, "连接成功", HexAscByteUtil.byteArr2HexStr(sn), HexAscByteUtil.byteArr2HexStr(ats));
                            }else {
                                mCallback.onReceiveBerbonConnectCard(0, "连接成功", HexAscByteUtil.byteArr2HexStr(sn), HexAscByteUtil.byteArr2HexStr(ats));
                            }
                        }else{
                            //execConnectCardCallBack(3, "连接卡片失败,数据异常",null, null);
                            if (isGetStauts){
                                mCallback.onReceiveBerbonGetCardStatus(3, "连接卡片失败,数据异常", null, null);
                            }else {
                                mCallback.onReceiveBerbonConnectCard(3, "连接卡片失败,数据异常", null, null);
                            }
                        }
                    }
                    else{
                        //execConnectCardCallBack(1, "连接卡片失败",null, null);
                        if (isGetStauts){
                            mCallback.onReceiveBerbonGetCardStatus(1, "连接卡片失败", null, null);
                        }else {
                            mCallback.onReceiveBerbonConnectCard(1, "连接卡片失败", null, null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //execConnectCardCallBack(102, "连接卡片失败("+e.getMessage()+")",null, null);
                    if (isGetStauts){
                        mCallback.onReceiveBerbonGetCardStatus(102, "连接卡片失败(" + e.getMessage() + ")", null, null);
                    }else {
                        mCallback.onReceiveBerbonConnectCard(102, "连接卡片失败(" + e.getMessage() + ")", null, null);
                    }
                }
            }
        }.start();
    }

    /* 描述：描述：断开卡片
 返回：void*/
    public void disconnectCard(){
        Log.i(LOGTAG, "disconnect card");
        //connectedCardSN = null;
        //connectedCardATS = null;
        //execDisconnectCardCallBack(0, "成功");
        mCallback.onReceiveBerbonDisconnectCard(0,"成功");
    }

    /*描述：描述：获取卡片信息
返回：void*/
//    public void getCardStatus(){
//        execConnectCard(true);
//    }

    /*描述：描述：发送APDU命令
参数：cmd:string 要发送的APDU命令
返回：void*/
    public void sendAPDU(final String cmd){
        if (checkHuaDaDeviceIsConnect() == false){
            //execApduCallBack(10,"设备未连接", null,0);
            mCallback.onReceiveBerbonSendAPDU(10,"设备未连接", null);
            return;
        }

        new Thread(){
            public void run(){
                byte ICC_Slot_No=0x01;/*大卡座=0x01 ，PSAM1 =0x11，PSAM2=0x12 */
                byte[] ReadBuffer = new byte[2048];
                int ReadLength = 0;
                ReadLength = blueToothMinipay.ICC_Reader_Application_HID(ICC_Slot_No,cmd, ReadBuffer);
                //Log.i(TAG,"data=" +data + ",ReadLength=" + ReadLength);
                //execApduCallBack(0,"卡片返回数据成功", ReadBuffer,ReadLength);

                if (ReadLength >0) {
                    byte[] buffer = new byte[ReadLength];
                    System.arraycopy(ReadBuffer,0,buffer,0,ReadLength);
                    mCallback.onReceiveBerbonSendAPDU(0,"卡片返回数据成功", HexAscByteUtil.byteArr2HexStr(buffer));
                }else{
                    mCallback.onReceiveBerbonSendAPDU(1,"卡片返回数据失败", null);
                }

            }
        }.start();
    }

    /*描述：在读卡器屏幕显示消息(仅华大读卡器支持)
参数：msg:string 要显示的消息(最多支持16个英文字符或者8个中文字符,中文符号不支持)
      line:int 显示在哪一行(1~4,第一行显示会和电量显示冲突，故最好从第二行开始显示)
     col:int 显示在哪一列(1~8)
返回：void
*/
    public void showMessage(String msg, int line, int col){
        try {
            if (checkHuaDaDeviceIsConnect() == false){
                mCallback.onReceiveBerbonShowMessage(4, "还未连接设备");
                return;
            }
            byte[] strData = msg.getBytes("GBK");
            byte[] readBuffer = new byte[1024];
            int ReadLength = blueToothMinipay.screenControl(line, col, strData, strData.length, readBuffer);
            if (ReadLength == 0) {
                mCallback.onReceiveBerbonShowMessage(0, "成功");
            } else {
                mCallback.onReceiveBerbonShowMessage(1, "失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            mCallback.onReceiveBerbonShowMessage(103, e.getMessage());
        }
    }

    /* 描述：清除读卡器屏幕显示(仅华大读卡器支持)
参数：type:int 0表示清除屏幕，1表示显示默认主屏
返回：void*/
    public void clearScreen(int type){
        if (checkHuaDaDeviceIsConnect() == false){
            mCallback.onReceiveBerbonClearScreen(4, "还未连接设备");
            return;
        }
        byte[] readBuffer = new byte[1024];
        int ReadLength = blueToothMinipay.clearMiniPayScreen(type,readBuffer);
        if (ReadLength == 0) {
            mCallback.onReceiveBerbonClearScreen(0, "成功");
        } else {
            mCallback.onReceiveBerbonClearScreen(1, "失败");
        }
    }

    /*描述：设置当读卡器设备蓝牙断开时是否通知客户端
 参数：enable: 是否打开通知
 返回：void*/
    public void enableDeviceStatusNotify(boolean enable){
        autoDisableIsCall=enable;
    }

    /*描述：获取输入的明文密码。目前华大的读卡器只支持6位的纯数字密码。输入后需要用户按确认键才能收到。另，实际测试发现有诸多显示及流程上的问题，但是产品及领导不care，平台不为此负责。(相关问题已反馈sdk方，如果对方处理，平台将更新并删除相关描述)
 返回：void*/
    public void getPlainTextPassword(){
        if (checkHuaDaDeviceIsConnect() == false) {
            mCallback.onReceiveBerbonGetPlainTextPassword(4, "还未连接设备",null);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] readBuffer = new byte[1024];
                int ReadLength = blueToothMinipay.ICC_Reader_GetPass_HID(0x00,0x04,0x0a,readBuffer);
                if (ReadLength == 6) {
                    StringBuffer password = new StringBuffer(ReadLength);
                    for(int i=0;i<ReadLength;i++){
                        password.append(readBuffer[i]);
                    }
                    //execPlainTextPasswordCallback(0, "成功", password.toString());
                    mCallback.onReceiveBerbonGetPlainTextPassword(0, "成功",password.toString());
                } else {
                    //execPlainTextPasswordCallback(3, "用户已取消", null);
                    mCallback.onReceiveBerbonGetPlainTextPassword(3, "用户已取消",null);
                }
            }
        }).start();
    }

    /*描述：修改设备蓝牙名称
参数：btName:string 设备新蓝牙名
返回：void*/
    public void setDeviceBtName(String btName){
        if (checkHuaDaDeviceIsConnect() == false) {
            mCallback.onReceiveBerbonSetDeviceBtName(4, "还未连接设备");
            return;
        }
        mCallback.onReceiveBerbonSetDeviceBtName(5, "暂不支持");
    }

}
