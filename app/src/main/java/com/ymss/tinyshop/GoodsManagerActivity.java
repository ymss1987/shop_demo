package com.ymss.tinyshop;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

    private ImageView title_imgback;
    private TextView title_txtback;
    private LinearLayout searchTitle;
    private ImageView title_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_manager);
        title_imgback = (ImageView)findViewById(R.id.title_imgback);
        title_txtback = (TextView) findViewById(R.id.title_txtback);
        title_imgback.setOnClickListener(this);
        title_txtback.setOnClickListener(this);

        title_back = (ImageView)findViewById(R.id.title_back);
        title_back.setOnClickListener(this);
        searchTitle = (LinearLayout) findViewById(R.id.searchTitle);

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

        setSearchViewHide(true);
        mSearchEdit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) mSearchEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mSearchEdit, 0);
        //mManagerTitle.setVisibility(View.GONE);
        mSearchTitle.setVisibility(View.VISIBLE);
        //Intent intent = new Intent(GoodsManagerActivity.this,GoodsSearchActivity.class);
        // startActivity(intent);

    }

    public static void setSearchViewHide(boolean isHide){
        if (isHide == true) {
            mSearch.setVisibility(View.GONE);
        }else{
            mSearch.setVisibility(View.VISIBLE);
        }

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
        tab1Tv.setTextColor(Color.parseColor("#4a9df8"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selling:
                viewPager.setCurrentItem(0);
                tab1Tv.setTextColor(Color.parseColor("#4a9df8"));
                tab2Tv.setTextColor(Color.parseColor("#a0a0a0"));
                break;
            case R.id.sold_out:
                viewPager.setCurrentItem(1);
                tab1Tv.setTextColor(Color.parseColor("#a0a0a0"));
                tab2Tv.setTextColor(Color.parseColor("#4a9df8"));
                break;
            case R.id.search:
                startSearch();
                break;
            case R.id.title_imgback:
                finish();
                break;
            case R.id.title_txtback:
                finish();
                break;
            case R.id.title_back:
                searchTitle.setVisibility(View.GONE);
                setSearchViewHide(false);
                InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if ( imm.isActive( ) ) {
                    imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );
                }
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
