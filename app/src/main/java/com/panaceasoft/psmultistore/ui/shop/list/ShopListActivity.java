package com.panaceasoft.psmultistore.ui.shop.list;

import android.os.Bundle;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityShopListBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;
import com.panaceasoft.psmultistore.utils.Constants;

import androidx.databinding.DataBindingUtil;

public class ShopListActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityShopListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_list);

        initUI(binding);

    }

    private void initUI(ActivityShopListBinding binding) {

        String title = getIntent().getStringExtra(Constants.SHOP_TITLE);

        if (title != null) {
            initToolbar(binding.toolbar, title);
        } else {
            initToolbar(binding.toolbar, getResources().getString(R.string.shop__list));
        }

        // setup Fragment
        setupFragment(new ShopListFragment());

    }
}
