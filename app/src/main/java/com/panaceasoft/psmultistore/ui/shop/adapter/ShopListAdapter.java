package com.panaceasoft.psmultistore.ui.shop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemShopListAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.Shop;

import androidx.databinding.DataBindingUtil;

public class ShopListAdapter extends DataBoundListAdapter<Shop, ItemShopListAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ShopListAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ShopListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                           ShopListAdapter.NewsClickCallback callback,
                           DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemShopListAdapterBinding createBinding(ViewGroup parent) {
        ItemShopListAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_shop_list_adapter, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Shop shop = binding.getShop();
            if (shop != null && callback != null) {
                callback.onClick(shop);
            }
        });
        return binding;
    }

    
    @Override
    public void bindView(DataBoundViewHolder<ItemShopListAdapterBinding> holder, int position) {
        super.bindView(holder, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemShopListAdapterBinding binding, Shop shop) {

        binding.setShop(shop);

    }

    @Override
    protected boolean areItemsTheSame(Shop oldItem, Shop newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(Shop oldItem, Shop newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface NewsClickCallback {
        void onClick(Shop shop);
    }


}


