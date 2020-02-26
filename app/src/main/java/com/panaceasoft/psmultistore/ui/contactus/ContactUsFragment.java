package com.panaceasoft.psmultistore.ui.contactus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentContactUsBinding;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.contactus.ContactUsViewModel;
import com.panaceasoft.psmultistore.viewobject.common.Status;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;


public class ContactUsFragment extends PSFragment {
    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ContactUsViewModel contactUsViewModel;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentContactUsBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentContactUsBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact_us, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        psDialogMsg = new PSDialogMsg(getActivity(), false);

        psDialogMsg = new PSDialogMsg(this.getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

//        binding.get().contactNameTextInput.setHint(getString(R.string.contact_us__name));
//        binding.get().contactNameTextInput.setText(getString(R.string.contact_us__name));

        binding.get().btnSubmit.setOnClickListener(view -> {

            if (connectivity.isConnected()) {
                String contactName = binding.get().contactNameTextInput.getText().toString().trim();
                String contactEmail = binding.get().contactEmailTextInput.getText().toString().trim();
                String contactDesc = binding.get().contactDescEditText.getText().toString().trim();
                String contactPhone = binding.get().contactPhoneTextInput.getText().toString().trim();

                if (contactName.equals("")) {

                    psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok));

                    psDialogMsg.show();

                    return;
                }

                if (contactEmail.equals("")) {

                    psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));
                    psDialogMsg.show();
                    return;
                }

                if (contactDesc.equals("")) {

                    psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_contact_message), getString(R.string.app__ok));
                    psDialogMsg.show();
                    return;
                }


                if (!contactUsViewModel.isLoading) {
                    contactUsViewModel.contactName = contactName;
                    contactUsViewModel.contactEmail = contactEmail;
                    contactUsViewModel.contactDesc = contactDesc;
                    contactUsViewModel.contactPhone = contactPhone;
                    doSubmit();
                }

            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        });
    }

    @Override
    protected void initViewModels() {
        contactUsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactUsViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

    }

    //endregion

    //region Methods

    private void doSubmit() {

        prgDialog.get().show();

        contactUsViewModel.postContactUs(Config.API_KEY, contactUsViewModel.contactName, contactUsViewModel.contactEmail, contactUsViewModel.contactDesc, contactUsViewModel.contactPhone);

        contactUsViewModel.getPostContactUsData().observe(this, state -> {
            if (state != null && state.status == Status.SUCCESS) {
                //Success
                binding.get().contactNameTextInput.setText("");
                binding.get().contactEmailTextInput.setText("");
                binding.get().contactPhoneTextInput.setText("");
                binding.get().contactDescEditText.setText("");

                psDialogMsg.showSuccessDialog(getString(R.string.message_sent), getString(R.string.app__ok));
                psDialogMsg.show();

                contactUsViewModel.setLoadingState(false);
            }
        });

        contactUsViewModel.getLoadingState().observe(this, state -> {
            if (state != null && state) {
                prgDialog.get().show();
            } else {
                prgDialog.get().cancel();
            }
        });

    }

    //endregion

}
