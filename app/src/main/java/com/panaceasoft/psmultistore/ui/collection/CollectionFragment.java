package com.panaceasoft.psmultistore.ui.collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentCollectionProductsBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.adapter.ProductVerticalListAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.collection.ProductCollectionProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.FavouriteViewModel;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.List;

public class CollectionFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ProductCollectionProductViewModel productCollectionProductViewModel;
    private FavouriteViewModel favouriteViewModel;
    private BasketViewModel basketViewModel;
    private MenuItem basketMenuItem;
    private PSDialogMsg psDialogMsg;
    @VisibleForTesting
    private
    AutoClearedValue<FragmentCollectionProductsBinding> binding;
    private AutoClearedValue<ProductVerticalListAdapter> adapter;
    private AutoClearedValue<Intent> intent;
    private String id;

    //endregion


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCollectionProductsBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection_products, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());
        setHasOptionsMenu(true);

        if (this.getActivity() != null) {
            Intent intent1 = getActivity().getIntent();
            intent = new AutoClearedValue<>(this, intent1);
        }

        this.id = intent.get().getStringExtra(Constants.COLLECTION_ID);
        String image = intent.get().getStringExtra(Constants.COLLECTION_IMAGE);

        binding.get().setImage(image);

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

        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

        binding.get().newsList.setNestedScrollingEnabled(false);
        binding.get().newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {
                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !productCollectionProductViewModel.forceEndLoading) {

                            if (connectivity.isConnected()) {

                                productCollectionProductViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.PRODUCT_COUNT;
                                productCollectionProductViewModel.offset = productCollectionProductViewModel.offset + limit;

                                productCollectionProductViewModel.setNextPageLoadingStateObj(String.valueOf(Config.PRODUCT_COUNT), String.valueOf(productCollectionProductViewModel.offset), id);
                            }
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            productCollectionProductViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            productCollectionProductViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            productCollectionProductViewModel.forceEndLoading = false;

            // update live data
            productCollectionProductViewModel.setProductCollectionProductListObj(String.valueOf(Config.PRODUCT_COUNT), String.valueOf(productCollectionProductViewModel.offset), id);

        });
    }

    @Override
    protected void initViewModels() {
        productCollectionProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductCollectionProductViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel.class);
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);

    }

    @Override
    protected void initAdapters() {

        ProductVerticalListAdapter nvAdapter = new ProductVerticalListAdapter(dataBindingComponent, new ProductVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(CollectionFragment.this.getActivity(), product);
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
        binding.get().newsList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        loadDiscount();
        basketData();
    }

    private void basketData() {
        //set and get basket list
        basketViewModel.setBasketListObj(selectedShopId);
        basketViewModel.getAllBasketList().observe(this, resource -> {
            if (resource != null) {
                basketViewModel.basketCount = resource.size();
                if (resource.size() > 0) {
                    setBasketMenuItemVisible(true);
                } else {
                    setBasketMenuItemVisible(false);
                }
            }
        });
    }

    //region Private Methods

    private void loadDiscount() {

        // Load Latest Product
        productCollectionProductViewModel.setProductCollectionProductListObj(String.valueOf(Config.PRODUCT_COUNT), String.valueOf(productCollectionProductViewModel.offset), id);

        LiveData<Resource<List<Product>>> news = productCollectionProductViewModel.getProductCollectionProductListData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

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

                            productCollectionProductViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            productCollectionProductViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (productCollectionProductViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        productCollectionProductViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        productCollectionProductViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    productCollectionProductViewModel.setLoadingState(false);
                    productCollectionProductViewModel.forceEndLoading = true;
                }
            }
        });

        productCollectionProductViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(productCollectionProductViewModel.isLoading);

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
        if (productCollectionProductViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().newsList != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().newsList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
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
