package com.panaceasoft.psmultistore.ui.product.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentHistoryBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.history.adapter.HistoryAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.product.HistoryProductViewModel;
import com.panaceasoft.psmultistore.viewobject.HistoryProduct;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private HistoryProductViewModel historyProductViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentHistoryBinding> binding;
    private AutoClearedValue<HistoryAdapter> historyAdapter;

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentHistoryBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);


        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {
        if (historyProductViewModel.loadingDirection == Utils.LoadingDirection.bottom) {

            if (binding.get().historyRecycler != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().historyRecycler.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }

    @Override
    protected void initUIAndActions() {

        binding.get().historyRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == historyAdapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !historyProductViewModel.forceEndLoading) {

                            historyProductViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.COMMENT_COUNT;
                            historyProductViewModel.offset = historyProductViewModel.offset + limit;
                            historyProductViewModel.setHistoryProductListObj(String.valueOf(historyProductViewModel.offset));
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void initViewModels() {
        historyProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HistoryProductViewModel.class);
    }

    @Override
    protected void initAdapters() {
        HistoryAdapter historyAdapter = new HistoryAdapter(dataBindingComponent,
                historyProduct -> navigationController.navigateToProductDetailActivity(HistoryFragment.this.getActivity(), historyProduct));
        this.historyAdapter = new AutoClearedValue<>(this, historyAdapter);
        binding.get().historyRecycler.setAdapter(historyAdapter);
    }

    @Override
    protected void initData() {
        loadData();
    }

    private void loadData() {

        //load basket
        historyProductViewModel.offset = Config.COMMENT_COUNT;
        historyProductViewModel.setHistoryProductListObj(String.valueOf(historyProductViewModel.offset));
        LiveData<List<HistoryProduct>> historyProductList = historyProductViewModel.getAllHistoryProductList();
        if (historyProductList != null) {
            historyProductList.observe(this, listResource -> {
                if (listResource != null) {

                    replaceProductHistoryData(listResource);

                }

            });
        }


        historyProductViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(historyProductViewModel.isLoading));

    }

    private void replaceProductHistoryData(List<HistoryProduct> historyProductList) {
        historyAdapter.get().replace(historyProductList);
        binding.get().executePendingBindings();

    }
}
