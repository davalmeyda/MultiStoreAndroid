package com.panaceasoft.psmultistore.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.MainActivity;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentUserLoginBinding;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.user.UserViewModel;
import com.panaceasoft.psmultistore.viewobject.User;

import org.json.JSONException;

import java.util.Collections;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;


/**
 * UserLoginFragment
 */
public class UserLoginFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    private boolean checkFlag;

    @VisibleForTesting
    private AutoClearedValue<FragmentUserLoginBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    private String token;


    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);


        callbackManager = CallbackManager.Factory.create();
        // Inflate the layout for this fragment
        FragmentUserLoginBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_login, container, false, dataBindingComponent);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if (getActivity() != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        }

        binding = new AutoClearedValue<>(this, dataBinding);

        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //fadeIn Animation
        fadeIn(binding.get().getRoot());

        // Configure Google Sign In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if (getActivity() != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
        }

        binding.get().loginButton.setOnClickListener(view -> {

            Utils.hideKeyboard(getActivity());

            if (connectivity.isConnected()) {
                String userEmail = binding.get().emailEditText.getText().toString().trim();
                String userPassword = binding.get().passwordEditText.getText().toString().trim();

                Utils.psLog("Email " + userEmail);
                Utils.psLog("Password " + userPassword);

                if (userEmail.equals("")) {

                    psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));
                    psDialogMsg.show();
                    return;
                }

                if (userPassword.equals("")) {

                    psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_password), getString(R.string.app__ok));
                    psDialogMsg.show();
                    return;
                }

                if (!userViewModel.isLoading) {

                    updateLoginBtnStatus();

                    doSubmit(userEmail, userPassword);

                }
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
                psDialogMsg.show();
            }

        });

        binding.get().registerButton.setOnClickListener(view -> {

            if (getActivity() instanceof MainActivity) {
                navigationController.navigateToUserRegister((MainActivity) getActivity());
            } else {

                navigationController.navigateToUserRegisterActivity(getActivity());

                try {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in closing activity.", e);
                }
            }
        });

        binding.get().forgotPasswordButton.setOnClickListener(view -> {

            if (getActivity() instanceof MainActivity) {
                navigationController.navigateToUserForgotPassword((MainActivity) getActivity());
            } else {

                navigationController.navigateToUserForgotPasswordActivity(getActivity());

                try {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in closing activity.", e);
                }
            }
        });

        //google login
        binding.get().googleSignInButton.setOnClickListener(v -> signIn());


        //for check privacy and policy
        binding.get().privacyPolicyCheckbox.setOnClickListener(v -> {
            if (binding.get().privacyPolicyCheckbox.isChecked()) {
                navigationController.navigateToPrivacyPolicyActivity(getActivity());
                checkFlag = true;
                binding.get().view29.setVisibility(View.GONE);
                binding.get().view31.setVisibility(View.GONE);
                binding.get().fbLoginButton.setEnabled(true);
                binding.get().googleSignInButton.setEnabled(true);
            } else {
                checkFlag = false;
                binding.get().view29.setVisibility(View.VISIBLE);
                binding.get().view31.setVisibility(View.VISIBLE);
                binding.get().fbLoginButton.setEnabled(false);
                binding.get().googleSignInButton.setEnabled(false);
            }
        });

        if (!checkFlag) {
            binding.get().view29.setVisibility(View.VISIBLE);
            binding.get().view31.setVisibility(View.VISIBLE);
            binding.get().fbLoginButton.setEnabled(false);
        } else {
            binding.get().view29.setVisibility(View.GONE);
            binding.get().view31.setVisibility(View.GONE);
            binding.get().fbLoginButton.setEnabled(true);
        }

        View.OnClickListener onAgreementClickListener = v -> Toast.makeText(getContext(), getString(R.string.error_message__to_check_agreement), Toast.LENGTH_SHORT).show();
        binding.get().view29.setOnClickListener(onAgreementClickListener);
        binding.get().view31.setOnClickListener(onAgreementClickListener);

        if (Config.ENABLE_FACEBOOK_LOGIN && Config.ENABLE_GOOGLE_LOGIN) {
            binding.get().fbLoginButton.setVisibility(View.VISIBLE);
            binding.get().googleSignInButton.setVisibility(View.VISIBLE);
            binding.get().privacyPolicyCheckbox.setVisibility(View.VISIBLE);
        } else if (Config.ENABLE_FACEBOOK_LOGIN) {
            binding.get().fbLoginButton.setVisibility(View.VISIBLE);
            binding.get().googleSignInButton.setVisibility(View.GONE);
            binding.get().privacyPolicyCheckbox.setVisibility(View.VISIBLE);
        } else if (Config.ENABLE_GOOGLE_LOGIN) {
            binding.get().fbLoginButton.setVisibility(View.GONE);
            binding.get().googleSignInButton.setVisibility(View.VISIBLE);
            binding.get().privacyPolicyCheckbox.setVisibility(View.VISIBLE);
        } else {
            binding.get().fbLoginButton.setVisibility(View.GONE);
            binding.get().googleSignInButton.setVisibility(View.GONE);
            binding.get().privacyPolicyCheckbox.setVisibility(View.GONE);
        }

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.REQUEST_CODE__GOOGLE_SIGN);
    }

    private void updateLoginBtnStatus() {
        if (userViewModel.isLoading) {
            binding.get().loginButton.setText(getResources().getString(R.string.message__loading));
        } else {
            binding.get().loginButton.setText(getResources().getString(R.string.login__login));
        }
    }

    private void doSubmit(String email, String password) {

        //prgDialog.get().show();
        userViewModel.setUserLogin(new User(
                "",
                "",
                "",
                "",
                "",
                email,
                email,
                "",
                password,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "", null, null
        ));

        userViewModel.isLoading = true;

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

        token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);

        userViewModel.getLoadingState().observe(this, loadingState -> {

            if (loadingState != null && loadingState) {
                prgDialog.get().show();
            } else {
                prgDialog.get().cancel();
            }

            updateLoginBtnStatus();

        });

        userViewModel.getUserLoginStatus().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {

                                Utils.updateUserLoginData(pref, listResource.data.user);
                                Utils.navigateAfterUserLogin(getActivity(),navigationController);

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.setLoadingState(false);


                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        userViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        userViewModel.setLoadingState(false);

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
        });

        binding.get().fbLoginButton.setFragment(this);
        binding.get().fbLoginButton.setPermissions(Collections.singletonList("email"));
        binding.get().fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {

                            String name = "";
                            String email = "";
                            String id = "";
                            String imageURL = "";
                            try {
                                if (object != null) {

                                    name = object.getString("name");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (object != null) {

                                    email = object.getString("email");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                if (object != null) {

                                    id = object.getString("id");

                                }
                                //link.setText(object.getString("link"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (!id.equals("")) {
                                prgDialog.get().show();
                                userViewModel.registerFBUser(id, name, email, imageURL, token);
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,name,id");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                Utils.psLog("OnCancel.");
            }

            @Override
            public void onError(FacebookException e) {
                Utils.psLog("OnError.");
            }


        });


        userViewModel.getRegisterFBUserData().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {
                                Utils.updateUserLoginData(pref, listResource.data.user);
                                Utils.navigateAfterUserLogin(getActivity(),navigationController);


                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();

                        }

                        break;
                    case ERROR:
                        // Error State

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default
                        //userViewModel.isLoading = false;
                        //prgDialog.get().cancel();
                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

        });

        // Tmp Later to delete
        userViewModel.getUserLoginStatus().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {

                                if (getActivity() != null) {

                                    pref.edit().putString(Constants.USER_ID, listResource.data.userId).apply();

                                }

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.setLoadingState(false);

                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.profile__title));
                                navigationController.navigateToUserProfile((MainActivity) getActivity());
                            } else {
                                try {
                                    if (getActivity() != null) {
                                        getActivity().finish();
                                    }
                                } catch (Exception e) {
                                    Utils.psErrorLog("Error in closing parent activity.", e);
                                }
                            }

                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        userViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        userViewModel.setLoadingState(false);

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }
        });


        userViewModel.getRegisterGoogleUserData().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {

                                Utils.updateUserLoginData(pref,listResource.data.user);
                                Utils.navigateAfterUserLogin(getActivity(),navigationController);

                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();

                        }

                        break;
                    case ERROR:
                        // Error State

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

        });

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        if (getActivity() != null) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                userViewModel.setRegisterGoogleUser(user.getDisplayName(), user.getEmail(),user.getUid(), String.valueOf(user.getPhotoUrl()), token);

                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(UserLoginFragment.this.getActivity(), "SignIn Failed", Toast.LENGTH_LONG).show();

                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.REQUEST_CODE__GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


}


//endregion

