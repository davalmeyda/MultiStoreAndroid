package com.panaceasoft.psmultistore.ui.product.detail.adapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemProductColorAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.ProductColor;
import androidx.databinding.DataBindingUtil;
public class ProductColorAdapter extends DataBoundListAdapter<ProductColor, ItemProductColorAdapterBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private ColorClickCallBack callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;


    public ProductColorAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, ColorClickCallBack colorClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = colorClickCallback;
    }

    @Override
    protected ItemProductColorAdapterBinding createBinding(ViewGroup parent) {
        ItemProductColorAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_product_color_adapter, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener((View v) -> {
            ProductColor productColor = binding.getProductColor();
            if (productColor != null && callback != null) {

                callback.onClick(productColor, productColor.id,productColor.colorValue);
            }
        });
        return binding;
    }


    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemProductColorAdapterBinding binding, ProductColor item) {
        binding.setProductColor(item);

        Bitmap b = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        canvas.drawColor(Color.parseColor(item.colorValue));

        dataBindingComponent.getFragmentBindingAdapters().bindCircleBitmap(binding.color1BgImageView, b);

        if (item.isColorSelect) {
            binding.color1ImageView.setVisibility(View.VISIBLE);
        } else {
            binding.color1ImageView.setVisibility(View.GONE);
        }

    }

    @Override
    protected boolean areItemsTheSame(ProductColor oldItem, ProductColor newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId)
                && oldItem.isColorSelect == newItem.isColorSelect;
    }

    @Override
    protected boolean areContentsTheSame(ProductColor oldItem, ProductColor newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId)
                && oldItem.isColorSelect == newItem.isColorSelect;
    }

    public interface ColorClickCallBack {
        void onClick(ProductColor productColor, String selectedColorId, String selectedColourValue);
    }

}
