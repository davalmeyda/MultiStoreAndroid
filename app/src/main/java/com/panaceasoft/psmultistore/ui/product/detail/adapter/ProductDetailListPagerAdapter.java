package com.panaceasoft.psmultistore.ui.product.detail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemProductDetailListPagerAdapterBinding;
import com.panaceasoft.psmultistore.viewobject.Image;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ProductDetailListPagerAdapter extends PagerAdapter {

    private List<Image> images;

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private ImageClickCallback callback;

    public ProductDetailListPagerAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                         ImageClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    @Override
    public int getCount() {
        if (images == null) {
            return 0;
        } else {
            return images.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ItemProductDetailListPagerAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(container.getContext()),
                        R.layout.item_product_detail_list_pager_adapter, container, false,
                        dataBindingComponent);
        binding.setImg(images.get(position));

        container.addView(binding.getRoot());

        binding.getRoot().setOnClickListener(view -> {
            Image image = binding.getImg();
            if (image != null && callback != null) {
                callback.onItemClick(view, image, position);
            }
        });

        return binding.getRoot();
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    public void setImageList(List<Image> imageList) {
        this.images = imageList;
        this.notifyDataSetChanged();
    }

    public interface ImageClickCallback {
        void onItemClick(View view, Image obj, int position);
    }


}
