package com.ymss.tinyshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ymss.adapter.ViewPagerAdapter;
import com.ymss.config.CommonVar;
import com.ymss.config.Constants;

import java.util.ArrayList;
import java.util.List;

public class GuidActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;

    //引导图片资源
    private static final int[] pics = { R.drawable.a1,
            R.drawable.a2, R.drawable.a3,
            R.drawable.a4 };

    //底部小店图片
    private ImageView[] dots ;

    //记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);

        views = new ArrayList<View>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);



        //初始化引导图片列表
        for(int i=0; i<pics.length; i++) {
            //ImageView iv = new ImageView(this);
            //iv.setLayoutParams(mParams);
            View view = LayoutInflater.from(this).inflate(R.layout.guid_view,null);
            ImageView iv =(ImageView)view.findViewById(R.id.image_pic);
            iv.setImageResource(pics[i]);

            ImageView ss = (ImageView) view.findViewById(R.id.image_start);
            if (i == pics.length-1) {
                ss.setImageResource(R.drawable.words_ex);
                ss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLoginActivity();
                    }
                });
            }else{
                ss.setVisibility(View.GONE);
            }
            Button bb = (Button) view.findViewById(R.id.btn_over);
            bb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoginActivity();
                }
            });

            views.add(view);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        //绑定回调
        vp.setOnPageChangeListener(this);

        //初始化底部小点
        initDots();
    }

    private void startLoginActivity(){

        String key = CommonVar.getGuidPreferencesKey(this);
        if (key!=null) {
            SharedPreferences sp= getSharedPreferences(Constants.SharedPreferencesName, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(key,true);
            editor.commit();
        }
        Intent loginIntent = new Intent(GuidActivity.this, LoginActivity.class);
        GuidActivity.this.startActivity(loginIntent);
        GuidActivity.this.finish();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }

    /**
     *设置当前的引导页
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     *这只当前引导小点的选中
     */
    private void setCurDot(int positon)
    {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
    }
}
