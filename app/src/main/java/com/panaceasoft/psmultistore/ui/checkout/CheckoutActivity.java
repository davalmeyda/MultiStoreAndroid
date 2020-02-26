package com.panaceasoft.psmultistore.ui.checkout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.MainActivity;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ActivityCheckoutBinding;
import com.panaceasoft.psmultistore.ui.common.PSAppCompactActivity;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.MyContextWrapper;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.viewobject.TransactionObject;
import com.panaceasoft.psmultistore.viewobject.User;
import com.panaceasoft.psmultistore.viewobject.holder.TransactionValueHolder;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class CheckoutActivity extends PSAppCompactActivity {

    public int number = 1;
    private int maxNumber = 4;
    User user;
    PSFragment fragment;
    public ActivityCheckoutBinding binding;
    public ProgressDialog progressDialog;
    private PSDialogMsg psDialogMsg;
    public TransactionValueHolder transactionValueHolder;
    public TransactionObject transactionObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);

        // Init all UI
        initUI(binding);

        progressDialog = new ProgressDialog(this);

        progressDialog.setCancelable(false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE__MAIN_ACTIVITY
                && resultCode == Constants.RESULT_CODE__RESTART_MAIN_ACTIVITY) {

            finish();
            startActivity(new Intent(this, MainActivity.class));

        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, CURRENT_LANG_COUNTRY_CODE, true));
    }

    private void initUI(ActivityCheckoutBinding binding) {

        transactionValueHolder = new TransactionValueHolder();

        psDialogMsg = new PSDialogMsg(this, false);

        // click close image button
        binding.closeImageButton.setOnClickListener(view -> finish());

        // fragment1 default initialize
        navigateFragment(binding, number);

        binding.nextButton.setOnClickListener(view -> {
            if (number < maxNumber) {
                number++;
                if (number == 3) {

                    navigateFragment(binding, number);

                } else if (number == 2) {
                    if ((user.city != null && user.city.id.isEmpty())) {
                        psDialogMsg.showErrorDialog(getString(R.string.error_message__select_city), getString(R.string.app__ok));
                        psDialogMsg.show();
                        number--;
                    } else if (((CheckoutFragment1)fragment).checkShippingAddressEditTextIsEmpty()) {
                        psDialogMsg.showErrorDialog(getString(R.string.shipping_address_one_error_message), getString(R.string.app__ok));
                        psDialogMsg.show();
                        number--;
                    } else if (((CheckoutFragment1)fragment).checkBillingAddressEditTextIsEmpty()) {
                        psDialogMsg.showErrorDialog(getString(R.string.billing_address_one_error_message), getString(R.string.app__ok));
                        psDialogMsg.show();
                        number--;
                    }
                    else {
//                        if (!((CheckoutFragment1) fragment).checkToUpdateProfile()) {
//                            psDialogMsg.showConfirmDialog(getString(R.string.update_address), getString(R.string.app__ok), getString(R.string.app__cancel));
//                            psDialogMsg.show();
//
//                            psDialogMsg.okButton.setOnClickListener(v -> {
//
//                                psDialogMsg.cancel();
                                ((CheckoutFragment1) fragment).updateUserProfile();

//                            });
//
//                            psDialogMsg.cancelButton.setOnClickListener(v -> {
//                                psDialogMsg.cancel();
//
//                                navigateFragment(binding, number);
//                            });
//
//                        } else {
//                            navigateFragment(binding, number);
//                        }
                    }


                } else if (number == 4) {

                    if (((CheckoutFragment3) fragment).binding.get().checkBox.isChecked()) {

                        number--;

                        if (((CheckoutFragment3) fragment).clicked) {

                            psDialogMsg.showConfirmDialog(getString(R.string.confirm_to_Buy), getString(R.string.app__ok), getString(R.string.app__cancel));
                            psDialogMsg.show();

                            psDialogMsg.okButton.setOnClickListener(v -> {

                                psDialogMsg.cancel();

                                switch (((CheckoutFragment3) fragment).paymentMethod) {
                                    case Constants.PAYMENT_PAYPAL:

                                        ((CheckoutFragment3) fragment).getToken();

                                        break;

                                    case Constants.PAYMENT_CASH_ON_DELIVERY:

                                        ((CheckoutFragment3) fragment).sendData();

                                        break;

                                    case Constants.PAYMENT_STRIPE:

                                        navigationController.navigateToStripeActivity(CheckoutActivity.this);

                                        break;

                                    case Constants.PAYMENT_BANK:

                                        ((CheckoutFragment3) fragment).sendData();

                                        break;

                                }

                            });

                            psDialogMsg.cancelButton.setOnClickListener(v -> psDialogMsg.cancel());
                        } else {

                            psDialogMsg.showErrorDialog(getString(R.string.checkout__choose_a_method), getString(R.string.app__ok));
                            psDialogMsg.show();
                        }

                    } else {

                        number--;

                        psDialogMsg.showInfoDialog(getString(R.string.not_checked), getString(R.string.app__ok));
                        psDialogMsg.show();

                    }

                } else {

                    navigateFragment(binding, number);
                }

            }
        });

        binding.prevButton.setOnClickListener(view -> {
            if (number > 1) {
                number--;
                binding.shippingImageView.setImageResource(R.drawable.baseline_circle_line_uncheck_24);
                binding.paymentImageView.setImageResource(R.drawable.baseline_circle_black_uncheck_24);
                navigateFragment(binding, number);
            }

        });

        binding.keepShoppingButton.setOnClickListener(v -> {

            navigationController.navigateBackToBasketActivity(CheckoutActivity.this);
            CheckoutActivity.this.finish();

        });

    }

    public void navigateFragment(ActivityCheckoutBinding binding, int position) {
        updateCheckoutUI(binding);

        if (position == 1) {

            CheckoutFragment1 fragment = new CheckoutFragment1();
            this.fragment = fragment;
            setupFragment(fragment);

        } else if (position == 2) {

            CheckoutFragment2 fragment = new CheckoutFragment2();
            this.fragment = fragment;
            setupFragment(fragment);

        } else if (position == 3) {

            CheckoutFragment3 fragment = new CheckoutFragment3();
            this.fragment = fragment;
            setupFragment(fragment);

        } else if (position == 4) {
            setupFragment(new CheckoutStatusFragment());
        }
    }

    private void updateCheckoutUI(ActivityCheckoutBinding binding) {
        if (number == 1) {
            binding.nextButton.setVisibility(View.VISIBLE);
            binding.prevButton.setVisibility(View.GONE);
            binding.keepShoppingButton.setVisibility(View.GONE);
            binding.step2View.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
            binding.step3View.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
            binding.nextButton.setText(R.string.checkout__next);

        } else if (number == 2) {
            binding.nextButton.setVisibility(View.VISIBLE);
            binding.prevButton.setVisibility(View.VISIBLE);
            binding.step2View.setBackgroundColor(getResources().getColor(R.color.global__primary));
            binding.step3View.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
            binding.keepShoppingButton.setVisibility(View.GONE);
            binding.paymentImageView.setImageResource(R.drawable.baseline_circle_line_uncheck_24);
            binding.shippingImageView.setImageResource(R.drawable.baseline_circle_line_check_24);

            binding.nextButton.setText(R.string.checkout__next);
            binding.prevButton.setText(R.string.back);

        } else if (number == 3) {
            binding.nextButton.setVisibility(View.VISIBLE);
            binding.prevButton.setVisibility(View.VISIBLE);
            binding.keepShoppingButton.setVisibility(View.GONE);
            binding.step3View.setBackgroundColor(getResources().getColor(R.color.global__primary));
            binding.paymentImageView.setImageResource(R.drawable.baseline_circle_line_check_24);
            binding.successImageView.setImageResource(R.drawable.baseline_circle_line_uncheck_24);

            binding.nextButton.setText(R.string.checkout);
            binding.prevButton.setText(R.string.back);
        } else if (number == 4) {
            binding.constraintLayout25.setVisibility(View.GONE);
            binding.nextButton.setVisibility(View.GONE);
            binding.prevButton.setVisibility(View.GONE);
            binding.keepShoppingButton.setVisibility(View.VISIBLE);
            binding.paymentImageView.setImageResource(R.drawable.baseline_circle_line_check_24);
            binding.successImageView.setImageResource(R.drawable.baseline_circle_line_check_24);
        }

    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

    public User getCurrentUser() {
        return this.user;
    }

    public void goToFragment4() {
        navigateFragment(binding, 4);
        number = 4;
    }

    @Override
    public void onBackPressed() {
    }
}
