package com.panaceasoft.psmultistore.ui.shop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemShopFeaturedListAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.Shop;

import androidx.databinding.DataBindingUtil;

public class ShopFeaturedListAdapter extends DataBoundListAdapter<Shop, ItemShopFeaturedListAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ShopFeaturedListAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ShopFeaturedListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                   ShopFeaturedListAdapter.NewsClickCallback callback,
                                   DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }


    @Override
    protected ItemShopFeaturedListAdapterBinding createBinding(ViewGroup parent) {
        ItemShopFeaturedListAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_shop_featured_list_adapter, parent, false,
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
    public void bindView(DataBoundViewHolder<ItemShopFeaturedListAdapterBinding> holder, int position) {
        super.bindView(holder, position);

    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemShopFeaturedListAdapterBinding binding, Shop shop) {

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

//    private void setAnimation(View viewToAnimate, int position) {
//        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        } else {
//            lastPosition = position;
//        }
//    }
}


