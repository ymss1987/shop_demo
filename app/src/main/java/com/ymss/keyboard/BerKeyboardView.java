package com.ymss.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;


import com.ymss.tinyshop.R;

import java.util.List;

/**
 * Created by ymss on 2016/6/8.
 */
public class BerKeyboardView extends KeyboardView {
    private Context context;
    private boolean mDoneKeyActive = true;   //ok键是否是可用状态

    public BerKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setDoneKeyActive(Boolean active){
        mDoneKeyActive = active;
        this.postInvalidate();
    }

    public boolean getDoneKeyActive(){
        return mDoneKeyActive;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if(key.label!=null && key.label.equals("确定")){
                reDrawOKBtn(key, canvas);
            }else if(key.codes!=null && key.codes.length==1 && key.codes[0]==-5){
                reDrawDeleteBtn(key, canvas);
            }else if (key.codes!=null && key.codes.length==1 && key.codes[0]==-9999){
                Rect targetRect = new Rect(key.x,key.y,key.x+ key.width,key.y + key.height);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(0xfff6f6f7);
                canvas.drawRect(targetRect, paint);
            }
        }
    }

    private void reDrawOKBtn(Keyboard.Key key, Canvas canvas) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int textSize =  (int)(20 * scale + 0.5f);
        Rect targetRect = new Rect(key.x,key.y,key.x+ key.width,key.y + key.height);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //paint.setStrokeWidth(3);
        paint.setTextSize(textSize);
        String testString = "确定";

        if (getDoneKeyActive() == false){
            paint.setColor(0xff92c4fb);
            canvas.drawRect(targetRect, paint);
            paint.setColor(0xffd8e9fd);
        }else {
            if (key.pressed == false) {
                paint.setColor(0xff4a9df8);
                // paint.setColor(0xff92c4fb);
            } else {
                paint.setColor(0xff428ddf);
                // paint.setColor(0xff4a9df8);
            }
            canvas.drawRect(targetRect, paint);
            paint.setColor(0xffffffff);
        }
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(testString, targetRect.centerX(), baseline, paint);
    }

    private void reDrawDeleteBtn(Keyboard.Key key, Canvas canvas) {
        Rect targetRect = new Rect(key.x, key.y, key.x + key.width, key.y + key.height+2);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (key.pressed == false) {
            paint.setColor(0xfff6f6f7);
        } else {
            paint.setColor(0xffdfdfdf);
        }
        canvas.drawRect(targetRect, paint);
        Drawable npd = (Drawable) context.getResources().getDrawable(R.drawable.delete);
        int width = npd.getIntrinsicWidth();
        int height = npd.getIntrinsicHeight();
        int offsetX = (key.width-width)/2;
        if (offsetX<=0){
            offsetX = 0;
        }
        int offsetY = (key.height-height)/2;
        if (offsetY <= 0){
            offsetY = 0;
        }
        npd.setBounds(key.x + offsetX, key.y + offsetY, key.x + offsetX + width, key.y + offsetY + height);
        npd.draw(canvas);
    }
}
