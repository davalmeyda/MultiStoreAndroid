package com.panaceasoft.psmultistore.ui.product.productbycatId;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentProdcutListByCatidBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.adapter.ProductVerticalListAdapter;
import com.panaceasoft.psmultistore.ui.shop.selectedshop.SelectedShopActivity;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.product.FavouriteViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductListByCatIdViewModel;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.List;

public class ProductListByCatIdFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private String catId;
    private FavouriteViewModel favouriteViewModel;
    private PSDialogMsg psDialogMsg;
    private ProductListByCatIdViewModel productListByCatIdViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentProdcutListByCatidBinding> binding;
    private AutoClearedValue<ProductVerticalListAdapter> adapter;

    //endregion


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentProdcutListByCatidBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_prodcut_list_by_catid, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));


        binding.get().productList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {
                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !productListByCatIdViewModel.forceEndLoading) {

                            if (connectivity.isConnected()) {

                                productListByCatIdViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.LIST_CATEGORY_COUNT;
                                productListByCatIdViewModel.offset = productListByCatIdViewModel.offset + limit;

                                productListByCatIdViewModel.setNextPageLoadingStateObj(loginUserId, String.valueOf(productListByCatIdViewModel.offset), catId);
                            }
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            productListByCatIdViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            productListByCatIdViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            productListByCatIdViewModel.forceEndLoading = false;

            // update live data
            productListByCatIdViewModel.setProductListObj(loginUserId, String.valueOf(productListByCatIdViewModel.offset), catId);

        });
    }

    @Override
    protected void initViewModels() {
        productListByCatIdViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductListByCatIdViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ProductVerticalListAdapter nvAdapter = new ProductVerticalListAdapter(dataBindingComponent, new ProductVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToShopProfile((SelectedShopActivity) getActivity());
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
        binding.get().productList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {

                    catId = getActivity().getIntent().getExtras().getString(Constants.CATEGORY_ID);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
        loadProduct();
    }


    //region Private Methods

    private void loadProduct() {

        // Load Latest Product
        productListByCatIdViewModel.setProductListObj(loginUserId, String.valueOf(productListByCatIdViewModel.offset), catId);

        LiveData<Resource<List<Product>>> news = productListByCatIdViewModel.getproductList();

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

                            productListByCatIdViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            productListByCatIdViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (productListByCatIdViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        productListByCatIdViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        productListByCatIdViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    productListByCatIdViewModel.setLoadingState(false);
                    productListByCatIdViewModel.forceEndLoading = true;
                }
            }
        });

        productListByCatIdViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(productListByCatIdViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    private void replaceData(List<Product> newsList) {

        adapter.get().replace(newsList);
        binding.get().executePendingBindings();

    }


    @Override
    public void onDispatched() {
        if (productListByCatIdViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().productList != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().productList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }

    private void unFavFunction(Product product, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(product.id, loginUserId,selectedShopId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }

        });

    }

    private void favFunction(Product product, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(product.id, loginUserId,selectedShopId);
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
