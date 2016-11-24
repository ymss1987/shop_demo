package com.ymss.tinyshop;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ymss.adapter.GridViewAdapter;
import com.ymss.utility.ViewTools;

/**
 * Created by adminstrator on 2016/11/10.
 */

public class GoodsMenuPopWindow extends PopupWindow {
    private View conentView;
    private LinearLayout layout_batch_manager;
    private ImageView image_batch_manager;
    private TextView text_batch_manager;
    private LinearLayout layout_class_manager;
    private ImageView image_class_manager;
    private TextView text_class_manager;
    private PopupWindow popupWindow;
    private GoodsManagerActivity mActivity;

    public GoodsMenuPopWindow(final Activity context, int left, int right, int top, int bottom) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popwindow_goods_menu, null);
        mActivity = (GoodsManagerActivity)context;
        //int h = context.getWindowManager().getDefaultDisplay().getHeight();
        // int w = context.getWindowManager().getDefaultDisplay().getWidth();
        layout_batch_manager = (LinearLayout)conentView.findViewById(R.id.layout_batch_manager);
        layout_batch_manager.setOnTouchListener(onTouchListener);
        layout_batch_manager.setOnClickListener(onClickListener);
        image_batch_manager = (ImageView) conentView.findViewById(R.id.image_batch_manager);
        text_batch_manager = (TextView) conentView.findViewById(R.id.text_batch_manager);
        layout_class_manager = (LinearLayout)conentView.findViewById(R.id.layout_class_manager);
        layout_class_manager.setOnTouchListener(onTouchListener);
        layout_class_manager.setOnClickListener(onClickListener);
        image_class_manager = (ImageView) conentView.findViewById(R.id.image_class_manager);
        text_class_manager = (TextView) conentView.findViewById(R.id.text_class_manager);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        popupWindow = this;
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimationPreview);

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            //this.showAsDropDown(parent, parent.getLayoutParams().width , 18);
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            int off = ViewTools.dip2px(parent.getContext(), 110);
            int off1= parent.getHeight();
            this.showAtLocation(parent, Gravity.NO_GRAVITY, location[0]-off+parent.getWidth(), location[1]+off1);
        } else {
            this.dismiss();
        }
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.layout_batch_manager:
                    mActivity.setEnterBatchManager(true);
                    popupWindow.dismiss();
                    break;
                case R.id.layout_class_manager:
                    popupWindow.dismiss();
                    break;
            }
        }
    };

    public View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            ImageView imageView;
            TextView textView;
            if (R.id.layout_batch_manager == view.getId()){
                imageView = image_batch_manager;
                textView = text_batch_manager;
            }else{
                imageView = image_class_manager;
                textView = text_class_manager;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    changeLight((ImageView) imageView, 0);
                    textView.setTextColor(Color.parseColor("#ffffff"));
                    // onclick
                    break;
                case MotionEvent.ACTION_DOWN:
                    changeLight((ImageView) imageView, -80);
                    textView.setTextColor(Color.parseColor("#969696"));
                    break;
                case MotionEvent.ACTION_MOVE:
                    // changeLight(view, 0);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    changeLight((ImageView) imageView, 0);
                    textView.setTextColor(Color.parseColor("#ffffff"));
                    break;
                default:
                    break;
            }
            return false;
        }

    };

    private void changeLight(ImageView imageview, int brightness) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
        imageview.setColorFilter(new ColorMatrixColorFilter(matrix));
        imageview.getDrawable().setColorFilter(new ColorMatrixColorFilter(matrix));

    }

}
