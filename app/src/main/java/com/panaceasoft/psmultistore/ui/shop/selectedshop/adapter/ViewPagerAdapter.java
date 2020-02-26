package com.panaceasoft.psmultistore.ui.shop.selectedshop.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemViewPagerAdapterBinding;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    private List<Product> featuredProducts;
    private androidx.databinding.DataBindingComponent dataBindingComponent;
    private ItemClick callback;

    public ViewPagerAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, ItemClick callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    public void replaceFeaturedList(List<Product> featuredProductList) {
        this.featuredProducts = featuredProductList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if (featuredProducts != null && featuredProducts.size() != 0) {
            return featuredProducts.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ItemViewPagerAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.item_view_pager_adapter, container, false, dataBindingComponent);

        binding.setProduct(featuredProducts.get(position));

        if (featuredProducts.get(position).isDiscount.equals(Constants.ONE)) {

            changeVisibilityOfDiscountTextView(binding, View.VISIBLE);
            binding.oldDiscountPriceTextView.setPaintFlags(binding.oldDiscountPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            int discountValue = (int) featuredProducts.get(position).discountPercent;
            String discountValueStr = "-" + discountValue + "%";
            binding.discountPercentTextView.setText(discountValueStr);


        } else {
            changeVisibilityOfDiscountTextView(binding, View.GONE);
        }

        if (featuredProducts.get(position).isFeatured.equals(Constants.ONE)) {
            binding.featuredIconImageView.setVisibility(View.VISIBLE);
        } else {
            binding.featuredIconImageView.setVisibility(View.GONE);
        }

        binding.ratingBar.setRating(featuredProducts.get(position).ratingDetails.totalRatingValue);
        binding.newDiscountPriceTextView.setText(String.valueOf(featuredProducts.get(position).currencySymbol + " " + Utils.format(featuredProducts.get(position).unitPrice)));
        binding.oldDiscountPriceTextView.setText(String.valueOf(featuredProducts.get(position).currencySymbol + " " + Utils.format(featuredProducts.get(position).originalPrice)));
        binding.ratingValueTextView.setText(binding.getRoot().getResources().getString(R.string.discount__rating5,
                String.valueOf(featuredProducts.get(position).ratingDetails.totalRatingValue),
                String.valueOf(featuredProducts.get(position).ratingDetails.totalRatingCount)));

        binding.getRoot().setOnClickListener(view -> callback.onClick(featuredProducts.get(position)));

        container.addView(binding.getRoot());

        return binding.getRoot();
    }

    public interface ItemClick {
        void onClick(Product product);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }

    private void changeVisibilityOfDiscountTextView(ItemViewPagerAdapterBinding binding, int status) {

        binding.discountPercentTextView.setVisibility(status);

        binding.oldDiscountPriceTextView.setVisibility(status);
    }
}
