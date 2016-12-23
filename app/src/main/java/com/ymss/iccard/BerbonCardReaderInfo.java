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

    public String getBtName() {
        return btName;
    }

    public void setBtName(String btName) {
        this.btName = btName;
    }

    public String getBtAddr() {
        return btAddr;
    }

    public void setBtAddr(String btAddr) {
        this.btAddr = btAddr;
    }

    public String getProdModel() {
        return prodModel;
    }

    public void setProdModel(String prodModel) {
        this.prodModel = prodModel;
    }

    public String getProdHwVer() {
        return prodHwVer;
    }

    public void setProdHwVer(String prodHwVer) {
        this.prodHwVer = prodHwVer;
    }

    public String getProdSwVer() {
        return prodSwVer;
    }

    public void setProdSwVer(String prodSwVer) {
        this.prodSwVer = prodSwVer;
    }

    public String getChipUid() {
        return chipUid;
    }

    public void setChipUid(String chipUid) {
        this.chipUid = chipUid;
    }

    public int getVoltPercent() {
        return voltPercent;
    }

    public void setVoltPercent(int voltPercent) {
        this.voltPercent = voltPercent;
    }
}
