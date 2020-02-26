package com.panaceasoft.psmultistore.ui.shop.tag;

import android.os.Bundle;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityShopTagListBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;

import androidx.databinding.DataBindingUtil;

public class ShopTagListActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityShopTagListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_tag_list);

        initUI(binding);

    }

    private void initUI(ActivityShopTagListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.shop_category__title));

        // setup Fragment
        setupFragment(new ShopTagListFragment());

    }
}
