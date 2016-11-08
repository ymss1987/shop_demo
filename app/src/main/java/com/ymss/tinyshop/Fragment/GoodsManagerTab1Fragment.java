package com.ymss.tinyshop.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymss.tinyshop.GoodsMorePopWindow;
import com.ymss.tinyshop.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adminstrator on 2016/10/31.
 */

public class GoodsManagerTab1Fragment extends Fragment implements View.OnClickListener {

    private List<Map<String, Object>> mData;
    private ListView mListView;

    private TextView shown_time;
    private ImageView shown_order1;
    private TextView shown_sell;
    private ImageView shown_order2;
    private TextView shown_stock;
    private ImageView shown_order3;
    private boolean mBefore = true;
    private MyAdapter mAdapter;

    private Activity mActivity;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goods_manager_fragment, null);
        mListView = (ListView)view.findViewById(R.id.goods_list);
        mData = getData();
        mAdapter = new MyAdapter(this.getContext());

        compareTime(mBefore);

        mListView.setAdapter(mAdapter);
        //mListView.setListAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("MyListView4-click", (String)mData.get(position).get("title"));
                Toast.makeText(mActivity,(String)mData.get(position).get("title"),Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout layAddTime = (LinearLayout)view.findViewById(R.id.add_time);
        layAddTime.setOnClickListener(this);
        LinearLayout laySellSum = (LinearLayout)view.findViewById(R.id.sell_sum);
        laySellSum.setOnClickListener(this);
        LinearLayout layStockSum = (LinearLayout)view.findViewById(R.id.stock_sum);
        layStockSum.setOnClickListener(this);
        shown_time = (TextView)view.findViewById(R.id.shown_time);;
        shown_order1 = (ImageView)view.findViewById(R.id.shown_order1);;
        shown_sell = (TextView)view.findViewById(R.id.shown_sell);;
        shown_order2 = (ImageView)view.findViewById(R.id.shown_order2);;
        shown_stock = (TextView)view.findViewById(R.id.shown_stock);;
        shown_order3 = (ImageView)view.findViewById(R.id.shown_order3);
        setCurrentSelectOrder(0);

        mActivity = this.getActivity();

        return view;
    }

    public void compareTime(final boolean before){
        Collections.sort(mData, new Comparator<Map>() {
            /**
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(Map lhs, Map rhs) {
                Date date1 = stringToDate(lhs.get("time").toString());
                Date date2 = stringToDate(rhs.get("time").toString());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (before) {
                    if (date1.before(date2)) {
                        return 1;
                    }
                }else{
                    if (date1.after(date2)) {
                        return 1;
                    }
                }
                return -1;
            }
        });
    }

    public void compareValue(final String key, final boolean before){
        Collections.sort(mData, new Comparator<Map>() {
            /**
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(Map lhs, Map rhs) {
                int date1 = Integer.valueOf(lhs.get(key).toString());
                int date2 = Integer.valueOf(rhs.get(key).toString());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (before) {
                    if (date1 > date2) {
                        return 1;
                    }
                }else{
                    if (date1 < date2) {
                        return 1;
                    }
                }
                return -1;
            }
        });
    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    private void setCurrentSelectOrder(int order){
        shown_time.setTextColor(Color.parseColor("#a0a0a0"));
        shown_sell.setTextColor(Color.parseColor("#a0a0a0"));
        shown_stock.setTextColor(Color.parseColor("#a0a0a0"));
        shown_order1.setVisibility(View.INVISIBLE);
        shown_order2.setVisibility(View.INVISIBLE);
        shown_order3.setVisibility(View.INVISIBLE);
        if (mBefore == true){
            mBefore = false;
        }else{
            mBefore = true;
        }
        if (order == 0){
            shown_time.setTextColor(Color.parseColor("#68adf9"));
            shown_order1.setVisibility(View.VISIBLE);
            compareTime(mBefore);
           // mListView.deferNotifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
            if (mBefore == true){
                shown_order1.setImageResource(R.drawable.asset_sort_up_icon);
            }else{
                shown_order1.setImageResource(R.drawable.asset_sort_down_icon);
            }
        }else  if (order == 1){
            shown_sell.setTextColor(Color.parseColor("#68adf9"));
            shown_order2.setVisibility(View.VISIBLE);
            compareValue("sell", mBefore);
            mAdapter.notifyDataSetChanged();
            if (mBefore == true){
                shown_order2.setImageResource(R.drawable.asset_sort_up_icon);
            }else{
                shown_order2.setImageResource(R.drawable.asset_sort_down_icon);
            }
        }else  if (order == 2){
            shown_stock.setTextColor(Color.parseColor("#68adf9"));
            shown_order3.setVisibility(View.VISIBLE);
            compareValue("stock", mBefore);
            mAdapter.notifyDataSetChanged();
            if (mBefore == true){
                shown_order3.setImageResource(R.drawable.asset_sort_up_icon);
            }else{
                shown_order3.setImageResource(R.drawable.asset_sort_down_icon);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_time:
                setCurrentSelectOrder(0);
                break;
            case R.id.sell_sum:
                setCurrentSelectOrder(1);
                break;
            case R.id.stock_sum:
                setCurrentSelectOrder(2);
                break;
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "舞动的女孩");
        //map.put("stock", "销量:101  库存:99");
        map.put("price", "¥18.00");
        map.put("img", 0);
        map.put("time","2012-12-12 12:30");
        map.put("sell", 99);
        map.put("stock", 101);

        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "八仙花");
        //map.put("stock", "销量:10  库存:91");
        map.put("price", "¥12.00");
        map.put("img", R.drawable.shop640x640_01);
        map.put("time","2012-12-12 10:20");
        map.put("sell", 10);
        map.put("stock", 91);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "库存测试");
        //map.put("stock", "销量:0  库存:4");
        map.put("price", "¥14.00");
        map.put("img", R.drawable.asset_logorectangle);
        map.put("time","2012-12-13 10:20");
        map.put("sell", 0);
        map.put("stock", 4);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "小猫咪");
        //map.put("stock", "销量:11  库存:92");
        map.put("price", "¥0.10");
        map.put("img", R.drawable.shop640x640_01);
        map.put("time","2012-12-15 10:20");
        map.put("sell", 11);
        map.put("stock", 92);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "支付宝测试商品支付宝测试商品");
        //map.put("stock", "销量:14  库存:0");
        map.put("price", "¥0.01");
        map.put("img", R.drawable.asset_logorectangle);
        map.put("time","2012-12-16 10:20");
        map.put("sell", 14);
        map.put("stock", 0);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "搞笑");
       // map.put("stock", "销量:2  库存:68");
        map.put("price", "¥90.00");
        map.put("img", R.drawable.shop640x640_01);
        map.put("time","2012-12-17 10:20");
        map.put("sell", 2);
        map.put("stock", 68);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "红酒");
       // map.put("stock", "销量:0  库存:0");
        map.put("price", "¥89.00");
        map.put("img", R.drawable.asset_logorectangle);
        map.put("time","2012-12-18 10:20");
        map.put("sell", 0);
        map.put("stock", 0);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "回锅肉");
        //map.put("stock", "销量:17  库存:0");
        map.put("price", "¥12.00");
        map.put("img", R.drawable.asset_logorectangle);
        map.put("time","2012-12-20 10:20");
        map.put("sell", 17);
        map.put("stock", 0);
        list.add(map);

        return list;
    }

    /**
     * listview中点击按键弹出对话框
     */
    public void showInfo(final int pos){
        new AlertDialog.Builder(this.getContext())
                .setTitle("我的listview")
                .setMessage("你点击了第 " + pos +" 行")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }



    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView price;
        public TextView stock;
        public Button viewBtn;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.goods_list_view, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.price = (TextView)convertView.findViewById(R.id.price);
                holder.stock = (TextView)convertView.findViewById(R.id.stock);
                holder.viewBtn = (Button)convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }

            holder.img.setImageResource((Integer)mData.get(position).get("img"));
            holder.title.setText((String)mData.get(position).get("title"));
            holder.price.setText((String)mData.get(position).get("price"));
            String stock = "销量:"+(String)mData.get(position).get("sell").toString()+"  库存:"+(String)mData.get(position).get("stock").toString();
            holder.stock.setText(stock);

            holder.viewBtn.setTag(position);
            holder.viewBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //showInfo(Integer.parseInt(v.getTag().toString()));
                    GoodsMorePopWindow morePopWindow = new GoodsMorePopWindow(mActivity,v.getLeft(),v.getRight(),v.getTop(),v.getBottom());
                    morePopWindow.showPopupWindow(v);
                }
            });


            return convertView;
        }

    }
}
