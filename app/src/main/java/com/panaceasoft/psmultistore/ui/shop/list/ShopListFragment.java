package com.panaceasoft.psmultistore.ui.shop.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentShopListBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.shop.adapter.ShopFeaturedListAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.shop.ShopViewModel;
import com.panaceasoft.psmultistore.viewobject.Shop;
import com.panaceasoft.psmultistore.viewobject.common.Status;
import com.panaceasoft.psmultistore.viewobject.holder.ShopParameterHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ShopViewModel shopViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentShopListBinding> binding;
    private AutoClearedValue<ShopFeaturedListAdapter> shopFeaturedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentShopListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_list, container, false, dataBindingComponent);

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

                    if (lastPosition == shopFeaturedAdapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !shopViewModel.forceEndLoading) {

                            shopViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_SHOP_COUNT;

                            shopViewModel.offset = shopViewModel.offset + limit;

                            shopViewModel.setLoadingState(true);

                            checkConnectionAndLoad();
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

            shopViewModel.limit = 0;

            // reset productViewModel.forceEndLoading
            shopViewModel.forceEndLoading = false;

            checkConnectionAndLoad();

            // update live data

        });

    }

    @Override
    protected void initViewModels() {

        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel.class);

    }

    @Override
    protected void initAdapters() {

        ShopFeaturedListAdapter shopFeaturedAdapter = new ShopFeaturedListAdapter(dataBindingComponent, shop -> {

            pref.edit().putString(Constants.SHOP_ID, shop.id).apply();
            pref.edit().putString(Constants.SHIPPING_ID, shop.shippingId).apply();

            navigationController.navigateToSelectedShopDetail(getActivity(), shop.id, shop.name);

        }, this);

        this.shopFeaturedAdapter = new AutoClearedValue<>(this, shopFeaturedAdapter);
        binding.get().shopListRecyclerView.setAdapter(shopFeaturedAdapter);

    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            shopViewModel.shopParameterHolder = (ShopParameterHolder) getActivity().getIntent().getSerializableExtra(Constants.SHOP_HOLDER);
        }

        shopViewModel.setShopListObj(Config.API_KEY, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(shopViewModel.offset), shopViewModel.shopParameterHolder);
        shopViewModel.getShopListData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceFeaturedShopList(result.data);
                        shopViewModel.setLoadingState(false);
                        break;

                    case LOADING:
                        replaceFeaturedShopList(result.data);

                        break;

                    case ERROR:
                        shopViewModel.setLoadingState(false);
                        break;
                }
            }
        });

        shopViewModel.getNextPageShopListData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.status);

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
        this.shopFeaturedAdapter.get().replace(shops);
        binding.get().executePendingBindings();
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

    private void checkConnectionAndLoad() {
        if (connectivity.isConnected()) {
            shopViewModel.setNextPageShopListObj(Config.API_KEY, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(shopViewModel.offset), shopViewModel.shopParameterHolder);
        } else {
            shopViewModel.setShopListObj(Config.API_KEY, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(shopViewModel.offset), shopViewModel.shopParameterHolder);
        }
    }
}
