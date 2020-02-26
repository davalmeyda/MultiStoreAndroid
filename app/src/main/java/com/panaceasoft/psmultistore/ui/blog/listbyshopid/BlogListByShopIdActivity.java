package com.panaceasoft.psmultistore.ui.blog.listbyshopid;


import android.os.Bundle;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityBlogListByShopIdBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;

import androidx.databinding.DataBindingUtil;

public class BlogListByShopIdActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBlogListByShopIdBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_blog_list_by_shop_id);

        initUI(binding);

    }

    private void initUI(ActivityBlogListByShopIdBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.blog_list__title));

        // setup Fragment

        setupFragment(new BlogListByShopIdFragment());

    }
}
