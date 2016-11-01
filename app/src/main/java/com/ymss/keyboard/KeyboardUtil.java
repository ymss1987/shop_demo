package com.ymss.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.ymss.keyboard.BerKeyboardView;
import com.ymss.tinyshop.R;


public class KeyboardUtil {
    public static final int BerKeyboardTypeNumber = 0; //常规输入数字键盘,含小数点
    public static final int BerKeyboardTypeBankCardNo = 1; //银行卡输入数字键盘,无小数点
    public static final int BerKeyboardTypeID_CardNo = 2; //身份证输入数数字键盘,无小数点,但有X
    public static final int ANIMATION_TIME = 100;   //动画持续时间，毫秒

    private BerKeyboardView keyboardView;
    private Keyboard k;// 数字键盘
    private EditText ed;
    private Context mReactContext;
    private Activity mActivity;
    private int mMoveOffset = 0;
    public KeyboardUtil(Context reactContext, Activity activity) {
        mReactContext = reactContext;
        mActivity = activity;
    }

    public void setType(Activity act, EditText edit, int keyboardType){
        this.ed = edit;
        int res = R.xml.berbon_keyboard_number;
        switch (keyboardType){
            case BerKeyboardTypeNumber:
                break;
            case BerKeyboardTypeBankCardNo:
                res = R.xml.berbon_keyboard_card;
                break;
            case BerKeyboardTypeID_CardNo:
                res = R.xml.berbon_keyboard_idcard;
                break;
        }
        k = new Keyboard(act, res);
        if(null == keyboardView) {
            View v = LayoutInflater.from(act).inflate(R.layout.keyboard, null);
            ViewGroup vg = (ViewGroup) act.findViewById(android.R.id.content);//.getChildAt(0);
            vg.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            keyboardView = (BerKeyboardView) act.findViewById(R.id.keyboard_view);
            keyboardView.setEnabled(true);
            keyboardView.setPreviewEnabled(true);
            keyboardView.setOnKeyboardActionListener(listener);
        }
        keyboardView.setKeyboard(k);

    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
            Log.e("KeyboardUtils", "keyboard --swipeUp");
        }

        @Override
        public void swipeRight() {
            Log.e("KeyboardUtils", "keyboard --swipeRight");
        }

        @Override
        public void swipeLeft() {
            Log.e("KeyboardUtils", "keyboard --swipeLeft");
        }

        @Override
        public void swipeDown() {
            Log.e("KeyboardUtils", "keyboard --swipeDown");
        }

        @Override
        public void onText(CharSequence text) {
            Log.e("KeyboardUtils", "keyboard --onText");
        }

        @Override
        public void onRelease(int primaryCode) {
            Log.e("KeyboardUtils", "keyboard --onRelease");
        }

        @Override
        public void onPress(int primaryCode) {
            Log.e("KeyboardUtils", "keyboard --onPress="+primaryCode);
        }
        //一些特殊操作按键的codes是固定的比如完成、回退等
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Log.e("KeyboardUtils", "keyboard --onKey");
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }else if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 清空
                keyboardView.setVisibility(View.GONE);
                //keyboardDidHide();
                keyboardViewAnimationEnd();
            }else if (primaryCode == Keyboard.KEYCODE_DONE) {// 完成
                if (keyboardView.getDoneKeyActive() == true) {
                    keyboardView.setVisibility(View.GONE);
                    //keyboardDidHide();
                    keyboardViewAnimationEnd();

                }

            } else if(primaryCode == -9999){
             //无效字符
            }else { //将要输入的数字现在编辑框中
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
            //keyboardDidShow();
            keyboardViewAnimationStart();
        }
    }

    public void hideKeyboard() {
        if (keyboardView==null) {
            return;
        }
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
            //keyboardDidHide();
            keyboardViewAnimationEnd();
        }
    }


    private void keyboardViewAnimationStart() {

        int keyHeight = keyboardView.getKeyboard().getHeight();
        int editHeight = ed.getHeight();
        int[] location = new  int[2] ;
        ed.getLocationOnScreen(location);
        int screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        if (screenHeight-(location[1] + editHeight) < keyHeight) {
            mMoveOffset = keyHeight - (screenHeight-(location[1] + editHeight));
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0-mMoveOffset);
            animation.setDuration(ANIMATION_TIME);//设置动画持续时间
            //animation.setRepeatCount(2);//设置重复次数
            animation.setFillAfter(true);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    View view = ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
                    view.clearAnimation();
                    view.layout(view.getLeft(),view.getTop()-mMoveOffset,view.getRight(),view.getBottom()-mMoveOffset);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            //animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
            ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0).startAnimation(animation);

        }
    }
    private void keyboardViewAnimationEnd(){
        if (mMoveOffset!=0){
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0-mMoveOffset, 0);

            animation.setDuration(ANIMATION_TIME);//设置动画持续时间
            //animation.setRepeatCount(2);//设置重复次数
            animation.setFillAfter(true);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    View view = ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0);
                    view.clearAnimation();
                    view.layout(view.getLeft(),view.getTop()+mMoveOffset,view.getRight(),view.getBottom()+mMoveOffset);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mMoveOffset = 0;
            //animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
            ((ViewGroup) mActivity.findViewById(android.R.id.content)).getChildAt(0).startAnimation(animation);
        }
    }


    public void setDoneKeyActive(boolean active) {
        if (keyboardView==null) {
            return;
        }
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setDoneKeyActive(active);
        }
    }

}

