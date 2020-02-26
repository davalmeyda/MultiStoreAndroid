package com.panaceasoft.psmultistore.ui.product.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemSearchSubCategoryAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.SubCategory;

import androidx.databinding.DataBindingUtil;

public class SearchSubCategoryAdapter extends DataBoundListAdapter<SubCategory, ItemSearchSubCategoryAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final SearchSubCategoryAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private int lastPosition = -1;
    private String subCatId;

    public SearchSubCategoryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                    SearchSubCategoryAdapter.NewsClickCallback callback,
                                    DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    public SearchSubCategoryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                    SearchSubCategoryAdapter.NewsClickCallback callback, String subCatId) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.subCatId = subCatId;
    }

    @Override
    protected ItemSearchSubCategoryAdapterBinding createBinding(ViewGroup parent) {
        ItemSearchSubCategoryAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_search_sub_category_adapter, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {
            SubCategory subCategory = binding.getSubCategory();
            if (subCategory != null && callback != null) {

                callback.onClick(subCategory);
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemSearchSubCategoryAdapterBinding> holder, int position) {
        super.bindView(holder, position);

    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemSearchSubCategoryAdapterBinding binding, SubCategory item) {
        binding.setSubCategory(item);

        if (subCatId != null) {
            if (item.id.equals(subCatId)) {
                binding.groupview.setBackgroundColor(binding.groupview.getResources().getColor((R.color.md_green_50)));
            }
        }

    }

    @Override
    protected boolean areItemsTheSame(SubCategory oldItem, SubCategory newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    @Override
    protected boolean areContentsTheSame(SubCategory oldItem, SubCategory newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    public interface NewsClickCallback {
        void onClick(SubCategory subCategory);
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
