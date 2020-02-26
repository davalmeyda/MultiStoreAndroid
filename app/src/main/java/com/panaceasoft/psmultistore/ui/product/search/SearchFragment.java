package com.panaceasoft.psmultistore.ui.product.search;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentSearchBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeSearchProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;

import static androidx.databinding.DataBindingUtil.inflate;

public class SearchFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private String catId = Constants.NO_DATA;
    private String subCatId = Constants.NO_DATA;
    private PSDialogMsg psDialogMsg;
    private HomeSearchProductViewModel homeSearchProductViewModel;
    private BasketViewModel basketViewModel;
    private MenuItem basketMenuItem;


    @VisibleForTesting
    private AutoClearedValue<FragmentSearchBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSearchBinding dataBinding = inflate(inflater, R.layout.fragment_search, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    private void setBasketMenuItemVisible(Boolean isVisible) {
        if (basketMenuItem != null) {
            basketMenuItem.setVisible(isVisible);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.basket_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        basketMenuItem = menu.findItem(R.id.action_basket);

        if (basketViewModel != null) {
            if (basketViewModel.basketCount > 0) {
                basketMenuItem.setVisible(true);
            } else {
                basketMenuItem.setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_basket) {
            navigationController.navigateToBasketList(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CATEGORY) {

            this.catId = data.getStringExtra(Constants.CATEGORY_ID);
            binding.get().categoryTextView.setText(data.getStringExtra(Constants.CATEGORY_NAME));
            homeSearchProductViewModel.holder.catId = this.catId;
            this.subCatId = "";
            homeSearchProductViewModel.holder.subCatId = this.subCatId;
            binding.get().subCategoryTextView.setText("");

        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_SUBCATEGORY) {

            this.subCatId = data.getStringExtra(Constants.SUBCATEGORY_ID);
            binding.get().subCategoryTextView.setText(data.getStringExtra(Constants.SUBCATEGORY_NAME));
            homeSearchProductViewModel.holder.subCatId = this.subCatId;
        }
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

        binding.get().itemNameEditText.setHint(R.string.search__notSet);
        binding.get().categoryTextView.setHint(R.string.search__notSet);
        binding.get().subCategoryTextView.setHint(R.string.search__notSet);
        binding.get().lowestPriceEditText.setHint(R.string.search__notSet);
        binding.get().highestPriceEditText.setHint(R.string.search__notSet);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        AutoClearedValue<AlertDialog.Builder> alertDialog = new AutoClearedValue<>(this, builder);
        alertDialog.get().setTitle(getResources().getString(R.string.Feature_UI__search_alert_cat_title));

        binding.get().productNameTextView.setText(binding.get().productNameTextView.getText().toString());
        binding.get().productTypeTextView.setText(binding.get().productTypeTextView.getText().toString());
        binding.get().subProductTypeTextView.setText(binding.get().subProductTypeTextView.getText().toString());
        binding.get().priceTextView.setText(binding.get().priceTextView.getText().toString());
        binding.get().lowestPriceTextView.setText(binding.get().lowestPriceTextView.getText().toString());
        binding.get().highestPriceTextView.setText(binding.get().highestPriceTextView.getText().toString());
        binding.get().specialCheckTextView.setText(binding.get().specialCheckTextView.getText().toString());
        binding.get().selectionTextView.setText(binding.get().selectionTextView.getText().toString());
        binding.get().discountPriceTextView.setText(binding.get().discountPriceTextView.getText().toString());
        binding.get().searchButton.setText(binding.get().searchButton.getText().toString());

        binding.get().categoryTextView.setText("");
        binding.get().subCategoryTextView.setText("");

        binding.get().categorySelectionView.setOnClickListener(view -> navigationController.navigateToSearchActivityCategoryFragment(SearchFragment.this.getActivity(), Constants.CATEGORY, catId, subCatId, Constants.NO_DATA, Constants.NO_DATA, selectedShopId));

        binding.get().subCategorySelectionView.setOnClickListener(view -> {

            if (catId.equals(Constants.NO_DATA)) {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__choose_category), getString(R.string.app__ok));

                psDialogMsg.show();

            } else {
                navigationController.navigateToSearchActivityCategoryFragment(SearchFragment.this.getActivity(), Constants.SUBCATEGORY, catId, subCatId, Constants.NO_DATA, Constants.NO_DATA, selectedShopId);
            }
        });

        binding.get().oneStar.setOnClickListener(v -> {

            if (homeSearchProductViewModel.holder.rating_value_one.equals("")) {
                selectStar(Constants.RATING_ONE);
                unSelectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_FIVE);
            } else {
                unSelectStar(Constants.RATING_ONE);
            }

        });

        binding.get().twoStar.setOnClickListener(v -> {
            if (homeSearchProductViewModel.holder.rating_value_two.equals("")) {
                selectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_ONE);
                unSelectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_FIVE);
            } else {
                unSelectStar(Constants.RATING_TWO);
            }

        });

        binding.get().threeStar.setOnClickListener(v -> {
            if (homeSearchProductViewModel.holder.rating_value_three.equals("")) {
                selectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_ONE);
                unSelectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_FIVE);
            } else {
                unSelectStar(Constants.RATING_THREE);
            }
        });

        binding.get().fourStar.setOnClickListener(v -> {
            if (homeSearchProductViewModel.holder.rating_value_four.equals("")) {
                selectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_ONE);
                unSelectStar(Constants.RATING_FIVE);
            } else {
                unSelectStar(Constants.RATING_FOUR);
            }
        });

        binding.get().fiveStar.setOnClickListener(v -> {
            if (homeSearchProductViewModel.holder.rating_value_five.equals("")) {
                selectStar(Constants.RATING_FIVE);
                unSelectStar(Constants.RATING_TWO);
                unSelectStar(Constants.RATING_THREE);
                unSelectStar(Constants.RATING_FOUR);
                unSelectStar(Constants.RATING_ONE);
            } else {
                unSelectStar(Constants.RATING_FIVE);
            }
        });

        binding.get().searchButton.setOnClickListener(view -> {

            // Get Name
            homeSearchProductViewModel.holder.search_term = binding.get().itemNameEditText.getText().toString();

            // Get Price
            homeSearchProductViewModel.holder.min_price = binding.get().lowestPriceEditText.getText().toString();
            homeSearchProductViewModel.holder.max_price = binding.get().highestPriceEditText.getText().toString();

            // Prepare Rating
//            homeSearchProductViewModel.holder.overall_rating = "";

            if (!homeSearchProductViewModel.holder.rating_value_one.equals("")) {
                homeSearchProductViewModel.holder.overall_rating = Constants.RATING_ONE;
            }
            if (!homeSearchProductViewModel.holder.rating_value_two.equals("")) {
                homeSearchProductViewModel.holder.overall_rating = Constants.RATING_TWO;
            }

            if (!homeSearchProductViewModel.holder.rating_value_three.equals("")) {
                homeSearchProductViewModel.holder.overall_rating = Constants.RATING_THREE;
            }

            if (!homeSearchProductViewModel.holder.rating_value_four.equals("")) {
                homeSearchProductViewModel.holder.overall_rating = Constants.RATING_FOUR;
            }

            if (!homeSearchProductViewModel.holder.rating_value_five.equals("")) {
                homeSearchProductViewModel.holder.overall_rating = Constants.RATING_FIVE;
            }

            if(homeSearchProductViewModel.holder.rating_value_one.equals("") && homeSearchProductViewModel.holder.rating_value_two.equals("") && homeSearchProductViewModel.holder.rating_value_three.equals("")
            && homeSearchProductViewModel.holder.rating_value_four.equals("") && homeSearchProductViewModel.holder.rating_value_five.equals("")){
                homeSearchProductViewModel.holder.overall_rating = "";
            }

            // Get Feature Switch Data
            if (binding.get().featuredSwitch.isChecked()) {
                homeSearchProductViewModel.holder.isFeatured = Constants.ONE;
            } else {
                homeSearchProductViewModel.holder.isFeatured = "";
            }

            // Get Discount Switch Data
            if (binding.get().discountSwitch.isChecked()) {
                homeSearchProductViewModel.holder.isDiscount = Constants.ONE;
            } else {
                homeSearchProductViewModel.holder.isDiscount = "";
            }

            // For Sorting
            if (homeSearchProductViewModel.holder.isFeatured.equals(Constants.ONE)) {
                homeSearchProductViewModel.holder.order_by = Constants.FILTERING_FEATURE;
            }

            // Set to Intent
            navigationController.navigateToHomeFilteringActivity(SearchFragment.this.getActivity(), homeSearchProductViewModel.holder, null);

        });

    }

    private void unSelectStar(Button star) {
        star.setTextColor(getResources().getColor(R.color.text__primary));
        star.setBackgroundResource(R.drawable.button_border);
        star.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_full_gray, 0, 0);
    }

    private void selectStar(Button star) {
        star.setTextColor(getResources().getColor(R.color.text__white));
        star.setBackgroundResource(R.drawable.button_border_pressed);
        star.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_white, 0, 0);
    }

    private void unSelectStar(String stars) {

        switch (stars) {
            case Constants.RATING_ONE:

                unSelectStar(binding.get().oneStar);
                homeSearchProductViewModel.holder.rating_value_one = "";

                break;
            case Constants.RATING_TWO:

                unSelectStar(binding.get().twoStar);
                homeSearchProductViewModel.holder.rating_value_two = "";

                break;
            case Constants.RATING_THREE:

                unSelectStar(binding.get().threeStar);
                homeSearchProductViewModel.holder.rating_value_three = "";

                break;
            case Constants.RATING_FOUR:

                unSelectStar(binding.get().fourStar);
                homeSearchProductViewModel.holder.rating_value_four = "";

                break;
            case Constants.RATING_FIVE:

                unSelectStar(binding.get().fiveStar);
                homeSearchProductViewModel.holder.rating_value_five = "";

                break;
        }

    }

    private void selectStar(String stars) {
        switch (stars) {
            case Constants.RATING_ONE:

                selectStar(binding.get().oneStar);
                homeSearchProductViewModel.holder.rating_value_one = Constants.RATING_ONE;

                break;
            case Constants.RATING_TWO:

                selectStar(binding.get().twoStar);
                homeSearchProductViewModel.holder.rating_value_two = Constants.RATING_TWO;

                break;
            case Constants.RATING_THREE:

                selectStar(binding.get().threeStar);
                homeSearchProductViewModel.holder.rating_value_three = Constants.RATING_THREE;

                break;
            case Constants.RATING_FOUR:

                selectStar(binding.get().fourStar);
                homeSearchProductViewModel.holder.rating_value_four = Constants.RATING_FOUR;

                break;

            case Constants.RATING_FIVE:

                selectStar(binding.get().fiveStar);
                homeSearchProductViewModel.holder.rating_value_five = Constants.RATING_FIVE;

                break;

        }
    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {
        basketData();
    }

    private void basketData() {
        //set and get basket list
        basketViewModel.setBasketListObj(selectedShopId);
        basketViewModel.getAllBasketList().observe(this, resourse -> {
            if (resourse != null) {
                basketViewModel.basketCount = resourse.size();
                if (resourse.size() > 0) {
                    setBasketMenuItemVisible(true);
                } else {
                    setBasketMenuItemVisible(false);
                }
            }
        });
    }
}
