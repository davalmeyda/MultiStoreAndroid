package com.panaceasoft.psmultistore.ui.comment.detail;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentCommentDetailBinding;
import com.panaceasoft.psmultistore.ui.comment.detail.adapter.CommentDetailAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.comment.CommentDetailListViewModel;
import com.panaceasoft.psmultistore.viewobject.CommentDetail;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.List;

/**
 * Created by Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */
public class CommentDetailFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private CommentDetailListViewModel commentDetailListViewModel;

    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private
    AutoClearedValue<FragmentCommentDetailBinding> binding;
    private AutoClearedValue<CommentDetailAdapter> adapter;
    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCommentDetailBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment_detail, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {
        if (commentDetailListViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().commentList != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().commentList.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
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

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);


        // Set reverse layout
        LinearLayoutManager layoutManager = (LinearLayoutManager)
                binding.get().commentList.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.setReverseLayout(true);


            binding.get().commentList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager)
                            recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int lastPosition = layoutManager
                                .findLastVisibleItemPosition();
                        if (lastPosition == adapter.get().getItemCount() - 1) {

                            if (!binding.get().getLoadingMore() && !commentDetailListViewModel.forceEndLoading) {

                                if (connectivity.isConnected()) {

                                    commentDetailListViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                    int limit = Config.COMMENT_COUNT;
                                    commentDetailListViewModel.offset = commentDetailListViewModel.offset + limit;

                                    commentDetailListViewModel.setNextPageLoadingCommentDetailObj(String.valueOf(commentDetailListViewModel.offset), commentDetailListViewModel.commentId);
                                }
                            }
                        }
                    }

                }
            });
        }

//        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
//        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
//        binding.get().swipeRefresh.setOnRefreshListener(() -> {
//
//            commentDetailListViewModel.loadingDirection = Utils.LoadingDirection.top;
//
//            // reset productViewModel.offset
//            commentDetailListViewModel.offset = 0;
//
//            // reset productViewModel.forceEndLoading
//            commentDetailListViewModel.forceEndLoading = false;
//
//            // update live data
//            commentDetailListViewModel.setCommentDetailListObj(String.valueOf(commentDetailListViewModel.offset), commentDetailListViewModel.commentId);
//
//        });

        binding.get().sendImageButton.setOnClickListener(view -> {

            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, this::sendComment);

        });

    }

    @Override
    protected void initViewModels() {
        commentDetailListViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentDetailListViewModel.class);
    }

    @Override
    protected void initAdapters() {
        CommentDetailAdapter nvAdapter = new CommentDetailAdapter(dataBindingComponent, comment -> {
        }, this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().commentList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    commentDetailListViewModel.commentId = getActivity().getIntent().getExtras().getString(Constants.COMMENT_ID);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

        loadCommentDetail();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();

    }

    private void loadCommentDetail() {
        // Load Latest Product
        commentDetailListViewModel.setCommentDetailListObj(String.valueOf(commentDetailListViewModel.offset), commentDetailListViewModel.commentId);

        LiveData<Resource<List<CommentDetail>>> news = commentDetailListViewModel.getCommentDetailListData();

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

                            commentDetailListViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            commentDetailListViewModel.setLoadingState(false);


                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (commentDetailListViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        commentDetailListViewModel.forceEndLoading = true;
                    }

                }

            });
        }
        commentDetailListViewModel.getNextPageCommentDetailLoadingData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    commentDetailListViewModel.setLoadingState(false);//hide
                    commentDetailListViewModel.forceEndLoading = true;//stop
                }
            }
        });

        commentDetailListViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(commentDetailListViewModel.isLoading);

//            if (loadingState != null && !loadingState) {
//                binding.get().swipeRefresh.setRefreshing(false);
//            }

        });

        commentDetailListViewModel.getsendCommentDetailPostData().observe(this, result -> {
            prgDialog.get().cancel();
            if (result != null) {
                Utils.psLog("Got Data");
                if (result.status == Status.SUCCESS) {
                    commentDetailListViewModel.loadingDirection = Utils.LoadingDirection.top;
                    onDispatched();

                    binding.get().editText.setText("");
                    commentDetailListViewModel.setLoadingState(false);

                    if (commentDetailListViewModel.getCommentDetailListData().getValue() == null || commentDetailListViewModel.getCommentDetailListData().getValue().data == null ||
                            commentDetailListViewModel.getCommentDetailListData().getValue().data.size() <= 1) {
                        commentDetailListViewModel.offset = 0;

                        // reset productViewModel.forceEndLoading
                        commentDetailListViewModel.forceEndLoading = false;

                        // update live data
                        commentDetailListViewModel.setCommentDetailListObj(String.valueOf(commentDetailListViewModel.offset), commentDetailListViewModel.commentId);
                    }

                    if (getActivity() != null) {
                        navigationController.navigateBackToCommentListFragment(getActivity(), commentDetailListViewModel.commentId);
                    }
                } else if (result.status == Status.ERROR) {
                    commentDetailListViewModel.setLoadingState(false);
                }

            } else {

                psDialogMsg.showInfoDialog(getString(R.string.error), getString(R.string.app__ok));
                psDialogMsg.show();

            }
        });

    }

    private void sendComment() {

        String description = binding.get().editText.getText().toString();

        if (description.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.comment__empty_comment), getString(R.string.app__ok));
            psDialogMsg.show();

        } else {

            prgDialog.get().show();

            commentDetailListViewModel.setSendCommentDetailPostDataObj(commentDetailListViewModel.commentId, loginUserId, description, selectedShopId);

        }
    }

    private void replaceData(List<com.panaceasoft.psmultistore.viewobject.CommentDetail> commentList) {
        adapter.get().replace(commentList);
        binding.get().executePendingBindings();

    }
}
