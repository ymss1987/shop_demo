package com.ymss.tinyshop;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymss.adapter.FragmentAdapter;
import com.ymss.adapter.GridViewAdapter;
import com.ymss.keyboard.KeyboardTool;
import com.ymss.tinyshop.Fragment.Tab1Fragment;
import com.ymss.tinyshop.Fragment.Tab2Fragment;
import com.ymss.tinyshop.Fragment.Tab3Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    // 三个textview
    private LinearLayout tab1Layout, tab2Layout, tab3Layout;
    // 指示器
    //private ImageView cursorImg;
    // viewpager
    private ViewPager viewPager;
    // fragment对象集合
    private ArrayList<Fragment> fragmentsList;
    // 记录当前选中的tab的index
    private int currentIndex = 0;
    // 指示器的偏移量
    private int offset = 0;
    // 左margin
    private int leftMargin = 0;
    // 屏幕宽度
    private int screenWidth = 0;
    // 屏幕宽度的三分之一
    private int screen1_3;
    //
    private LinearLayout.LayoutParams lp;

    private TextView tab1Tv, tab2Tv, tab3Tv;
    private ImageView tab1Img, tab2Img, tab3Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * 初始化操作
     */
    private void init() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screen1_3 = screenWidth / 3;

       // cursorImg = (ImageView) findViewById(R.id.cursor);
        //lp = (LinearLayout.LayoutParams) cursorImg.getLayoutParams();
        //leftMargin = lp.leftMargin;

        tab1Tv = (TextView) findViewById(R.id.tab1_tv);
        tab2Tv = (TextView) findViewById(R.id.tab2_tv);
        tab3Tv = (TextView) findViewById(R.id.tab3_tv);
        tab1Img = (ImageView) findViewById(R.id.tab1_img);
        tab2Img = (ImageView) findViewById(R.id.tab2_img);
        tab3Img = (ImageView) findViewById(R.id.tab3_img);

        tab1Layout = (LinearLayout) findViewById(R.id.tab1_layout);
        tab2Layout = (LinearLayout) findViewById(R.id.tab2_layout);
        tab3Layout = (LinearLayout) findViewById(R.id.tab3_layout);
        //
        tab1Layout.setOnClickListener(this);
        tab2Layout.setOnClickListener(this);
        tab3Layout.setOnClickListener(this);

        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.third_vp);
        fragmentsList = new ArrayList<>();
        Fragment fragment = new Tab1Fragment();
        fragmentsList.add(fragment);
        fragment = new Tab2Fragment();
        fragmentsList.add(fragment);
        fragment = new Tab3Fragment();
        fragmentsList.add(fragment);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
                fragmentsList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1_layout:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab2_layout:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tab3_layout:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        /*offset = (screen1_3 - cursorImg.getLayoutParams().width) / 2;
        Log.d("111", position + "--" + positionOffset + "--"
                + positionOffsetPixels);
        final float scale = getResources().getDisplayMetrics().density;
        if (position == 0) {// 0<->1
            lp.leftMargin = (int) (positionOffsetPixels / 3) + offset;
        } else if (position == 1) {// 1<->2
            lp.leftMargin = (int) (positionOffsetPixels / 3) + screen1_3 +offset;
        }
        cursorImg.setLayoutParams(lp);*/
        currentIndex = position;
        tab1Tv.setTextColor(Color.parseColor("#707070"));
        tab1Img.setBackgroundResource(R.drawable.component_tabhost_images_homegray);
        tab2Tv.setTextColor(Color.parseColor("#707070"));
        tab2Img.setBackgroundResource(R.drawable.component_tabhost_images_bbcardgray);
        tab3Tv.setTextColor(Color.parseColor("#707070"));
        tab3Img.setBackgroundResource(R.drawable.component_tabhost_images_megray);

        if (position == 0 && positionOffsetPixels ==0 && positionOffset == 0){
            tab1Tv.setTextColor(Color.parseColor("#4f9ff5"));
            tab1Img.setBackgroundResource(R.drawable.component_tabhost_images_home);
        } else if (position == 1 && positionOffsetPixels ==0 && positionOffset == 0){

            tab2Tv.setTextColor(Color.parseColor("#4f9ff5"));
            tab2Img.setBackgroundResource(R.drawable.component_tabhost_images_bbcard);
        }else  if (position == 2 && positionOffsetPixels ==0 && positionOffset == 0){

            tab3Tv.setTextColor(Color.parseColor("#4f9ff5"));
            tab3Img.setBackgroundResource(R.drawable.component_tabhost_images_me);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
    }

}
