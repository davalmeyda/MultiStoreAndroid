package com.panaceasoft.psmultistore.ui.user;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentUserForgotPasswordBinding;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.user.UserViewModel;

/**
 * UserForgotPasswordFragment
 */
public class UserForgotPasswordFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentUserForgotPasswordBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion

    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserForgotPasswordBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_forgot_password, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        //prgDialog.get().setMessage(getString(R.string.message__please_wait));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);


        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        binding.get().loginButton.setOnClickListener(view ->
                Utils.navigateToLogin(UserForgotPasswordFragment.this.getActivity(), navigationController));

        binding.get().forgotPasswordButton.setOnClickListener(view -> {
            if (connectivity.isConnected()) {
                forgotPassword();
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
                psDialogMsg.show();
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

    }

    //endregion


    //region Private Methods

    private void updateForgotBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().forgotPasswordButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().forgotPasswordButton.setText(getResources().getString(R.string.forgot_password__title));
        }
    }

    private void forgotPassword() {

        Utils.hideKeyboard(getActivity());

        String email = binding.get().emailEditText.getText().toString().trim();
        if (email.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        userViewModel.isLoading = true;

        userViewModel.forgotPassword(email).observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();
                        updateForgotBtnStatus();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            psDialogMsg.showSuccessDialog(listResource.data.message, getString(R.string.app__ok));
                            psDialogMsg.show();

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();

                            updateForgotBtnStatus();
                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        binding.get().forgotPasswordButton.setText(getResources().getString(R.string.forgot_password__title));
                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default
                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();
                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


        });
    }

    //endregion


}
