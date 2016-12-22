package com.ymss.iccard;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by adminstrator on 2016/11/28.
 */

public class BerbonCardReaderInfo {
    private String btName;        //读卡器蓝牙名称
    private String btAddr;       //读卡器蓝牙地址
    private String prodModel;   //读卡器型号
    private String prodHwVer;   //读卡器硬件版本
    private String prodSwVer;   //读卡器软件版本
    private String chipUid;     //读卡器芯片ID
    private int voltPercent;  //读卡器电量信息(0~100)

    public void setBtName(String name){
        btName = name;
    }

    public String getBtName(){
        return btName;
    }

    public void setBtAddr(String addr){
        btAddr = addr;
    }

    public String getBtAddr(){
        return btAddr;
    }

    public void setProdModel(String model){
        prodModel = model;
    }

    public String getProdModel(){
        return prodModel;
    }

    public void setProdHwVer(String hwVer){
        prodHwVer = hwVer;
    }

    public String getProdHwVer(){
        return prodHwVer;
    }

    public void setProdSwVer(String swVer){
        prodSwVer = swVer;
    }

    public String getProdSwVer(){
        return prodSwVer;
    }

    public void setChipUid(String uid){
        chipUid = uid;
    }

    public String getChipUid(){
        return chipUid;
    }

    public void setVoltPercent(int volt){
        voltPercent = volt;
    }

    public int getVoltPercent(){
        return voltPercent;
    }

}
