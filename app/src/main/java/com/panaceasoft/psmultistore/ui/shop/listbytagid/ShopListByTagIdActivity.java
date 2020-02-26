package com.panaceasoft.psmultistore.ui.shop.listbytagid;

import android.os.Bundle;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityShopListByTagIdBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;
import com.panaceasoft.psmultistore.utils.Constants;

import androidx.databinding.DataBindingUtil;

public class ShopListByTagIdActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityShopListByTagIdBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shop_list_by_tag_id);

        initUI(binding);

    }

    private void initUI(ActivityShopListByTagIdBinding binding) {

        // Toolbar

        String title = getIntent().getStringExtra(Constants.SHOP_TITLE);

        if(title != null)
        {
            initToolbar(binding.toolbar, title);
        }else {
            initToolbar(binding.toolbar, getResources().getString(R.string.shop__list));
        }

        // setup Fragment
        setupFragment(new ShopListByTagIdFragment());

    }
}
