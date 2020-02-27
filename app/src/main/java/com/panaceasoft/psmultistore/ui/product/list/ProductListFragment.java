package com.panaceasoft.psmultistore.ui.product.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.like.LikeButton;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.BottomBoxLayoutBinding;
import com.panaceasoft.psmultistore.databinding.FragmentProductListBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.adapter.ProductVerticalListAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeSearchProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.FavouriteViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.TouchCountViewModel;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;
import com.panaceasoft.psmultistore.viewobject.holder.ProductParameterHolder;

import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private boolean typeClicked = false;
    private boolean filterClicked = false;
    private List<Product> clearRecyclerView = new ArrayList<>();

    private HomeSearchProductViewModel homeSearchProductViewModel;
    private TouchCountViewModel touchCountViewModel;
    private FavouriteViewModel favouriteViewModel;
    private BasketViewModel basketViewModel;
    private PSDialogMsg psDialogMsg;


    @VisibleForTesting
    private AutoClearedValue<FragmentProductListBinding> binding;
    private AutoClearedValue<ProductVerticalListAdapter> adapter;
    private AutoClearedValue<BottomBoxLayoutBinding> bottomBoxLayoutBinding;
    private AutoClearedValue<BottomSheetDialog> mBottomSheetDialog;
    private AutoClearedValue<MenuItem> basketMenuItem;


    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        FragmentProductListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    private void setBasketMenuItemVisible(Boolean isVisible) {
        if (basketMenuItem != null) {
            basketMenuItem.get().setVisible(isVisible);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.basket_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        basketMenuItem = new AutoClearedValue<>(this, menu.findItem(R.id.action_basket));

        if (basketViewModel != null) {
            if (basketViewModel.basketCount > 0) {
                basketMenuItem.get().setVisible(true);
            } else {
                basketMenuItem.get().setVisible(false);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Constants.REQUEST_CODE__PRODUCT_LIST_FRAGMENT
                && resultCode == Constants.RESULT_CODE__CATEGORY_FILTER) {

            // Result from Category Filter List

            String catId = data.getStringExtra(Constants.CATEGORY_ID);
            if (catId != null) {
                homeSearchProductViewModel.holder.catId = catId;
            }

            String subCatId = data.getStringExtra(Constants.SUBCATEGORY_ID);
            if (subCatId != null) {
                homeSearchProductViewModel.holder.subCatId = subCatId;
            }

            typeClicked = (homeSearchProductViewModel.holder.catId != null && !homeSearchProductViewModel.holder.catId.equals(""))
                    || ( homeSearchProductViewModel.holder.subCatId != null && !homeSearchProductViewModel.holder.subCatId.equals(""));

            typeButtonClicked(typeClicked);

            replaceData(clearRecyclerView);

            loadProductList();

        } else if (requestCode == Constants.REQUEST_CODE__PRODUCT_LIST_FRAGMENT
                && resultCode == Constants.RESULT_CODE__SPECIAL_FILTER) {

            // Result From Filter

            if (data.getSerializableExtra(Constants.FILTERING_HOLDER) != null) {

                homeSearchProductViewModel.holder = (ProductParameterHolder) data.getSerializableExtra(Constants.FILTERING_HOLDER);

            }

            filterClicked = !homeSearchProductViewModel.holder.search_term.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.min_price.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.max_price.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.isFeatured.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.isDiscount.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.overall_rating.equals(Constants.FILTERING_INACTIVE);

            tuneButtonClicked(filterClicked);

            replaceData(clearRecyclerView);

            loadProductList();

        }

    }


    @Override
    protected void initUIAndActions() {

        if (getContext() != null) {

            // Prepare Sorting Bottom Sheet
            mBottomSheetDialog = new AutoClearedValue<>(this, new BottomSheetDialog(getContext()));

            bottomBoxLayoutBinding = new AutoClearedValue<>(this, DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.bottom_box_layout, null, false));

            mBottomSheetDialog.get().setContentView(bottomBoxLayoutBinding.get().getRoot());

        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

        binding.get().typeButton.setOnClickListener(this::ButtonClick);

        binding.get().tuneButton.setOnClickListener(this::ButtonClick);

        binding.get().sortButton.setOnClickListener(this::ButtonClick);

        binding.get().newsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !homeSearchProductViewModel.forceEndLoading) {

                            homeSearchProductViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.PRODUCT_COUNT;

                            homeSearchProductViewModel.offset = homeSearchProductViewModel.offset + limit;

                            loadNextPageProductList(String.valueOf(homeSearchProductViewModel.offset));

                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            homeSearchProductViewModel.forceEndLoading = false;

            homeSearchProductViewModel.loadingDirection = Utils.LoadingDirection.top;

            loadProductList();

        });
    }


    @Override
    protected void initViewModels() {
        // ViewModel need to get from ViewModelProviders
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel.class);
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel.class);
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);

    }

    @Override
    protected void initAdapters() {

        ProductVerticalListAdapter nvAdapter = new ProductVerticalListAdapter(dataBindingComponent, new ProductVerticalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(ProductListFragment.this.getActivity(), product);
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

        homeSearchProductViewModel.holder.shopId = selectedShopId;

        basketData();

        if (getActivity() != null) {

            homeSearchProductViewModel.holder = (ProductParameterHolder) getActivity().getIntent().getSerializableExtra(Constants.PRODUCT_PARAM_HOLDER_KEY);
            homeSearchProductViewModel.holder.shopId = selectedShopId;

            switch (homeSearchProductViewModel.holder.order_by) {
                case Constants.FILTERING_ADDED_DATE:
                    setSortingSelection(0);
                    break;
                case Constants.FILTERING_TRENDING:
                    setSortingSelection(1);
                    break;
                default:
                    setSortingSelection(0);
                    break;
            }

            initProductData();

            setTouchCount();

            filterClicked = !homeSearchProductViewModel.holder.search_term.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.min_price.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.max_price.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.isFeatured.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.isDiscount.equals(Constants.FILTERING_INACTIVE) ||
                    !homeSearchProductViewModel.holder.overall_rating.equals(Constants.FILTERING_INACTIVE);

            typeClicked = !homeSearchProductViewModel.holder.catId.equals(Constants.FILTERING_INACTIVE) || !homeSearchProductViewModel.holder.subCatId.equals(Constants.FILTERING_INACTIVE);

            typeButtonClicked(typeClicked);

            tuneButtonClicked(filterClicked);

        }

        // touch count post method
        if (connectivity.isConnected()) {
            touchCountViewModel.setTouchCountPostDataObj(loginUserId, homeSearchProductViewModel.holder.catId, Constants.FILTERING_TYPE_NAME_CAT, selectedShopId);
        }
        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ProductListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (ProductListFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
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

    //endregion


    //region Private Methods

    private void initProductData() {

        loadProductList();

        LiveData<Resource<List<Product>>> news = homeSearchProductViewModel.getGetProductListByKeyData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {
                    switch (listResource.status) {
                        case SUCCESS:
                            if (listResource.data != null) {
                                if (listResource.data.size() == 0) {

                                    if (!binding.get().getLoadingMore()) {
                                        binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);

                                }

                                fadeIn(binding.get().getRoot());

                                replaceData(listResource.data);

                                homeSearchProductViewModel.setLoadingState(false);

                                onDispatched();
                            }
                            break;

                        case LOADING:
                            if (listResource.data != null) {
                                if (listResource.data.size() == 0) {

                                    if (!binding.get().getLoadingMore()) {
                                        binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    binding.get().noItemConstraintLayout.setVisibility(View.INVISIBLE);

                                }

                                fadeIn(binding.get().getRoot());

                                replaceData(listResource.data);

                                onDispatched();
                            }
                            break;

                        case ERROR:

                            homeSearchProductViewModel.setLoadingState(false);
                            homeSearchProductViewModel.forceEndLoading = true;

                            if(homeSearchProductViewModel.getGetProductListByKeyData() != null) {
                                if(homeSearchProductViewModel.getGetProductListByKeyData().getValue() != null) {
                                    if(homeSearchProductViewModel.getGetProductListByKeyData().getValue().data != null) {
                                        if (!binding.get().getLoadingMore() && homeSearchProductViewModel.getGetProductListByKeyData().getValue().data.size() == 0) {
                                            binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }

                            break;

                        default:
                            break;
                    }
                }
            });
        }

        homeSearchProductViewModel.getGetNextPageProductListByKeyData().observe(this, state -> {

            if (state != null) {
                if (state.status == Status.ERROR) {

                    homeSearchProductViewModel.setLoadingState(false);
                    homeSearchProductViewModel.forceEndLoading = true;

                }
            }

        });

        homeSearchProductViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(homeSearchProductViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });
    }

    private void replaceData(List<Product> newsList) {
        adapter.get().replace(newsList);
        binding.get().executePendingBindings();
    }


    private void typeButtonClicked(Boolean b) {
        if (b) {
            binding.get().typeButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_list_with_check_orange_24), null, null);
        } else {
            binding.get().typeButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_list_orange_24), null, null);
        }
    }

    private void tuneButtonClicked(Boolean b) {
        if (b) {

            binding.get().tuneButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_tune_with_check_orange_24), null, null);

        } else {
            binding.get().tuneButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_tune_orange_24), null, null);
        }
    }

    private void ButtonClick(View view) {

        switch (view.getId()) {
            case R.id.typeButton:

                navigationController.navigateToTypeFilterFragment(ProductListFragment.this.getActivity(), homeSearchProductViewModel.holder.catId,
                        homeSearchProductViewModel.holder.subCatId, homeSearchProductViewModel.holder, Constants.FILTERING_TYPE_FILTER);

                typeButtonClicked(typeClicked);

                break;

            case R.id.tuneButton:

                navigationController.navigateToTypeFilterFragment(ProductListFragment.this.getActivity(), homeSearchProductViewModel.holder.catId,
                        homeSearchProductViewModel.holder.subCatId, homeSearchProductViewModel.holder, Constants.FILTERING_SPECIAL_FILTER);

                tuneButtonClicked(filterClicked);

                break;

            case R.id.sortButton:

                mBottomSheetDialog.get().show();
                ButtonSheetClick();

                break;

            default:
                Utils.psLog("No ID for Buttons");
        }
    }

    private void ButtonSheetClick() {

        bottomBoxLayoutBinding.get().popularButton.setOnClickListener(view -> {

            homeSearchProductViewModel.loadingDirection = Utils.LoadingDirection.top;

            setSortingSelection(1);

            homeSearchProductViewModel.holder.order_by = Constants.FILTERING_TRENDING;
            homeSearchProductViewModel.holder.order_type = Constants.FILTERING_DESC;

            replaceData(clearRecyclerView);

            loadProductList();

            mBottomSheetDialog.get().dismiss();

        });

        bottomBoxLayoutBinding.get().recentButton.setOnClickListener(view -> {
            homeSearchProductViewModel.loadingDirection = Utils.LoadingDirection.top;

            setSortingSelection(0);


            if (homeSearchProductViewModel.holder.isFeatured.equals(Constants.ONE)) {
                homeSearchProductViewModel.holder.order_by = Constants.FILTERING_FEATURE;
            } else {
                homeSearchProductViewModel.holder.order_by = Constants.FILTERING_ADDED_DATE;
            }


            homeSearchProductViewModel.holder.order_type = Constants.FILTERING_DESC;

            replaceData(clearRecyclerView);

            loadProductList();

            mBottomSheetDialog.get().dismiss();

        });


        bottomBoxLayoutBinding.get().lowestButton.setOnClickListener(view -> {
            homeSearchProductViewModel.loadingDirection = Utils.LoadingDirection.top;

            setSortingSelection(2);

            homeSearchProductViewModel.holder.order_by = Constants.FILTERING_PRICE;
            homeSearchProductViewModel.holder.order_type = Constants.FILTERING_ASC;

            replaceData(clearRecyclerView);

            loadProductList();

            mBottomSheetDialog.get().dismiss();
        });


        bottomBoxLayoutBinding.get().highestButton.setOnClickListener(view -> {

            homeSearchProductViewModel.loadingDirection = Utils.LoadingDirection.top;

            setSortingSelection(3);

            homeSearchProductViewModel.holder.order_by = Constants.FILTERING_PRICE;
            homeSearchProductViewModel.holder.order_type = Constants.FILTERING_DESC;


            replaceData(clearRecyclerView);

            loadProductList();

            mBottomSheetDialog.get().dismiss();

        });
    }

    private void setSortingSelection(int index) {
        binding.get().sortButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_sort_with_check_orange_24), null, null);

        if (index == 0) {
            bottomBoxLayoutBinding.get().recentButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baesline_access_time_black_24), null, getResources().getDrawable(R.drawable.baseline_check_green_24), null);
        } else {
            bottomBoxLayoutBinding.get().recentButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baesline_access_time_black_24), null, null, null);
        }

        if (index == 1) {
            bottomBoxLayoutBinding.get().popularButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_graph_black_24), null, getResources().getDrawable(R.drawable.baseline_check_green_24), null);
        } else {
            bottomBoxLayoutBinding.get().popularButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_graph_black_24), null, null, null);
        }

        if (index == 2) {
            bottomBoxLayoutBinding.get().lowestButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_price_down_black_24), null, getResources().getDrawable(R.drawable.baseline_check_green_24), null);
        } else {
            bottomBoxLayoutBinding.get().lowestButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_price_down_black_24), null, null, null);
        }

        if (index == 3) {
            bottomBoxLayoutBinding.get().highestButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_price_up_black_24), null, getResources().getDrawable(R.drawable.baseline_check_green_24), null);
        } else {
            bottomBoxLayoutBinding.get().highestButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.baseline_price_up_black_24), null, null, null);
        }
    }

    private void setTouchCount() {

        if (!homeSearchProductViewModel.holder.catId.isEmpty()) {
            //Utils.psLog("Hotel Id : " + touchCountViewModel.hotelId + "  Login User Id : " + loginUserId);
            if (connectivity.isConnected()) {
                if (loginUserId.equals("")) {
                    touchCountViewModel.setTouchCountPostDataObj(Constants.ZERO, homeSearchProductViewModel.holder.catId, Constants.FILTERING_CATEGORY_TYPE_NAME, selectedShopId);
                } else {
                    touchCountViewModel.setTouchCountPostDataObj(loginUserId, homeSearchProductViewModel.holder.catId, Constants.FILTERING_CATEGORY_TYPE_NAME, selectedShopId);
                }

                touchCountViewModel.getTouchCountPostData().observe(this, state -> {
                    if (state != null) {
                        if (state.status == Status.SUCCESS) {

                            Utils.psLog("SUCCEED");

                        } else if (state.status == Status.ERROR) {

                            Utils.psLog("FAILED");
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onDispatched() {

        if (homeSearchProductViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get() != null) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        binding.get().newsList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPositionWithOffset(0, 0);
                }
            }
        }
    }

    private void resetLimitAndOffset() {
        homeSearchProductViewModel.offset = 0;
        homeSearchProductViewModel.limit = Config.PRODUCT_COUNT;
    }

    private void loadNextPageProductList(String offset) {

        checkAndUpdateFeatureSorting();

        homeSearchProductViewModel.setGetNextPageProductListByKeyObj(homeSearchProductViewModel.holder, loginUserId, String.valueOf(Config.PRODUCT_COUNT), offset);

    }

    private void loadProductList() {

        resetLimitAndOffset();

        checkAndUpdateFeatureSorting();

        homeSearchProductViewModel.setGetProductListByKeyObj(homeSearchProductViewModel.holder, loginUserId, String.valueOf(Config.PRODUCT_COUNT), Constants.ZERO);

    }

    private void checkAndUpdateFeatureSorting() {
        if (homeSearchProductViewModel.holder.isFeatured.equals(Constants.ONE)
        && homeSearchProductViewModel.holder.order_by.equals(Constants.FILTERING_ADDED_DATE)) {
            homeSearchProductViewModel.holder.order_by = Constants.FILTERING_FEATURE;
        }
    }

    //endregion
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

