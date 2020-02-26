package com.panaceasoft.psmultistore.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.CheckoutFragment2Binding;
import com.panaceasoft.psmultistore.ui.checkout.adapter.ShippingMethodsAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.coupondiscount.CouponDiscountViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.shippingmethod.ShippingMethodViewModel;
import com.panaceasoft.psmultistore.viewobject.Basket;
import com.panaceasoft.psmultistore.viewobject.ShippingCostContainer;
import com.panaceasoft.psmultistore.viewobject.ShippingMethod;
import com.panaceasoft.psmultistore.viewobject.ShippingProductContainer;
import com.panaceasoft.psmultistore.viewobject.common.Status;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class CheckoutFragment2 extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private BasketViewModel basketViewModel;
    private ShippingMethodViewModel shippingMethodViewModel;
    private CouponDiscountViewModel couponDiscountViewModel;

    @VisibleForTesting
    private AutoClearedValue<CheckoutFragment2Binding> binding;
    private AutoClearedValue<ShippingMethodsAdapter> adapter;

    private PSDialogMsg psDialogMsg;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CheckoutFragment2Binding dataBinding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_2, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(this.getActivity(), false);

        if (getActivity() instanceof CheckoutActivity) {
            ((CheckoutActivity) getActivity()).progressDialog.setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
            ((CheckoutActivity) getActivity()).progressDialog.setCancelable(false);
        }

        binding.get().couponDiscountButton.setOnClickListener(v -> {
            if ((CheckoutFragment2.this.getActivity()) != null) {
                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.couponDiscountText = binding.get().couponDiscountValueEditText.getText().toString();
            }
            couponDiscountViewModel.setCouponDiscountObj(binding.get().couponDiscountValueEditText.getText().toString(), selectedShopId);
            if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                ((CheckoutActivity) getActivity()).progressDialog.setMessage(getString(R.string.check_coupon));
                ((CheckoutActivity) getActivity()).progressDialog.show();
            }
        });

        if (!overAllTaxLabel.isEmpty()) {
            binding.get().overAllTaxTextView.setText(getString(R.string.tax, overAllTaxLabel));
        }

        if (!shippingTaxLabel.isEmpty()) {
            binding.get().shippingTaxTextView.setText(getString(R.string.shipping_tax, shippingTaxLabel));
        }

    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        shippingMethodViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShippingMethodViewModel.class);
        couponDiscountViewModel = ViewModelProviders.of(this, viewModelFactory).get(CouponDiscountViewModel.class);
    }

    @Override
    protected void initAdapters() {

        if (getActivity() != null) {

            ShippingMethodsAdapter nvAdapter = new ShippingMethodsAdapter(dataBindingComponent, shippingMethod -> {

                if (CheckoutFragment2.this.getActivity() != null) {

                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);
                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shippingMethodName = shippingMethod.name;
                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId = shippingMethod.id;

                    CheckoutFragment2.this.calculateTheBalance();
                }
            }, shippingId, ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId);

            adapter = new AutoClearedValue<>(this, nvAdapter);
            binding.get().shippingMethodsRecyclerView.setAdapter(adapter.get());

        }

    }

    @Override
    protected void initData() {

        if (shopNoShippingEnable.equals(Constants.ONE)) {
            binding.get().shippingMethodsCardView.setVisibility(View.GONE);
        }

        if (shopStandardShippingEnable.equals(Constants.ONE)) {
            binding.get().shippingMethodsCardView.setVisibility(View.VISIBLE);
            shippingMethodViewModel.setShippingMethodsObj(selectedShopId);
        }

        shippingMethodViewModel.getshippingCostByCountryAndCityData().observe(this, result -> {

            if (result != null) {
                if (result.status == Status.SUCCESS) {

                    if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                        ((CheckoutActivity) getActivity()).progressDialog.hide();
                    }

                    if (result.data != null) {

                        if (CheckoutFragment2.this.getActivity() != null) {
                            ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shippingMethodName = result.data.shippingZone.shippingZonePackageName;
                            ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Float.parseFloat(result.data.shippingZone.shippingCost);

                            calculateTheBalance();
                        }
                    }
                } else if (result.status == Status.ERROR) {
                    if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                        ((CheckoutActivity) getActivity()).progressDialog.hide();
                    }

                }
            }

        });

        shippingMethodViewModel.getShippingMethodsData().observe(this, result -> {

            if (result.data != null) {
                switch (result.status) {

                    case SUCCESS:
                        CheckoutFragment2.this.replaceShippingMethods(result.data);

                        for (ShippingMethod shippingMethod : result.data) {

                            if ((CheckoutFragment2.this.getActivity()) != null) {
                                if (!shippingId.isEmpty()) {
                                    if (shippingMethod.id.equals(shippingId) && ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId.isEmpty()) {
                                        if (CheckoutFragment2.this.getActivity() != null) {
                                            if (shopNoShippingEnable.equals(Constants.ONE)) {
                                                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = 0;
                                            } else {
                                                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);
                                            }
                                            CheckoutFragment2.this.calculateTheBalance();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;

                    case ERROR:
                        break;
                    case LOADING:
                        CheckoutFragment2.this.replaceShippingMethods(result.data);

                        for (ShippingMethod shippingMethod : result.data) {

                            if ((CheckoutFragment2.this.getActivity()) != null) {
                                if (!shippingId.isEmpty()) {
                                    if (shippingMethod.id.equals(shippingId) && ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId.isEmpty()) {
                                        if (CheckoutFragment2.this.getActivity() != null) {
                                            if (shopNoShippingEnable.equals(Constants.ONE))
                                                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = 0;
                                            else {
                                                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);
                                            }
                                            CheckoutFragment2.this.calculateTheBalance();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        });

        couponDiscountViewModel.getCouponDiscountData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case ERROR:

                        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                            ((CheckoutActivity) getActivity()).progressDialog.hide();
                        }

                        psDialogMsg.showErrorDialog(getString(R.string.error_coupon), getString(R.string.app__ok));
                        psDialogMsg.show();

                        break;

                    case SUCCESS:

                        if (result.data != null) {

                            if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                                ((CheckoutActivity) getActivity()).progressDialog.hide();
                            }

                            psDialogMsg.showSuccessDialog(getString(R.string.checkout_detail__claimed_coupon), getString(R.string.app__ok));
                            psDialogMsg.show();

                            if (getActivity() != null) {
                                ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount = Utils.round(Float.parseFloat(result.data.couponAmount), 2);
                                Utils.psLog("coupon5" + ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount + "");
                            }

                            calculateTheBalance();
                        }

                        break;
                }
            }
        });

        basketViewModel.setBasketListWithProductObj(selectedShopId);
        basketViewModel.getAllBasketWithProductList().observe(this, baskets -> {
            if (baskets != null && baskets.size() > 0) {

                if (getActivity() != null) {
                    ((CheckoutActivity) getActivity()).transactionValueHolder.resetValues();

                    for (Basket basket : baskets) {

                        ((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count += basket.count;
                        ((CheckoutActivity) getActivity()).transactionValueHolder.total += Utils.round((basket.basketOriginalPrice) * basket.count, 2);

                        ((CheckoutActivity) getActivity()).transactionValueHolder.discount += Utils.round(basket.product.discountAmount * basket.count, 2);
                        ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol = basket.product.currencySymbol;

                        ShippingProductContainer shippingProductContainer = new ShippingProductContainer(
                                basket.product.id,
                                basket.count
                        );
                        shippingMethodViewModel.shippingProductContainer.add(shippingProductContainer);
                    }

                    if (shopZoneShippingEnable.equals(Constants.ONE)) {
                        binding.get().shippingMethodsCardView.setVisibility(View.GONE);

                        if (getActivity() != null) {
                            //progressDialog.get().show();
                            if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                                ((CheckoutActivity) getActivity()).progressDialog.show();
                                Utils.psLog(((CheckoutActivity) getActivity()).user.country.id + " - " + ((CheckoutActivity) getActivity()).user.city.id + " - " + selectedShopId);


                                shippingMethodViewModel.setshippingCostByCountryAndCityObj(new ShippingCostContainer(
                                        ((CheckoutActivity) getActivity()).user.country.id, ((CheckoutActivity) getActivity()).user.city.id, selectedShopId,
                                        shippingMethodViewModel.shippingProductContainer));
                            }
                        }
                    }else if(shopStandardShippingEnable.equals(Constants.ONE)){
                        binding.get().shippingMethodsCardView.setVisibility(View.VISIBLE);
                    }else {
                        binding.get().shippingMethodsCardView.setVisibility(View.GONE);
                    }
                }
                calculateTheBalance();
            }
        });

        basketViewModel.getWholeBasketDeleteData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    Utils.psLog("Success");
                } else if (result.status == Status.ERROR) {
                    Utils.psLog("Fail");
                }
            }
        });
    }

    private void replaceShippingMethods(List<ShippingMethod> shippingMethods) {

        this.adapter.get().replace(shippingMethods);
        this.binding.get().executePendingBindings();
    }

    private void calculateTheBalance() {

        if (getActivity() != null) {

            ((CheckoutActivity) getActivity()).transactionValueHolder.sub_total = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.total - (((CheckoutActivity) getActivity()).transactionValueHolder.discount), 2);

            if (((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount > 0) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.sub_total = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total - ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount, 2);
            }

            if (!overAllTax.isEmpty()) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.tax = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total * Float.parseFloat(overAllTax), 2);
            }

            if (!shippingTax.isEmpty() && ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost >= 0) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost * Float.parseFloat(shippingTax), 2);
            }

            float total = ((CheckoutActivity) getActivity()).transactionValueHolder.sub_total +
                    ((CheckoutActivity) getActivity()).transactionValueHolder.tax +
                    ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax +
                    ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost;

            ((CheckoutActivity) getActivity()).transactionValueHolder.final_total =
                    Utils.round(total, 2);
            updateUI();

        }

    }

    private void updateUI() {

        if (getActivity() != null) {

            if (((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count > 0) {
                binding.get().totalItemCountValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.total > 0) {
                binding.get().totalValueTextView.setText((((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.total)));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount > 0) {
                binding.get().couponDiscountValueTextView.setText((((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount)));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.discount > 0) {
                binding.get().discountValueTextView.setText((((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.discount)));
            }

            if (!((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText.isEmpty()) {
                binding.get().couponDiscountValueEditText.setText(((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText);
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.sub_total > 0) {
                binding.get().subtotalValueTextView.setText((((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total)));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.tax > 0) {
                binding.get().taxValueTextView.setText((((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.tax)));
            }

            if (!shippingTax.equals("0.0") && !shippingTax.equals(Constants.RATING_ZERO)) {
                binding.get().shippingTaxValueTextView.setText((((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(Utils.round((((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax), 2))));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.final_total > 0.0) {
                binding.get().finalTotalValueTextView.setText((((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.final_total)));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost >= 0) {
                binding.get().shippingCostValueTextView.setText((((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost)));
            }

        }

    }

}
