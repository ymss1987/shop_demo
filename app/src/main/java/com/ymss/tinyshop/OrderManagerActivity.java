package com.ymss.tinyshop;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymss.adapter.NormalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout pending;
    private LinearLayout processed;
    private LinearLayout refund;
    private LinearLayout done;
    private LinearLayout closed;
    private View underLine;
    private LinearLayout titleextra;
    private RecyclerView orderRecycler;
    private RelativeLayout mNoContent;
    private LinearLayout mTitleBack;

    private LinearLayout oldFocus = null;
    private List<Map<String, Object>> mData = new ArrayList<>();
    private NormalRecyclerViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);

        titleextra = (LinearLayout)findViewById(R.id.title_extra);
        orderRecycler = (RecyclerView)findViewById(R.id.order_recycler);
        orderRecycler.setLayoutManager(new LinearLayoutManager(this));
        mNoContent = (RelativeLayout) findViewById(R.id.no_content);

        mAdapter = new NormalRecyclerViewAdapter(this,mData);
        orderRecycler.setAdapter(mAdapter);

        mTitleBack = (LinearLayout)findViewById(R.id.title_back);
        mTitleBack.setOnClickListener(this);


        underLine = findViewById(R.id.underline);
        pending = (LinearLayout)findViewById(R.id.order_pending);
        pending.setOnClickListener(this);
        setTextColor(pending, null);
        oldFocus = (LinearLayout)pending;
        processed = (LinearLayout)findViewById(R.id.order_processed);
        processed.setOnClickListener(this);
        refund = (LinearLayout)findViewById(R.id.order_refund);
        refund.setOnClickListener(this);
        done = (LinearLayout)findViewById(R.id.order_done);
        done.setOnClickListener(this);
        closed = (LinearLayout)findViewById(R.id.order_closed);
        closed.setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.order_pending:
                setTextColor((LinearLayout)v, oldFocus);
                oldFocus = (LinearLayout)v;
                titleextra.setVisibility(View.VISIBLE);
                mNoContent.setVisibility(View.VISIBLE);
                orderRecycler.setVisibility(View.GONE);
                break;
            case R.id.order_processed:
                setTextColor((LinearLayout)v, oldFocus);
                oldFocus = (LinearLayout)v;
                titleextra.setVisibility(View.VISIBLE);
                mNoContent.setVisibility(View.GONE);
                orderRecycler.setVisibility(View.VISIBLE);
                initData1();
                mAdapter.notifyDataSetChanged();
                orderRecycler.scrollToPosition(0);
                break;
            case R.id.order_refund:
                setTextColor((LinearLayout)v, oldFocus);
                oldFocus = (LinearLayout)v;
                titleextra.setVisibility(View.GONE);
                mNoContent.setVisibility(View.VISIBLE);
                orderRecycler.setVisibility(View.GONE);
                break;
            case R.id.order_done:
                setTextColor((LinearLayout)v, oldFocus);
                oldFocus = (LinearLayout)v;
                titleextra.setVisibility(View.GONE);
                mNoContent.setVisibility(View.GONE);
                orderRecycler.setVisibility(View.VISIBLE);
                initData2();
                //orderRecycler.setAdapter(new NormalRecyclerViewAdapter(this,mData));
                mAdapter.notifyDataSetChanged();
                orderRecycler.scrollToPosition(0);
                break;
            case R.id.order_closed:
                setTextColor((LinearLayout)v, oldFocus);
                oldFocus = (LinearLayout)v;
                titleextra.setVisibility(View.GONE);
                mNoContent.setVisibility(View.GONE);
                orderRecycler.setVisibility(View.VISIBLE);
                initData3();
                //orderRecycler.setAdapter(new NormalRecyclerViewAdapter(this,mData));
                mAdapter.notifyDataSetChanged();
                orderRecycler.scrollToPosition(0);
                break;
            case R.id.title_back:
                finish();
                break;

        }

    }

    private void setTextColor(LinearLayout newView, LinearLayout oldView){
        int count = newView.getChildCount();
        for(int i=0;i<count;i++){
            View view = newView.getChildAt(i);
            if(view!=null){
                ((TextView)view).setTextColor(Color.parseColor("#4a9df8"));
            }
        }

        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(underLine.getLayoutParams());
        //ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) underLine.getLayoutParams();
        //lp.setMargins(newView.getLeft(),0,0,0);
        //underLine.requestLayout();
        if (oldView!=null) {
            underLine.setLeft(newView.getLeft());
            underLine.setRight(newView.getRight());
        }
        if (oldView!=null) {
            count = oldView.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = oldView.getChildAt(i);
                if (view != null) {
                    ((TextView) view).setTextColor(Color.parseColor("#888888"));
                }
            }
        }
    }

    private void initData1(){
        mData.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("title","2016年09月09日");
        mData.add(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("orderNo","订单编号：V347");
        map1.put("customerName","*长安");
        map1.put("customerAmount","¥0.10");
        map1.put("customerTime","15:48:10");
        map1.put("orderType","倍棒商城订单");
        map1.put("payType","在线支付");
        mData.add(map1);

        Map<String,Object> map0 = new HashMap<>();
        map0.put("title","2016年09月01日");
        mData.add(map0);

        Map<String,Object> map2 = new HashMap<>();
        map2.put("orderNo","订单编号：2197");
        map2.put("customerName","*阳");
        map2.put("customerAmount","¥0.10");
        map2.put("customerTime","18:07:02");
        map2.put("orderType","倍棒商城订单");
        map2.put("payType","在线支付");
        mData.add(map2);
    }

    private void initData2(){
        mData.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("title","2016年11月24日");
        mData.add(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("orderNo","订单编号：S891");
        map1.put("customerName","*长安");
        map1.put("customerAmount","¥0.05");
        map1.put("customerTime","10:28:17");
        map1.put("orderType","当面付");
        map1.put("payType","在线支付");
        mData.add(map1);

        Map<String,Object> map0 = new HashMap<>();
        map0.put("title","2016年11月23日");
        mData.add(map0);

        Map<String,Object> map2 = new HashMap<>();
        map2.put("orderNo","订单编号：G238");
        map2.put("customerName","*长安");
        map2.put("customerAmount","¥0.05");
        map2.put("customerTime","15:57:10");
        map2.put("orderType","当面付");
        map2.put("payType","在线支付");
        mData.add(map2);
        Map<String,Object> map3 = new HashMap<>();
        map3.put("orderNo","订单编号：0463");
        map3.put("customerName","*长安");
        map3.put("customerAmount","¥0.05");
        map3.put("customerTime","14:48:25");
        map3.put("orderType","当面付");
        map3.put("payType","在线支付");
        mData.add(map3);
        Map<String,Object> map4 = new HashMap<>();
        map4.put("orderNo","订单编号：F754");
        map4.put("customerName","*长安");
        map4.put("customerAmount","¥0.05");
        map4.put("customerTime","10:43:49");
        map4.put("orderType","当面付");
        map4.put("payType","在线支付");
        mData.add(map4);
        Map<String,Object> map5 = new HashMap<>();
        map5.put("orderNo","订单编号：G974");
        map5.put("customerName","*长安");
        map5.put("customerAmount","¥0.05");
        map5.put("customerTime","09:42:38");
        map5.put("orderType","当面付");
        map5.put("payType","在线支付");
        mData.add(map5);
        Map<String,Object> map6 = new HashMap<>();
        map6.put("orderNo","订单编号：2371");
        map6.put("customerName","*长安");
        map6.put("customerAmount","¥0.10");
        map6.put("customerTime","09:42:38");
        map6.put("orderType","倍棒商城订单");
        map6.put("payType","在线支付");
        mData.add(map6);
        Map<String,Object> map00 = new HashMap<>();
        map00.put("title","2016年11月22日");
        mData.add(map00);
        Map<String,Object> map7 = new HashMap<>();
        map7.put("orderNo","订单编号：L230");
        map7.put("customerName","*顺苹");
        map7.put("customerAmount","¥0.10");
        map7.put("customerTime","11:40:13");
        map7.put("orderType","倍棒商城订单");
        map7.put("payType","在线支付");
        mData.add(map7);
    }

    private void initData3(){
        mData.clear();
        Map<String,Object> map = new HashMap<>();
        map.put("title","2016年10月25日");
        mData.add(map);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("orderNo","订单编号：X138");
        map1.put("customerName","匿名客户");
        map1.put("customerAmount","¥0.10");
        map1.put("customerTime","10:20:05");
        map1.put("orderType","当面付");
        map1.put("payType","微信扫码");
        mData.add(map1);

        Map<String,Object> map2 = new HashMap<>();
        map2.put("orderNo","订单编号：0864");
        map2.put("customerName","匿名客户");
        map2.put("customerAmount","¥0.10");
        map2.put("customerTime","10:20:05");
        map2.put("orderType","当面付");
        map2.put("payType","微信扫码");
        mData.add(map2);

        Map<String,Object> map0 = new HashMap<>();
        map0.put("title","2016年09月09日");
        mData.add(map0);

        Map<String,Object> map3 = new HashMap<>();
        map3.put("orderNo","订单编号：L771");
        map3.put("customerName","*长安");
        map3.put("customerAmount","¥0.10");
        map3.put("customerTime","22:15:06");
        map3.put("orderType","倍棒商城订单");
        map3.put("payType","在线支付");
        mData.add(map3);

        Map<String,Object> map10 = new HashMap<>();
        map0.put("title","2016年09月01日");
        mData.add(map0);

        Map<String,Object> map4 = new HashMap<>();
        map4.put("orderNo","订单编号：V212");
        map4.put("customerName","*阳");
        map4.put("customerAmount","¥0.10");
        map4.put("customerTime","22:15:05");
        map4.put("orderType","倍棒商城订单");
        map4.put("payType","在线支付");
        mData.add(map4);
        Map<String,Object> map5 = new HashMap<>();
        map5.put("orderNo","订单编号：X181");
        map5.put("customerName","*阳");
        map5.put("customerAmount","¥0.10");
        map5.put("customerTime","22:15:05");
        map5.put("orderType","当面付");
        map5.put("payType","在线支付");
        mData.add(map5);
        Map<String,Object> map6 = new HashMap<>();
        map6.put("orderNo","订单编号：W140");
        map6.put("customerName","*阳");
        map6.put("customerAmount","¥0.10");
        map6.put("customerTime","22:15:05");
        map6.put("orderType","当面付");
        map6.put("payType","在线支付");
        mData.add(map6);
        Map<String,Object> map00 = new HashMap<>();
        map00.put("title","2016年08月31日");
        mData.add(map00);
        Map<String,Object> map7 = new HashMap<>();
        map7.put("orderNo","订单编号：E136");
        map7.put("customerName","*顺苹");
        map7.put("customerAmount","¥0.10");
        map7.put("customerTime","11:40:13");
        map7.put("orderType","倍棒商城订单");
        map7.put("payType","在线支付");
        mData.add(map7);
    }

}
