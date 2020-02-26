package com.panaceasoft.psmultistore.ui.basket.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemBasketAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.Basket;

import androidx.databinding.DataBindingUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BasketAdapter extends DataBoundListAdapter<Basket, ItemBasketAdapterBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private BasketClickCallBack callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    public Context context;
//    private int minOrder = 0;

    public BasketAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, BasketClickCallBack basketClickCallBack, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = basketClickCallBack;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemBasketAdapterBinding createBinding(ViewGroup parent) {
        ItemBasketAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_basket_adapter, parent, false,
                        dataBindingComponent);
        context = parent.getContext();
        binding.getRoot().setOnClickListener(v -> {
            Basket basket = binding.getBasket();
            if (basket != null && callback != null) {
                callback.onClick(basket);
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
    protected void bind(ItemBasketAdapterBinding binding, Basket item) {
        binding.setBasket(item);
        final int minOrder;

        Utils.psLog("********** SelectedColorId " + item.selectedColorId);
        Utils.psLog("********** SelectedColorValue " + item.selectedColorValue);
        Utils.psLog("********** selectedAttributes " + item.selectedAttributes);
        Utils.psLog("********** selectedAttributesPrice" + item.selectedAttributesPrice);
        Utils.psLog("********** selectedAttributeTotalPrice" + item.selectedAttributeTotalPrice);

        if (item.selectedColorValue.equals("")) {
            binding.colorAttributeImageView.setVisibility(View.GONE);
        } else {
            binding.colorAttributeImageView.setVisibility(View.VISIBLE);
            Bitmap b = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            canvas.drawColor(Color.parseColor(item.selectedColorValue));
            dataBindingComponent.getFragmentBindingAdapters().bindCircleBitmap(binding.colorAttributeImageView, b);

        }

        HashMap<String, String> priceMap = new Gson().fromJson(item.selectedAttributes, new TypeToken<HashMap<String, String>>() {
        }.getType());
        Iterator<String> priceKeyIterator = priceMap.keySet().iterator();
        StringBuilder priceStr = new StringBuilder();
        while (priceKeyIterator.hasNext()) {
            String key = priceKeyIterator.next();

            if (!key.equals("-1")) {
                if (priceMap.containsKey(key)) {

                    if (!priceStr.toString().equals("")) {
                        priceStr.append(Config.ATTRIBUT_SEPERATOR);
                    }
                    priceStr.append(priceMap.get(key));
                }
            }
        }

        String replaceComaForAttribute = priceStr.toString();
        String replaceString = replaceComaForAttribute.replace("#", ", ");
        if (replaceString.equals("")) {
            binding.attributeTextView.setVisibility(View.GONE);
        } else {
            binding.attributeTextView.setVisibility(View.VISIBLE);
            binding.attributeTextView.setText(String.format("(%s)", replaceString));
        }

        if (replaceString.equals("") && item.selectedColorValue.equals("")) {
            binding.attributeTitleTextView.setVisibility(View.GONE);
        } else {
            binding.attributeTitleTextView.setVisibility(View.VISIBLE);
        }

        binding.basketNameTextView.setText(item.product.name);

        if (item.count == 0) {
            item.count = 1;
        }

        if (item.product.minimumOrder != null && !item.product.minimumOrder.equals("0")) {
            minOrder = Integer.valueOf(item.product.minimumOrder);
        } else {
            minOrder = 0;
        }

        binding.minusImageView.setOnClickListener(view -> {

            if (item.count > 1) {
                if (minOrder != 0) {
                    if (item.count <= minOrder) {
                        Toast.makeText(binding.getRoot().getContext(), binding.getRoot().getContext().getString(R.string.product_detail__min_order_error) + Constants.SPACE_STRING + minOrder, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                item.count -= 1;
                binding.qtyEditText.setText(String.valueOf(item.count));
                TotalPriceUpdate(binding, item);

                callback.onMinusClick(item);
            }
        });
        binding.plusImageView.setOnClickListener(view -> {
            item.count += 1;
            binding.qtyEditText.setText(String.valueOf(item.count));
            TotalPriceUpdate(binding, item);

            callback.onAddClick(item);
        });

        callback.onAddClick(item);
        TotalPriceUpdate(binding, item);

        binding.deleteIconImageView.setOnClickListener(view -> callback.onDeleteConfirm(item));

        binding.deleteBgImageView.setOnClickListener(view -> callback.onDeleteConfirm(item));

    }

    private void TotalPriceUpdate(ItemBasketAdapterBinding binding, Basket item) {


        String priceString = item.product.currencySymbol + Constants.SPACE_STRING + Utils.format(item.basketPrice);
        binding.priceTextView.setText(priceString);
        float subTotalPrice = Utils.round(item.count * item.basketPrice, 2);
        String subTotalString = item.product.currencySymbol + Constants.SPACE_STRING + Utils.format(subTotalPrice);

        binding.subTotalTextView.setText(subTotalString);
    }

    @Override
    protected boolean areItemsTheSame(Basket oldItem, Basket newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId)
                && oldItem.count == newItem.count
                && oldItem.selectedAttributes.equals(newItem.selectedAttributes)
                && oldItem.selectedColorId.equals(newItem.selectedColorId);
    }

    @Override
    protected boolean areContentsTheSame(Basket oldItem, Basket newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId)
                && oldItem.count == newItem.count
                && oldItem.selectedAttributes.equals(newItem.selectedAttributes)
                && oldItem.selectedColorId.equals(newItem.selectedColorId);
    }

    public interface BasketClickCallBack {
        void onMinusClick(Basket basket);

        void onAddClick(Basket basket);

        void onDeleteConfirm(Basket basket);

        void onClick(Basket basket);
    }

}
