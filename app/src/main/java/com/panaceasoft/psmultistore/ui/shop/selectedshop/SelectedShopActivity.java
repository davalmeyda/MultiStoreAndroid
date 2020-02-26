package com.panaceasoft.psmultistore.ui.shop.selectedshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivitySelectedShopBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.MyContextWrapper;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.shoptag.ShopTagViewModel;
import com.panaceasoft.psmultistore.viewobject.Basket;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class SelectedShopActivity extends PSAppCompactActivity {

    SharedPreferences preferences;
    ActivitySelectedShopBinding binding;
    PSDialogMsg psDialogMsg;
    ShopTagViewModel shopTagViewModel;
    BasketViewModel basketViewModel;
    private TextView basketNotificationTextView;

    @Inject
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_selected_shop);

        setupFragment(new SelectedShopFragment());

        initUI(binding);
        initData();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);



        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) binding.bottomNavigationView.getChildAt(0);
        View bTMView = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) bTMView;

        View badgeView = LayoutInflater.from(this).inflate(R.layout.notification_badge,itemView,true);
        basketNotificationTextView = badgeView.findViewById(R.id.notifications_badge);

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home_menu:

                    navigationController.navigateToHome(this);
                    setToolbarText(binding.toolbar,pref.getString(Constants.SHOP_NAME, ""));

                    break;
                case R.id.shop_profile_menu:
                    navigationController.navigateToShopProfile(this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__shop_profile));
                    break;

                case R.id.basket_menu:

                    navigationController.navigateToBasket(this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__basket));

                    break;

                case R.id.search_menu:

                    navigationController.navigateToSearch(this);
                    setToolbarText(binding.toolbar, getString(R.string.menu__search));

                    break;

                case R.id.side_bar_menu:

                    navigationController.navigateToShopMenu(this);
                    setToolbarText(binding.toolbar, getString(R.string.shop_menu__title));
                    ButtonSheetClick();
                    break;

                default:

                    navigationController.navigateToShopProfile(this);
                    setToolbarText(binding.toolbar, getString(R.string.app__app_name));

                    break;
            }

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBasketCount();

    }

    public void refreshBasketCount(){

        basketViewModel.setBasketListObj( pref.getString(Constants.SHOP_ID, "" ));

    }
    private void initData(){
        basketViewModel = ViewModelProviders.of(this,viewModelFactory).get(BasketViewModel.class);

        basketViewModel.getAllBasketList().observe(this, resourse -> {
            if (resourse != null) {
                int total = 0;
                for(int i = 0; i < resourse.size(); i++){
                    total +=  resourse.get(i).count;
                    }

                if (total == 0) {
                    basketNotificationTextView.setVisibility(View.GONE);
                } else {
                    basketNotificationTextView.setVisibility(View.VISIBLE);
                    basketNotificationTextView.setText(String.valueOf(total));

                }
            }
        });

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));
    }

    private void initModels() {

        shopTagViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopTagViewModel.class);
    }

    private void ButtonSheetClick() {

    }

    private void initUI(ActivitySelectedShopBinding binding) {

        initModels();
        psDialogMsg = new PSDialogMsg(this, false);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                shopTagViewModel.toolbarName = getIntent().getExtras().getString(Constants.SHOP_NAME);
                initToolbar(binding.toolbar, shopTagViewModel.toolbarName);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            psDialogMsg.showConfirmDialog(getString(R.string.shop__sure_to_exit), getString(R.string.app__ok), getString(R.string.app__cancel));

            psDialogMsg.show();

            psDialogMsg.okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectedShopActivity.this.finish();
                }
            });
            psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());

            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        psDialogMsg.showConfirmDialog(getString(R.string.shop__sure_to_exit), getString(R.string.app__ok), getString(R.string.app__cancel));

        psDialogMsg.show();

        psDialogMsg.okButton.setOnClickListener(view -> finish());
        psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.psLog("Inside Result MainActivity");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
