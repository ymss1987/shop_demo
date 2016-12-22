package com.ymss.iccard;

/**
 * Created by adminstrator on 2016/11/25.
 */

public abstract class BerbonCardReader {

    /*描述：查询系统是否支持ICCard功能
参数：
返回：bool 是否支持*/
    public abstract boolean isDeviceSupportICCard();

    /* 描述：搜索读卡器设备
参数：timeout:int 超时时间(毫秒)，超出设置时间未找到设备就会回调
返回：void*/
    public abstract void searchDevice(int timeout);

    /*描述：描述：停止搜索读卡器设备
返回：void*/
    public abstract void stopSearchDevice();

    /*描述：描述：连接蓝牙设备
参数：addr:string 蓝牙地址/addr
返回：void*/
    public abstract void connectDevice(int timeout, String addr);

    /*描述： 描述：断开蓝牙设备
参数：addr:string 蓝牙地址/addr
返回：void*/
    public abstract void disconnectDevice(String addr);

    /*描述：描述：获取设备详细信息
返回：void*/
    public abstract void getDeviceStatus();

    /* 描述：描述：连接卡片
返回：void*/
    public abstract void connectCard();

    /* 描述：描述：断开卡片
 返回：void*/
    public abstract void disconnectCard();

    /*描述：描述：获取卡片信息
返回：void*/
//    public abstract void getCardStatus();

    /*描述：描述：发送APDU命令
参数：cmd:string 要发送的APDU命令
返回：void*/
    public abstract void sendAPDU(String cmd);

    /*描述：在读卡器屏幕显示消息(仅华大读卡器支持)
参数：msg:string 要显示的消息(最多支持16个英文字符或者8个中文字符,中文符号不支持)
      line:int 显示在哪一行(1~4,第一行显示会和电量显示冲突，故最好从第二行开始显示)
     col:int 显示在哪一列(1~8)
返回：void
*/
    public abstract void showMessage(String msg, int line, int col);

    /* 描述：清除读卡器屏幕显示(仅华大读卡器支持)
参数：type:int 0表示清除屏幕，1表示显示默认主屏
返回：void*/
    public abstract void clearScreen(int type);

    /*描述：设置当读卡器设备蓝牙断开时是否通知客户端
 参数：enable: 是否打开通知
 返回：void*/
    public abstract void enableDeviceStatusNotify(boolean enable);

    /*描述：获取输入的明文密码。目前华大的读卡器只支持6位的纯数字密码。输入后需要用户按确认键才能收到。另，实际测试发现有诸多显示及流程上的问题，但是产品及领导不care，平台不为此负责。(相关问题已反馈sdk方，如果对方处理，平台将更新并删除相关描述)
 返回：void*/
    public abstract void getPlainTextPassword();

    /*描述：修改设备蓝牙名称
参数：btName:string 设备新蓝牙名
返回：void*/
    public abstract void setDeviceBtName(String btName);

    //退出销毁函数
    public abstract void releaseDevice();



    public interface CardReaderCallback{

        //搜索读卡器设备的回调
        void onReceiveBerbonSearchDevice(int status, String desc, String addr, String name);

        //停止搜索读卡器设备的回调
        void onReceiveBerbonStopSearchDevice(int status, String desc);

        //连接读卡器设备的回调
        void onReceiveBerbonConnectDevice(int status, String desc);

        //断开读卡器设备的回调
        void onReceiveBerbonDisconnectDevice(int status, String desc);

        //获取设备信息的回调
        void onReceiveBerbonGetDeviceStatus(int status, String desc, BerbonCardReaderInfo info);

        //连接卡片的回调
        void onReceiveBerbonConnectCard(int status, String desc, String csn, String ats);

        //断开卡片连接的回调
        void onReceiveBerbonDisconnectCard(int status, String desc);

        //获取卡片状态的回调
        void onReceiveBerbonGetCardStatus(int status, String desc, String csn, String ats);

        //发送APDU命令的回调
        void onReceiveBerbonSendAPDU(int status, String desc, String data);

        //设置屏幕文字的回调
        void onReceiveBerbonShowMessage(int status, String desc);

        //读卡器清屏的回调
        void onReceiveBerbonClearScreen(int status, String desc);

        //读卡器设备的断开状态通知
        void onReceiveBerbonDeviceStatusNotify(int status, String desc, int deviceStatus);

        //读卡器输入密码的回调
        void onReceiveBerbonGetPlainTextPassword(int status, String desc, String passWord);

        //修改设备蓝牙名称的回调
        void onReceiveBerbonSetDeviceBtName(int status, String desc);
    }

}
