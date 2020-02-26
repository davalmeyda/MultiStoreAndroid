package com.panaceasoft.psmultistore.ui.product.history.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemHistoryAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.HistoryProduct;

import androidx.databinding.DataBindingUtil;

public class HistoryAdapter extends DataBoundListAdapter<HistoryProduct, ItemHistoryAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private HistoryClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;

    public HistoryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, HistoryClickCallback historyClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = historyClickCallback;
    }

    @Override
    protected ItemHistoryAdapterBinding createBinding(ViewGroup parent) {
        ItemHistoryAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_history_adapter, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {
            HistoryProduct historyProduct = binding.getHistory();
            if (historyProduct != null && callback != null) {
                callback.onClick(historyProduct);
            }

        });


        return binding;
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemHistoryAdapterBinding binding, HistoryProduct item) {
        binding.setHistory(item);
        binding.historyNameTextView.setText(item.historyName);
    }

    @Override
    protected boolean areItemsTheSame(HistoryProduct oldItem, HistoryProduct newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.historyName.equals(newItem.historyName);
    }

    @Override
    protected boolean areContentsTheSame(HistoryProduct oldItem, HistoryProduct newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.historyName.equals(newItem.historyName);
    }

    public interface HistoryClickCallback {
        void onClick(HistoryProduct historyProduct);
    }
}
