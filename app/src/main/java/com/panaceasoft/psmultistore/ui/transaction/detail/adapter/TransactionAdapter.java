package com.panaceasoft.psmultistore.ui.transaction.detail.adapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemTransactionAdapterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.NavigationController;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.TransactionDetail;

import androidx.databinding.DataBindingUtil;

public class TransactionAdapter extends DataBoundListAdapter<TransactionDetail, ItemTransactionAdapterBinding> {

    protected NavigationController navigationController;

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private TransactionClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    private String transactionIsZoneShipping;

    public TransactionAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                              TransactionClickCallback callback,
                              DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    public void setZoneShipping(String transactionIsZoneShipping){
        this.transactionIsZoneShipping= transactionIsZoneShipping;
    }

    @Override
    protected ItemTransactionAdapterBinding createBinding(ViewGroup parent) {
        ItemTransactionAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_transaction_adapter, parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            TransactionDetail transactionDetail = binding.getTransactionDetail();
            if (transactionDetail != null && callback != null) {
                callback.onClick(transactionDetail);
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
    protected void bind(ItemTransactionAdapterBinding binding, TransactionDetail item) {
        binding.setTransactionDetail(item);
        setDataToBalanceAndSubTotalToTransactionDetailOrder(binding, item);

        if(item.productColorCode.equals("")){
            binding.color1BgImageView.setVisibility(View.GONE);
        }else {
            binding.color1BgImageView.setVisibility(View.VISIBLE);
            Bitmap b = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            canvas.drawColor(Color.parseColor(item.productColorCode));
            dataBindingComponent.getFragmentBindingAdapters().bindCircleBitmap(binding.color1BgImageView, b);

        }

        if(item.productAttributeName.equals("")){
            binding.attributesTextView.setVisibility(View.GONE);
        }else {
            binding.attributesTextView.setVisibility(View.VISIBLE);
            String replaceComaForAttribute = item.productAttributeName;
            String replaceString = replaceComaForAttribute.replace("#",",");
            binding.attributesTextView.setText(String.format("(%s)", replaceString));
        }

        if(item.productColorCode.equals("") && item.productAttributeName.equals("")){
            binding.attributesView.setVisibility(View.GONE);
        }else{
            binding.attributesView.setVisibility(View.VISIBLE);
        }
        if (item.shippingCost != null) {
            if (transactionIsZoneShipping.equals(Constants.ONE)) {
                binding.shippingCostTextView.setVisibility(View.VISIBLE);
                binding.shippingCostValueText.setText(("$ " + Utils.format(Integer.parseInt(item.shippingCost))));
            }else {
                binding.shippingCostTextView.setVisibility(View.GONE);
                binding.shippingCostValueText.setVisibility(View.GONE);
            }
        }else {
            binding.shippingCostValueText.setText(("0.00"));
        }
    }

    @Override
    protected boolean areItemsTheSame(TransactionDetail oldItem, TransactionDetail newItem) {
        return Objects.equals(oldItem.id, newItem.id);

    }

    @Override
    protected boolean areContentsTheSame(TransactionDetail oldItem, TransactionDetail newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }


    public interface TransactionClickCallback {
        void onClick(TransactionDetail transactionDetail);
    }

    private void setDataToBalanceAndSubTotalToTransactionDetailOrder(ItemTransactionAdapterBinding binding, TransactionDetail item) {

        int qty = Integer.parseInt(item.qty);
        float subTotal ;

        if(item.originalPrice != 0 && item.discountAvailableAmount != 0) {
            int originalPrice =(int)item.originalPrice - (int)item.discountAvailableAmount;
            String balanceString =item.currencySymbol + " " + Utils.format(originalPrice);
            binding.balanceValueTextView.setText(balanceString);
            subTotal = originalPrice * qty;
        }
        else {
            String balanceString =item.currencySymbol + " " + Utils.format(item.originalPrice);
            binding.balanceValueTextView.setText(balanceString);

            subTotal = item.originalPrice * qty;
        }

        String subTotalValueString = item.currencySymbol + Constants.SPACE_STRING + Utils.format(subTotal);
        binding.subTotalValueTextView.setText(subTotalValueString);

        String priceString =item.currencySymbol + " " + Utils.format(item.originalPrice);
        binding.priceValueTextView.setText(priceString);

        String discountString = item.currencySymbol + " " + Utils.format(item.discountAvailableAmount);
        binding.discountAvailableAmountValueTextView.setText(discountString);
    }
}
