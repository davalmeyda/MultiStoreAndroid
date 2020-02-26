package com.panaceasoft.psmultistore.ui.blog.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentBlogDetailBinding;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.viewmodel.blog.BlogViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class BlogDetailFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private BlogViewModel blogViewModel;
    private String blogId;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentBlogDetailBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentBlogDetailBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog_detail, container, false, dataBindingComponent);

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

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().titleTextView.setOnClickListener(v -> navigationController.navigateToSelectedShopDetail(getActivity(), blogViewModel.shopId, blogViewModel.shopName));

        binding.get().emailTextView.setOnClickListener(v -> navigationController.navigateToSelectedShopDetail(getActivity(), blogViewModel.shopId, blogViewModel.shopName));

        binding.get().phoneTextView.setOnClickListener(v -> navigationController.navigateToSelectedShopDetail(getActivity(), blogViewModel.shopId, blogViewModel.shopName));

        binding.get().aboutDefaultIcon.setOnClickListener(v -> navigationController.navigateToSelectedShopDetail(getActivity(), blogViewModel.shopId, blogViewModel.shopName));

    }

    @Override
    protected void initViewModels() {

        blogViewModel = ViewModelProviders.of(this, viewModelFactory).get(BlogViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            blogId = getActivity().getIntent().getStringExtra(Constants.BLOG_ID);
        }

        if (blogId != null && !blogId.isEmpty()) {
            blogViewModel.setBlogByIdObj(blogId, selectedShopId);

            blogViewModel.getBlogByIdData().observe(this, result -> {

                if (result != null) {
                    if (result.data != null) {
                        switch (result.status) {

                            case LOADING:
                                binding.get().setBlog(result.data);
                                blogViewModel.shopName = result.data.shop.name;
                                blogViewModel.shopId = result.data.shopId;
                                break;

                            case SUCCESS:
                                binding.get().setBlog(result.data);
                                blogViewModel.shopName = result.data.shop.name;
                                blogViewModel.shopId = result.data.shopId;
                                break;

                            case ERROR:
                                psDialogMsg.showErrorDialog(getString(R.string.blog_detail__error_message), getString(R.string.app__ok));
                                psDialogMsg.show();
                                break;


                        }
                    }
                }
            });
        }

    }
}
