package com.panaceasoft.psmultistore.ui.blog.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentBlogListBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.blog.list.adapter.BlogListAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.blog.BlogViewModel;
import com.panaceasoft.psmultistore.viewobject.Blog;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BlogListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private BlogViewModel blogViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentBlogListBinding> binding;
    private AutoClearedValue<BlogListAdapter> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentBlogListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

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

        binding.get().shopListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !blogViewModel.forceEndLoading) {

                            blogViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_NEW_FEED_COUNT;

                            blogViewModel.offset = blogViewModel.offset + limit;

                            blogViewModel.setLoadingState(true);

                            blogViewModel.setNextPageNewsFeedObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(blogViewModel.offset));
                        }
                    }
                }
            }
        });


        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            blogViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset

            blogViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            blogViewModel.forceEndLoading = false;

            blogViewModel.setNewsFeedObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(blogViewModel.offset));

            // update live data

        });

    }

    @Override
    protected void initViewModels() {

        blogViewModel = ViewModelProviders.of(this, viewModelFactory).get(BlogViewModel.class);

    }

    @Override
    protected void initAdapters() {

        BlogListAdapter nvAdapter = new BlogListAdapter(dataBindingComponent, newsFeed -> navigationController.navigateToBlogDetailActivity(BlogListFragment.this.getActivity(), newsFeed.id), this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().shopListRecyclerView.setAdapter(adapter.get());

    }

    @Override
    protected void initData() {

        blogViewModel.setNewsFeedObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(blogViewModel.offset));

        blogViewModel.getNewsFeedData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceNewsFeedList(result.data);
                        blogViewModel.setLoadingState(false);
                        break;

                    case LOADING:
                        replaceNewsFeedList(result.data);
                        break;

                    case ERROR:

                        blogViewModel.setLoadingState(false);
                        break;
                }
            }

        });

        blogViewModel.getNextPageNewsFeedData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {

                    blogViewModel.setLoadingState(false);
                    blogViewModel.forceEndLoading = true;
                }
            }
        });


        blogViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(blogViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    private void replaceNewsFeedList(List<Blog> blogs) {
        this.adapter.get().replace(blogs);
        binding.get().executePendingBindings();
    }


    @Override
    public void onDispatched() {
        if (blogViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().shopListRecyclerView != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().shopListRecyclerView.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }
}
