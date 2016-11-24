package com.ymss.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymss.tinyshop.R;

import java.util.List;
import java.util.Map;

/**
 * Created by adminstrator on 2016/11/24.
 */

public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Map<String, Object>> mData;

    public NormalRecyclerViewAdapter(Context context, List<Map<String, Object>> data) {
        mData = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new NormalTitleViewHolder(mLayoutInflater.inflate(R.layout.order_item_title, parent, false));
        }else {
            return new NormalContentViewHolder(mLayoutInflater.inflate(R.layout.order_item_content, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType()==1){
            ((NormalTitleViewHolder)holder).mTextView.setText(mData.get(position).get("title").toString());
        }else {
            NormalContentViewHolder contentHolder = (NormalContentViewHolder)holder;
            contentHolder.mOrderNo.setText(mData.get(position).get("orderNo").toString());
            contentHolder.mCustomerName.setText(mData.get(position).get("customerName").toString());
            contentHolder.mCustomerAmount.setText(mData.get(position).get("customerAmount").toString());
            contentHolder.mCustomerTime.setText(mData.get(position).get("customerTime").toString());
            String orderType = mData.get(position).get("orderType").toString();
            if (orderType.equals("当面付")){
                contentHolder.morderTypeIcon.setVisibility(View.VISIBLE);
            }else{
                contentHolder.morderTypeIcon.setVisibility(View.GONE);
            }
            contentHolder.mOrderType.setText(orderType);
            contentHolder.mCustomerPayType.setText(mData.get(position).get("payType").toString());
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType ( int position ) {
        int viewType;

        if (mData.get(position).containsKey("title")) {
            viewType = 1;    //标题
        } else {
            viewType = 0;
        }
        return viewType;
    }

    public class NormalTitleViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        NormalTitleViewHolder(View view) {
            super(view);
            mTextView= (TextView) view.findViewById(R.id.title);
        }
    }
    public static class NormalContentViewHolder extends RecyclerView.ViewHolder {
        TextView mOrderNo;
        TextView mCustomerName;
        TextView mCustomerAmount;
        TextView mCustomerTime;
        TextView mOrderType;
        TextView mCustomerPayType;
        ImageView morderTypeIcon;

        NormalContentViewHolder(View view) {
            super(view);
            mOrderNo = (TextView) view.findViewById(R.id.order_no);
            mCustomerName = (TextView) view.findViewById(R.id.customer_name);
            mCustomerAmount = (TextView) view.findViewById(R.id.customer_amount);
            mCustomerTime = (TextView) view.findViewById(R.id.customer_time);
            mOrderType = (TextView) view.findViewById(R.id.order_type);
            morderTypeIcon = (ImageView) view.findViewById(R.id.order_type_icon);
            mCustomerPayType = (TextView) view.findViewById(R.id.customer_paytype);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }



}
