package com.panaceasoft.psmultistore.ui.product.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemSearchCountryBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.Country;

public class SearchCountryAdapter extends DataBoundListAdapter<Country, ItemSearchCountryBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final SearchCountryAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private int lastPosition = -1;
    public String countryId = "";

    public SearchCountryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                SearchCountryAdapter.NewsClickCallback callback,
                                DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    public SearchCountryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                SearchCountryAdapter.NewsClickCallback callback, String countryId) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.countryId = countryId;
    }

    @Override
    protected ItemSearchCountryBinding createBinding(ViewGroup parent) {
        ItemSearchCountryBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_search_country, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {

            Country country = binding.getCountry();

            if (country != null && callback != null) {

                binding.groupview.setBackgroundColor(parent.getResources().getColor(R.color.md_green_50));

                callback.onClick(country, country.id);
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemSearchCountryBinding> holder, int position) {
        super.bindView(holder, position);

    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemSearchCountryBinding binding, Country item) {
        binding.setCountry(item);

        if (countryId != null) {
            if (item.id.equals(countryId)) {
                binding.groupview.setBackgroundColor(binding.groupview.getResources().getColor((R.color.md_green_50)));
            }
        }

    }

    @Override
    protected boolean areItemsTheSame(Country oldItem, Country newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    @Override
    protected boolean areContentsTheSame(Country oldItem, Country newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    public interface NewsClickCallback {
        void onClick(Country Country, String id);
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
