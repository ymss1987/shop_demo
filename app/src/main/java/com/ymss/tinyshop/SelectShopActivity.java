package com.ymss.tinyshop;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ymss.adapter.Category;
import com.ymss.adapter.CategoryAdapter;
import com.ymss.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectShopActivity extends Activity {

    private CategoryAdapter mCustomBaseAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);

        ListView listView = (ListView) findViewById(R.id.shop_list);

        // 数据
        ArrayList<Category> listData = getData();

        mCustomBaseAdapter = new CategoryAdapter(getBaseContext(), listData);

        // 适配器与ListView绑定
        listView.setAdapter(mCustomBaseAdapter);

        listView.setOnItemClickListener(new ItemClickListener());

        TextView exit = (TextView) findViewById(R.id.title_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private class ItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Category.ItemEntity1 item = mCustomBaseAdapter.getItemContent(position);
            if (item!=null) {
                Toast.makeText(view.getContext(), (String) item.getContent(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }


    /**
     * 创建测试数据
     */
    private ArrayList<Category> getData() {
        ArrayList<Category> listData = new ArrayList<Category>();
        Category categoryOne = new Category("倍棒合作代理",R.drawable.asset_detail15);
        categoryOne.addItem("我的推广",R.drawable.promotion_logo_3x, null);
        Category categoryTwo = new Category("我的店铺",R.drawable.detail_14_3x);
        categoryTwo.addItem("自主lm5",R.drawable.shop640x640_01,"如果太热个人");

        Category categoryThree = new Category("我加入的店铺",R.drawable.icon_invoice);
        categoryThree.addItem("红旗超市",0,"艾思思");
        categoryThree.addItem("十里香蛋糕铺",R.drawable.shop640x640_02,"罗曼门店1");

        listData.add(categoryOne);
        listData.add(categoryTwo);
        listData.add(categoryThree);

        return listData;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = getListView();

        List<ItemEntity> data = createTestData();

        ListViewAdapter customAdapter = new ListViewAdapter(getApplication(), data);

        listView.setAdapter(customAdapter);
    }

    // ===========================================================
    // Methods
    // ===========================================================
    private List<ItemEntity> createTestData() {

        List<ItemEntity> data = new ArrayList<ItemEntity>();

        ItemEntity itemEntity1 = new ItemEntity("倍棒合作代理",R.drawable.asset_detail15, "我的推广",R.drawable.promotion_logo_3x, null);
        ItemEntity itemEntity2 = new ItemEntity("我的店铺",R.drawable.detail_14_3x, "自主lm5",R.drawable.shop640x640_01,"如果太热个人");
        ItemEntity itemEntity3 = new ItemEntity("我加入的店铺",R.drawable.icon_invoice, "红旗超市",0,"艾思思");
        ItemEntity itemEntity4 = new ItemEntity("我加入的店铺",R.drawable.icon_invoice, "十里香蛋糕铺",R.drawable.shop640x640_02,"罗曼门店1");

        data.add(itemEntity1);
        data.add(itemEntity2);
        data.add(itemEntity3);
        data.add(itemEntity4);

        return data;

    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


    */

    public class ItemEntity {
        private String mTitle;
        private String mContent;
        private String mDescribe;
        private int mTitleImage;
        private int mItemImage;

        public ItemEntity(String pTitle, int titleImage, String pContent, int itemImage, String pDescribe) {
            mTitle = pTitle;
            mTitleImage = titleImage;
            mContent = pContent;
            mItemImage = itemImage;
            mDescribe =pDescribe;
        }

        public String getTitle() {
            return mTitle;
        }
        public int getTitleImage() {
            return mTitleImage;
        }
        public String getContent() {
            return mContent;
        }
        public int getmItemImage() {
            return mItemImage;
        }
        public String getDescribe() {
            return mDescribe;
        }
    }


  /*  ArrayList<HashMap<String, Object>> listItem;
    HashMap<String, Object> map ;
    SimpleAdapter listItemAdapter;
    ListView list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_shop);

        // 绑定Layout里面的ListView
        list = (ListView) findViewById(R.id.shop_list);

        // 生成动态数组，加入数据
        listItem = new ArrayList<HashMap<String, Object>>();
        String[] a={"倍棒合作代理","我的推广"};
        String[] b={"我的店铺","自主lm5"};
        String[] c={"我加入的店铺","红旗超市","十里香蛋糕铺"};


        String[] describeA={null};
        String[] describeB={"如果太热个人"};
        String[] describeC={"艾思思","罗曼门店1"};

        int[] Image_a={R.drawable.asset_detail15,R.drawable.promotion_logo_3x};

        int[] Image_b={R.drawable.detail_14_3x,R.drawable.shop640x640_01};

        int[] Image_c={R.drawable.icon_invoice,0,R.drawable.shop640x640_02};

        map(a,Image_a,describeA);
        map(b,Image_b,describeB);
        map(c,Image_c,describeC);
        //map(d,Image_a);
        listItemAdapter = new SimpleAdapter(this, listItem,// 数据源
                R.layout.shoplist_items_menu,// ListItem的XML实现
                // 动态数组与ImageItem对应的子项
                new String[] {"TitleImage", "TitleText", "ItemImage", "ItemText","ItemDescribe" },
                // ImageItem的XML文件里面的一个ImageView,TextView ID
                new int[] { R.id.TitleImage, R.id.TitleText,R.id.ItemImage, R.id.ItemText,R.id.ItemDescribe });

        // 添加并且显示
        list.setAdapter(listItemAdapter);

        // 添加菜单点击
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                for (int i = 0; i < listItem.size(); i++) {
                    if (arg2 == i) {
                        Toast.makeText(getApplicationContext(),
                                listItem.get(arg2).get("ItemText") + "    " + arg2, Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            }
        });

    }


    public void map(String[] a,int[] Image,String[] describe){
        for(int i=0;i<a.length;i++) {
            map = new HashMap<String, Object>();
            if (i == 0) {
                map.put("TitleImage", Image[0]);
                map.put("TitleText", a[0]);
            } else {
                map.put("ItemImage", Image[i]);// 图像资源的ID
                map.put("ItemText", a[i]);
                map.put("ItemDescribe", describe[i-1]);
            }
            listItem.add(map);
        }
    }*/



}
