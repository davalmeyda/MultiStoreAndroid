package com.panaceasoft.psmultistore.ui.shop.detail;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.FragmentShopBinding;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.detail.ProductDetailFragment;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.ShopViewModel;
import com.panaceasoft.psmultistore.viewobject.Shop;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;


public class ShopFragment extends PSFragment {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ShopViewModel shopViewModel;

    private static final int REQUEST_CALL = 1;
    private MenuItem basketMenuItem;
    private BasketViewModel basketViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentShopBinding> binding;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentShopBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false, dataBindingComponent);

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
    protected void initUIAndActions() {

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        binding.get().aboutImageView.setOnClickListener(view ->
                navigationController.navigateToGalleryActivity(getActivity(), Constants.IMAGE_TYPE_ABOUT, shopViewModel.selectedShopId));


        //For Phone 1
        binding.get().phoneTextView.setOnClickListener(view1 -> {

            String number = binding.get().phoneTextView.getText().toString();
            if (!(number.trim().isEmpty() || number.trim().equals("-"))) {
                Utils.callPhone(ShopFragment.this, number);
            }
        });


        //For phone 2
        binding.get().phone1TextView.setOnClickListener(view -> {

            String number1 = binding.get().phone1TextView.getText().toString();
            if (!(number1.trim().isEmpty() || number1.trim().equals("-"))) {
                Utils.callPhone(ShopFragment.this, number1);
            }
        });


        //For phone 3
        binding.get().phone2TextView.setOnClickListener(view -> {

            String number2 = binding.get().phone2TextView.getText().toString();
            if (!(number2.trim().isEmpty() || number2.trim().equals("-"))) {
                Utils.callPhone(ShopFragment.this, number2);
            }
        });


        //For phone 4
        binding.get().phone3TextView.setOnClickListener(view -> {

            String number3 = binding.get().phone3TextView.getText().toString();
            if (!(number3.trim().isEmpty() || number3.trim().equals("-"))) {
                Utils.callPhone(ShopFragment.this, number3);
            }

        });

        //For email
        binding.get().emailTextView.setOnClickListener(view -> {
            try {
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType(Constants.EMAIL_TYPE);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{String.valueOf(binding.get().emailTextView.getText())});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.hello));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
            } catch (Exception e) {
                Utils.psErrorLog("doEmail", e);
            }
        });

        //For website
        binding.get().WebsiteTextView.setOnClickListener(view -> {

            if (binding.get().WebsiteTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().WebsiteTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().WebsiteTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }

        });

        //For facebook
        binding.get().facebookTextView.setOnClickListener(view -> {

            if (binding.get().facebookTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().facebookTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().facebookTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        //For google plus
        binding.get().gplusTextView.setOnClickListener(view -> {

            if (binding.get().gplusTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().gplusTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().gplusTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        //For twitter
        binding.get().twitterTextView.setOnClickListener(view -> {

            if (binding.get().twitterTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().twitterTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().twitterTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }

        });

        //For instagram
        binding.get().instaTextView.setOnClickListener(view -> {

            if (binding.get().instaTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().instaTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().instaTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        //For youtube
        binding.get().youtubeTextView.setOnClickListener(view -> {

            if (binding.get().youtubeTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().youtubeTextView.getText().toString().startsWith(Constants.HTTPS)) {
                String url = binding.get().youtubeTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }

        });

        //For pinterest
        binding.get().pinterestTextView.setOnClickListener(view -> {

            if (binding.get().pinterestTextView.getText().toString().startsWith(Constants.HTTP) || binding.get().pinterestTextView.getText().toString().startsWith(Constants.HTTPS) ) {
                String url = binding.get().pinterestTextView.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        binding.get().shopPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number1 = binding.get().phone1TextView.getText().toString();
                if (!(number1.trim().isEmpty() || number1.trim().equals("-"))) {
                    Utils.callPhone(ShopFragment.this, number1);
                }
            }
        });
    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel.class);
    }

    @Override
    protected void initAdapters() {
    }

    //  private  void replaceAboutUsData()
    @Override
    protected void initData() {

        basketData();

        shopViewModel.setShopObj(Config.API_KEY, selectedShopId);
        shopViewModel.getShopData().observe(this, resource -> {

            if (resource != null) {

                Utils.psLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            fadeIn(binding.get().getRoot());

                            binding.get().setShop(resource.data);
                            setAboutUsData(resource.data);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {

                            binding.get().setShop(resource.data);
                            setAboutUsData(resource.data);
                        }

                        break;
                    case ERROR:
                        // Error State

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of About Us.");


            } else {
                //noinspection ConstantConditions
                Utils.psLog("No Data of About Us.");
            }
        });

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

    private void setAboutUsData(Shop shop) {

        binding.get().setShop(shop);

        shopViewModel.selectedShopId = shop.id;

        // For Contact
        // For SourceAddress

        binding.get().textView36.setText(binding.get().textView36.getText().toString());
        binding.get().textView39.setText(binding.get().textView39.getText().toString());

        // For Address 1
        if (shop.address1.equals("")) {
            binding.get().address1.setText(Constants.DASH);
        } else {

            binding.get().address1.setText(shop.address1);
        }

        // For Address 2
        if (shop.address2.equals("")) {
            binding.get().address2.setText(Constants.DASH);
        } else {
            binding.get().address2.setText(shop.address2);
        }

        // For Address 3
        if (shop.address3.equals("")) {
            binding.get().address3.setText(Constants.DASH);
        } else {
            binding.get().address3.setText(shop.address3);
        }

        //For Pinterest.com
        if (shop.pinterest.equals("")) {
            binding.get().pinterestTextView.setText(Constants.DASH);

        } else {
            binding.get().pinterestTextView.setText(shop.pinterest);
        }

        // For youtube.com
        if (shop.youtube.equals("")) {
            binding.get().youtubeTextView.setText(Constants.DASH);
        } else {
            binding.get().youtubeTextView.setText(shop.youtube);
        }

        // For instagram.com
        if (shop.instagram.equals("")) {
            binding.get().instaTextView.setText(Constants.DASH);
        } else {
            binding.get().instaTextView.setText(shop.instagram);
        }

        // For twitter.com
        if (shop.twitter.equals("")) {
            binding.get().twitterTextView.setText(Constants.DASH);
        } else {
            binding.get().twitterTextView.setText(shop.twitter);
        }

        // For googleplus.com
        if (shop.googlePlus.equals("")) {
            binding.get().gplusTextView.setText(Constants.DASH);
        } else {
            binding.get().gplusTextView.setText(shop.googlePlus);
        }

        // For facebook.com
        if (shop.facebook.equals("")) {
            binding.get().facebookTextView.setText(Constants.DASH);
        } else {
            binding.get().facebookTextView.setText(shop.facebook);
        }

        // For website.com
        if (shop.aboutWebsite.equals("")) {
            binding.get().WebsiteTextView.setText(Constants.DASH);
        } else {
            binding.get().WebsiteTextView.setText(shop.aboutWebsite);
        }

        // For email
        if (shop.email.equals("")) {
            binding.get().emailTextView.setText(Constants.DASH);
        } else {
            binding.get().emailTextView.setText(shop.email);
        }

        // For shop phone
        if (shop.aboutPhone1.equals("")) {
            binding.get().phoneTextView.setText(Constants.DASH);
        } else {
            binding.get().phoneTextView.setText(shop.aboutPhone1);
        }

        // For phone1
        if (shop.aboutPhone1.equals("")) {
            binding.get().phone1TextView.setText(Constants.DASH);
        } else {
            binding.get().phone1TextView.setText(shop.aboutPhone1);
        }

        // For phone2
        if (shop.aboutPhone2.equals("")) {
            binding.get().phone2TextView.setText(Constants.DASH);
        } else {
            binding.get().phone2TextView.setText(shop.aboutPhone2);
        }

        // For phone3
        if (shop.aboutPhone3.equals("")) {
            binding.get().phone3TextView.setText(Constants.DASH);
        } else {
            binding.get().phone3TextView.setText(shop.aboutPhone3);
        }


        // For description
        if (shop.description.equals("")) {
            binding.get().descTextView.setText(Constants.DASH);
        } else {
            binding.get().descTextView.setText(shop.description);
        }

        // For title
        if (shop.name.equals("")) {
            binding.get().titleTextView.setText(Constants.DASH);
        } else {
            binding.get().titleTextView.setText(shop.name);
        }

    }
    //endregion

}