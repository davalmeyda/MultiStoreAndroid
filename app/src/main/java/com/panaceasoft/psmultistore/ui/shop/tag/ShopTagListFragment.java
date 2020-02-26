package com.panaceasoft.psmultistore.ui.shop.tag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentShopTagListBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.shop.tag.adapter.ShopTagListAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.shoptag.ShopTagViewModel;
import com.panaceasoft.psmultistore.viewobject.ShopTag;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopTagListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ShopTagViewModel shopTagViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentShopTagListBinding> binding;
    private AutoClearedValue<ShopTagListAdapter> shopCategoryViewAllAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentShopTagListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_tag_list, container, false, dataBindingComponent);

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

                    if (lastPosition == shopCategoryViewAllAdapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !shopTagViewModel.forceEndLoading) {

                            shopTagViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_CATEGORY_COUNT;

                            shopTagViewModel.offset = shopTagViewModel.offset + limit;

                            shopTagViewModel.setLoadingState(true);

                            shopTagViewModel.setNextPageShopTagObj(String.valueOf(Config.LIST_CATEGORY_COUNT), String.valueOf(shopTagViewModel.offset));

                        }
                    }
                }
            }
        });


        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            shopTagViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset

            shopTagViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            shopTagViewModel.forceEndLoading = false;

            shopTagViewModel.setShopTagObj(String.valueOf(shopTagViewModel.limit), String.valueOf(shopTagViewModel.offset));

        });

    }

    @Override
    protected void initViewModels() {

        shopTagViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopTagViewModel.class);

    }

    @Override
    protected void initAdapters() {

        ShopTagListAdapter nvAdapter = new ShopTagListAdapter(dataBindingComponent, category ->
                navigationController.navigateToShopCategoryDetailActivity(getActivity(), category.id, category.name), this);

        this.shopCategoryViewAllAdapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().shopListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.get().shopListRecyclerView.setAdapter(nvAdapter);

    }

    @Override
    protected void initData() {

        shopTagViewModel.setShopTagObj(String.valueOf(Config.LIST_CATEGORY_COUNT), String.valueOf(shopTagViewModel.offset));

        shopTagViewModel.getShopTagData().observe(this, (Resource<List<ShopTag>> result) -> {
            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceShopCategories(result.data);
                        shopTagViewModel.setLoadingState(false);
                        break;

                    case LOADING:
                        replaceShopCategories(result.data);
                        break;

                    case ERROR:

                        shopTagViewModel.setLoadingState(false);
                        break;
                }
            }
        });

        shopTagViewModel.getNextPageShopTagData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    shopTagViewModel.setLoadingState(false);
                    shopTagViewModel.forceEndLoading = true;
                }
            }
        });


        shopTagViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(shopTagViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    @Override
    public void onDispatched() {

    }

    private void replaceShopCategories(List<ShopTag> shopCategories) {
        this.shopCategoryViewAllAdapter.get().replace(shopCategories);
        binding.get().executePendingBindings();
    }
}
