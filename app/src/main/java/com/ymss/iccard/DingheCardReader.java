package com.ymss.iccard;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.siodata.bleSdk.BleDeviceInitInfo;
import com.siodata.bleSdk.BleDeviceVisualInfo;
import com.siodata.bleSdk.HexAscByteUtil;
import com.siodata.bleSdk.TPSDeviceManager;
import com.siodata.bleSdk.TPSDeviceManagerCallback;
import com.siodata.bleSdk.TPScanner;
import com.siodata.bleSdk.TPScannerCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by adminstrator on 2016/11/25.
 */

public class DingheCardReader extends BerbonCardReader implements TPSDeviceManagerCallback,TPScannerCallback {

    private static final String LOGTAG = DingheCardReader.class.getSimpleName();

    private TPSDeviceManager mRequestPosImpl;
    private TPScanner scanner;
    public HashMap<BluetoothDevice,Integer> searchedDeviceMap; //防止设备被重复搜索到
    private Context mContext;
    public boolean isSupportBLE=true;
    private CardReaderCallback mCallback;
    int timeoutMS=20*1000;
    private Timer scanTimeOutTimer=null;
    int connectDeviceMs=18*1000;
    Timer connectDeviceTimer=null;
    public boolean autoDisableIsCall=false;
    public String connectedDevice = null;

    public DingheCardReader(Context context, CardReaderCallback callback){
        mContext = context;
        mCallback = callback;
        checkBLE();
        if(!isSupportBLE){
            return;
        }
        searchedDeviceMap=new HashMap<BluetoothDevice, Integer>();
        mRequestPosImpl=new TPSDeviceManager(context);
        mRequestPosImpl.setCallBack(this);
        scanner = new TPScanner(context, this);
    }

    public void checkBLE(){
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
                || android.os.Build.VERSION.SDK_INT < 18) {
            isSupportBLE=false;
        }
    }

    // --------------- TPScanner Interface --------------
    @Override
    public void onReceiveScanDevice(final BluetoothDevice device, int rssi, byte[] scanRecord) {
        stopScanTimeOutTimer();
        Integer index=searchedDeviceMap.get(device);
        if(index==null){
            searchedDeviceMap.put(device, new Integer(1));
            String name;
            if(device.getName()==null){
                name="未知";
            }else{
                name=device.getName();
            }
            mCallback.onReceiveBerbonSearchDevice(0, "成功",device.getAddress(),name);
            Log.i(LOGTAG, "onReceiveScanDevice "+device.getName());
        }
    }
    @Override
    public void onScanDeviceStopped() {
        mCallback.onReceiveBerbonStopSearchDevice(0, "成功");
    }

    /*描述：查询系统是否支持ICCard功能
参数：
返回：bool 是否支持*/
    public boolean isDeviceSupportICCard(){
        if (isSupportBLE){
            return true;
        }
        return false;
    }

    private void stopScanTimeOutTimer(){
        if(scanTimeOutTimer!=null){
            scanTimeOutTimer.cancel();
            scanTimeOutTimer=null;
        }
    }

    public void icStartScan(){
        searchedDeviceMap.clear();
        stopScanTimeOutTimer();
        scanner.startScan();
        scanTimeOutTimer=new Timer(true);
        scanTimeOutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(scanner.isScanning()){
                    scanner.stopScan();
                }
                scanTimeOutTimer=null;
                //execSearchCallBack(1,"搜索超时", null,null);
                mCallback.onReceiveBerbonSearchDevice(1, "搜索超时",null,null);
                Log.e(LOGTAG, "Bluetooth Scan time out");
            }
        }, timeoutMS);

    }

    //退出销毁函数
    public void releaseDevice(){
        if(!isSupportBLE){
            return;
        }
        if(null!=mRequestPosImpl){
            mRequestPosImpl.requestDisConnectDevice();
            mRequestPosImpl = null;
        }
        //mContext.unregisterReceiver(bluetoothState);
    }

    /* 描述：搜索读卡器设备
参数：timeout:int 超时时间(毫秒)，超出设置时间未找到设备就会回调
返回：void*/
    public void searchDevice(int timeout){
        if(timeout>5000 && timeout <60*1000){
            timeoutMS=timeout;
        }
        //searchCallBack=callBack;
        /*if(isBLEcloseing ||isBLEOpenging){
            execSearchCallBack(4, "蓝牙未打开",null,null);
            return;
        }*/
        if(isSupportBLE){
            /*if(!mBluetoothAdapter.isEnabled()){
                isOpenBLEStartScaned=true;
                mBluetoothAdapter.enable();
                Log.i(LOGTAG, "Bluetooth not turn on");
                return;
            }else{*/

                    if(scanner.isScanning()){
                        scanner.stopScan();
                    }
                    icStartScan();
            //}
        }else{
            //execSearchCallBack(5,"手机不支持BLE蓝牙", null,null);
            mCallback.onReceiveBerbonSearchDevice(5, "手机不支持BLE蓝牙",null,null);
        }
    }

    /*描述：描述：停止搜索读卡器设备
返回：void*/
    public void stopSearchDevice(){
        stopScanTimeOutTimer();
        if(isSupportBLE){
            if(scanner.isScanning()){
                scanner.stopScan();
            } else{
                mCallback.onReceiveBerbonStopSearchDevice(0, "成功");
            }
        }else{
            mCallback.onReceiveBerbonStopSearchDevice(5, "手机不支持BLE蓝牙");
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
                if(connectedDevice!=null){
                    mRequestPosImpl.requestDisConnectDevice();
                }

                connectedDevice = null;
                //execConnectedSuccessBack(3, "连接超时");
                mCallback.onReceiveBerbonConnectDevice(3, "连接超时");
            }
        }, timeoutMS);
    }

    /*描述：描述：连接蓝牙设备
参数：addr:string 蓝牙地址/addr
返回：void*/
    public void connectDevice(int timeout, String addr){
        connectDeviceMs = timeout;
        stopConnectDeviceTimer();
        //BluetoothDevice device=mBluetoothAdapter.getRemoteDevice(addr);
       // if( device!=null){
            //isConnced=false;
            connectedDevice=addr;
            mRequestPosImpl.requestConnectBleDevice(addr);
            //Log.e(TAG, "BerICCardConnectDevice");
            startConnectDeviceTimer(timeout);
        //}

    }

    /*描述： 描述：断开蓝牙设备
参数：addr:string 蓝牙地址/addr
返回：void*/
    public void disconnectDevice(String addr){
        if(connectedDevice!=null){
            mRequestPosImpl.requestDisConnectDevice();
        }else{
            //execDisconnectedSuccessBack(0, "断开成功");
            mCallback.onReceiveBerbonDisconnectDevice(0, "断开成功");
        }
    }

    /*描述：描述：获取设备详细信息
返回：void*/
    public void getDeviceStatus(){
        if(!isSupportBLE){
            //execStatusCallBack(3,"蓝牙未打开",null,null,null,null,null,null,0);
            mCallback.onReceiveBerbonGetDeviceStatus(3,"不支持BLE蓝牙",null);
            return;
        }
        mRequestPosImpl.requestConnectionStatus();
    }

    /* 描述：描述：连接卡片
返回：void*/
    public void connectCard(){
        byte bytCardType=1;
        mRequestPosImpl.requestRfmSearchCard(bytCardType);
    }

    /* 描述：描述：断开卡片
 返回：void*/
    public void disconnectCard(){
        if(connectedDevice!=null){
            mRequestPosImpl.requestRfmClose();
        }else{
            //execDisconnectCardCallBack(0, "成功");
            mCallback.onReceiveBerbonDisconnectCard(0,"成功");
        }
    }

    /*描述：描述：获取卡片信息
返回：void*/
//    public void getCardStatus(){
//
//    }

    /*描述：描述：发送APDU命令
参数：cmd:string 要发送的APDU命令
返回：void*/
    public void sendAPDU(String cmd){
        if(connectedDevice == null){  //设备未连接
            //execApduCallBack(2,"设备未连接", null,0);
            mCallback.onReceiveBerbonSendAPDU(2,"设备未连接", null);
            return;
        }
        mRequestPosImpl.sendApduCmd(HexAscByteUtil.hexStr2ByteArr(cmd));
    }

    /*描述：在读卡器屏幕显示消息(仅华大读卡器支持)
参数：msg:string 要显示的消息(最多支持16个英文字符或者8个中文字符,中文符号不支持)
      line:int 显示在哪一行(1~4,第一行显示会和电量显示冲突，故最好从第二行开始显示)
     col:int 显示在哪一列(1~8)
返回：void
*/
    public void showMessage(String msg, int line, int col){
        mCallback.onReceiveBerbonShowMessage(1, "设备不支持");
    }

    /* 描述：清除读卡器屏幕显示(仅华大读卡器支持)
参数：type:int 0表示清除屏幕，1表示显示默认主屏
返回：void*/
    public void clearScreen(int type){
        mCallback.onReceiveBerbonClearScreen(1, "设备不支持");
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
        mCallback.onReceiveBerbonGetPlainTextPassword(1, "设备不支持",null);
    }

    @Override
    public void setDeviceBtName(String btName) {
        BleDeviceVisualInfo bleDeviceVisualInfo=new BleDeviceVisualInfo();
        bleDeviceVisualInfo.setStrBtName(btName);
        //bleDeviceVisualInfo.setStrProdSn("12345678");
        //bleDeviceVisualInfo.setStrBtName(BleDeviceVisualInfo.strAttributeNeed);
        //bleDeviceVisualInfo.setStrBtAddr(BleDeviceVisualInfo.strAttributeNeed);
        //bleDeviceVisualInfo.setBytChipUid(new byte[]{BleDeviceVisualInfo.bytAttributeNeed});
        //bleDeviceVisualInfo.setBytVoltPercent(BleDeviceVisualInfo.bytAttributeNeed);
        //bleDeviceVisualInfo.setBytAmkVer(BleDeviceVisualInfo.bytAttributeNeed);
        //bleDeviceVisualInfo.setBytTdkVer(BleDeviceVisualInfo.bytAttributeNeed);
        //bleDeviceVisualInfo.setBytProdHwVer(BleDeviceVisualInfo.bytAttributeNeed);
        //bleDeviceVisualInfo.setBytProdSwVer(BleDeviceVisualInfo.bytAttributeNeed);
        mRequestPosImpl.requestSetDeviceInitInfo(bleDeviceVisualInfo);
    }


    @Override
    public void onReceiveConnectBtDevice(boolean arg0) {
        // TODO Auto-generated method stub
        //	BluetoothDevice device=icCardOs.deviceArray.get(icCardOs.connectedDevice);
        Log.e(LOGTAG, "ssss onReceiveConnectBtDevice "+arg0);
        stopConnectDeviceTimer();
        if( arg0){
            //execConnectedSuccessBack(0, "连接读卡器成功");
            mCallback.onReceiveBerbonConnectDevice(0, "连接读卡器成功");
        }else{
            connectedDevice=null;
            //execConnectedSuccessBack(5, "连接读卡器失败");
            mCallback.onReceiveBerbonConnectDevice(5, "连接读卡器失败");
        }
    }

    @Override
    public void onReceiveConnectionStatus(boolean arg0) {
        // TODO Auto-generated method stub
        Log.i(LOGTAG, "ssss onReceiveConnectionStatus");
        if( connectedDevice!=null && arg0){
            mRequestPosImpl.requestGetDeviceVisualInfo();
        }else{
            //execStatusCallBack(4,"获取状态信息失败",null,null,null,null,null,null,0);
            mCallback.onReceiveBerbonGetDeviceStatus(4,"获取状态信息失败",null);
        }
    }

    @Override
    public void onReceiveDeviceUpdate(boolean arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveDeviceUpdateProgress(float arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveDisConnectDevice(boolean arg0) {
        // TODO Auto-generated method stub
        Log.e(LOGTAG, "ssss onReceiveDisConnectDevice "+arg0);
        connectedDevice =null;
        if (connectedDevice !=null) {
            if (arg0) { //断开成功
                mCallback.onReceiveBerbonDisconnectDevice(0,"断开成功");
                /*if (disconnectedSuccessBack == null){   //如果没有回调，则发送消息

                    execDeviceStatusNotify(false);
                }else {
                    execDisconnectedSuccessBack(0, "断开成功");
                }*/
            } else {
                //execDisconnectedSuccessBack(1, "断开失败");
                mCallback.onReceiveBerbonDisconnectDevice(1,"断开失败");
            }
        }else{
            if (autoDisableIsCall) {
                mCallback.onReceiveBerbonDeviceStatusNotify(0, "成功", 0);
            }
        }
    }

    @Override
    public void onReceiveGetDeviceVisualInfo(BleDeviceVisualInfo arg0) {
        // TODO Auto-generated method stub
        Log.d(LOGTAG, "ssss onReceiveGetDeviceVisualInfo "+arg0);
        if(arg0!=null){
            String name=arg0.getStrBtName();
            if( name==null || name.length()==0 ){
                name="unknow";
            }
           // execStatusCallBack(0,"获取成功",arg0.getStrProdModel(),Byte.toString(arg0.getBytProdHwVer()),Byte.toString(arg0.getBytProdSwVer()),
             //       HexAscByteUtil.byteArr2HexStr(arg0.getBytChipUid()),name,arg0.getStrBtAddr(),arg0.getBytVoltPercent());
            BerbonCardReaderInfo info = new BerbonCardReaderInfo();
            info.setBtName(name);
            info.setBtAddr(arg0.getStrBtAddr());
            info.setProdModel(arg0.getStrProdModel());
            info.setProdHwVer(Byte.toString(arg0.getBytProdHwVer()));
            info.setProdSwVer(Byte.toString(arg0.getBytProdSwVer()));
            info.setVoltPercent(arg0.getBytVoltPercent());
            info.setChipUid(HexAscByteUtil.byteArr2HexStr(arg0.getBytChipUid()));
            mCallback.onReceiveBerbonGetDeviceStatus(0,"获取成功",info);
            Log.i(LOGTAG, "onReceiveGetDeviceVisualInfo success");
        }else{
            //execStatusCallBack(5,"获取状态信息失败",null,null,null,null,null,null,0);
            mCallback.onReceiveBerbonGetDeviceStatus(0,"获取成功",null);
            Log.i(LOGTAG, "onReceiveGetDeviceVisualInfo fail");
        }
    }

    @Override
    public void onReceiveInitCiphy(boolean arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveRfmClose(boolean arg0) {
        // TODO Auto-generated method stub
        Log.i(LOGTAG, "onReceiveRfmClose");
        //connectedCardSN=null;
        //connectedCardATS=null;
        if(arg0){
            //execDisconnectCardCallBack(0, "成功");
            mCallback.onReceiveBerbonDisconnectCard(0,"成功");
        }else{
            //execDisconnectCardCallBack(1, "失败");
            mCallback.onReceiveBerbonDisconnectCard(1,"失败");
        }
    }

    @Override
    public void onReceiveRfmSearchCard(boolean blnIsSuc, byte[] bytCardSn,byte[] bytCardATS) {
        Log.e(LOGTAG, "onReceiveRfmSearchCard");
        if(blnIsSuc && bytCardSn!=null && bytCardATS!=null ){
            //connectedCardSN=HexAscByteUtil.byteArr2HexStr(bytCardSn);
            //connectedCardATS=HexAscByteUtil.byteArr2HexStr(bytCardATS);

            //execConnectCardCallBack(0, "连接成功",HexAscByteUtil.byteArr2HexStr(bytCardSn), HexAscByteUtil.byteArr2HexStr(bytCardATS));
            mCallback.onReceiveBerbonConnectCard(0, "连接成功",HexAscByteUtil.byteArr2HexStr(bytCardSn), HexAscByteUtil.byteArr2HexStr(bytCardATS));
            Log.i(LOGTAG, "onReceiveRfmSearchCard 111111");
        }else{
            //connectedCardSN=null;
            //connectedCardATS=null;
            //execConnectCardCallBack(3, "连接卡片失败",null, null);
            mCallback.onReceiveBerbonConnectCard(1, "连接卡片失败",null,null);
            Log.e(LOGTAG, "onReceiveRfmSearchCard 2222222");
        }

    }

    @Override
    public void onReceiveRfmSentApduCmd(byte[] arg0) {
        // TODO Auto-generated method stub
        if(arg0==null) {
            //execApduCallBack(5, "卡片返回数据失败", null, 0);
            mCallback.onReceiveBerbonSendAPDU(1, "连接卡片失败",null);
        }else {
            //execApduCallBack(0, "卡片返回数据成功", arg0, arg0.length);
            mCallback.onReceiveBerbonSendAPDU(0, "卡片返回数据成功",HexAscByteUtil.byteArr2HexStr(arg0));
        }
    }

    @Override
    public void onReceiveSetDeviceInitInfo(BleDeviceInitInfo arg0) {
        // TODO Auto-generated method stub
        if(arg0!=null && !arg0.getStrBtName().equalsIgnoreCase(BleDeviceInitInfo.strAttributeNull)){
            //this.activity.mDataField.setText("蓝牙名称"+bleDeviceInit.getStrBtName()+"设置成功,请重新连接设备");
            mCallback.onReceiveBerbonSetDeviceBtName(0, "修改成功");
        }else{
            //this.activity.mDataField.setText("蓝牙名称"+bleDeviceInit.getStrBtName()+"设置失败,请重新连接设备");
            mCallback.onReceiveBerbonSetDeviceBtName(1, "修改失败");
        }
    }

    @Override
    public void onReceiveDeviceAuth(byte[] arg0) {
        // TODO Auto-generated method stub

    }


}
