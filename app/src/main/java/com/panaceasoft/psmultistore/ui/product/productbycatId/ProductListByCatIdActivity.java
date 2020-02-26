package com.panaceasoft.psmultistore.ui.product.productbycatId;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityProductListByCatidBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.MyContextWrapper;

import androidx.databinding.DataBindingUtil;

public class ProductListByCatIdActivity extends PSAppCompactActivity {

    public final String KEY = Constants.CATEGORY_NAME;
    public String catName;

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProductListByCatidBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product_list_by_catid);
        catName = getIntent().getStringExtra(KEY);
        // Init all UI
        initUI(binding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));
    }

    //endregion


    //region Private Methods

    private void initUI(ActivityProductListByCatidBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, catName);

        // setup Fragment
        ProductListByCatIdFragment productListByCatIdFragment = new ProductListByCatIdFragment();
        setupFragment(productListByCatIdFragment);
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }
    //endregion


}
