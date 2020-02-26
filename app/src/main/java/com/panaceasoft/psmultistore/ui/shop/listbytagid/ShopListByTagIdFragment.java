package com.panaceasoft.psmultistore.ui.shop.listbytagid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentShopListByTagIdBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.shop.listbytagid.adapter.ShopListByTagIdAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.shop.ShopViewModel;
import com.panaceasoft.psmultistore.viewobject.Shop;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopListByTagIdFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ShopViewModel shopViewModel;
    private String shopCategoryId;

    @VisibleForTesting
    private AutoClearedValue<FragmentShopListByTagIdBinding> binding;
    private AutoClearedValue<ShopListByTagIdAdapter> shopCategoryDetailListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentShopListByTagIdBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_list_by_tag_id, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();

    }

    @Override
    protected void initUIAndActions() {

        binding.get().shopListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == shopCategoryDetailListAdapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !shopViewModel.forceEndLoading) {

                            shopViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_SHOP_COUNT;

                            shopViewModel.offset = shopViewModel.offset + limit;

                            shopViewModel.setLoadingState(true);

                            shopViewModel.setNextPageShopListByTagIdObj(shopCategoryId, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(shopViewModel.offset));

                        }
                    }
                }
            }
        });


        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            shopViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset

            shopViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            shopViewModel.forceEndLoading = false;

            shopViewModel.setShopListByTagIdObj(shopCategoryId, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(shopViewModel.offset));

            // update live data

        });

    }

    @Override
    protected void initViewModels() {

        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel.class);

    }

    @Override
    protected void initAdapters() {

        ShopListByTagIdAdapter nvAdapter2 = new ShopListByTagIdAdapter(dataBindingComponent, shop -> {

            pref.edit().putString(Constants.SHOP_ID, shop.id).apply();
            pref.edit().putString(Constants.SHIPPING_ID, shop.shippingId).apply();

            navigationController.navigateToSelectedShopDetail(this.getActivity(), shop.id, shop.name);

        }, this);

        this.shopCategoryDetailListAdapter = new AutoClearedValue<>(this, nvAdapter2);
        binding.get().shopListRecyclerView.setAdapter(nvAdapter2);
    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            shopCategoryId = getActivity().getIntent().getStringExtra(Constants.SHOP_CATEGORY_ID);
        }

        shopViewModel.setShopListByTagIdObj(shopCategoryId, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(shopViewModel.offset));

        shopViewModel.getShopListByTagIdData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceFeaturedShopList(result.data);
                        shopViewModel.setLoadingState(false);
                        break;

//                    case LOADING:
//                        replaceFeaturedShopList(result.data);
//
//                        break;

                    case ERROR:
                        shopViewModel.setLoadingState(false);
                        break;
                }
            }
        });

        shopViewModel.getNextPageShopByTagIdData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    shopViewModel.setLoadingState(false);
                    shopViewModel.forceEndLoading = true;
                }
            }
        });


        shopViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(shopViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    private void replaceFeaturedShopList(List<Shop> shops) {
        if (shopCategoryDetailListAdapter != null) {
            if (shopCategoryDetailListAdapter.get() != null) {
                this.shopCategoryDetailListAdapter.get().replace(shops);
                binding.get().executePendingBindings();
            }
        }
    }

    @Override
    public void onDispatched() {

        if (shopViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().shopListRecyclerView != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().shopListRecyclerView.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        List<Shop> clearList = new ArrayList<>();

        replaceFeaturedShopList(clearList);
    }
}
