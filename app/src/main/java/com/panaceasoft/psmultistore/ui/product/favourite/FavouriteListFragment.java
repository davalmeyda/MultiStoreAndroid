package com.panaceasoft.psmultistore.ui.product.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentFavouriteListBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.adapter.ProductVerticalListAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.product.FavouriteViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductFavouriteViewModel;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.List;


public class FavouriteListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ProductFavouriteViewModel productFavouriteViewModel;
    private FavouriteViewModel favouriteViewModel;

    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentFavouriteListBinding> binding;
    private AutoClearedValue<ProductVerticalListAdapter> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFavouriteListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_list, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();

    }

    @Override
    public void onDispatched() {
        if (productFavouriteViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().favouriteList != null) {

                GridLayoutManager layoutManager = (GridLayoutManager)
                        binding.get().favouriteList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

        binding.get().favouriteList.setNestedScrollingEnabled(false);
        binding.get().favouriteList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !productFavouriteViewModel.forceEndLoading) {

                            if (connectivity.isConnected()) {

                                productFavouriteViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.PRODUCT_COUNT;
                                productFavouriteViewModel.offset = productFavouriteViewModel.offset + limit;

                                productFavouriteViewModel.setNextPageLoadingFavouriteObj(String.valueOf(productFavouriteViewModel.offset), loginUserId);
                            }
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            productFavouriteViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            productFavouriteViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            productFavouriteViewModel.forceEndLoading = false;

            // update live data
            productFavouriteViewModel.setProductFavouriteListObj(loginUserId, String.valueOf(productFavouriteViewModel.offset));

        });
    }

    @Override
    protected void initViewModels() {
        productFavouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductFavouriteViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ProductVerticalListAdapter nvAdapter = new ProductVerticalListAdapter(dataBindingComponent, new ProductVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(getActivity(), product);
            }

            @Override
            public void onFavLikeClick(Product product, LikeButton likeButton) {
                favFunction(product, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                unFavFunction(product, likeButton);
            }
        }, this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().favouriteList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {

        productFavouriteViewModel.getNextPageFavouriteLoadingData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {

                    productFavouriteViewModel.setLoadingState(false);//hide
                    productFavouriteViewModel.forceEndLoading = true;//stop

                }
            }
        });

        productFavouriteViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(productFavouriteViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

        productFavouriteViewModel.setProductFavouriteListObj(loginUserId, String.valueOf(productFavouriteViewModel.offset));

        LiveData<Resource<List<Product>>> news = productFavouriteViewModel.getProductFavouriteData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            productFavouriteViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            productFavouriteViewModel.setLoadingState(false);
                            productFavouriteViewModel.forceEndLoading = true;

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data

                    if (productFavouriteViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        productFavouriteViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (FavouriteListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }

                } else if (result.status == Status.ERROR) {
                    if (FavouriteListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }
                }
            }
        });
    }

    private void replaceData(List<Product> favouriteList) {

        adapter.get().replace(favouriteList);
        binding.get().executePendingBindings();

    }

    private void unFavFunction(Product product, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(product.id, loginUserId, selectedShopId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }

        });

    }

    private void favFunction(Product product, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(product.id, loginUserId, selectedShopId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null));
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();
    }
}
