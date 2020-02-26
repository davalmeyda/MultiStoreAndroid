package com.panaceasoft.psmultistore.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentCategoryListBinding;
import com.panaceasoft.psmultistore.ui.category.adapter.CategoryAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeTrendingCategoryListViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewobject.Category;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TrendingCategoryFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private HomeTrendingCategoryListViewModel homeTrendingCategoryListViewModel;
    private BasketViewModel basketViewModel;
    private MenuItem basketMenuItem;

    @VisibleForTesting
    private AutoClearedValue<FragmentCategoryListBinding> binding;
    private AutoClearedValue<CategoryAdapter> adapter;

    //endregion


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCategoryListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        Utils.psLog("I am Trending Category");

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
        binding.get().categoryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !homeTrendingCategoryListViewModel.forceEndLoading) {

                            if (connectivity.isConnected()) {

                                homeTrendingCategoryListViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.LIST_CATEGORY_COUNT;
                                homeTrendingCategoryListViewModel.offset = homeTrendingCategoryListViewModel.offset + limit;

                                homeTrendingCategoryListViewModel.setHomeTrendingCategoryLoadNetworkObj(loginUserId, String.valueOf(Config.LIST_CATEGORY_COUNT),
                                        String.valueOf(homeTrendingCategoryListViewModel.offset), homeTrendingCategoryListViewModel.categoryParameterHolder);
                            }
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            homeTrendingCategoryListViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset homeTrendingCategoryListViewModel.offset
            homeTrendingCategoryListViewModel.offset = 0;

            // reset categoryViewMdoel.forceEndLoading
            homeTrendingCategoryListViewModel.forceEndLoading = false;

            // update live data
            homeTrendingCategoryListViewModel.setHomeTrendingCatrgoryListDataObj(homeTrendingCategoryListViewModel.categoryParameterHolder, String.valueOf(Config.LIST_CATEGORY_COUNT), String.valueOf(homeTrendingCategoryListViewModel.offset));

        });
    }

    @Override
    protected void initViewModels() {
        homeTrendingCategoryListViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeTrendingCategoryListViewModel.class);
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);

    }

    @Override
    protected void initAdapters() {
        CategoryAdapter nvAdapter = new CategoryAdapter(dataBindingComponent,
                category -> {
                    homeTrendingCategoryListViewModel.productParameterHolder.catId = category.id;
                    navigationController.navigateToHomeFilteringActivity(getActivity(), homeTrendingCategoryListViewModel.productParameterHolder, null);
                }, this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().categoryList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        loadCategory();
        basketData();
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

    //region Private Methods

    private void loadCategory() {

        homeTrendingCategoryListViewModel.categoryParameterHolder.shopId = selectedShopId;

        // Load Category List
        homeTrendingCategoryListViewModel.setHomeTrendingCatrgoryListDataObj(homeTrendingCategoryListViewModel.categoryParameterHolder, String.valueOf(Config.LIST_CATEGORY_COUNT), String.valueOf(homeTrendingCategoryListViewModel.offset));

        LiveData<Resource<List<Category>>> news = homeTrendingCategoryListViewModel.getHomeTrendingCategoryListData();

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

                            homeTrendingCategoryListViewModel.setLoadingState(false);

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

        homeTrendingCategoryListViewModel.getHomeTrendingCategoryLoadNetworkData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    homeTrendingCategoryListViewModel.setLoadingState(false);
                    homeTrendingCategoryListViewModel.forceEndLoading = true;
                }
            }
        });

        homeTrendingCategoryListViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(homeTrendingCategoryListViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    private void replaceData(List<Category> categoryList) {

        adapter.get().replace(categoryList);
        binding.get().executePendingBindings();

    }


    @Override
    public void onDispatched() {
        if (homeTrendingCategoryListViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().categoryList != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().categoryList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }
}
