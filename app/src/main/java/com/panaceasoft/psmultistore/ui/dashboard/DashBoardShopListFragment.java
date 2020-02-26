package com.panaceasoft.psmultistore.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentDashboardShopListBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.shop.adapter.ShopFeaturedListAdapter;
import com.panaceasoft.psmultistore.ui.shop.adapter.ShopListAdapter;
import com.panaceasoft.psmultistore.ui.shop.adapter.ShopTagIconListAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.blog.BlogViewModel;
import com.panaceasoft.psmultistore.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.psmultistore.viewmodel.apploading.AppLoadingViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.FeaturedShopViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.LatestShopViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.PopularShopViewModel;
import com.panaceasoft.psmultistore.viewmodel.shoptag.ShopTagViewModel;
import com.panaceasoft.psmultistore.viewobject.PSAppInfo;
import com.panaceasoft.psmultistore.viewobject.Shop;
import com.panaceasoft.psmultistore.viewobject.ShopTag;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class DashBoardShopListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private FeaturedShopViewModel featuredShopViewModel;
    private LatestShopViewModel latestShopViewModel;
    private PopularShopViewModel popularShopViewModel;
    private ShopTagViewModel shopTagViewModel;
    private BlogViewModel blogViewModel;
    private AppLoadingViewModel psAppInfoViewModel;
    private ClearAllDataViewModel clearAllDataViewModel;

    private ImageView[] dots;
    private Handler handler = new Handler();
    private Runnable update;
    private int NUM_PAGES = 10;
    private int currentPage = 0;
    private boolean touched = false;
    private Timer unTouchedTimer;
    private PSDialogMsg psDialogMsg;

    @Inject
    protected SharedPreferences pref;

    private String startDate = Constants.ZERO;
    private String endDate = Constants.ZERO;

    @VisibleForTesting
    private AutoClearedValue<FragmentDashboardShopListBinding> binding;
    private AutoClearedValue<ShopTagIconListAdapter> shopCategoryAdapter;
    private AutoClearedValue<ShopListAdapter> shopRecentAdapter;
    private AutoClearedValue<ShopListAdapter> shopPopularAdapter;
    private AutoClearedValue<ShopFeaturedListAdapter> shopFeaturedAdapter;
    private AutoClearedValue<DashBoardViewPagerAdapter> dashBoardViewPagerAdapter;
    private AutoClearedValue<LinearLayout> pageIndicatorLayout;
    private AutoClearedValue<ViewPager> viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentDashboardShopListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard_shop_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        loadDates();

        return binding.get().getRoot();
    }

    private void loadDates() {
        try {

            if (getActivity() != null && getActivity().getBaseContext() != null) {
                startDate = pref.getString(Constants.SHOP_START_DATE, Constants.ZERO);
                endDate = pref.getString(Constants.SHOP_END_DATE, Constants.ZERO);
            }

        } catch (NullPointerException ne) {
            Utils.psErrorLog("Null Pointer Exception.", ne);
        } catch (Exception e) {
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }
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

        viewPager = new AutoClearedValue<>(this, binding.get().viewPager);

        pageIndicatorLayout = new AutoClearedValue<>(this, binding.get().pagerIndicator);

        if (viewPager.get() != null && viewPager.get() != null && viewPager.get() != null) {
            viewPager.get().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    currentPage = position;

                    if (pageIndicatorLayout != null) {
                        // setupSliderPagination(binding.getRoot());
                        setupSliderPagination();
                    }

                    for (ImageView dot : dots) {
                        if (dots != null) {
                            dot.setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                        }
                    }

                    if (dots != null && dots.length > position) {
                        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    }

                    touched = true;

                    handler.removeCallbacks(update);

                    setUnTouchedTimer();

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        startPagerAutoSwipe();

        binding.get().featuredShopViewAllTextView.setOnClickListener(v -> navigationController.navigateToSelectedShopList(DashBoardShopListFragment.this.getActivity(), featuredShopViewModel.shopParameterHolder.getFeaturedParameterHolder(), DashBoardShopListFragment.this.getString(R.string.shop_featured_shops)));

        binding.get().newShopsViewAllTextView.setOnClickListener(v -> navigationController.navigateToSelectedShopList(DashBoardShopListFragment.this.getActivity(), latestShopViewModel.shopParameterHolder.getLatestParameterHolder(), DashBoardShopListFragment.this.getString(R.string.shop_new_shops)));

        binding.get().popularShopsViewAllTextView.setOnClickListener(v -> navigationController.navigateToSelectedShopList(DashBoardShopListFragment.this.getActivity(), popularShopViewModel.shopParameterHolder.getPopularParameterHolder(), DashBoardShopListFragment.this.getString(R.string.shop_popular_shops)));

        binding.get().blogViewAllTextView.setOnClickListener(v -> navigationController.navigateToBlogList(getActivity()));

        binding.get().viewAllSliderTextView.setOnClickListener(v -> navigationController.navigateToShopCategoryViewAllActivity(getActivity()));

        if (force_update) {
            navigationController.navigateToForceUpdateActivity(this.getActivity(),force_update_title ,force_update_msg);
        }
    }

    private void setupSliderPagination() {

        int dotsCount = dashBoardViewPagerAdapter.get().getCount();


        if (dotsCount > 0) {

            dots = new ImageView[dotsCount];

            if (pageIndicatorLayout != null) {
                if (pageIndicatorLayout.get().getChildCount() > 0) {
                    pageIndicatorLayout.get().removeAllViewsInLayout();
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

                pageIndicatorLayout.get().addView(dots[i], params);
            }

            int currentItem = viewPager.get().getCurrentItem();
            if (currentItem > 0 && currentItem < dots.length) {
                dots[currentItem].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            } else {
                dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

        }

    }

    @Override
    protected void initViewModels() {
        featuredShopViewModel = ViewModelProviders.of(this, viewModelFactory).get(FeaturedShopViewModel.class);
        shopTagViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopTagViewModel.class);
        latestShopViewModel = ViewModelProviders.of(this, viewModelFactory).get(LatestShopViewModel.class);
        popularShopViewModel = ViewModelProviders.of(this, viewModelFactory).get(PopularShopViewModel.class);
        blogViewModel = ViewModelProviders.of(this, viewModelFactory).get(BlogViewModel.class);
        psAppInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(AppLoadingViewModel.class);
        clearAllDataViewModel = ViewModelProviders.of(this, viewModelFactory).get(ClearAllDataViewModel.class);
    }

    @Override
    protected void initAdapters() {
        ShopTagIconListAdapter nvAdapter = new ShopTagIconListAdapter(dataBindingComponent, category ->
                navigationController.navigateToShopCategoryDetailActivity(this.getActivity(), category.id, category.name));

        this.shopCategoryAdapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().shopCategoryRecyclerView.setAdapter(nvAdapter);

        DashBoardViewPagerAdapter nvAdapter3 = new DashBoardViewPagerAdapter(dataBindingComponent, blog -> navigationController.navigateToBlogDetailActivity(getActivity(), blog.id));

        this.dashBoardViewPagerAdapter = new AutoClearedValue<>(this, nvAdapter3);
        viewPager.get().setAdapter(dashBoardViewPagerAdapter.get());

        ShopListAdapter nvAdapter1 = new ShopListAdapter(dataBindingComponent, shop -> navigationController.navigateToSelectedShopDetail(this.getActivity(), shop.id, shop.name), this);

        this.shopRecentAdapter = new AutoClearedValue<>(this, nvAdapter1);
        binding.get().newShopRecyclerView.setAdapter(nvAdapter1);

        ShopListAdapter nvAdapter4 = new ShopListAdapter(dataBindingComponent, shop ->
                navigationController.navigateToSelectedShopDetail(this.getActivity(), shop.id, shop.name), this);

        this.shopPopularAdapter = new AutoClearedValue<>(this, nvAdapter4);
        binding.get().popularShopsRecyclerView.setAdapter(nvAdapter4);

        ShopFeaturedListAdapter nvAdapter2 = new ShopFeaturedListAdapter(dataBindingComponent, shop -> navigationController.navigateToSelectedShopDetail(this.getActivity(), shop.id, shop.name), this);

        this.shopFeaturedAdapter = new AutoClearedValue<>(this, nvAdapter2);
        binding.get().featuredShopRecyclerView.setAdapter(nvAdapter2);

    }

    @Override
    protected void initData() {
        if (connectivity.isConnected()) {
            if (startDate.equals(Constants.ZERO)) {

                startDate = getDateTime();
                Utils.setDatesToShared(startDate, endDate, pref);
            }

        }
//        else {

//            if (!Config.APP_VERSION.equals(versionNo) && !force_update) {
//                psDialogMsg.showInfoDialog(getString(R.string.force_update_true), getString(R.string.app__ok));
//                psDialogMsg.show();
//            }
//        }

        clearAllDataViewModel.getDeleteAllDataData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case ERROR:
                        break;

                    case SUCCESS:
                        break;
                }
            }
        });

        blogViewModel.setNewsFeedObj(String.valueOf(Config.LIST_NEW_FEED_COUNT_PAGER), String.valueOf(blogViewModel.offset));
        blogViewModel.getNewsFeedData().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {

                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {

                            // Update the data
                            dashBoardViewPagerAdapter.get().replaceNewsFeedList(listResource.data);

                        }

                        break;

                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            // Update the data

                            dashBoardViewPagerAdapter.get().replaceNewsFeedList(listResource.data);

                        }

                        blogViewModel.setLoadingState(false);

                        break;

                    case ERROR:
                        // Error State

                        blogViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

                if (blogViewModel.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    blogViewModel.forceEndLoading = true;
                }

            }

        });

        shopTagViewModel.setShopTagObj(String.valueOf(Config.LIST_CATEGORY_COUNT), String.valueOf(shopTagViewModel.offset));
        shopTagViewModel.getShopTagData().observe(this, (Resource<List<ShopTag>> listResource) -> {

            if (listResource != null) {

                switch (listResource.status) {

                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {

                            // Update the data
                            replaceCategory(listResource.data);

                        }

                        break;

                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            // Update the data
                            replaceCategory(listResource.data);

                        }

                        shopTagViewModel.setLoadingState(false);

                        break;

                    case ERROR:
                        // Error State

                        shopTagViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

                if (shopTagViewModel.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    shopTagViewModel.forceEndLoading = true;
                }

            }

        });

        featuredShopViewModel.setFeaturedShopListObj(Config.API_KEY, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(featuredShopViewModel.offset), featuredShopViewModel.shopParameterHolder.getFeaturedParameterHolder());

        featuredShopViewModel.getFeaturedShopListData().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {

                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {

                            // Update the data
                            replaceFeaturedShopList(listResource.data);

                        }

                        break;

                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            // Update the data
                            replaceFeaturedShopList(listResource.data);

                        }

                        featuredShopViewModel.setLoadingState(false);

                        break;

                    case ERROR:
                        // Error State

                        featuredShopViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

                if (featuredShopViewModel.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    featuredShopViewModel.forceEndLoading = true;
                }

            }

        });

        latestShopViewModel.setLatestShopListObj(Config.API_KEY, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(featuredShopViewModel.offset), latestShopViewModel.shopParameterHolder.getLatestParameterHolder());
        latestShopViewModel.getLatestShopListData().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {

                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {

                            // Update the data
                            replaceRecentShopList(listResource.data);

                        }

                        break;

                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            // Update the data
                            replaceRecentShopList(listResource.data);

                        }

                        latestShopViewModel.setLoadingState(false);

                        break;

                    case ERROR:
                        // Error State

                        latestShopViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

                if (latestShopViewModel.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    latestShopViewModel.forceEndLoading = true;
                }

            }

        });

        popularShopViewModel.setPopularShopListObj(Config.API_KEY, String.valueOf(Config.LIST_SHOP_COUNT), String.valueOf(featuredShopViewModel.offset), popularShopViewModel.shopParameterHolder.getPopularParameterHolder());
        popularShopViewModel.getPopularShopListData().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {

                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {

                            // Update the data
                            replacePopularShopList(listResource.data);

                        }

                        break;

                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            // Update the data
                            replacePopularShopList(listResource.data);

                        }

                        popularShopViewModel.setLoadingState(false);

                        break;

                    case ERROR:
                        // Error State

                        popularShopViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

                if (popularShopViewModel.offset > 1) {
                    // No more data for this list
                    // So, Block all future loading
                    popularShopViewModel.forceEndLoading = true;
                }

            }

        });
    }

    private void replaceCategory(List<ShopTag> categories) {
        this.shopCategoryAdapter.get().replaceCategories(categories);
        binding.get().executePendingBindings();
    }

    private void replaceRecentShopList(List<Shop> shops) {
        this.shopRecentAdapter.get().replace(shops);
        binding.get().executePendingBindings();
    }

    private void replacePopularShopList(List<Shop> shops) {
        this.shopPopularAdapter.get().replace(shops);
        binding.get().executePendingBindings();
    }

    private void replaceFeaturedShopList(List<Shop> shops) {
        this.shopFeaturedAdapter.get().replace(shops);
        binding.get().executePendingBindings();
    }

    @Override
    public void onDispatched() {

    }


    private void startPagerAutoSwipe() {

        update = () -> {
            if (!touched) {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }

                if (viewPager.get() != null) {
                    viewPager.get().setCurrentItem(currentPage++, true);
                }

            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 3000);
    }

    private void setUnTouchedTimer() {

        if (unTouchedTimer == null) {
            unTouchedTimer = new Timer();
            unTouchedTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    touched = false;

                    handler.post(update);
                }
            }, 3000, 6000);
        } else {
            unTouchedTimer.cancel();
            unTouchedTimer.purge();

            unTouchedTimer = new Timer();
            unTouchedTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    touched = false;

                    handler.post(update);
                }
            }, 3000, 6000);
        }
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
        Date date = new Date();
        return dateFormat.format(date);
    }
}

