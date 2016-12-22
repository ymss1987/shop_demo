package com.ymss.iccard;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ymss.config.Constants;


@SuppressLint("NewApi")
public class BerICCardOs implements BerbonCardReader.CardReaderCallback {

	private static final String LOGTAG=BerICCardOs.class.getSimpleName();

	public static final int MODE_HUADA = 2;
	public static final int MODE_DINGHE = 0;
	public static final int MODE_NFC = 1;

	private Context mContext;

	private int nICCardConnectMode=2;  //0:dinghe 1:NFC 2:huada
	private BerbonCardReader mCardReader;
	private HuadaCardReader mHuadaReader;
	private DingheCardReader mDingheReader;
	private String conncetDevice;

	private static BerICCardOs instance = null;

	private SearchDeviceCallback mSearchDeviceCallback;
	private ConnectDeviceCallback mConnectDeviceCallback;
	private StopSearchDeviceCallback mStopSearchDeviceCallback;
	private DisConnectDeviceCallback mDisConnectDeviceCallback;
	private GetDeviceStatusCallback mGetDeviceStatusCallback;
	private ConnectCardCallback mConnectCardCallback;

	private DisconnectCardCallback mDisconnectCardCallback;
	private SendAPDUCallback mSendAPDUCallback;
	private GetCardStatusCallback mGetCardStatusCallback;
	private ShowMessageCallback mShowMessageCallback;
	private ClearScreenCallback mClearScreenCallback;
	private PlainTextPasswordCallback mPlainTextPasswordCallback;
	private SetDeviceBtNameCallback mSetDeviceBtNameCallback;


	private BerICCardOs(Context context){
		mContext = context;

		mHuadaReader = new HuadaCardReader(context,this);
		mDingheReader = new DingheCardReader(context, this);
	}

	public static BerICCardOs getInstance(Context context){
		synchronized (BerICCardOs.class) {
			if (instance == null) {
				instance = new BerICCardOs(context);
			}
		}
		return instance;
	}

	public void setICCardConnectMode(int mode){
		nICCardConnectMode = mode;
		if (mode == 0){
			mCardReader = mDingheReader;
		}else if (mode == 2){
			mCardReader = mHuadaReader;
		}else if (mode == 1){

		}else{
			Log.e(LOGTAG,"setICCardConnectMode mode="+mode+" is error");
		}
	}

	public int getICCardConnectMode(){
		return nICCardConnectMode;
	}

	public void BerICCardReleaseDevice(){
		if (mHuadaReader!=null){
			mHuadaReader.releaseDevice();
		}
		if (mDingheReader!=null){
			mDingheReader.releaseDevice();
		}
		mCardReader = null;
	}

	public boolean BerICCardDeviceIsSupport(){
		if (mCardReader!=null){
			return mCardReader.isDeviceSupportICCard();
		}
     	return false;
    }

	public void BerICCardSearchDevice(int mtimeoutMS, SearchDeviceCallback callback){
		if (mCardReader!=null){
			mSearchDeviceCallback =callback;
			mCardReader.searchDevice(mtimeoutMS);
		}else if (callback!=null){
			callback.onReceiveBerbonSearchDevice(10,"请先选择连接方式",null,null);
		}
	}

	public void BerICCardStopSearchDevice(StopSearchDeviceCallback callback){
		if (mCardReader!=null){
			mSearchDeviceCallback = null;
			mStopSearchDeviceCallback =callback;
			mCardReader.stopSearchDevice();
		}else if (callback!=null){
			callback.onReceiveBerbonStopSearchDevice(10,"请先选择连接方式");
		}
	}

	public void BerICCardConnectDevice(int timeout, String address, ConnectDeviceCallback callback){
		if (mCardReader!=null){
			mConnectDeviceCallback = callback;
			conncetDevice = address;
			mCardReader.connectDevice(timeout,address);
		}else if (callback!=null){
			callback.onReceiveBerbonConnectDevice(10,"请先选择连接方式");
		}
	}
	
	public void BerICCardSetDeviceAutoDisconnectIsCall(boolean enable){
		if (mCardReader!=null){
			mCardReader.enableDeviceStatusNotify(enable);
		}
	}

	public void BerICCardSetDeviceBtName(String btName, SetDeviceBtNameCallback callback){
		if (mCardReader!=null){
			mSetDeviceBtNameCallback =callback;
			mCardReader.setDeviceBtName(btName);
		}else if (callback!=null){
			callback.onReceiveBerbonSetDeviceBtName(10,"请先选择连接方式");
		}
	}
	
	public void BerICCardGetDeviceStatus(GetDeviceStatusCallback callback){
		if (mCardReader!=null){
			mGetDeviceStatusCallback =callback;
			mCardReader.getDeviceStatus();
		}else if (callback!=null){
			callback.onReceiveBerbonGetDeviceStatus(10,"请先选择连接方式",null);
		}
	}
	
	public void BerICCardDisconnectDevice(String addr, DisConnectDeviceCallback callback){
		if (mCardReader!=null){
			mDisConnectDeviceCallback = callback;
			mCardReader.disconnectDevice(addr);
		}else if (callback!=null){
			callback.onReceiveBerbonDisconnectDevice(10,"请先选择连接方式");
		}
	}

	public void BerICCardConnectCard(ConnectCardCallback callback){
		if (mCardReader!=null){
			mConnectCardCallback = callback;
			mCardReader.connectCard();
		}else if (callback!=null){
			callback.onReceiveBerbonConnectCard(10,"请先选择连接方式",null,null);
		}
	}
		
	public void BerICCardDisconnectCard(DisconnectCardCallback callback){
		if (mCardReader!=null){
			mDisconnectCardCallback = callback;
			mCardReader.disconnectCard();
		}else if (callback!=null){
			callback.onReceiveBerbonDisconnectCard(10,"请先选择连接方式");
		}
	}

//	public void BerICCardGetCardStatus(GetCardStatusCallback callback){
//		if (mCardReader!=null){
//			mGetCardStatusCallback = callback;
//			mCardReader.getCardStatus();
//		}else if (callback!=null){
//			callback.onReceiveBerbonGetCardStatus(10,"请先选择连接方式",null,null);
//		}
//	}

	public void BerICCardSendAPDU(final String data, SendAPDUCallback callback){
		if (mCardReader!=null){
			mSendAPDUCallback = callback;
			mCardReader.sendAPDU(data);
		}else if (callback!=null){
			callback.onReceiveBerbonSendAPDU(10,"请先选择连接方式",null);
		}
	}

	public void BerShowMessage(String msg, int line, int col, ShowMessageCallback callback){
		if (mCardReader!=null){
			mShowMessageCallback = callback;
			mCardReader.showMessage(msg,line,col);
		}else if (callback!=null){
			callback.onReceiveBerbonShowMessage(10,"请先选择连接方式");
		}
	}

	public void BerClearScreen(int type, ClearScreenCallback callback){
		if (mCardReader!=null){
			mClearScreenCallback = callback;
			mCardReader.clearScreen(type);
		}else if (callback!=null){
			callback.onReceiveBerbonClearScreen(10,"请先选择连接方式");
		}
	}

	public void getPlainTextPassword(PlainTextPasswordCallback callback){
		if (mCardReader!=null){
			mPlainTextPasswordCallback = callback;
			mCardReader.getPlainTextPassword();
		}
		else if (callback!=null){
			callback.onReceiveBerbonPlainTextPassword(10,"请先选择连接方式",null);
		}
	}

	//搜索读卡器设备的回调
	@Override
	public void onReceiveBerbonSearchDevice(int status, String desc, String addr, String name){
		if (mSearchDeviceCallback!=null){
			mSearchDeviceCallback.onReceiveBerbonSearchDevice(status,desc,addr,name);
		}
	}

	//停止搜索读卡器设备的回调
	@Override
	public void onReceiveBerbonStopSearchDevice(int status, String desc){
		if (mStopSearchDeviceCallback!=null){
			mStopSearchDeviceCallback.onReceiveBerbonStopSearchDevice(status,desc);
			mStopSearchDeviceCallback = null;
		}
	}

	//连接读卡器设备的回调
	@Override
	public void onReceiveBerbonConnectDevice(int status, String desc){
		if (status !=0){
			conncetDevice = null;
		}
		if (mConnectDeviceCallback!=null){
			mConnectDeviceCallback.onReceiveBerbonConnectDevice(status,desc);
			mConnectDeviceCallback = null;
		}
	}

	//断开读卡器设备的回调
	@Override
	public void onReceiveBerbonDisconnectDevice(int status, String desc){
		if (mDisConnectDeviceCallback!=null){
			mDisConnectDeviceCallback.onReceiveBerbonDisconnectDevice(status,desc);
			mDisConnectDeviceCallback = null;
		}
	}

	//获取设备信息的回调
	@Override
	public void onReceiveBerbonGetDeviceStatus(int status, String desc, BerbonCardReaderInfo info){
		if (mGetDeviceStatusCallback!=null){
			mGetDeviceStatusCallback.onReceiveBerbonGetDeviceStatus(status, desc, info);
			mGetDeviceStatusCallback = null;
		}
	}

	//连接卡片的回调
	@Override
	public void onReceiveBerbonConnectCard(int status, String desc, String csn, String ats){
		if (mConnectCardCallback!=null){
			ConnectCardCallback callback = mConnectCardCallback;
			mConnectCardCallback = null;
			callback.onReceiveBerbonConnectCard(status, desc, csn, ats);
		}
	}

	//断开卡片连接的回调
	@Override
	public void onReceiveBerbonDisconnectCard(int status, String desc){
		if (mDisconnectCardCallback!=null){
			mDisconnectCardCallback.onReceiveBerbonDisconnectCard(status,desc);
			mDisconnectCardCallback = null;
		}
	}

	//获取卡片状态的回调
	@Override
	public void onReceiveBerbonGetCardStatus(int status, String desc, String csn, String ats){
		if (mGetCardStatusCallback!=null){
			mGetCardStatusCallback.onReceiveBerbonGetCardStatus(status, desc, csn, ats);
			mGetCardStatusCallback= null;
		}
	}

	//发送APDU命令的回调
	@Override
	public void onReceiveBerbonSendAPDU(int status, String desc, String data){
		if (mSendAPDUCallback!=null){
			SendAPDUCallback callback = mSendAPDUCallback;
			mSendAPDUCallback = null;
			callback.onReceiveBerbonSendAPDU(status, desc, data);

		}
	}

	//设置屏幕文字的回调
	@Override
	public void onReceiveBerbonShowMessage(int status, String desc){
		if (mShowMessageCallback!=null){
			mShowMessageCallback.onReceiveBerbonShowMessage(status,desc);
			mShowMessageCallback = null;
		}
	}

	//读卡器清屏的回调
	@Override
	public void onReceiveBerbonClearScreen(int status, String desc){
		if (mClearScreenCallback!=null){
			mClearScreenCallback.onReceiveBerbonClearScreen(status,desc);
			mClearScreenCallback = null;
		}
	}

	//读卡器设备的断开状态通知
	@Override
	public void onReceiveBerbonDeviceStatusNotify(int status, String desc, int deviceStatus){
		if (deviceStatus == 0){
			Intent intent = new Intent();
			intent.setAction(Constants.AUTODISCONNECT_ACTION);		//设置Action
			//intent.putExtra("msg", "接收静态注册广播成功！");	//添加附加信息
			mContext.sendBroadcast(intent);				//发送Intent
		}
	}

	//读卡器输入密码的回调
	@Override
	public void onReceiveBerbonGetPlainTextPassword(int status, String desc, String passWord){
		if (mPlainTextPasswordCallback!=null){
			mPlainTextPasswordCallback.onReceiveBerbonPlainTextPassword(status,desc,passWord);
			mPlainTextPasswordCallback = null;
		}
	}

	@Override
	public void onReceiveBerbonSetDeviceBtName(int status, String desc) {
		if (mSetDeviceBtNameCallback!=null){
			mSetDeviceBtNameCallback.onReceiveBerbonSetDeviceBtName(status, desc);
		}
	}


	//获取当前已连接的设备mac，如果没有连接，则返回null
	public String getConnectDeviceAddress(){
		return conncetDevice;
	}











	public interface SearchDeviceCallback {
		//搜索读卡器设备的回调
		void onReceiveBerbonSearchDevice(int status, String desc, String addr, String name);
	}
	public interface StopSearchDeviceCallback {
		//停止搜索读卡器设备的回调
		void onReceiveBerbonStopSearchDevice(int status, String desc);
	}

	public interface ConnectDeviceCallback {
		//连接读卡器设备的回调
		void onReceiveBerbonConnectDevice(int status, String desc);
	}
	public interface DisConnectDeviceCallback {
		//断开读卡器设备的回调
		void onReceiveBerbonDisconnectDevice(int status, String desc);
	}
	public interface GetDeviceStatusCallback{
		//获取读卡器信息的回调
		void onReceiveBerbonGetDeviceStatus(int status, String desc, BerbonCardReaderInfo info);
	}
	public interface ConnectCardCallback{
		//连接卡片的回调
		void onReceiveBerbonConnectCard(int status, String desc, String csn, String ats);
	}

	public interface DisconnectCardCallback{
		//连接卡片的回调
		void onReceiveBerbonDisconnectCard(int status, String desc);
	}
	public interface SendAPDUCallback{
		//连接卡片的回调
		void onReceiveBerbonSendAPDU(int status, String desc, String data);
	}
	public interface GetCardStatusCallback{
		//连接卡片的回调
		void onReceiveBerbonGetCardStatus(int status, String desc, String csn, String ats);
	}
	public interface ShowMessageCallback{
		//连接卡片的回调
		void onReceiveBerbonShowMessage(int status, String desc);
	}
	public interface ClearScreenCallback{
		//连接卡片的回调
		void onReceiveBerbonClearScreen(int status, String desc);
	}
	public interface PlainTextPasswordCallback{
		//连接卡片的回调
		void onReceiveBerbonPlainTextPassword(int status, String desc, String password);
	}
	public interface SetDeviceBtNameCallback{
		//修改设备蓝牙名称的回调
		void onReceiveBerbonSetDeviceBtName(int status, String desc);
	}





}
