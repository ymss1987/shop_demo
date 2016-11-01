package com.ymss.tinyshop;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymss.adapter.FragmentAdapter;
import com.ymss.tinyshop.Fragment.GoodsManagerTab1Fragment;
import com.ymss.tinyshop.Fragment.GoodsManagerTab2Fragment;

import java.util.ArrayList;

public class GoodsManagerActivity extends FragmentActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener {

    // 三个textview
    private TextView tab1Tv, tab2Tv;
    // 指示器
    private ImageView cursorImg;
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
    // 屏幕宽度的二分之一
    private int screen1_2;
    //
    private LinearLayout.LayoutParams lp;

    public static LinearLayout mSearch;
    private LinearLayout mSearchTitle;
    private EditText mSearchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_manager);
        init();

    }

    /**
     * 初始化操作
     */
    private void init() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screen1_2 = screenWidth / 2;

        cursorImg = (ImageView) findViewById(R.id.cursor);
        lp = (LinearLayout.LayoutParams) cursorImg.getLayoutParams();
        leftMargin = lp.leftMargin;

        tab1Tv = (TextView) findViewById(R.id.selling);
        tab2Tv = (TextView) findViewById(R.id.sold_out);

        //
        tab1Tv.setOnClickListener(this);
        tab2Tv.setOnClickListener(this);

        mSearch = (LinearLayout) findViewById(R.id.search);
        mSearch.setOnClickListener(this);

        mSearchEdit = (EditText) findViewById(R.id.search_edit);
        mSearchTitle = (LinearLayout) findViewById(R.id.searchTitle);


        initViewPager();
    }

    private void startSearch(){

        setSearchViewHide();
        mSearchEdit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) mSearchEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mSearchEdit, 0);
        //mManagerTitle.setVisibility(View.GONE);
        mSearchTitle.setVisibility(View.VISIBLE);
        //Intent intent = new Intent(GoodsManagerActivity.this,GoodsSearchActivity.class);
        // startActivity(intent);

    }

    public static void setSearchViewHide(){
        mSearch.setVisibility(View.GONE);

    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.goods_viewPager);
        fragmentsList = new ArrayList<>();
        Fragment fragment = new GoodsManagerTab1Fragment();
        fragmentsList.add(fragment);
        fragment = new GoodsManagerTab2Fragment();
        fragmentsList.add(fragment);

        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
                fragmentsList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selling:
                viewPager.setCurrentItem(0);
                break;
            case R.id.sold_out:
                viewPager.setCurrentItem(1);
                break;
            case R.id.search:
                startSearch();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        offset = (screen1_2 - cursorImg.getLayoutParams().width) / 2;
        Log.d("111", position + "--" + positionOffset + "--"
                + positionOffsetPixels);
        final float scale = getResources().getDisplayMetrics().density;
        if (position == 0) {// 0<->1
            lp.leftMargin = (int) (positionOffsetPixels / 2) + offset;
        } else if (position == 1) {// 1<->2
            lp.leftMargin = (int) (positionOffsetPixels / 2) + screen1_2 +offset;
        }
        cursorImg.setLayoutParams(lp);
        currentIndex = position;
    }

    @Override
    public void onPageSelected(int arg0) {
    }

}
