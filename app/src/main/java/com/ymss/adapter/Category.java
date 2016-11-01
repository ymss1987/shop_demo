package com.ymss.adapter;

import com.ymss.tinyshop.R;
import com.ymss.tinyshop.SelectShopActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymss on 2016/10/30.
 */

public class Category {

    private String mCategoryName;
    private int mCategoryIcon;
    private List<ItemEntity1> mCategoryItem = new ArrayList<ItemEntity1>();

    public Category(String mCategroyName, int mCategoryIcon) {
        this.mCategoryName = mCategroyName;
        this.mCategoryIcon = mCategoryIcon;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public int getmCategoryIcon() {
        return mCategoryIcon;
    }

    public void addItem(String pContent, int itemImage, String pDescribe) {
        ItemEntity1 itemEntity1 = new ItemEntity1(pContent,itemImage, pDescribe);
        mCategoryItem.add(itemEntity1);
    }


    /**
     *  获取Item内容
     *
     * @param pPosition
     * @return
     */
    public ItemEntity1 getItem(int pPosition) {
        if (pPosition >0) {
            return mCategoryItem.get(pPosition - 1);
        }
        return null;
    }

    /**
     * 当前类别Item总数。Category也需要占用一个Item
     * @return
     */
    public int getItemCount() {
        return mCategoryItem.size() + 1;
    }

    public class ItemEntity1 {
        private String mContent;
        private String mDescribe;
        private int mItemImage;

        public ItemEntity1(String pContent, int itemImage, String pDescribe) {
            mContent = pContent;
            mItemImage = itemImage;
            mDescribe =pDescribe;
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

}
