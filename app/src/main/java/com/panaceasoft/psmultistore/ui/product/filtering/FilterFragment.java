package com.panaceasoft.psmultistore.ui.product.filtering;

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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentFilterBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeSearchProductViewModel;
import com.panaceasoft.psmultistore.viewobject.holder.ProductParameterHolder;

public class FilterFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private HomeSearchProductViewModel homeSearchProductViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentFilterBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentFilterBinding fragmentFilterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, fragmentFilterBinding);
        setHasOptionsMenu(true);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
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

        binding.get().setItemName.setHint(R.string.sf__notSet);
        binding.get().minimumEditText.setHint(R.string.sf__notSet);
        binding.get().maximumEditText.setHint(R.string.sf__notSet);

        if (getActivity() != null) {

            // Get Data from Intent
            Intent intent = getActivity().getIntent();
            homeSearchProductViewModel.holder = (ProductParameterHolder) intent.getSerializableExtra(Constants.FILTERING_HOLDER);

            // Name Binding
            binding.get().setItemName.setText(homeSearchProductViewModel.holder.search_term);

            // Price Binding
            binding.get().minimumEditText.setText(homeSearchProductViewModel.holder.min_price);
            binding.get().maximumEditText.setText(homeSearchProductViewModel.holder.max_price);

            // Feature Switch Binding
            if (homeSearchProductViewModel.holder.isFeatured != null) {
                if (homeSearchProductViewModel.holder.isFeatured.equals(Constants.ONE)) {
                    binding.get().featuredSwitch.setChecked(true);
                } else {
                    binding.get().featuredSwitch.setChecked(false);
                }
            }

            // Discount Switch Binding
            if (homeSearchProductViewModel.holder.isDiscount != null) {
                if (homeSearchProductViewModel.holder.isDiscount.equals(Constants.ONE)) {
                    binding.get().discountSwitch.setChecked(true);
                } else {
                    binding.get().discountSwitch.setChecked(false);
                }

            }

            // Rating Binding
            if (!homeSearchProductViewModel.holder.rating_value_one.isEmpty()) {
                selectStar(Constants.RATING_ONE);
            }
            if (!homeSearchProductViewModel.holder.rating_value_two.isEmpty()) {
                selectStar(Constants.RATING_TWO);
            }
            if (!homeSearchProductViewModel.holder.rating_value_three.isEmpty()) {
                selectStar(Constants.RATING_THREE);
            }
            if (!homeSearchProductViewModel.holder.rating_value_four.isEmpty()) {
                selectStar(Constants.RATING_FOUR);
            }
            if (!homeSearchProductViewModel.holder.rating_value_five.isEmpty()) {
                selectStar(Constants.RATING_FIVE);
            }

        }

        binding.get().filter.setOnClickListener(view -> {

            // Get Name
            homeSearchProductViewModel.holder.search_term = binding.get().setItemName.getText().toString();

            // Get Price
            homeSearchProductViewModel.holder.min_price = binding.get().minimumEditText.getText().toString();
            homeSearchProductViewModel.holder.max_price = binding.get().maximumEditText.getText().toString();

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
            navigationController.navigateBackToHomeFeaturedFragmentFromFiltering(FilterFragment.this.getActivity(), homeSearchProductViewModel.holder);
            FilterFragment.this.getActivity().finish();
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

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ok_button, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.clearButton) {

            binding.get().setItemName.setText("");
            binding.get().minimumEditText.setText("");
            binding.get().maximumEditText.setText("");
            binding.get().featuredSwitch.setChecked(false);
            binding.get().discountSwitch.setChecked(false);
            homeSearchProductViewModel.holder.overall_rating = "";

            unSelectStar(Constants.RATING_ONE);
            unSelectStar(Constants.RATING_TWO);
            unSelectStar(Constants.RATING_THREE);
            unSelectStar(Constants.RATING_FOUR);
            unSelectStar(Constants.RATING_FIVE);

            //navigationController.navigateBackToHomeFeaturedFragmentFromFiltering(SpecialFilteringFragment.this.getActivity(), homeSearchProductViewModel.holder);
        }
        return super.onOptionsItemSelected(item);
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
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {


    }

    @Override
    public void onDispatched() {

    }
}
