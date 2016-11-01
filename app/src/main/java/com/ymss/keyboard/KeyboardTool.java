package com.ymss.keyboard;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adminstrator on 2016/4/27.
 */
public class KeyboardTool {
    private Activity mActivity;
    private KeyboardUtil mKeyboardUtil;

    private static final String BerKeyboardTypeNumber = "BerKeyboardTypeNumber";
    private static final String BerKeyboardTypeBankCardNo = "BerKeyboardTypeBankCardNo";
    private static final String BerKeyboardTypeID_CardNo = "BerKeyboardTypeID_CardNo";


    public KeyboardTool(Context reactContext, Activity activity) {
        this.mActivity = activity;
        mKeyboardUtil = new KeyboardUtil(reactContext,activity);
    }




    public void setKeyboardTypeById(final int id, final int type){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final EditText edt = (EditText)mActivity.findViewById(id);
                if(null == edt || type <0 || type > 2){
                    return;
                }
                hideSoftInputMethod(edt);
                mKeyboardUtil.setType(mActivity, edt, type);
                mKeyboardUtil.showKeyboard();
                edt.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP ) {
                            mKeyboardUtil.setType(mActivity, edt, type);
                            mKeyboardUtil.showKeyboard();
                        }
                        return false;
                    }
                });
            }
        });
    }

    public void setKeyboardTypeByTag(final String tag, final int type){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final EditText edt = (EditText)mActivity.getWindow().getDecorView().findViewWithTag(tag);
                if(null == edt || type <0 || type > 2){
                    return;
                }
                hideSoftInputMethod(edt);
                mKeyboardUtil.setType(mActivity, edt, type);
                mKeyboardUtil.showKeyboard();
                edt.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            mKeyboardUtil.setType(mActivity, edt, type);
                            mKeyboardUtil.showKeyboard();
                        }
                        return false;
                    }
                });
            }
        });
    }

    public void setKeyboardHide(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mKeyboardUtil.hideKeyboard();
            }
        });
    }

    /*描述:设置"确定"键是否激活.默认为非激活状态.未调用则保留该模块原设计状态.
需要在收到setKeyboardTypeByTag或setKeyboardTypeById回调后调用才有效.
参数:
   tag:string. input控件testID的值.唯一标记input控件
   active:bool. 是否激活.true,激活,false不激活.
返回值:
   无
*/

    public void setDoneKeyActiveByTag(final String tag, final boolean active){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mKeyboardUtil.setDoneKeyActive(active);
            }
        });
    }

    /*描述:设置"确定"键是否激活.默认为非激活状态.未调用则保留该模块原设计状态.
需要在收到setKeyboardTypeByTag或setKeyboardTypeById回调后调用才有效.
参数:
   Id:int. event.nativeEvent.target的值.唯一标记input控件
   active:bool. 是否激活.true,激活,false不激活.
返回值:
   无
*/

    public void setDoneKeyActiveById(final int id, final boolean active){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mKeyboardUtil.setDoneKeyActive(active);
            }
        });
    }



    private void hideSoftInputMethod(EditText ed){
        //mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(mActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed.getWindowToken(),0);

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if(currentVersion >= 16){
            // 4.2
            methodName = "setShowSoftInputOnFocus";

        }
        else if(currentVersion >= 14){
            // 4.0
            methodName = "setSoftInputShownOnFocus";

        }

        if(methodName == null){
            ed.setInputType(InputType.TYPE_NULL);
        }
        else{
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
