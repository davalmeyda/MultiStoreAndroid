package com.panaceasoft.psmultistore.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.CheckoutFragment1Binding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.user.UserViewModel;
import com.panaceasoft.psmultistore.viewobject.City;
import com.panaceasoft.psmultistore.viewobject.Country;
import com.panaceasoft.psmultistore.viewobject.User;


public class CheckoutFragment1 extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private UserViewModel userViewModel;
    private PSDialogMsg psDialogMsg;
//    public String countryId = Constants.NO_DATA;
//    public String cityId = Constants.NO_DATA;

    @VisibleForTesting
    private AutoClearedValue<CheckoutFragment1Binding> binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CheckoutFragment1Binding dataBinding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_1, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();

    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(this.getActivity(), true);

        if (getActivity() instanceof CheckoutActivity) {
            ((CheckoutActivity) getActivity()).progressDialog.setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
            ((CheckoutActivity) getActivity()).progressDialog.setCancelable(false);
        }

        if (userViewModel.user != null) {
            if (userViewModel.user.billingFirstName != null && !userViewModel.user.billingFirstName.equals("")) {
                binding.get().card2FirstNameEditText.setText(this.userViewModel.user.billingFirstName);
            } else {
                binding.get().card2FirstNameEditText.setText(this.userViewModel.user.userName);
            }

            if (userViewModel.user.billingLastName != null && !userViewModel.user.billingLastName.equals("")) {
                binding.get().card2LastNameEditText.setText(this.userViewModel.user.billingLastName);
            }

            if (userViewModel.user.billingEmail != null && !userViewModel.user.billingEmail.equals("")) {
                binding.get().card2EmailEditText.setText(this.userViewModel.user.billingEmail);
            } else {
                binding.get().card2EmailEditText.setText(this.userViewModel.user.userEmail);
            }

            if (userViewModel.user.billingPhone != null && !userViewModel.user.billingPhone.equals("")) {
                binding.get().card2PhoneEditText.setText(this.userViewModel.user.billingPhone);
            }

            if (userViewModel.user.billingCompany != null && !userViewModel.user.billingCompany.equals("")) {
                binding.get().card2CompanyEditText.setText(this.userViewModel.user.billingCompany);
            }

            if (userViewModel.user.billingAddress1 != null && !userViewModel.user.billingAddress1.equals("")) {
                binding.get().card2Address1EditText.setText(this.userViewModel.user.billingAddress1);
            }

            if (userViewModel.user.billingAddress2 != null && !userViewModel.user.billingAddress2.equals("")) {
                binding.get().card2Address2EditText.setText(this.userViewModel.user.billingAddress2);
            }

            if (userViewModel.user.billingCountry != null && !userViewModel.user.billingCountry.equals("")) {
                binding.get().card2CountryEditText.setText(this.userViewModel.user.billingCountry);
            }

            if (userViewModel.user.billingState != null && !userViewModel.user.billingState.equals("")) {
                binding.get().card2StateEditText.setText(this.userViewModel.user.billingState);
            }

            if (userViewModel.user.billingCity != null && !userViewModel.user.billingCity.equals("")) {
                binding.get().card2CityEditText.setText(this.userViewModel.user.billingCity);
            }

            if (userViewModel.user.billingPostalCode != null && !userViewModel.user.billingPostalCode.equals("")) {
                binding.get().card2PostalEditText.setText(this.userViewModel.user.billingPostalCode);
            }

            //Shipping Address

            if (userViewModel.user.shippingFirstName != null && !userViewModel.user.shippingFirstName.equals("")) {
                binding.get().firstNameEditText.setText(this.userViewModel.user.shippingFirstName);
            } else {
                binding.get().firstNameEditText.setText(this.userViewModel.user.userName);
            }

            if (userViewModel.user.shippingLastName != null && !userViewModel.user.shippingLastName.equals("")) {
                binding.get().lastNameEditText.setText(this.userViewModel.user.shippingLastName);
            }

            if (userViewModel.user.shippingEmail != null && !userViewModel.user.shippingEmail.equals("")) {
                binding.get().emailEditText.setText(this.userViewModel.user.shippingEmail);
            } else {
                binding.get().emailEditText.setText(this.userViewModel.user.userEmail);
            }

            if (userViewModel.user.shippingPhone != null && !userViewModel.user.shippingPhone.equals("")) {
                binding.get().phoneEditText.setText(this.userViewModel.user.shippingPhone);
            }

            if (userViewModel.user.shippingCompany != null && !userViewModel.user.shippingCompany.equals("")) {
                binding.get().companyEditText.setText(this.userViewModel.user.shippingCompany);
            }

            if (userViewModel.user.shippingAddress1 != null && !userViewModel.user.shippingAddress1.equals("")) {
                binding.get().address1EditText.setText(this.userViewModel.user.shippingAddress1);
            }

            if (userViewModel.user.shippingAddress2 != null && !userViewModel.user.shippingAddress2.equals("")) {
                binding.get().address2EditText.setText(this.userViewModel.user.shippingAddress2);
            }

            if (userViewModel.user.shippingCountry != null && !userViewModel.user.shippingCountry.equals("")) {
                binding.get().countryTextView.setText(this.userViewModel.user.shippingCountry);
            }

            if (userViewModel.user.shippingState != null && !userViewModel.user.shippingState.equals("")) {
                binding.get().stateEditText.setText(this.userViewModel.user.shippingState);
            }

            if (userViewModel.user.shippingCity != null && !userViewModel.user.shippingCity.equals("")) {
                binding.get().cityTextView.setText(this.userViewModel.user.shippingCity);
            }

            if (userViewModel.user.shippingPostalCode != null && !userViewModel.user.shippingPostalCode.equals("")) {
                binding.get().postalEditText.setText(this.userViewModel.user.shippingPostalCode);
            }
        }

        binding.get().switch3.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (binding.get().switch3.isChecked()) {
                copyTheText();
            } else {
                returnToOriginalValue();
            }
        });

        binding.get().countrySelectionView.setOnClickListener(v ->
                navigationController.navigateToSearchActivityCategoryFragment(getActivity(), Constants.COUNTRY, Constants.NO_DATA, Constants.NO_DATA, userViewModel.countryId, userViewModel.cityId, selectedShopId));

        binding.get().citySelectionView.setOnClickListener(v -> {
            if (binding.get().countryTextView.getText().toString().equals(Constants.EMPTY_STRING)) {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__choose_country), getString(R.string.app__ok));

                psDialogMsg.show();

            } else {
                navigationController.navigateToSearchActivityCategoryFragment(getActivity(), Constants.CITY, Constants.NO_DATA, Constants.NO_DATA, userViewModel.countryId, userViewModel.cityId, selectedShopId);
            }
        });
    }

    @Override
    protected void initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        userViewModel.setLoginUser();
        userViewModel.getLoginUser().observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.size() > 0) {

                //fadeIn Animation
                CheckoutFragment1.this.fadeIn(binding.get().getRoot());

                userViewModel.user = listResource.get(0).user;
                userViewModel.countryId = userViewModel.user.country.id;
                userViewModel.cityId = userViewModel.user.city.id;
                if (getActivity() != null) {

                    ((CheckoutActivity) CheckoutFragment1.this.getActivity()).setCurrentUser(userViewModel.user);
                    CheckoutFragment1.this.initUIAndActions();
                }

            }
        });

        userViewModel.getUpdateUserData().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            userViewModel.setLoadingState(false);
                            if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                                ((CheckoutActivity) getActivity()).progressDialog.hide();
                            }

                            Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                            userViewModel.setLoginUser();

                            if (getActivity() != null) {
                                ((CheckoutActivity) getActivity()).navigateFragment(((CheckoutActivity) getActivity()).binding, 2);
                            }

                        }
                        break;

                    case ERROR:
                        // Error State

                        if (getActivity() != null) {
                            ((CheckoutActivity) getActivity()).number = 1;
                        }

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        psDialogMsg.okButton.setOnClickListener(v -> psDialogMsg.cancel());
                        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
                            ((CheckoutActivity) getActivity()).progressDialog.hide();
                        }

                        userViewModel.setLoadingState(false);
                        break;
                    default:
                        break;
                }

            }
        });

    }

    private void copyTheText() {

        binding.get().card2FirstNameEditText.setText(binding.get().firstNameEditText.getText());
        binding.get().card2LastNameEditText.setText(binding.get().lastNameEditText.getText());
        binding.get().card2EmailEditText.setText(binding.get().emailEditText.getText());
        binding.get().card2PhoneEditText.setText(binding.get().phoneEditText.getText());
        binding.get().card2CompanyEditText.setText(binding.get().companyEditText.getText());
        binding.get().card2Address1EditText.setText(binding.get().address1EditText.getText());
        binding.get().card2Address2EditText.setText(binding.get().address2EditText.getText());
        binding.get().card2CountryEditText.setText(binding.get().countryTextView.getText());
        binding.get().card2CityEditText.setText(binding.get().cityTextView.getText());
        binding.get().card2StateEditText.setText(binding.get().stateEditText.getText());
        binding.get().card2PostalEditText.setText(binding.get().postalEditText.getText());
    }

    private void returnToOriginalValue() {

        if (userViewModel.user.billingFirstName != null && !userViewModel.user.billingFirstName.equals("")) {
            binding.get().card2FirstNameEditText.setText(this.userViewModel.user.billingFirstName);
        }

        if (userViewModel.user.billingLastName != null && !userViewModel.user.billingLastName.equals("")) {
            binding.get().card2LastNameEditText.setText(this.userViewModel.user.billingLastName);
        }

        if (userViewModel.user.billingEmail != null && !userViewModel.user.billingEmail.equals("")) {
            binding.get().card2EmailEditText.setText(this.userViewModel.user.billingEmail);
        }

        if (userViewModel.user.billingPhone != null && !userViewModel.user.billingPhone.equals("")) {
            binding.get().card2PhoneEditText.setText(this.userViewModel.user.billingPhone);
        }

        if (userViewModel.user.billingCompany != null && !userViewModel.user.billingCompany.equals("")) {
            binding.get().card2CompanyEditText.setText(this.userViewModel.user.billingCompany);
        }

        if (userViewModel.user.billingAddress1 != null && !userViewModel.user.billingAddress1.equals("")) {
            binding.get().card2Address1EditText.setText(this.userViewModel.user.billingAddress1);
        }

        if (userViewModel.user.billingAddress2 != null && !userViewModel.user.billingAddress2.equals("")) {
            binding.get().card2Address2EditText.setText(this.userViewModel.user.billingAddress2);
        }

        if (userViewModel.user.billingCountry != null && !userViewModel.user.billingCountry.equals("")) {
            binding.get().card2CountryEditText.setText(this.userViewModel.user.billingCountry);
        }

        if (userViewModel.user.billingState != null && !userViewModel.user.billingState.equals("")) {
            binding.get().card2StateEditText.setText(this.userViewModel.user.billingState);
        }

        if (userViewModel.user.billingCity != null && !userViewModel.user.billingCity.equals("")) {
            binding.get().card2CityEditText.setText(this.userViewModel.user.billingCity);
        }

        if (userViewModel.user.billingPostalCode != null && !userViewModel.user.billingPostalCode.equals("")) {
            binding.get().card2PostalEditText.setText(this.userViewModel.user.billingPostalCode);
        }

    }

    boolean checkToUpdateProfile() {

        return binding.get().firstNameEditText.getText().toString().equals(userViewModel.user.shippingFirstName) &&
                binding.get().lastNameEditText.getText().toString().equals(userViewModel.user.shippingLastName) &&
                binding.get().companyEditText.getText().toString().equals(userViewModel.user.shippingCompany) &&
                binding.get().address1EditText.getText().toString().equals(userViewModel.user.shippingAddress1) &&
                binding.get().address2EditText.getText().toString().equals(userViewModel.user.shippingAddress2) &&
                binding.get().countryTextView.getText().toString().equals(userViewModel.user.shippingCountry) &&
                binding.get().stateEditText.getText().toString().equals(userViewModel.user.shippingState) &&
                binding.get().cityTextView.getText().toString().equals(userViewModel.user.shippingCity) &&
                binding.get().postalEditText.getText().toString().equals(userViewModel.user.shippingPostalCode) &&
                binding.get().emailEditText.getText().toString().equals(userViewModel.user.shippingEmail) &&
                binding.get().phoneEditText.getText().toString().equals(userViewModel.user.shippingPhone) &&
                binding.get().card2FirstNameEditText.getText().toString().equals(userViewModel.user.billingFirstName) &&
                binding.get().card2LastNameEditText.getText().toString().equals(userViewModel.user.billingLastName) &&
                binding.get().card2CompanyEditText.getText().toString().equals(userViewModel.user.billingCompany) &&
                binding.get().card2Address1EditText.getText().toString().equals(userViewModel.user.billingAddress1) &&
                binding.get().card2Address2EditText.getText().toString().equals(userViewModel.user.billingAddress2) &&
                binding.get().card2CountryEditText.getText().toString().equals(userViewModel.user.billingCountry) &&
                binding.get().card2StateEditText.getText().toString().equals(userViewModel.user.billingState) &&
                binding.get().card2CityEditText.getText().toString().equals(userViewModel.user.billingCity) &&
                binding.get().card2PostalEditText.getText().toString().equals(userViewModel.user.billingPostalCode) &&
                binding.get().card2EmailEditText.getText().toString().equals(userViewModel.user.billingEmail) &&
                binding.get().card2PhoneEditText.getText().toString().equals(userViewModel.user.billingPhone);
    }

    boolean checkShippingAddressEditTextIsEmpty(){
        return binding.get().address1EditText.getText().toString().isEmpty();
    }

    boolean checkBillingAddressEditTextIsEmpty(){
        return binding.get().card2Address1EditText.getText().toString().isEmpty();
    }
    void updateUserProfile() {

        User user = new User(userViewModel.user.userId,
                userViewModel.user.userIsSysAdmin,
                userViewModel.user.isShopAdmin,
                userViewModel.user.facebookId,
                userViewModel.user.googleId,
                userViewModel.user.userName,
                userViewModel.user.userEmail,
                userViewModel.user.userPhone,
                userViewModel.user.userPassword,
                userViewModel.user.userAboutMe,
                userViewModel.user.userCoverPhoto,
                userViewModel.user.userProfilePhoto,
                userViewModel.user.roleId,
                userViewModel.user.status,
                userViewModel.user.isBanned,
                userViewModel.user.addedDate,
                binding.get().card2FirstNameEditText.getText().toString(),
                binding.get().card2LastNameEditText.getText().toString(),
                binding.get().card2CompanyEditText.getText().toString(),
                binding.get().card2Address1EditText.getText().toString(),
                binding.get().card2Address2EditText.getText().toString(),
                binding.get().card2CountryEditText.getText().toString(),
                binding.get().card2StateEditText.getText().toString(),
                binding.get().card2CityEditText.getText().toString(),
                binding.get().card2PostalEditText.getText().toString(),
                binding.get().card2EmailEditText.getText().toString(),
                binding.get().card2PhoneEditText.getText().toString(),
                binding.get().firstNameEditText.getText().toString(),
                binding.get().lastNameEditText.getText().toString(),
                binding.get().companyEditText.getText().toString(),
                binding.get().address1EditText.getText().toString(),
                binding.get().address2EditText.getText().toString(),
                binding.get().countryTextView.getText().toString(),
                binding.get().stateEditText.getText().toString(),
                binding.get().cityTextView.getText().toString(),
                binding.get().postalEditText.getText().toString(),
                binding.get().emailEditText.getText().toString(),
                binding.get().phoneEditText.getText().toString(),
                userViewModel.user.deviceToken,
                userViewModel.user.code,
                userViewModel.user.verifyTypes,
                userViewModel.user.addedDateStr,
                new Country(userViewModel.countryId,
                        binding.get().countryTextView.getText().toString(),
                        null, null, null, null,
                        null, null, null),
                new City(userViewModel.cityId,
                        binding.get().cityTextView.getText().toString(),
                        userViewModel.countryId, null, null,
                        null, null, null, null, null));


        if (getActivity() != null && getActivity() instanceof CheckoutActivity) {
            ((CheckoutActivity) getActivity()).progressDialog.show();
        }

        userViewModel.setUpdateUserObj(user);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_COUNTRY) {

            userViewModel.countryId = data.getStringExtra(Constants.COUNTRY_ID);
            binding.get().countryTextView.setText(data.getStringExtra(Constants.COUNTRY_NAME));

            userViewModel.cityId = Constants.EMPTY_STRING;
            binding.get().cityTextView.setText("");
            if ((getActivity()) != null) {
                ((CheckoutActivity) getActivity()).user.city.id = userViewModel.cityId;
            }

        } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CITY) {

            userViewModel.cityId = data.getStringExtra(Constants.CITY_ID);
            binding.get().cityTextView.setText(data.getStringExtra(Constants.CITY_NAME));

            if ((getActivity()) != null) {
                ((CheckoutActivity) getActivity()).user.city.id = userViewModel.cityId;
            }
        }
    }
}
