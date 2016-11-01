package com.ymss.tinyshop.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ymss.tinyshop.R;

/**
 * Created by adminstrator on 2016/10/31.
 */

public class GoodsManagerTab2Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goods_manager_fragment, null);
        return view;
    }
}
