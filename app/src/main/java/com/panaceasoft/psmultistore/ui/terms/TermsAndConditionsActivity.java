package com.panaceasoft.psmultistore.ui.terms;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityTermsAndConditionBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.MyContextWrapper;

public class TermsAndConditionsActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTermsAndConditionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_condition);

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

    private void initUI(ActivityTermsAndConditionBinding binding) {

        // Toolbar
        if(getIntent().getStringExtra(Constants.SHOP_TERMS_FLAG).equals(Constants.SHOP_REFUND)){
            initToolbar(binding.toolbar, getResources().getString(R.string.product_detail__refund_policy));
        }else {
            initToolbar(binding.toolbar, getResources().getString(R.string.terms_and_conditions__title));
        }

        // setup Fragment
        setupFragment(new TermsAndConditionsFragment());

    }

    //endregion


}