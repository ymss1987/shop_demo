package com.ymss.tinyshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class EditGoodsActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Integer> mImgageList = new ArrayList<Integer>();
    private boolean[] mStandardList = new boolean[10];

    private ImageView imageview1;
    private ImageView imageview2;
    private ImageView imageview3;
    private ImageView imageview4;
    private ImageView imageview5;
    private ImageView imageview6;
    private ImageView imageview7;
    private ImageView imageview8;
    private ImageView imageview9;
    private ImageView imageview10;

    private FrameLayout imageframe1;
    private FrameLayout imageframe2;
    private FrameLayout imageframe3;
    private FrameLayout imageframe4;
    private FrameLayout imageframe5;
    private FrameLayout imageframe6;
    private FrameLayout imageframe7;
    private FrameLayout imageframe8;
    private FrameLayout imageframe9;
    private FrameLayout imageframe10;

    private Button imageaddbtn1;
    private Button imageaddbtn2;

    private LinearLayout imageLayout2;

    private LinearLayout[] goodsstandard = new LinearLayout[10];

    private View[] goodsstandard_line  =new View[10];

    private LinearLayout goodsadd;

    private View selllayoutline;
    private View stocklayoutline;
    private LinearLayout selllayout;
    private LinearLayout stocklayout;

    private LinearLayout goodsfunction;

    private EditText goodsname;
    private EditText goodssell;
    private EditText goodsstock;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goods);

        stocklayout = (LinearLayout) findViewById(R.id.stock_layout);
        stocklayoutline = (View) findViewById(R.id.stock_layout_line);
        selllayout = (LinearLayout) findViewById(R.id.sell_layout);
        selllayoutline = (View) findViewById(R.id.sell_layout_line);

        goodsfunction = (LinearLayout) findViewById(R.id.goods_function_layout);
        goodsname = (EditText)findViewById(R.id.goods_name_edit);
        goodssell = (EditText)findViewById(R.id.goods_sell_edit);
        goodsstock = (EditText)findViewById(R.id.goods_stock_edit);

        Intent intent = getIntent();
        if (intent!=null) {
            String title = intent.getStringExtra("title");
            goodsname.setText(title);
            String price = intent.getStringExtra("price");
            goodssell.setText(price);
            int img = intent.getIntExtra("img", 0);
            if (img!=0){
                mImgageList.add(new Integer(img));
            }
            String time = intent.getStringExtra("time");
            int sell = intent.getIntExtra("sell", 0);
            int stock = intent.getIntExtra("stock", 0);
            goodsstock.setTextColor(stock);
        }else{
            goodsfunction.setVisibility(View.GONE);
        }

        imageview1 = (ImageView)findViewById(R.id.image_view1);
        imageview2 = (ImageView)findViewById(R.id.image_view2);
        imageview3 = (ImageView)findViewById(R.id.image_view3);
        imageview4 = (ImageView)findViewById(R.id.image_view4);
        imageview5 = (ImageView)findViewById(R.id.image_view5);
        imageview6 = (ImageView)findViewById(R.id.image_view6);
        imageview7 = (ImageView)findViewById(R.id.image_view7);
        imageview8 = (ImageView)findViewById(R.id.image_view8);
        imageview9 = (ImageView)findViewById(R.id.image_view9);
        imageview10 = (ImageView)findViewById(R.id.image_view10);

        imageframe1 = (FrameLayout)findViewById(R.id.image_frame1);
        imageframe2 = (FrameLayout)findViewById(R.id.image_frame2);
        imageframe3 = (FrameLayout)findViewById(R.id.image_frame3);
        imageframe4 = (FrameLayout)findViewById(R.id.image_frame4);
        imageframe5 = (FrameLayout)findViewById(R.id.image_frame5);
        imageframe6 = (FrameLayout)findViewById(R.id.image_frame6);
        imageframe7 = (FrameLayout)findViewById(R.id.image_frame7);
        imageframe8 = (FrameLayout)findViewById(R.id.image_frame8);
        imageframe9 = (FrameLayout)findViewById(R.id.image_frame9);
        imageframe10 = (FrameLayout)findViewById(R.id.image_frame10);

        imageaddbtn1 = (Button) findViewById(R.id.image_addbtn1);
        imageaddbtn2 = (Button) findViewById(R.id.image_addbtn2);

        imageLayout2 = (LinearLayout) findViewById(R.id.image_layout2);

        goodsadd = (LinearLayout) findViewById(R.id.goods_add);
        goodsadd.setOnClickListener(this);

        ((ImageView) findViewById(R.id.goods_standard_delete1)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete2)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete3)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete4)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete5)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete6)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete7)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete8)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete9)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.goods_standard_delete10)).setOnClickListener(this);

        ((ImageButton) findViewById(R.id.include_title_back)).setOnClickListener(this);


        goodsstandard[0] = (LinearLayout) findViewById(R.id.goods_standard1);
        goodsstandard[1] = (LinearLayout) findViewById(R.id.goods_standard2);
        goodsstandard[2] = (LinearLayout) findViewById(R.id.goods_standard3);
        goodsstandard[3] = (LinearLayout) findViewById(R.id.goods_standard4);
        goodsstandard[4] = (LinearLayout) findViewById(R.id.goods_standard5);
        goodsstandard[5] = (LinearLayout) findViewById(R.id.goods_standard6);
        goodsstandard[6] = (LinearLayout) findViewById(R.id.goods_standard7);
        goodsstandard[7] = (LinearLayout) findViewById(R.id.goods_standard8);
        goodsstandard[8] = (LinearLayout) findViewById(R.id.goods_standard9);
        goodsstandard[9] = (LinearLayout) findViewById(R.id.goods_standard10);

        goodsstandard_line[0] = (View) findViewById(R.id.goods_standard_line1);
        goodsstandard_line[1] = (View) findViewById(R.id.goods_standard_line2);
        goodsstandard_line[2] = (View) findViewById(R.id.goods_standard_line3);
        goodsstandard_line[3] = (View) findViewById(R.id.goods_standard_line4);
        goodsstandard_line[4] = (View) findViewById(R.id.goods_standard_line5);
        goodsstandard_line[5] = (View) findViewById(R.id.goods_standard_line6);
        goodsstandard_line[6] = (View) findViewById(R.id.goods_standard_line7);
        goodsstandard_line[7] = (View) findViewById(R.id.goods_standard_line8);
        goodsstandard_line[8] = (View) findViewById(R.id.goods_standard_line9);
        goodsstandard_line[9] = (View) findViewById(R.id.goods_standard_line10);

        for(int i=0;i<10;i++){
            mStandardList[i] = false;
        }





        setImageShow();

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.goods_standard_delete1:
                goodsstandard[0].setVisibility(View.GONE);
                goodsstandard_line[0].setVisibility(View.GONE);
                mStandardList[0]=false;
            break;
            case R.id.goods_standard_delete2:
                goodsstandard[1].setVisibility(View.GONE);
                goodsstandard_line[1].setVisibility(View.GONE);
                mStandardList[1]=false;
                break;
            case R.id.goods_standard_delete3:
                goodsstandard[2].setVisibility(View.GONE);
                goodsstandard_line[2].setVisibility(View.GONE);
                mStandardList[2]=false;
                break;
            case R.id.goods_standard_delete4:
                goodsstandard[3].setVisibility(View.GONE);
                goodsstandard_line[3].setVisibility(View.GONE);
                mStandardList[3]=false;
                break;
            case R.id.goods_standard_delete5:
                goodsstandard[4].setVisibility(View.GONE);
                goodsstandard_line[4].setVisibility(View.GONE);
                mStandardList[4]=false;
                break;
            case R.id.goods_standard_delete6:
                goodsstandard[5].setVisibility(View.GONE);
                goodsstandard_line[5].setVisibility(View.GONE);
                mStandardList[5]=false;
                break;
            case R.id.goods_standard_delete7:
                goodsstandard[6].setVisibility(View.GONE);
                goodsstandard_line[6].setVisibility(View.GONE);
                mStandardList[6]=false;
                break;
            case R.id.goods_standard_delete8:
                goodsstandard[7].setVisibility(View.GONE);
                goodsstandard_line[7].setVisibility(View.GONE);
                mStandardList[7]=false;
                break;
            case R.id.goods_standard_delete9:
                goodsstandard[8].setVisibility(View.GONE);
                goodsstandard_line[8].setVisibility(View.GONE);
                mStandardList[8]=false;
                break;
            case R.id.goods_standard_delete10:
                goodsstandard[9].setVisibility(View.GONE);
                goodsstandard_line[9].setVisibility(View.GONE);
                mStandardList[9]=false;
                break;
            case R.id.goods_add:
                setStandardListShow();
                break;
            case R.id.include_title_back:
                finish();
                break;
        }
        setSellLayoutHide();
    }

    private void setStandardListShow(){
        for(int i=0;i<10;i++){
            if (mStandardList[i] == false){
                goodsstandard[i].setVisibility(View.VISIBLE);
                goodsstandard_line[i].setVisibility(View.VISIBLE);
                mStandardList[i] = true;
                break;
            }
        }
    }

    private void setSellLayoutHide(){
        int nShow = 0;
        for(int i=0;i<10;i++){
            if (mStandardList[i] == true){
                nShow++;
            }
        }
        if (nShow==0){
            selllayout.setVisibility(View.VISIBLE);
            selllayoutline.setVisibility(View.VISIBLE);
            stocklayout.setVisibility(View.VISIBLE);
            stocklayoutline.setVisibility(View.VISIBLE);
        }else{
            selllayout.setVisibility(View.GONE);
            selllayoutline.setVisibility(View.GONE);
            stocklayout.setVisibility(View.GONE);
            stocklayoutline.setVisibility(View.GONE);
        }
        if (nShow == 10){
            goodsadd.setVisibility(View.GONE);
        }
    }

    private void setImageShow(){
        switch (mImgageList.size()){
            case 0:
                imageframe1.setVisibility(View.GONE);
                imageframe2.setVisibility(View.GONE);
                imageframe3.setVisibility(View.GONE);
                imageframe4.setVisibility(View.GONE);
                imageframe5.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.GONE);
                break;
            case 1:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.GONE);
                imageframe3.setVisibility(View.GONE);
                imageframe4.setVisibility(View.GONE);
                imageframe5.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.GONE);
                break;
            case 2:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.GONE);
                imageframe4.setVisibility(View.GONE);
                imageframe5.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.GONE);
                break;
            case 3:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.VISIBLE);
                imageview3.setBackgroundResource(mImgageList.get(2));
                imageframe4.setVisibility(View.GONE);
                imageframe5.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.GONE);
                break;
            case 4:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.VISIBLE);
                imageview3.setBackgroundResource(mImgageList.get(2));
                imageframe4.setVisibility(View.VISIBLE);
                imageview4.setBackgroundResource(mImgageList.get(3));
                imageframe5.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.GONE);
                break;
            case 5:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.VISIBLE);
                imageview3.setBackgroundResource(mImgageList.get(2));
                imageframe4.setVisibility(View.VISIBLE);
                imageview4.setBackgroundResource(mImgageList.get(3));
                imageframe5.setVisibility(View.VISIBLE);
                imageview5.setBackgroundResource(mImgageList.get(4));
                imageaddbtn1.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.VISIBLE);
                imageframe6.setVisibility(View.GONE);
                imageframe7.setVisibility(View.GONE);
                imageframe8.setVisibility(View.GONE);
                imageframe9.setVisibility(View.GONE);
                imageframe10.setVisibility(View.GONE);
                break;
            case 6:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.VISIBLE);
                imageview3.setBackgroundResource(mImgageList.get(2));
                imageframe4.setVisibility(View.VISIBLE);
                imageview4.setBackgroundResource(mImgageList.get(3));
                imageframe5.setVisibility(View.VISIBLE);
                imageview5.setBackgroundResource(mImgageList.get(4));
                imageaddbtn1.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.VISIBLE);
                imageframe6.setVisibility(View.VISIBLE);
                imageview6.setBackgroundResource(mImgageList.get(5));
                imageframe7.setVisibility(View.GONE);
                imageframe8.setVisibility(View.GONE);
                imageframe9.setVisibility(View.GONE);
                imageframe10.setVisibility(View.GONE);
                break;
            case 7:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.VISIBLE);
                imageview3.setBackgroundResource(mImgageList.get(2));
                imageframe4.setVisibility(View.VISIBLE);
                imageview4.setBackgroundResource(mImgageList.get(3));
                imageframe5.setVisibility(View.VISIBLE);
                imageview5.setBackgroundResource(mImgageList.get(4));
                imageaddbtn1.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.VISIBLE);
                imageframe6.setVisibility(View.VISIBLE);
                imageview6.setBackgroundResource(mImgageList.get(5));
                imageframe7.setVisibility(View.VISIBLE);
                imageview7.setBackgroundResource(mImgageList.get(6));
                imageframe8.setVisibility(View.GONE);
                imageframe9.setVisibility(View.GONE);
                imageframe10.setVisibility(View.GONE);
                break;
            case 8:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.VISIBLE);
                imageview3.setBackgroundResource(mImgageList.get(2));
                imageframe4.setVisibility(View.VISIBLE);
                imageview4.setBackgroundResource(mImgageList.get(3));
                imageframe5.setVisibility(View.VISIBLE);
                imageview5.setBackgroundResource(mImgageList.get(4));
                imageaddbtn1.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.VISIBLE);
                imageframe6.setVisibility(View.VISIBLE);
                imageview6.setBackgroundResource(mImgageList.get(5));
                imageframe7.setVisibility(View.VISIBLE);
                imageview7.setBackgroundResource(mImgageList.get(6));
                imageframe8.setVisibility(View.VISIBLE);
                imageview8.setBackgroundResource(mImgageList.get(7));
                imageframe9.setVisibility(View.GONE);
                imageframe10.setVisibility(View.GONE);
                break;
            case 9:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.VISIBLE);
                imageview3.setBackgroundResource(mImgageList.get(2));
                imageframe4.setVisibility(View.VISIBLE);
                imageview4.setBackgroundResource(mImgageList.get(3));
                imageframe5.setVisibility(View.VISIBLE);
                imageview5.setBackgroundResource(mImgageList.get(4));
                imageaddbtn1.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.VISIBLE);
                imageframe6.setVisibility(View.VISIBLE);
                imageview6.setBackgroundResource(mImgageList.get(5));
                imageframe7.setVisibility(View.VISIBLE);
                imageview7.setBackgroundResource(mImgageList.get(6));
                imageframe8.setVisibility(View.VISIBLE);
                imageview8.setBackgroundResource(mImgageList.get(7));
                imageframe9.setVisibility(View.VISIBLE);
                imageview9.setBackgroundResource(mImgageList.get(8));
                imageframe10.setVisibility(View.GONE);
                break;
            case 10:
                imageframe1.setVisibility(View.VISIBLE);
                imageview1.setBackgroundResource(mImgageList.get(0));
                imageframe2.setVisibility(View.VISIBLE);
                imageview2.setBackgroundResource(mImgageList.get(1));
                imageframe3.setVisibility(View.VISIBLE);
                imageview3.setBackgroundResource(mImgageList.get(2));
                imageframe4.setVisibility(View.VISIBLE);
                imageview4.setBackgroundResource(mImgageList.get(3));
                imageframe5.setVisibility(View.VISIBLE);
                imageview5.setBackgroundResource(mImgageList.get(4));
                imageaddbtn1.setVisibility(View.GONE);
                imageLayout2.setVisibility(View.VISIBLE);
                imageframe6.setVisibility(View.VISIBLE);
                imageview6.setBackgroundResource(mImgageList.get(5));
                imageframe7.setVisibility(View.VISIBLE);
                imageview7.setBackgroundResource(mImgageList.get(6));
                imageframe8.setVisibility(View.VISIBLE);
                imageview8.setBackgroundResource(mImgageList.get(7));
                imageframe9.setVisibility(View.VISIBLE);
                imageview9.setBackgroundResource(mImgageList.get(8));
                imageframe10.setVisibility(View.VISIBLE);
                imageview10.setBackgroundResource(mImgageList.get(9));
                imageaddbtn2.setVisibility(View.GONE);
                break;
        }

    }


}
