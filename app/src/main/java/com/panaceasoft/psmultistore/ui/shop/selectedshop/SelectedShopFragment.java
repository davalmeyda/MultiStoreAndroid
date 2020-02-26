package com.panaceasoft.psmultistore.ui.shop.selectedshop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.like.LikeButton;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentSelectedShopBinding;
import com.panaceasoft.psmultistore.ui.category.adapter.CategoryIconListAdapter;
import com.panaceasoft.psmultistore.ui.category.adapter.TrendingCategoryAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.adapter.DiscountListAdapter;
import com.panaceasoft.psmultistore.ui.product.adapter.ProductHorizontalListAdapter;
import com.panaceasoft.psmultistore.ui.shop.selectedshop.adapter.ProductCollectionRowAdapter;
import com.panaceasoft.psmultistore.ui.shop.selectedshop.adapter.ViewPagerAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.category.CategoryViewModel;
import com.panaceasoft.psmultistore.viewmodel.collection.ProductCollectionViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeFeaturedProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeLatestProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeSearchProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeTrendingCategoryListViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeTrendingProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.FavouriteViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.TouchCountViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.ShopViewModel;
import com.panaceasoft.psmultistore.viewobject.Category;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.ProductCollectionHeader;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;
import com.panaceasoft.psmultistore.viewobject.holder.ProductParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class SelectedShopFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private HomeLatestProductViewModel homeLatestProductViewModel;
    private BasketViewModel basketViewModel;
    private HomeSearchProductViewModel homeSearchProductViewModel;
    private HomeTrendingProductViewModel homeTrendingProductViewModel;
    private HomeTrendingCategoryListViewModel homeTrendingCategoryListViewModel;
    private ProductCollectionViewModel productCollectionViewModel;
    private HomeFeaturedProductViewModel homeFeaturedProductViewModel;
    private TouchCountViewModel touchCountViewModel;
    private CategoryViewModel categoryViewModel;
    private FavouriteViewModel favouriteViewModel;
    private ShopViewModel shopViewModel;
    private int dotsCount = 0;
    private ImageView[] dots;
    private boolean layoutDone = false;
    private int loadingCount = 0;
    private GridLayoutManager gridLayoutManager, categoryGridLayoutManager;
    private MenuItem basketMenuItem;
    private PSDialogMsg psDialogMsg;

    @Inject
    protected SharedPreferences pref;

    @VisibleForTesting
    private AutoClearedValue<FragmentSelectedShopBinding> binding;
    private AutoClearedValue<ViewPagerAdapter> viewPagerAdapter;
    private AutoClearedValue<CategoryIconListAdapter> categoryIconListAdapter;
    private AutoClearedValue<DiscountListAdapter> discountListAdapter;
    private AutoClearedValue<ProductHorizontalListAdapter> trendingAdapter, latestAdapter;
    private AutoClearedValue<TrendingCategoryAdapter> categoryAdapter;
    private AutoClearedValue<ProductCollectionRowAdapter> verticalRowAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentSelectedShopBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_selected_shop, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());
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
        inflater.inflate(R.menu.blog_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        basketMenuItem = menu.findItem(R.id.action_basket);
        MenuItem blogMenuItem = menu.findItem(R.id.action_blog);
        blogMenuItem.setVisible(true);

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
        if (item.getItemId() == R.id.action_blog) {
            navigationController.navigateToBlogListBySelectedShop(getActivity(), selectedShopId);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
            AdRequest adRequest2 = new AdRequest.Builder()
                    .build();
            binding.get().adView2.loadAd(adRequest2);
        } else {
            binding.get().adView.setVisibility(View.GONE);
            binding.get().adView2.setVisibility(View.GONE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

        binding.get().viewAllSliderTextView.setOnClickListener(view -> navigationController.navigateToHomeFilteringActivity(getActivity(), homeFeaturedProductViewModel.productParameterHolder, getString(R.string.menu__featured_product)));

        binding.get().viewAllDiscountTextView.setOnClickListener(view -> navigationController.navigateToHomeFilteringActivity(getActivity(), homeSearchProductViewModel.holder.getDiscountParameterHolder(), getString(R.string.menu__discount)));

        binding.get().viewAllTrendingTextView.setOnClickListener(view -> navigationController.navigateToHomeFilteringActivity(getActivity(), homeTrendingProductViewModel.productParameterHolder, getString(R.string.menu__trending_products)));

        binding.get().viewAllTrendingCategoriesTextView.setOnClickListener(view -> navigationController.navigateToCategoryActivity(getActivity(), Constants.CATEGORY_TRENDING));

        binding.get().viewALlLatestTextView.setOnClickListener(view -> navigationController.navigateToHomeFilteringActivity(getActivity(), homeLatestProductViewModel.productParameterHolder, getString(R.string.menu__latest_product)));

        binding.get().categoryViewAllTextView.setOnClickListener(v -> navigationController.navigateToCategoryActivity(getActivity(), Constants.CATEGORY));

        binding.get().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setupSliderPagination();
                if (dotsCount != 0) {

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }

                    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initViewModels() {

        homeLatestProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeLatestProductViewModel.class);
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel.class);
        homeTrendingProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeTrendingProductViewModel.class);
        homeTrendingCategoryListViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeTrendingCategoryListViewModel.class);
        categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel.class);
        productCollectionViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductCollectionViewModel.class);
        homeFeaturedProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeFeaturedProductViewModel.class);
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel.class);
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel.class);
    }

    @Override
    protected void initAdapters() {

        /*LatestList*/

        ProductHorizontalListAdapter latestAdapter1 = new ProductHorizontalListAdapter(dataBindingComponent, new ProductHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(SelectedShopFragment.this.getActivity(), product);
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


        this.latestAdapter = new AutoClearedValue<>(this, latestAdapter1);
        binding.get().productList.setAdapter(latestAdapter1);

        /*LatestList*/

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*discountList*/

        DiscountListAdapter discountListAdapter1 = new DiscountListAdapter(dataBindingComponent, new DiscountListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(SelectedShopFragment.this.getActivity(), product);
            }

            @Override
            public void onViewAllClick() {
                navigationController.navigateToHomeFilteringActivity(getActivity(), new ProductParameterHolder().getDiscountParameterHolder(), getString(R.string.menu__discount));
            }

            @Override
            public void onFavLikeClick(Product product, LikeButton likeButton) {
                favFunction(product, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                unFavFunction(product, likeButton);
            }
        });

        this.discountListAdapter = new AutoClearedValue<>(this, discountListAdapter1);
        binding.get().discountList.setAdapter(discountListAdapter1);

        /*discountList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*featuredList*/

        ViewPagerAdapter viewPagerAdapter1 = new ViewPagerAdapter(dataBindingComponent, product -> navigationController.navigateToDetailActivity(SelectedShopFragment.this.getActivity(), product));

        this.viewPagerAdapter = new AutoClearedValue<>(this, viewPagerAdapter1);
        binding.get().viewPager.setAdapter(viewPagerAdapter1);
        binding.get().viewPagerCountDots.setVisibility(View.VISIBLE);


        /*featuredList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*CategoryIconList*/

        CategoryIconListAdapter categoryIconListAdapter1 = new CategoryIconListAdapter(dataBindingComponent, category -> {
            categoryViewModel.productParameterHolder.catId = category.id;
            navigationController.navigateToHomeFilteringActivity(SelectedShopFragment.this.getActivity(), categoryViewModel.productParameterHolder, category.name);
        }, this);

//                new CategoryIconListAdapter().CategoryClickCallback() {
//            @Override
//            public void onClick(Category category) {
//
//                categoryViewModel.productParameterHolder.catId = category.id;
//                navigationController.navigateToHomeFilteringActivity(SelectedShopFragment.this.getActivity(), categoryViewModel.productParameterHolder, category.name);
//            }
//
//        });

        this.categoryIconListAdapter = new AutoClearedValue<>(this, categoryIconListAdapter1);
        binding.get().categoryIconList.setAdapter(categoryIconListAdapter1);
//        binding.get().categoryIconList.setNestedScrollingEnabled(false);

        /*CategoryIconList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Latest Product*/

        ProductHorizontalListAdapter trendingAdapter1 = new ProductHorizontalListAdapter(dataBindingComponent, new ProductHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(SelectedShopFragment.this.getActivity(), product);
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

        this.trendingAdapter = new AutoClearedValue<>(this, trendingAdapter1);
        binding.get().trendingList.setAdapter(trendingAdapter1);

        /* Latest Product*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        TrendingCategoryAdapter trendingCategoryAdapter = new TrendingCategoryAdapter(dataBindingComponent,
                category -> {
                    categoryViewModel.productParameterHolder.catId = category.id;
                    navigationController.navigateToHomeFilteringActivity(this.getActivity(), categoryViewModel.productParameterHolder, category.name);
                }, this);

        this.categoryAdapter = new AutoClearedValue<>(this, trendingCategoryAdapter);
        binding.get().trendingCategoryList.setAdapter(trendingCategoryAdapter);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ProductCollectionRowAdapter verticalRowAdapter1 = new ProductCollectionRowAdapter(dataBindingComponent, new ProductCollectionRowAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(SelectedShopFragment.this.getActivity(), product);
            }

            @Override
            public void onViewAllClick(ProductCollectionHeader productCollectionHeader) {
                navigationController.navigateToCollectionProductList(SelectedShopFragment.this.getActivity(), productCollectionHeader.id, productCollectionHeader.name, productCollectionHeader.defaultPhoto.imgPath);
            }

            @Override
            public void onFavLikeClick(Product product, LikeButton likeButton) {
                favFunction(product, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                unFavFunction(product, likeButton);
            }
        });

        this.verticalRowAdapter = new AutoClearedValue<>(this, verticalRowAdapter1);
        binding.get().collections.setAdapter(verticalRowAdapter1);
        binding.get().collections.setNestedScrollingEnabled(false);
    }

    private void replaceLatestData(List<Product> productList) {
        latestAdapter.get().replace(productList);
        binding.get().executePendingBindings();
    }

    private void replaceCategoryIconList(List<Category> categoryList) {
        categoryIconListAdapter.get().replace(categoryList);
        binding.get().executePendingBindings();
    }

    private void replaceFeaturedData(List<Product> featuredProductList) {
        viewPagerAdapter.get().replaceFeaturedList(featuredProductList);
        setupSliderPagination();

        binding.get().executePendingBindings();
    }

    private void replaceDiscountList(List<Product> productList) {

        discountListAdapter.get().replaceDiscount(productList);
        discountListAdapter.get().notifyDataSetChanged();
        binding.get().executePendingBindings();
    }

    private void replaceTrendingData(List<Product> productList) {
        trendingAdapter.get().replace(productList);
        binding.get().executePendingBindings();
    }

    private void replaceTrendingCategoryData(List<Category> categoryList) {
        categoryAdapter.get().replace(categoryList);
        binding.get().executePendingBindings();
    }

    private void replaceCollection(List<ProductCollectionHeader> productCollectionHeaders) {
        verticalRowAdapter.get().replaceCollectionHeader(productCollectionHeaders);
        binding.get().executePendingBindings();
    }


    @Override
    protected void initData() {

        getIntentData();

        setShopTouchCount();

        basketData();

        loadProducts();
    }

    private void getIntentData() {
        if (getActivity() != null) {
            homeFeaturedProductViewModel.productParameterHolder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);
            homeSearchProductViewModel.holder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);
            selectedShopId = homeSearchProductViewModel.holder.shopId;
            homeTrendingProductViewModel.productParameterHolder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);
            homeLatestProductViewModel.productParameterHolder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);
            categoryViewModel.productParameterHolder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);

            shopViewModel.setShopObj(Config.API_KEY, getActivity().getIntent().getStringExtra(Constants.SHOP_ID));

        }
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

    private void loadProducts() {

        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });

        shopViewModel.getShopData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        if (result.data != null) {
                            pref.edit().putString(Constants.SHOP_ID, result.data.id).apply();
                            pref.edit().putString(Constants.SHIPPING_ID, result.data.shippingId).apply();
                            pref.edit().putString(Constants.SHOP_NAME, result.data.name).apply();
                            pref.edit().putString(Constants.PAYMENT_SHIPPING_TAX, String.valueOf(result.data.shippingTaxValue)).apply();
                            pref.edit().putString(Constants.PAYMENT_OVER_ALL_TAX, String.valueOf(result.data.overallTaxValue)).apply();
                            pref.edit().putString(Constants.PAYMENT_SHIPPING_TAX_LABEL, String.valueOf(result.data.shippingTaxLabel)).apply();
                            pref.edit().putString(Constants.PAYMENT_OVER_ALL_TAX_LABEL, String.valueOf(result.data.overallTaxLabel)).apply();
                            pref.edit().putString(Constants.PAYMENT_CASH_ON_DELIVERY, String.valueOf(result.data.codEnabled)).apply();
                            pref.edit().putString(Constants.PAYMENT_PAYPAL, String.valueOf(result.data.paypalEnabled)).apply();
                            pref.edit().putString(Constants.PAYMENT_STRIPE, String.valueOf(result.data.stripeEnabled)).apply();
                            pref.edit().putString(Constants.MESSENGER, String.valueOf(result.data.messenger)).apply();
                            pref.edit().putString(Constants.WHATSAPP, String.valueOf(result.data.whapsappNo)).apply();
                            pref.edit().putString(Constants.SHOP_PHONE_NUMBER,String.valueOf(result.data.aboutPhone1)).apply();
                            pref.edit().putString(Constants.SHOP_STANDARD_SHIPPING_ENABLE, result.data.standardShippingEnable).apply();
                            pref.edit().putString(Constants.SHOP_ZONE_SHIPPING_ENABLE, result.data.zoneShippingEnable).apply();
                            pref.edit().putString(Constants.SHOP_NO_SHIPPING_ENABLE, result.data.noShippingEnable).apply();


                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

        if (getActivity() != null) {
            homeFeaturedProductViewModel.productParameterHolder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);
            homeSearchProductViewModel.holder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);
            selectedShopId = homeSearchProductViewModel.holder.shopId;
            pref.edit().putString(Constants.SHOP_ID, homeSearchProductViewModel.holder.shopId).apply();
            homeTrendingProductViewModel.productParameterHolder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);
            homeLatestProductViewModel.productParameterHolder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);
            categoryViewModel.productParameterHolder.shopId = getActivity().getIntent().getStringExtra(Constants.SHOP_ID);

            shopViewModel.setShopObj(Config.API_KEY, getActivity().getIntent().getStringExtra(Constants.SHOP_ID));

        }

        // Load Latest Product List

        homeLatestProductViewModel.setGetProductListByKeyObj(homeLatestProductViewModel.productParameterHolder, loginUserId, String.valueOf(Config.HOME_PRODUCT_COUNT), String.valueOf(homeLatestProductViewModel.offset));

        LiveData<Resource<List<Product>>> latest = homeLatestProductViewModel.getGetProductListByKeyData();

        if (latest != null) {

            latest.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {

                        case LOADING:
                            // Loading State
                            // Data are from Local DB
                            if (listResource.data != null) {

                                binding.get().latestTitleTextView.setVisibility(View.VISIBLE);
                                binding.get().viewALlLatestTextView.setVisibility(View.VISIBLE);

                                // Update the data
                                replaceLatestData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                binding.get().latestTitleTextView.setVisibility(View.VISIBLE);
                                binding.get().viewALlLatestTextView.setVisibility(View.VISIBLE);

                                // Update the data
                                replaceLatestData(listResource.data);
                            }

                            homeLatestProductViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            homeLatestProductViewModel.setLoadingState(false);
                            homeLatestProductViewModel.forceEndLoading = true;

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (homeLatestProductViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        homeLatestProductViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        homeLatestProductViewModel.getGetNextPageProductListByKeyData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    homeLatestProductViewModel.setLoadingState(false);
                    homeLatestProductViewModel.forceEndLoading = true;
                }
            }
        });

        homeLatestProductViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(homeLatestProductViewModel.isLoading);
            Utils.psLog("getLoadingState : start");
            if (loadingState != null && !loadingState) {
                Utils.psLog("getLoadingState end");
            }

        });


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        homeFeaturedProductViewModel.setGetProductListByKeyObj(homeFeaturedProductViewModel.productParameterHolder, loginUserId, String.valueOf(Config.HOME_PRODUCT_COUNT), String.valueOf(homeFeaturedProductViewModel.offset));

        LiveData<Resource<List<Product>>> featured = homeFeaturedProductViewModel.getGetProductListByKeyData();

        if (featured != null) {

            featured.observe(this, listResource -> {

                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:

                            if (listResource.data != null) {
                                SelectedShopFragment.this.fadeIn(binding.get().viewPager);
                                binding.get().sliderHeaderTextView.setVisibility(View.VISIBLE);
                                binding.get().viewAllSliderTextView.setVisibility(View.VISIBLE);

                                SelectedShopFragment.this.replaceFeaturedData(listResource.data);
                            }
                            break;

                        case SUCCESS:
                            if (listResource.data != null) {
                                SelectedShopFragment.this.fadeIn(binding.get().viewPager);
                                binding.get().sliderHeaderTextView.setVisibility(View.VISIBLE);
                                binding.get().viewAllSliderTextView.setVisibility(View.VISIBLE);

                                SelectedShopFragment.this.replaceFeaturedData(listResource.data);

                                homeFeaturedProductViewModel.setLoadingState(false);
                            }
                            break;

                        case ERROR:

                            homeFeaturedProductViewModel.setLoadingState(false);
                            homeFeaturedProductViewModel.forceEndLoading = true;

                            break;

                        default:
                            break;
                    }
                }
            });
        }

        homeFeaturedProductViewModel.getGetNextPageProductListByKeyData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    homeFeaturedProductViewModel.setLoadingState(false);
                    homeFeaturedProductViewModel.forceEndLoading = true;
                }
            }
        });

        homeFeaturedProductViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(homeFeaturedProductViewModel.isLoading));

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* CategoryRecyclerView*/

        categoryViewModel.categoryParameterHolder.shopId = selectedShopId;


        categoryViewModel.setCategoryListObj(loginUserId, categoryViewModel.categoryParameterHolder, String.valueOf(Config.HOME_CATEGORY_COUNT), String.valueOf(homeSearchProductViewModel.offset));

        LiveData<Resource<List<Category>>> categories = categoryViewModel.getCategoryListData();

        if (categories != null) {
            categories.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().categoryIconList);

                                // Update the data

                                if (listResource.data.size() > 0) {
                                    if (listResource.data.size() < 9) {
                                        categoryGridLayoutManager = new GridLayoutManager(getContext(), 1);
                                        categoryGridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().categoryIconList.setLayoutManager(categoryGridLayoutManager);
                                    } else {
                                        categoryGridLayoutManager = new GridLayoutManager(getContext(), 2);
                                        categoryGridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().categoryIconList.setLayoutManager(categoryGridLayoutManager);
                                    }

                                    replaceCategoryIconList(listResource.data);
                                    binding.get().categoryTextView.setVisibility(View.VISIBLE);
                                    binding.get().categoryViewAllTextView.setVisibility(View.VISIBLE);
                                }
                            }

                            break;

                        case ERROR:
                            // Error State

                            categoryViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (categoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        categoryViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        categoryViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(categoryViewModel.isLoading));


        /* CategoryRecyclerView*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*DiscountList*/


        homeSearchProductViewModel.setGetProductListByKeyObj(homeSearchProductViewModel.holder, loginUserId, String.valueOf(Config.HOME_PRODUCT_COUNT), String.valueOf(homeSearchProductViewModel.offset));
        LiveData<Resource<List<Product>>> discount = homeSearchProductViewModel.getGetProductListByKeyData();

        if (discount != null) {

            discount.observe(this, listResource -> {

                if (listResource != null) {
                    switch (listResource.status) {

                        case LOADING:

                            if (listResource.data != null) {
                                binding.get().discountTitleTextView.setVisibility(View.VISIBLE);
                                binding.get().viewAllDiscountTextView.setVisibility(View.VISIBLE);
                                SelectedShopFragment.this.replaceDiscountList(listResource.data);
                            }

                            break;

                        case SUCCESS:

                            if (listResource.data != null) {
                                binding.get().discountTitleTextView.setVisibility(View.VISIBLE);
                                binding.get().viewAllDiscountTextView.setVisibility(View.VISIBLE);
                                SelectedShopFragment.this.replaceDiscountList(listResource.data);

                                homeSearchProductViewModel.setLoadingState(false);
                            }

                            break;

                        case ERROR:

                            homeSearchProductViewModel.setLoadingState(false);
                            homeSearchProductViewModel.forceEndLoading = true;

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
                    Utils.psLog("Next Page State : " + state.data);

                    homeSearchProductViewModel.setLoadingState(false);
                    homeSearchProductViewModel.forceEndLoading = true;
                }
            }
        });

        homeSearchProductViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(homeSearchProductViewModel.isLoading));

        /*DiscountList*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*trendingList*/

        homeTrendingProductViewModel.setGetProductListByKeyObj(homeTrendingProductViewModel.productParameterHolder, loginUserId, String.valueOf(Config.HOME_PRODUCT_COUNT), String.valueOf(homeSearchProductViewModel.offset));

        LiveData<Resource<List<Product>>> trending = homeTrendingProductViewModel.getGetProductListByKeyData();

        if (trending != null) {

            trending.observe(this, listResource -> {

                if (listResource != null) {
                    switch (listResource.status) {

                        case LOADING:

                            binding.get().trendingTitleTextView.setVisibility(View.VISIBLE);
                            binding.get().viewAllTrendingTextView.setVisibility(View.VISIBLE);

                            replaceTrendingData(listResource.data);

                            break;

                        case SUCCESS:

                            binding.get().trendingTitleTextView.setVisibility(View.VISIBLE);
                            binding.get().viewAllTrendingTextView.setVisibility(View.VISIBLE);

                            replaceTrendingData(listResource.data);

                            homeTrendingProductViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            homeTrendingProductViewModel.setLoadingState(false);
                            homeTrendingProductViewModel.forceEndLoading = true;

                            break;

                        default:
                            break;
                    }
                }
            });
        }

        homeTrendingProductViewModel.getGetNextPageProductListByKeyData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    homeTrendingProductViewModel.setLoadingState(false);
                    homeTrendingProductViewModel.forceEndLoading = true;
                }
            }
        });

        homeTrendingProductViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(homeTrendingProductViewModel.isLoading);

//                if (loadingState != null && !loadingState) {
////                    binding.get().swipeRefresh.setRefreshing(false);
//                }

        });

        /*trendingList*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*trendingCategoryList*/

        homeTrendingCategoryListViewModel.categoryParameterHolder.shopId = selectedShopId;

        homeTrendingCategoryListViewModel.setHomeTrendingCatrgoryListDataObj(homeTrendingCategoryListViewModel.categoryParameterHolder, String.valueOf(Config.HOME_CATEGORY_COUNT), String.valueOf(homeTrendingCategoryListViewModel.offset));

        LiveData<Resource<List<Category>>> trendingCategories = homeTrendingCategoryListViewModel.getHomeTrendingCategoryListData();

        if (trendingCategories != null) {
            trendingCategories.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());

                                if (listResource.data.size() > 0) {

                                    if (listResource.data.size() < 5) {
                                        gridLayoutManager = new GridLayoutManager(getContext(), 1);
                                        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().trendingCategoryList.setLayoutManager(gridLayoutManager);
                                    } else {
                                        gridLayoutManager = new GridLayoutManager(getContext(), 2);
                                        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().trendingCategoryList.setLayoutManager(gridLayoutManager);
                                    }

                                    binding.get().trendingCategoriesTitleTextView.setVisibility(View.VISIBLE);
                                    binding.get().viewAllTrendingCategoriesTextView.setVisibility(View.VISIBLE);
                                    replaceTrendingCategoryData(listResource.data);
                                }
                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data

                                if (listResource.data.size() > 0) {
                                    if (listResource.data.size() < 5) {
                                        gridLayoutManager = new GridLayoutManager(getContext(), 1);
                                        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().trendingCategoryList.setLayoutManager(gridLayoutManager);
                                    } else {
                                        gridLayoutManager = new GridLayoutManager(getContext(), 2);
                                        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().trendingCategoryList.setLayoutManager(gridLayoutManager);
                                    }

                                    binding.get().trendingCategoriesTitleTextView.setVisibility(View.VISIBLE);
                                    binding.get().viewAllTrendingCategoriesTextView.setVisibility(View.VISIBLE);
                                    replaceTrendingCategoryData(listResource.data);
                                }
                            }

                            categoryViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            homeTrendingCategoryListViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (homeTrendingCategoryListViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        homeTrendingCategoryListViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        homeTrendingCategoryListViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(homeTrendingCategoryListViewModel.isLoading));
        /*trendingCategoryList*/

        /*Collection List*/

        productCollectionViewModel.setProductCollectionHeaderListForHomeObj( String.valueOf(Config.COLLECTION_PRODUCT_LIST_LIMIT), String.valueOf(Config.COLLECTION_PRODUCT_LIST_LIMIT),
                String.valueOf(Config.COLLECTION_PRODUCT_LIST_LIMIT), String.valueOf(homeTrendingCategoryListViewModel.offset), selectedShopId);

        LiveData<Resource<List<ProductCollectionHeader>>> productCollection = productCollectionViewModel.getProductCollectionHeaderListDataForHome();

        if (productCollection != null) {
            productCollection.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());

                                // Update the data

                                replaceCollection(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data

                                replaceCollection(listResource.data);
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

        productCollectionViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(productCollectionViewModel.isLoading));

        binding.get().categoryIconList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (binding.get() != null) {
                    if (binding.get().categoryIconList != null) {
                        if (binding.get().categoryIconList.getChildCount() > 0) {
                            layoutDone = true;
                            loadingCount++;
                            hideLoading();
                            binding.get().categoryIconList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            }
        });

        binding.get().viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                if (binding.get() != null && binding.get().viewPager != null) {
                    if (binding.get().viewPager.getChildCount() > 0) {
                        layoutDone = true;
                        loadingCount++;
                        hideLoading();
                        binding.get().viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });

        binding.get().discountList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (binding.get() != null && binding.get().discountList != null &&
                        binding.get().discountList.getChildCount() > 0) {
                    layoutDone = true;
                    loadingCount++;
                    hideLoading();
                    binding.get().discountList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

    }

    @Override
    public void onDispatched() {

        if (homeLatestProductViewModel.loadingDirection == Utils.LoadingDirection.top) {

            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().productList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }

        }

        if (homeSearchProductViewModel.loadingDirection == Utils.LoadingDirection.top) {

            GridLayoutManager layoutManager = (GridLayoutManager)
                    binding.get().discountList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }

        }

        if (homeTrendingProductViewModel.loadingDirection == Utils.LoadingDirection.top) {

            GridLayoutManager layoutManager = (GridLayoutManager)
                    binding.get().trendingList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }

        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setupSliderPagination() {

        dotsCount = viewPagerAdapter.get().getCount();

        if (dotsCount > 0 && dots == null) {

            dots = new ImageView[dotsCount];

            if (binding.get() != null) {
                if (binding.get().viewPagerCountDots.getChildCount() > 0) {
                    binding.get().viewPagerCountDots.removeAllViewsInLayout();
                }
            }

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(getContext());
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                binding.get().viewPagerCountDots.addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        }

    }

    private void hideLoading() {

        if (loadingCount == 3 && layoutDone) {

            binding.get().loadingView.setVisibility(View.GONE);
            binding.get().loadHolder.setVisibility(View.GONE);
        }
    }

    private void setShopTouchCount() {
        touchCountViewModel.setTouchCountPostDataObj(loginUserId, selectedShopId, Constants.SHOP, selectedShopId);

        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
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
        loadLoginUserId();
        super.onResume();
    }
}
