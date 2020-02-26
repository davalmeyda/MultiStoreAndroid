package com.panaceasoft.psmultistore.ui.collection.productCollectionHeader;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentProductCollectionHeaderListBinding;
import com.panaceasoft.psmultistore.ui.collection.adapter.ProductCollectionHeaderListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.collection.ProductCollectionViewModel;
import com.panaceasoft.psmultistore.viewobject.ProductCollectionHeader;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCollectionHeaderListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ProductCollectionViewModel productCollectionViewModel;

    @VisibleForTesting
    private
    AutoClearedValue<FragmentProductCollectionHeaderListBinding> binding;
    private AutoClearedValue<ProductCollectionHeaderListAdapter> adapter;

    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentProductCollectionHeaderListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_collection_header_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
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

        binding.get().productCollectionHeaderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {


                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !productCollectionViewModel.forceEndLoading) {

                            if (connectivity.isConnected()) {

                                productCollectionViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.PRODUCT_COUNT;
                                productCollectionViewModel.offset = productCollectionViewModel.offset + limit;

                                productCollectionViewModel.setNextPageLoadingStateObj( String.valueOf(Config.PRODUCT_COUNT), String.valueOf(productCollectionViewModel.offset), selectedShopId);
                            }
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            productCollectionViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            productCollectionViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            productCollectionViewModel.forceEndLoading = false;

            // update live data
            productCollectionViewModel.setProductCollectionHeaderListObj( String.valueOf(Config.PRODUCT_COUNT), String.valueOf(productCollectionViewModel.offset), selectedShopId);

        });
    }

    @Override
    protected void initViewModels() {
        productCollectionViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductCollectionViewModel.class);
    }

    @Override
    protected void initAdapters() {
        ProductCollectionHeaderListAdapter nvAdapter = new ProductCollectionHeaderListAdapter(dataBindingComponent,
                productCollectionHeader -> navigationController.navigateToCollectionProductList(ProductCollectionHeaderListFragment.this.getActivity(), productCollectionHeader.id, productCollectionHeader.name, productCollectionHeader.defaultPhoto.imgPath),
                this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().productCollectionHeaderList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        loadDiscount();
    }


    //region Private Methods

    private void loadDiscount() {

        // Load Latest ProductCollectionHeader
        productCollectionViewModel.setProductCollectionHeaderListObj(String.valueOf(Config.PRODUCT_COUNT), String.valueOf(productCollectionViewModel.offset), selectedShopId);

        LiveData<Resource<List<ProductCollectionHeader>>> news = productCollectionViewModel.getProductCollectionHeaderListData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {

                                if(!connectivity.isConnected()) {
                                    //fadeIn Animation
                                    fadeIn(binding.get().getRoot());

                                    // Update the data
                                    replaceData(listResource.data);
                                }

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            productCollectionViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            productCollectionViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (productCollectionViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        productCollectionViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        productCollectionViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    productCollectionViewModel.setLoadingState(false);
                    productCollectionViewModel.forceEndLoading = true;
                }
            }
        });

        productCollectionViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(productCollectionViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    private void replaceData(List<ProductCollectionHeader> productCollectionHeaderList) {

        adapter.get().replace(productCollectionHeaderList);
        binding.get().executePendingBindings();

    }


    @Override
    public void onDispatched() {
        if (productCollectionViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().productCollectionHeaderList != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().productCollectionHeaderList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }
}
