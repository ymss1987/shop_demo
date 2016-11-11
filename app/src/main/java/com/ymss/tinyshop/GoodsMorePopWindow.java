package com.ymss.tinyshop;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.ymss.utility.ViewTools;

/**
 * Created by adminstrator on 2016/11/1.
 */

public class GoodsMorePopWindow extends PopupWindow {
    private View conentView;

    public GoodsMorePopWindow(final Context context, int left, int right, int top, int bottom) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popwindow_goods_more, null);
        //int h = context.getWindowManager().getDefaultDisplay().getHeight();
       // int w = context.getWindowManager().getDefaultDisplay().getWidth();
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
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimationPreview);

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            //this.showAsDropDown(parent, parent.getLayoutParams().width , 18);
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            int off = ViewTools.dip2px(parent.getContext(), 160);
            int off1= parent.getWidth();
            int off2 = ViewTools.dip2px(parent.getContext(), 50);
            this.showAtLocation(parent, Gravity.NO_GRAVITY, location[0]-off-off1, location[1]-off2);
        } else {
            this.dismiss();
        }
    }
}
