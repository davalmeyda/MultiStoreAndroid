package com.panaceasoft.psmultistore.ui.checkout.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemShippingMethodBinding;
import com.panaceasoft.psmultistore.ui.checkout.CheckoutActivity;
import com.panaceasoft.psmultistore.ui.checkout.CheckoutFragment2;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.ShippingMethod;

import androidx.databinding.DataBindingUtil;

public class ShippingMethodsAdapter extends DataBoundListAdapter<ShippingMethod, ItemShippingMethodBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ShippingMethodsAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    private String defaultShippingId;
    private ItemShippingMethodBinding oldItem, newItem;
    private String selectedShippingId;

    public ShippingMethodsAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                  NewsClickCallback callback, String defaultShippingId,String selectedShippingId) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.defaultShippingId = defaultShippingId;
        this.selectedShippingId = selectedShippingId;

    }


    @Override
    protected ItemShippingMethodBinding createBinding(ViewGroup parent) {
        ItemShippingMethodBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_shipping_method, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {
            callback.onClick(binding.getShippingMethod());

            if (newItem != null && oldItem != null) {
                oldItem = newItem;
                newItem = binding;

                ShippingMethodsAdapter.this.changeToWhite(oldItem);
                ShippingMethodsAdapter.this.changeToOrange(newItem);

            } else if (oldItem != null) {

                newItem = binding;

                ShippingMethodsAdapter.this.changeToWhite(oldItem);
                ShippingMethodsAdapter.this.changeToOrange(newItem);
            } else {
                newItem = binding;
                oldItem = binding;

                ShippingMethodsAdapter.this.changeToOrange(newItem);
            }
        });
        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemShippingMethodBinding> holder, int position) {
        super.bindView(holder, position);

        // setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemShippingMethodBinding binding, ShippingMethod shippingMethod) {
        binding.setShippingMethod(shippingMethod);

        if ((shippingMethod.price * 10) % 10 == 0) {
            binding.cashTextView.setText(String.valueOf(shippingMethod.currencySymbol + Utils.format(Math.round(shippingMethod.price))));
        } else {
            binding.cashTextView.setText(String.valueOf(shippingMethod.currencySymbol + Utils.format(shippingMethod.price)));

        }

//        String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol
        if(!selectedShippingId.isEmpty()){
            if (shippingMethod.id.equals(selectedShippingId)) {

                oldItem = binding;
                changeToOrange(binding);

            } else {
                changeToWhite(binding);
            }
        }
        else {
            if(!defaultShippingId.isEmpty())
            {
                if (shippingMethod.id.equals(defaultShippingId)) {

                    oldItem = binding;
                    changeToOrange(binding);

                } else {
                    changeToWhite(binding);
                }

            }
        }

    }

    @Override
    protected boolean areItemsTheSame(ShippingMethod oldItem, ShippingMethod newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(ShippingMethod oldItem, ShippingMethod newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface NewsClickCallback {
        void onClick(ShippingMethod shippingMethod);
    }

    private void changeToWhite(ItemShippingMethodBinding binding) {
        binding.cashCardView.setCardBackgroundColor(binding.getRoot().getResources().getColor(R.color.md_white_1000));
        binding.cashTextView.setTextColor(binding.getRoot().getResources().getColor(R.color.global__primary));
        binding.typeTextView.setTextColor(binding.getRoot().getResources().getColor(R.color.md_grey_700));
        binding.daysTextView.setTextColor(binding.getRoot().getResources().getColor(R.color.md_black_1000));
        binding.textView19.setTextColor(binding.getRoot().getResources().getColor(R.color.md_black_1000));
    }

    private void changeToOrange(ItemShippingMethodBinding binding) {
        binding.cashCardView.setCardBackgroundColor(binding.getRoot().getResources().getColor(R.color.global__primary));
        binding.cashTextView.setTextColor(binding.getRoot().getResources().getColor(R.color.md_white_1000));
        binding.typeTextView.setTextColor(binding.getRoot().getResources().getColor(R.color.md_white_1000));
        binding.daysTextView.setTextColor(binding.getRoot().getResources().getColor(R.color.md_white_1000));
        binding.textView19.setTextColor(binding.getRoot().getResources().getColor(R.color.md_white_1000));
    }
}
