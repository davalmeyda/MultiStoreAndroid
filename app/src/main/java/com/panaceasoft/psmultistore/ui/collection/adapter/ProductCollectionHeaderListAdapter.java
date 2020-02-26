package com.panaceasoft.psmultistore.ui.collection.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemProductCollectionHeaderListAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.ProductCollectionHeader;

import androidx.databinding.DataBindingUtil;

/**
 * Created by Panacea-Soft on 10/27/18.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class ProductCollectionHeaderListAdapter extends DataBoundListAdapter<ProductCollectionHeader, ItemProductCollectionHeaderListAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ProductCollectionHeaderClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    private int lastPosition = -1;

    public ProductCollectionHeaderListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                              ProductCollectionHeaderClickCallback callback,
                                              DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemProductCollectionHeaderListAdapterBinding createBinding(ViewGroup parent) {
        ItemProductCollectionHeaderListAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_product_collection_header_list_adapter, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            ProductCollectionHeader productCollectionHeader = binding.getProductCollectionHeader();
            if (productCollectionHeader != null && callback != null) {
                callback.onClick(productCollectionHeader);
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemProductCollectionHeaderListAdapterBinding> holder, int position) {
        super.bindView(holder, position);

        setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemProductCollectionHeaderListAdapterBinding binding, ProductCollectionHeader item) {
        binding.setProductCollectionHeader(item);

        binding.newsTitleTextView.setText(item.name);

    }

    @Override
    protected boolean areItemsTheSame(ProductCollectionHeader oldItem, ProductCollectionHeader newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(ProductCollectionHeader oldItem, ProductCollectionHeader newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface ProductCollectionHeaderClickCallback {
        void onClick(ProductCollectionHeader productCollectionHeader);
    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else {
            lastPosition = position;
        }
    }
}
