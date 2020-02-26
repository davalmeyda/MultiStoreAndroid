package com.panaceasoft.psmultistore.ui.shop.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentShopMenuBinding;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.user.UserViewModel;
import com.panaceasoft.psmultistore.viewobject.holder.ProductParameterHolder;

public class ShopMenuFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private UserViewModel userViewModel;
    private MenuItem basketMenuItem;
    private BasketViewModel basketViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentShopMenuBinding> binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentShopMenuBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_menu, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    private void setBasketMenuItemVisible(Boolean isVisible) {
        if (basketMenuItem != null) {
            basketMenuItem.setVisible(isVisible);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.basket_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        basketMenuItem = menu.findItem(R.id.action_basket);

        if (basketViewModel != null) {
            if (basketViewModel.basketCount > 0) {
                basketMenuItem.setVisible(true);
            } else {
                basketMenuItem.setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_basket) {
            navigationController.navigateToBasketList(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        checkUserLogin();

        binding.get().categoryCardView.setOnClickListener(v -> navigationController.navigateToCategoryActivity(getActivity(), Constants.CATEGORY));

        binding.get().latestProductCardView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), new ProductParameterHolder().getLatestParameterHolder(), getString(R.string.menu__latest_product)));

        binding.get().discountProductCardView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), new ProductParameterHolder().getDiscountParameterHolder(), getString(R.string.menu__discount)));

        binding.get().featuredProductCardView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), new ProductParameterHolder().getFeaturedParameterHolder(), getString(R.string.menu__featured_product)));

        binding.get().trendingProductCardView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), new ProductParameterHolder().getTrendingParameterHolder(), getString(R.string.menu__trending_products)));

        binding.get().collectionCardView.setOnClickListener(v -> navigationController.navigateToProductCollectionHeaderListActivity(getActivity()));

        binding.get().signInButton.setOnClickListener(v -> Utils.navigateToLogin(getActivity(), navigationController));
    }

    @Override
    protected void initViewModels() {

        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {
        basketData();
    }

    private void basketData() {
        //set and get basket list
        basketViewModel.setBasketListObj(selectedShopId);
        basketViewModel.getAllBasketList().observe(this, resourse -> {
            if (resourse != null) {
                basketViewModel.basketCount = resourse.size();
                if (resourse.size() > 0) {
                    setBasketMenuItemVisible(true);
                } else {
                    setBasketMenuItemVisible(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();

        checkUserLogin();
    }

    private void checkUserLogin() {
        if (loginUserId.isEmpty()) {
            binding.get().signInButton.setVisibility(View.VISIBLE);
            binding.get().phoneTextView.setVisibility(View.GONE);
            binding.get().statusTextView.setVisibility(View.GONE);

        } else {
            binding.get().signInButton.setVisibility(View.GONE);
            binding.get().phoneTextView.setVisibility(View.VISIBLE);
            binding.get().statusTextView.setVisibility(View.VISIBLE);

            userViewModel.getLoginUser().observe(this, listResource -> {
                // we don't need any null checks here for the adapter since LiveData guarantees that
                // it won't call us if fragment is stopped or not started.
                if (listResource != null && listResource.size() > 0) {
                    Utils.psLog("Got Data");

                    userViewModel.user = listResource.get(0).user;
                    Utils.psLog("Photo : " + listResource.get(0).user.userProfilePhoto);
                    binding.get().userNameTextView.setText(userViewModel.user.userName);

                    binding.get().phoneTextView.setText(userViewModel.user.userPhone);

                    binding.get().statusTextView.setText(userViewModel.user.userEmail);

                    if (!userViewModel.user.userProfilePhoto.isEmpty()) {
                        binding.get().setUser(userViewModel.user);
                    }

                } else {
                    //noinspection Constant Conditions
                    Utils.psLog("Empty Data");

                }
            });

        }
    }

}
