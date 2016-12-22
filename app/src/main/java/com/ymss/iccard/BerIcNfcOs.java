package com.ymss.iccard;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ymss.tinyshop.MainActivity;
import com.ymss.utility.InnerTools;

import java.io.IOException;
import java.util.Arrays;

public class BerIcNfcOs {

	private static final String TAG = "BerIcNfcOs";
	
	private static final byte[] RESULT_OK = {(byte) 0x90, (byte) 0x00};
	private IsoDep mIsoDep;
	private NfcA mNfcA;
	private MifareClassic mMifareClassic;
	private Activity mCtx;
	private boolean isListen;
	private NfcAdapter mAdapter = null;
	private Context mContext;
	
	public BerIcNfcOs(Activity ctx){
		this.mCtx = ctx;
		NfcManager nfcManager = (NfcManager)mCtx.getSystemService(Context.NFC_SERVICE);
		mAdapter = nfcManager.getDefaultAdapter();

	}
	
	public void initNfc(){
		if(Build.VERSION.SDK_INT < 10){
			return;
		}
		PendingIntent intent = PendingIntent.getActivity(this.mCtx, 0, new Intent(this.
				mCtx, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter filter = new IntentFilter("android.nfc.action.TECH_DISCOVERED");
		filter.addCategory("*/*");
        if (mAdapter != null) {
        	mAdapter.enableForegroundDispatch(mCtx, intent, new IntentFilter[]{filter}, new String[][]{new String[]{NfcA.class.getName(), IsoDep.class.getName()}});
        }
	}

	//@SuppressLint("InlinedApi")
	public void enableReadMode(){

	}



	public void disableReadMode(){
		if(mAdapter != null && mAdapter.isEnabled())
            mAdapter.disableForegroundDispatch(this.mCtx);
	}

	public void setIsListen(boolean isListen, Context context){
		this.isListen = isListen;
		mContext = context;
	}

	public void onNewTagDiscovered(Tag tag){

		//mCtx.startActivity(new Intent(mCtx, TestActivity.class));
		String testCmd = "0084000004";
		byte[] cmdB = InnerTools.HexStringToByteArray(testCmd);
		String tagId = InnerTools.ByteArrayToHexString(tag.getId());
		Log.i(TAG, "new NFC tag discovered id is " + tagId);
		boolean connectSuccess = false;

		mIsoDep = IsoDep.get(tag);
		if(!connectSuccess && null != mIsoDep){
			try {
				mIsoDep.connect();
				byte[] res = mIsoDep.transceive(cmdB);
				if(null!=res && res.length>=2) {
					connectSuccess = true;
					int timeOut = mIsoDep.getTimeout();
					mIsoDep.setTimeout(timeOut*2);
					Log.i(TAG, "new NFC tag nfccmd oldTimeout " + timeOut + " newTimeout " + timeOut*2);
				}else{
					if(mIsoDep.isConnected()){
						try {
							mIsoDep.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					mIsoDep = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.i(TAG, "ICNfcOs:" + e.getMessage());
				connectSuccess = false;
				if(mIsoDep.isConnected()){
					try {
						mIsoDep.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			mMifareClassic = null;
			mNfcA = null;
		}

		mNfcA = NfcA.get(tag);
		if(!connectSuccess && null != mNfcA){
			try {
				mNfcA.connect();
				byte [] res = mNfcA.transceive(cmdB);
				if(null!=res && res.length>=2) {
					connectSuccess = true;
					int timeOut = mNfcA.getTimeout();
					mNfcA.setTimeout(timeOut * 2);
				}else{
					if(mNfcA.isConnected()){
						try {
							mNfcA.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					mNfcA = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.i(TAG, "ICNfcOs:" + e.getMessage());
				connectSuccess = false;
				if(mNfcA.isConnected()){
					try {
						mNfcA.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			mMifareClassic = null;
			mIsoDep = null;
		}

		mMifareClassic = MifareClassic.get(tag);

		if(!connectSuccess && null != mMifareClassic){
			try {
				mMifareClassic.connect();
				byte []res = mMifareClassic.transceive(cmdB);
				if(null!=res && res.length>=2) {
					connectSuccess = true;
					int timeOut = mMifareClassic.getTimeout();
					mMifareClassic.setTimeout(timeOut * 2);
				}else{
					if(mMifareClassic.isConnected()){
						try {
							mMifareClassic.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					mMifareClassic = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.i(TAG, "ICNfcOs:" + e.getMessage());
				connectSuccess = false;
				if(mMifareClassic.isConnected()){
					try {
						mMifareClassic.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			mIsoDep = null;
			mNfcA = null;
		}

		//newTagFind(tagId);
		/*if (isListen==true){
			WritableMap map = Arguments.createMap();
			map.putString("csn", tagId);
			//map.putString("desc", "还未连接设备");
			sendEvent(mReactContext, "BerICCardNFCTagDiscovered", map);
		}*/
	}

	 private void dealResult(byte[] result) throws Exception{
	        int resultLength = result.length;
		 	if(resultLength < 2){
				return;
			}
	        byte[] statusWord = {result[resultLength-2], result[resultLength-1]};
	        byte[] payload = Arrays.copyOf(result, resultLength-2);
	        Log.d(TAG, "nfcsendapdu state:" + InnerTools.ByteArrayToHexString(statusWord));
	        if (Arrays.equals(RESULT_OK, statusWord)) {
	            // The remote NFC device will immediately respond with its stored account number
	            String data = new String(payload, "UTF-8");
	            data = InnerTools.ByteArrayToHexString(payload);
	            Log.i(TAG, " nfcsendapdu read result data: " + data);
			//	authorize(payload);
	        }
	    }

	 public int getStatus(byte[] result){
		 if(null==result || result.length<2){
			 return -1;
		 }

		 int resultLength = result.length;
	     byte[] statusWord = {result[resultLength-2], result[resultLength-1]};
	     int status = (statusWord[0]&0xFF)<<8 | statusWord[1];

	     return status;
	 }

	 public byte[] getPayload(byte[] result){
		 if(null==result || result.length<2){
			 return null;
		 }

		 int resultLength = result.length;
		 byte[] payload = Arrays.copyOf(result, resultLength-2);
		 return payload;
	 }

	 public byte[] sendCommand(byte[] cmd){
		 Log.d(TAG, "nfcsendapdu cmd:"+InnerTools.ByteArrayToHexString(cmd));
		 byte[] result = null;
		 if(!this.isConnected()){
			 //连接不存在
			 return result;
		 }
		try {
			if(mMifareClassic!=null && mMifareClassic.isConnected()){
				result = mMifareClassic.transceive(cmd);
			}else if(null!= mNfcA && mNfcA.isConnected()){
				result = mNfcA.transceive(cmd);
			}else if(null!= mIsoDep && mIsoDep.isConnected()){
				result = mIsoDep.transceive(cmd);
			}
			dealResult(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "ICNfcOs:" + e.getMessage());
		}
		return result;
	 }

	 public boolean isConnected(){

		 if(null!=mNfcA && mNfcA.isConnected()){
			 return true;
		 }

		 if(null!=mIsoDep && mIsoDep.isConnected()){
			 return true;
		 }

		 if(null!=mMifareClassic && mMifareClassic.isConnected()){
			 return true;
		 }
		 return false;
	 }

	 public boolean isSupporNfc(){
		 if(Build.VERSION.SDK_INT < 10){
				return false;
			}
		
	        if (mAdapter != null) {
	        	return true;
	        }
	        
	        return false;
	 }
	 
	 public boolean isNfcEnabled(){
		if(!isSupporNfc()){
			return false;
		}
		 
		return mAdapter.isEnabled();
	 }
	 

}
