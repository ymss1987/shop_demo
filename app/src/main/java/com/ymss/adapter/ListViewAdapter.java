package com.ymss.adapter;

/**
 * Created by ymss on 2016/10/28.
 */
import java.util.List;
import com.ymss.tinyshop.SelectShopActivity.ItemEntity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymss.tinyshop.R;

public class ListViewAdapter extends BaseAdapter {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private Context mContext;
    private List<ItemEntity> mData;
    private LayoutInflater mLayoutInflater;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ListViewAdapter(Context pContext, List<ItemEntity> pData) {
        mContext = pContext;
        mData = pData;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 常见的优化ViewHolder
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.shoplist_items_menu, null);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.TitleText);
            viewHolder.titleIcon = (ImageView) convertView.findViewById(R.id.TitleImage);
            viewHolder.content = (TextView) convertView.findViewById(R.id.ItemText);
            viewHolder.contentIcon = (ImageView) convertView.findViewById(R.id.ItemImage);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.ItemDescribe);

            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.titleLayout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 获取数据
        ItemEntity itemEntity = (ItemEntity) getItem(position);
        viewHolder.content.setText(itemEntity.getContent());
        viewHolder.contentIcon.setImageResource(itemEntity.getmItemImage());
        if (itemEntity.getDescribe() == null){
            viewHolder.describe.setVisibility(View.GONE);
        }else {
            viewHolder.describe.setText(itemEntity.getDescribe());
            viewHolder.describe.setVisibility(View.VISIBLE);
        }

        if ( needTitle(position) ) {
            // 显示标题并设置内容
            viewHolder.title.setText(itemEntity.getTitle());
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.titleIcon.setImageResource(itemEntity.getTitleImage());
            viewHolder.titleIcon.setVisibility(View.VISIBLE);
            //viewHolder.linearLayout.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            viewHolder.title.setVisibility(View.GONE);
            viewHolder.titleIcon.setVisibility(View.GONE);
           // viewHolder.linearLayout.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        if (null != mData) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != mData && position < getCount()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * 判断是否需要显示标题
     *
     * @param position
     * @return
     */
    private boolean needTitle(int position) {
        // 第一个肯定是分类
        if (position == 0) {
            return true;
        }

        // 边界处理
        if (position < 0) {
            return false;
        }

        // 当前  // 上一个
        ItemEntity currentEntity = (ItemEntity) getItem(position);
        ItemEntity previousEntity = (ItemEntity) getItem(position - 1);
        if (null == currentEntity || null == previousEntity) {
            return false;
        }

        String currentTitle = currentEntity.getTitle();
        String previousTitle = previousEntity.getTitle();
        if (null == previousTitle || null == currentTitle) {
            return false;
        }

        // 当前item分类名和上一个item分类名不同，则表示两item属于不同分类
        if (currentTitle.equals(previousTitle)) {
            return false;
        }

        return true;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    private class ViewHolder {
        TextView title;
        LinearLayout linearLayout;
        ImageView titleIcon;
        TextView content;
        ImageView contentIcon;
        TextView describe;
    }

}

