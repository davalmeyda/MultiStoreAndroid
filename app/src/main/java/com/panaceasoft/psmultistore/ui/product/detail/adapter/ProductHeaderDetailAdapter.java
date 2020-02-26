package com.panaceasoft.psmultistore.ui.product.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.SpinnerHeaderDetailBinding;
import com.panaceasoft.psmultistore.viewobject.ProductAttributeDetail;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class ProductHeaderDetailAdapter extends ArrayAdapter<ProductAttributeDetail> {
    private final List<ProductAttributeDetail> items;
    private final androidx.databinding.DataBindingComponent dataBindingComponent;

    ProductHeaderDetailAdapter(@NonNull Context context, @LayoutRes int resource,
                               @NonNull List<ProductAttributeDetail> objects, androidx.databinding.DataBindingComponent dataBindingComponent) {

        super(context, resource, 0, objects);
        items = objects;
        this.dataBindingComponent = dataBindingComponent;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, parent);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ProductAttributeDetail getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent);
    }

    private View createItemView(int position, ViewGroup parent) {

        SpinnerHeaderDetailBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.spinner_header_detail
                        , parent, false,
                        dataBindingComponent);

        bind(binding, getItem(position));
        return binding.getRoot();
    }

    protected void bind(SpinnerHeaderDetailBinding binding, ProductAttributeDetail item) {

        binding.setHeaderDetailProduct(item);

    }

    public interface ProductHeaderDetailClickCallBack {
        void onClick(ProductAttributeDetail productAttributeDetail);

    }


}
