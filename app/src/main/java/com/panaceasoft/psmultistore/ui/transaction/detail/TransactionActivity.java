package com.panaceasoft.psmultistore.ui.transaction.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityTransactionBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.MyContextWrapper;

import androidx.databinding.DataBindingUtil;

public class TransactionActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTransactionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction);

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


    private void initUI(ActivityTransactionBinding binding) {
        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.transaction_detail__title));

        // setup Fragment
        setupFragment(new TransactionFragment());

    }
    //endregion
}