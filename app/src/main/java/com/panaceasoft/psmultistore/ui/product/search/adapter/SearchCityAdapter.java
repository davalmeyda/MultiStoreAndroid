package com.panaceasoft.psmultistore.ui.product.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemSearchCityBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.City;

public class SearchCityAdapter extends DataBoundListAdapter<City, ItemSearchCityBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final SearchCityAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private int lastPosition = -1;
    public String cityId = "";

    public SearchCityAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                             SearchCityAdapter.NewsClickCallback callback,
                             DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    public SearchCityAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                             SearchCityAdapter.NewsClickCallback callback, String cityId) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.cityId = cityId;
    }

    @Override
    protected ItemSearchCityBinding createBinding(ViewGroup parent) {
        ItemSearchCityBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_search_city, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {

            City city = binding.getCity();

            if (city != null && callback != null) {

                binding.groupview.setBackgroundColor(parent.getResources().getColor(R.color.md_green_50));

                callback.onClick(city, city.id);
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemSearchCityBinding> holder, int position) {
        super.bindView(holder, position);

    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemSearchCityBinding binding, City item) {
        binding.setCity(item);

        if (cityId != null) {
            if (item.id.equals(cityId)) {
                binding.groupview.setBackgroundColor(binding.groupview.getResources().getColor((R.color.md_green_50)));
            }
        }

    }

    @Override
    protected boolean areItemsTheSame(City oldItem, City newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    @Override
    protected boolean areContentsTheSame(City oldItem, City newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }

    public interface NewsClickCallback {
        void onClick(City City, String id);
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

