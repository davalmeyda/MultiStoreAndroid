package com.panaceasoft.psmultistore.ui.shop.tag.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemShopTagListAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.ShopTag;

import androidx.databinding.DataBindingUtil;

public class ShopTagListAdapter extends DataBoundListAdapter<ShopTag, ItemShopTagListAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ShopTagListAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;


    public ShopTagListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                              ShopTagListAdapter.NewsClickCallback callback,
                              DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemShopTagListAdapterBinding createBinding(ViewGroup parent) {
        ItemShopTagListAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_shop_tag_list_adapter, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {

            ShopTag shop = binding.getShopTag();
            if (shop != null && callback != null) {
                callback.onClick(shop);
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemShopTagListAdapterBinding> holder, int position) {
        super.bindView(holder, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemShopTagListAdapterBinding binding, ShopTag shopTag) {
        if (shopTag != null) {
            binding.setShopTag(shopTag);
        }
    }

    @Override
    protected boolean areItemsTheSame(ShopTag oldItem, ShopTag newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(ShopTag oldItem, ShopTag newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface NewsClickCallback {
        void onClick(ShopTag shop);
    }

}



