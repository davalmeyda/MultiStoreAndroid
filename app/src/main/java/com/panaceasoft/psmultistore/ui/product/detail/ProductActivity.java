package com.panaceasoft.psmultistore.ui.product.detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityProductBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.MyContextWrapper;
import com.panaceasoft.psmultistore.utils.Utils;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class ProductActivity extends PSAppCompactActivity {

    public final String KEY = Constants.PRODUCT_NAME;
    public String productName;
    private ProductDetailFragment productDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProductBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product);

        if (this.getIntent() != null) {
            productName = getIntent().getStringExtra(KEY);
        }

        initUI(binding);
    }
    //region Private Methods

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));
    }

    boolean isShow;
    int scrollRange = -1;

    //endregion
    private void initUI(ActivityProductBinding binding) {

        initToolbar(binding.toolbar, productName);

        binding.toolbar.setNavigationIcon(R.drawable.baseline_back_white_with_grey_bg_24);

        binding.collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        // setup Fragment
        productDetailFragment = new ProductDetailFragment();
        setupFragment(productDetailFragment);

        productDetailFragment.imageViewPager = new AutoClearedValue<>(productDetailFragment, binding.imageViewPager);
        productDetailFragment.pageIndicatorLayout = new AutoClearedValue<>(productDetailFragment, binding.pagerIndicator);

        productDetailFragment.addToCartButton = new AutoClearedValue<>(productDetailFragment, binding.addToBasketButton);
        productDetailFragment.buyNowButton = new AutoClearedValue<>(productDetailFragment, binding.buyNowButton);

        binding.mainAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if (scrollRange + verticalOffset == 0) {
                //collapse map
                isShow = true;
                binding.toolbar.setNavigationIcon(R.drawable.baseline_back_white_24);
                Utils.psLog("ABC : True");
            } else if (isShow) {
                //expanded map
                isShow = false;
                binding.toolbar.setNavigationIcon(R.drawable.baseline_back_white_with_grey_bg_24);
                Utils.psLog("ABC : False");
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            productDetailFragment.refreshBasketData();
            finish();
        }
        if (item.getItemId() == R.id.action_basket) {
            try {
                productDetailFragment.callBasket();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


}
