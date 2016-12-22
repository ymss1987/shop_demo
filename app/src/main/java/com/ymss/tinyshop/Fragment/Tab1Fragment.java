package com.ymss.tinyshop.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymss.adapter.GridViewAdapter;
import com.ymss.keyboard.KeyboardTool;
import com.ymss.tinyshop.GoodsManagerActivity;
import com.ymss.tinyshop.MainActivity;
import com.ymss.tinyshop.OrderManagerActivity;
import com.ymss.tinyshop.R;
import com.ymss.tinyshop.SelectShopActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ymss on 2016/10/30.
 */

public class Tab1Fragment extends Fragment implements View.OnClickListener {

    // 图片封装为一个数组
    private int[] icon = { R.drawable.goodsmanagenor_3x, R.drawable.ordermanagenor_3x,
            R.drawable.shopmanagenor_3x, R.drawable.purchasemanagenor_3x, R.drawable.financemanagedis_3x,
            R.drawable.underordersdis_3x, R.drawable.membermanagedis_3x, R.drawable.marketingtooldis_3x};
    private String[] iconName = { "商品管理", "订单管理", "店铺管理", "供货管理", "财务管理", "代客下单", "会员管理",
            "营销管理"};

    private GridView gview;
    private List<Map<String, Object>> data_list;
    //private SimpleAdapter sim_adapter;
    private GridViewAdapter.GridViewClickListener listener = new GridViewAdapter.GridViewClickListener(){
        @Override
        public void onItemClick(View view, int position){
            if (position ==0){
                Intent intent = new Intent(view.getContext(), GoodsManagerActivity.class);
                startActivity(intent);
            } if(position==1){
                Intent intent = new Intent(view.getContext(), OrderManagerActivity.class);
                startActivity(intent);
            }  else{
                Toast.makeText(view.getContext(), data_list.get(position).get("text").toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragment_tab1, null);
        gview = (GridView) view.findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();

        final GridViewAdapter seriesAdapter = new GridViewAdapter(container.getContext(),data_list,listener);
        gview.setAdapter(seriesAdapter);

        //新建适配器
       /* String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
       // gview.setSelector(new ColorDrawable(Color.TRANSPARENT));
       */
       /* gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //在本例中arg2=arg3
                HashMap<String, Object> item=(HashMap<String, Object>) parent.getItemAtPosition(position);
                //显示所选Item的ItemText
                Toast.makeText(MainActivity.this,item.get("text").toString(),Toast.LENGTH_SHORT).show();
            }
        });*/

        TextView changeShop = (TextView)view.findViewById(R.id.change_shop);
        // changeShop.setText(Html.fromHtml("<u>切换店铺</u>"));
        changeShop.setOnClickListener(this);

        KeyboardTool tool = new KeyboardTool(this.getContext(),this.getActivity());
        tool.setKeyboardTypeById(R.id.edit_money,0);

        return view;
    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_shop:
                Intent intent = new Intent(this.getActivity(), SelectShopActivity.class);
                startActivity(intent);
                break;
            case R.id.tab2_layout:
                //viewPager.setCurrentItem(1);
                break;
            case R.id.tab3_layout:
               // viewPager.setCurrentItem(2);
                break;
        }
    }

}
