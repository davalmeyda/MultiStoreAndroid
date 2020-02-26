package com.panaceasoft.psmultistore.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentProfileEditBinding;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.user.UserViewModel;
import com.panaceasoft.psmultistore.viewobject.User;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

/**
 * ProfileEditFragment
 */
public class ProfileEditFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;
    public String countryId = Constants.NO_DATA;
    public String cityId = Constants.NO_DATA;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileEditBinding> binding;
    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        FragmentProfileEditBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
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
                Utils.psLog("Got Data");

                //fadeIn Animation
                fadeIn(binding.get().getRoot());

                binding.get().setUser(listResource.get(0).user);
                userViewModel.user = listResource.get(0).user;
                Utils.psLog("Photo : " + listResource.get(0).user.userProfilePhoto);
                userViewModel.countryId = userViewModel.user.country.id;
                userViewModel.cityId = userViewModel.user.city.id;
            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");

            }
        });

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        if (getContext() != null) {
            binding.get().userNameEditText.setHint(R.string.edit_profile__user_name);
            binding.get().userEmailEditText.setHint(R.string.edit_profile__email);
            binding.get().userPhoneEditText.setHint(R.string.edit_profile__phone);
            binding.get().userAboutMeEditText.setHint(R.string.edit_profile__about_me);

            binding.get().nameTextView.setText(getContext().getString(R.string.edit_profile__user_name));
            binding.get().emailTextView.setText(getContext().getString(R.string.edit_profile__email));
            binding.get().phoneTextView.setText(getContext().getString(R.string.edit_profile__phone));
            binding.get().aboutMeTextView.setText(getContext().getString(R.string.edit_profile__about_me));

        }

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        binding.get().profileImageView.setOnClickListener(view -> {

            if (connectivity.isConnected()) {
                try {

                    if (Utils.isStoragePermissionGranted(getActivity())) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in Image Gallery.", e);
                }
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
                psDialogMsg.show();
            }

        });

        binding.get().saveButton.setOnClickListener(view -> ProfileEditFragment.this.editProfileData());

        binding.get().passwordChangeButton.setOnClickListener(view -> navigationController.navigateToPasswordChangeActivity(getActivity()));

        binding.get().countrySelectionView.setOnClickListener(v ->
                navigationController.navigateToSearchActivityCategoryFragment(getActivity(), Constants.COUNTRY, Constants.NO_DATA, Constants.NO_DATA, userViewModel.countryId, userViewModel.cityId,Constants.EMPTY_STRING));

        binding.get().citySelectionView.setOnClickListener(v -> {
            if (binding.get().countryTextView.getText().toString().equals(Constants.EMPTY_STRING)) {

                psDialogMsg.showWarningDialog(getString(R.string.error_message__choose_country), getString(R.string.app__ok));

                psDialogMsg.show();

            } else {
                navigationController.navigateToSearchActivityCategoryFragment(getActivity(), Constants.CITY, Constants.NO_DATA, Constants.NO_DATA, userViewModel.countryId, userViewModel.cityId,Constants.EMPTY_STRING);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == Constants.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (getActivity() != null && selectedImage != null) {
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        userViewModel.profileImagePath = cursor.getString(columnIndex);
                        cursor.close();

                        uploadImage();
                    }

                }

            }

            else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_COUNTRY) {

                this.countryId = data.getStringExtra(Constants.COUNTRY_ID);
                userViewModel.countryName = data.getStringExtra(Constants.COUNTRY_NAME);
                binding.get().countryTextView.setText(userViewModel.countryName);
                userViewModel.countryId = this.countryId;
                userViewModel.user.country.id = this.countryId;

                this.cityId = "";
                userViewModel.cityId = this.cityId;
                binding.get().cityTextView.setText("");

                if ((getActivity()) != null) {
                    userViewModel.user.country.id = userViewModel.countryId;
                    userViewModel.user.city.id = userViewModel.cityId;
                }

            } else if (requestCode == Constants.REQUEST_CODE__SEARCH_FRAGMENT && resultCode == Constants.RESULT_CODE__SEARCH_WITH_CITY) {

                this.cityId = data.getStringExtra(Constants.CITY_ID);
                userViewModel.cityName = data.getStringExtra(Constants.CITY_NAME);
                binding.get().cityTextView.setText(userViewModel.cityName);
                userViewModel.cityId = this.cityId;
                userViewModel.user.city.id = this.cityId;


                if ((getActivity()) != null) {
                    userViewModel.user.city.id = userViewModel.cityId;
                }

            }


        } catch (Exception e) {
            Utils.psErrorLog("Error in load image.", e);
        }
    }


    //endregion


    //region Private Methods


    private void editProfileData() {

        if (!connectivity.isConnected()) {

            psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        String userName = binding.get().userNameEditText.getText().toString();
        if (userName.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        String userEmail = binding.get().userEmailEditText.getText().toString();
        if (userEmail.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        if(!checkToUpdateProfile())
        {
            userViewModel.user.city.name = binding.get().cityTextView.getText().toString();
            userViewModel.user.country.name = binding.get().countryTextView.getText().toString();
            updateUserProfile();
        }

        userViewModel.getUpdateUserData().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB
//                            if(listResource.data != null){
//                                fadeIn(binding.get().getRoot());
//                            }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

//                                userViewModel.updateUser(userViewModel.user);

                            psDialogMsg.showSuccessDialog(listResource.data.message, getString(R.string.app__ok));
                            psDialogMsg.show();
                            psDialogMsg.okButton.setOnClickListener(view -> psDialogMsg.cancel());


                        }
                        userViewModel.setLoadingState(false);
                        prgDialog.get().cancel();

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();
                        prgDialog.get().cancel();

                        userViewModel.setLoadingState(false);
                        break;
                    default:


                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");
            }


        });
    }


    private void uploadImage() {

        prgDialog.get().show();
        Utils.psLog("Uploading Image.");

        userViewModel.uploadImage(userViewModel.profileImagePath, loginUserId).observe(this, listResource -> {
            // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString());


                if (listResource.message != null && !listResource.message.equals("")) {
                    prgDialog.get().cancel();
                } else {
                    // Update the data
                    prgDialog.get().cancel();
                }

            } else if (listResource != null && listResource.message != null) {
                Utils.psLog("Message from server.");

                psDialogMsg.showInfoDialog(listResource.message, getString(R.string.app__ok));
                psDialogMsg.show();

                prgDialog.get().cancel();
            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");

            }

        });
    }

    private boolean checkToUpdateProfile() {

        return binding.get().userNameEditText.getText().toString().equals(userViewModel.user.userName) &&
                binding.get().userEmailEditText.getText().toString().equals(userViewModel.user.userEmail) &&
                binding.get().userPhoneEditText.getText().toString().equals(userViewModel.user.userPhone) &&
                binding.get().userAboutMeEditText.getText().toString().equals(userViewModel.user.userAboutMe) &&
                binding.get().firstNameEditText.getText().toString().equals(userViewModel.user.shippingFirstName) &&
                binding.get().lastNameEditText.getText().toString().equals(userViewModel.user.shippingLastName) &&
                binding.get().companyEditText.getText().toString().equals(userViewModel.user.shippingCompany) &&
                binding.get().address1EditText.getText().toString().equals(userViewModel.user.shippingAddress1) &&
                binding.get().address2EditText.getText().toString().equals(userViewModel.user.shippingAddress2) &&
                binding.get().countryTextView.getText().toString().equals(userViewModel.user.country.name) &&
                binding.get().stateEditText.getText().toString().equals(userViewModel.user.shippingState) &&
                binding.get().cityTextView.getText().toString().equals(userViewModel.user.city.name) &&
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

    private void updateUserProfile() {
        User user = new User(userViewModel.user.userId,
                userViewModel.user.userIsSysAdmin,
                userViewModel.user.isShopAdmin,
                userViewModel.user.facebookId,
                userViewModel.user.googleId,
                binding.get().userNameEditText.getText().toString(),
                binding.get().userEmailEditText.getText().toString(),
                binding.get().userPhoneEditText.getText().toString(),
                userViewModel.user.userPassword,
                binding.get().userAboutMeEditText.getText().toString(),
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
                userViewModel.user.country,
                userViewModel.user.city);

        userViewModel.setUpdateUserObj(user);

        prgDialog.get().show();
    }


    //endregion
}
