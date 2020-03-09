package com.panaceasoft.psmultistore.ui.basket;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentBasketListBinding;
import com.panaceasoft.psmultistore.ui.basket.adapter.BasketAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.SingleDavid;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewobject.Basket;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */
public class BasketListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private PSDialogMsg psDialogMsg;
    private BasketViewModel basketViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentBasketListBinding> binding;
    private AutoClearedValue<BasketAdapter> basketAdapter;

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBasketListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_list, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().checkoutButton.setText(binding.get().checkoutButton.getText().toString());

        binding.get().checkoutButton.setOnClickListener(view -> BasketListFragment.this.doCheckOut());
    }

    private void doCheckOut() {

        Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, () ->
                navigationController.navigateToCheckoutActivity(getActivity()));

    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
    }

    @Override
    protected void initAdapters() {

        //basket
        BasketAdapter basketAdapter1 = new BasketAdapter(dataBindingComponent, new BasketAdapter.BasketClickCallBack() {
            @Override
            public void onMinusClick(Basket basket) {
                basketViewModel.setUpdateToBasketListObj(basket.id, basket.count);

            }

            @Override
            public void onAddClick(Basket basket) {
                basketViewModel.setUpdateToBasketListObj(basket.id, basket.count);
            }

            @Override
            public void onDeleteConfirm(Basket basket) {

                psDialogMsg.showConfirmDialog(getString(R.string.delete_item_from_basket), getString(R.string.app__ok), getString(R.string.app__cancel));

                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(view -> {
                    basketViewModel.setDeleteToBasketListObj(basket.id);
                    psDialogMsg.cancel();
                });
                psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());

            }

            @Override
            public void onClick(Basket basket) {
                navigationController.navigateToProductDetailActivity(getActivity(), basket);
            }

        }, this);
        basketAdapter = new AutoClearedValue<>(this, basketAdapter1);
        bindingBasketAdapter(basketAdapter.get());

    }

    private void bindingBasketAdapter(BasketAdapter nvbasketAdapter) {
        this.basketAdapter = new AutoClearedValue<>(this, nvbasketAdapter);
//        binding.get().basketRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.get().basketRecycler.setAdapter(basketAdapter.get());
    }

    @Override
    protected void initData() {

        if (getContext() != null) {
            binding.get().noItemTitleTextView.setText(getContext().getString(R.string.basket__no_item_title));
            binding.get().noItemDescTextView.setText(getContext().getString(R.string.basket__no_item_desc));
        }

        LoadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__BASKET_FRAGMENT
                && resultCode == Constants.RESULT_CODE__REFRESH_BASKET_LIST) {

            basketViewModel.setBasketListWithProductObj(selectedShopId);

            loadLoginUserId();
        }

    }

    @Override
    public void onResume() {

        super.onResume();

        loadLoginUserId();


        basketViewModel.setBasketListWithProductObj(selectedShopId);

    }

    private void LoadData() {
        //load basket

        basketViewModel.setBasketListWithProductObj(selectedShopId);
        LiveData<List<Basket>> basketData = basketViewModel.getAllBasketWithProductList();
        if (basketData != null) {
            basketData.observe(this, listResource -> {
                if (listResource != null) {
                    if (listResource.size() > 0) {

                        binding.get().noItemConstraintLayout.setVisibility(View.GONE);
                        binding.get().checkoutConstraintLayout.setVisibility(View.VISIBLE);

                    } else {

                        binding.get().checkoutConstraintLayout.setVisibility(View.GONE);
                        binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);

                        if (getActivity() instanceof BasketListActivity) {
                            getActivity().finish();
                        }

                    }

                    replaceProductSpecsData(listResource);

                } else {
                    if (basketViewModel.getAllBasketWithProductList() != null) {
                        if (basketViewModel.getAllBasketWithProductList().getValue() != null) {
                            if (basketViewModel.getAllBasketWithProductList().getValue().size() == 0) {
                                binding.get().checkoutConstraintLayout.setVisibility(View.GONE);
                                binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            });
        }

        basketViewModel.getBasketUpdateData().observe(this, resourse -> {
            if (resourse != null) {
                if (resourse.status == Status.SUCCESS) {

                    basketViewModel.totalPrice = 0;
                    basketViewModel.basketCount = 0;

                    basketViewModel.setBasketListWithProductObj(selectedShopId);

                }
            }
        });

        basketViewModel.getBasketDeleteData().observe(this, resource -> {
            if (resource != null) {
                if (resource.status == Status.SUCCESS) {

                    basketViewModel.totalPrice = 0;
                    basketViewModel.basketCount = 0;

                    basketViewModel.setBasketListWithProductObj(selectedShopId);

                }
            }
        });


    }

    public void replaceProductSpecsData(List<Basket> basketList) {

        basketAdapter.get().replace(basketList);

        if (basketList != null) {
            basketAdapter.get().replace(basketList);

            if (basketList.size() > 0) {
                basketViewModel.totalPrice = 0;

                for (int i = 0; i < basketList.size(); i++) {
                    basketViewModel.totalPrice += basketList.get(i).basketPrice * basketList.get(i).count;
                }
                // DAVID RAYITO AGREGADO
                SingleDavid single = SingleDavid.INSTANCE;
                basketViewModel.totalPrice = single.getPrecio() + basketViewModel.totalPrice;

                basketViewModel.basketCount = 0;

                for (int i = 0; i < basketList.size(); i++) {
                    basketViewModel.basketCount += basketList.get(i).count;
                }

                String totalPriceString = basketList.get(0).product.currencySymbol + Constants.SPACE_STRING + (Utils.format(Utils.round(basketViewModel.totalPrice, 2)));

                binding.get().totalPriceTextView.setText(totalPriceString);
                binding.get().countTextView.setText(String.valueOf(basketViewModel.basketCount));
            } else {
                binding.get().totalPriceTextView.setText(Constants.ZERO);
                binding.get().countTextView.setText(Constants.ZERO);

                basketViewModel.totalPrice = 0;
                basketViewModel.basketCount = 0;

                if (basketList.size() > 0) {
                    for (int i = 0; i < basketList.size(); i++) {
                        basketViewModel.totalPrice += basketList.get(i).basketPrice * basketList.get(i).count;
                    }
                    // DAVID RAYITO AGREGADO
                    SingleDavid single = SingleDavid.INSTANCE;
                    basketViewModel.totalPrice = single.getPrecio() + basketViewModel.totalPrice;
                    for (int i = 0; i < basketList.size(); i++) {
                        basketViewModel.basketCount += basketList.get(i).count;
                    }

                    String totalPriceString = basketList.get(0).product.currencySymbol + Constants.SPACE_STRING + Utils.format(basketViewModel.totalPrice);
                    binding.get().totalPriceTextView.setText(totalPriceString);
                    binding.get().countTextView.setText(String.valueOf(basketViewModel.basketCount));
                } else {
                    binding.get().totalPriceTextView.setText(Constants.ZERO);
                    binding.get().countTextView.setText(Constants.ZERO);
                }
                binding.get().executePendingBindings();

            }
        }
    }


    public List<String> replaceProductSpecsData1(List<Basket> basketList, BasketViewModel bb) {

        if (basketList != null) {

            if (basketList.size() > 0) {
                bb.totalPrice = 0;

                for (int i = 0; i < basketList.size(); i++) {
                    bb.totalPrice += basketList.get(i).basketPrice * basketList.get(i).count;
                }

                bb.basketCount = 0;

                for (int i = 0; i < basketList.size(); i++) {
                    bb.basketCount += basketList.get(i).count;
                }

                List<String> todo = Arrays.asList(String.valueOf(bb.totalPrice), String.valueOf(bb.basketCount));

                return todo;
            } else {

                bb.totalPrice = 0;
                bb.basketCount = 0;

                if (basketList.size() > 0) {
                    for (int i = 0; i < basketList.size(); i++) {
                        bb.totalPrice += basketList.get(i).basketPrice * basketList.get(i).count;
                    }
                    for (int i = 0; i < basketList.size(); i++) {
                        bb.basketCount += basketList.get(i).count;
                    }

                    List<String> todo = Arrays.asList(String.valueOf(bb.totalPrice), String.valueOf(bb.basketCount));

                    return todo;
                }
                List<String> todo = Arrays.asList(Constants.ZERO, Constants.ZERO);

                return todo;

            }


        }
        List<String> todo = Arrays.asList(Constants.ZERO, Constants.ZERO);

        return todo;
    }

}