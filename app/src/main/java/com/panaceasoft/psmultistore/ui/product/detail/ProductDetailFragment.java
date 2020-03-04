package com.panaceasoft.psmultistore.ui.product.detail;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.binding.FragmentDataBindingComponent;
import com.panaceasoft.psmultistore.databinding.BottomBoxBasketAndBuyBinding;
import com.panaceasoft.psmultistore.databinding.FragmentProductDetailBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.PSFragment;
import com.panaceasoft.psmultistore.ui.product.adapter.ProductHorizontalListAdapter;
import com.panaceasoft.psmultistore.ui.product.detail.adapter.ProductAttributeHeaderAdapter;
import com.panaceasoft.psmultistore.ui.product.detail.adapter.ProductColorAdapter;
import com.panaceasoft.psmultistore.ui.product.detail.adapter.ProductDetailListPagerAdapter;
import com.panaceasoft.psmultistore.ui.product.detail.adapter.ProductDetailSpecsAdapter;
import com.panaceasoft.psmultistore.ui.product.detail.adapter.ProductDetailTagAdapter;
import com.panaceasoft.psmultistore.utils.AutoClearedValue;
import com.panaceasoft.psmultistore.utils.BottomSheetDialogExpanded;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.PSDialogMsg;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.utils.ViewAnimationUtil;
import com.panaceasoft.psmultistore.viewmodel.image.ImageViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.FavouriteViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductAttributeDetailViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductAttributeHeaderViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductColorViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductDetailViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductRelatedViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductSpecsViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.TouchCountViewModel;
import com.panaceasoft.psmultistore.viewobject.Image;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.ProductAttributeDetail;
import com.panaceasoft.psmultistore.viewobject.ProductAttributeHeader;
import com.panaceasoft.psmultistore.viewobject.ProductColor;
import com.panaceasoft.psmultistore.viewobject.ProductSpecs;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.common.Status;
import com.panaceasoft.psmultistore.viewobject.holder.ProductParameterHolder;
import com.panaceasoft.psmultistore.viewobject.holder.TabObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */
public class ProductDetailFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private int dotsCount;
    private ImageView[] dots;
    private int num = 1;
    private int minOrder = 0;

    private ImageViewModel imageViewModel;
    private ProductDetailViewModel productDetailViewModel;
    private ProductColorViewModel productColorViewModel;
    private ProductSpecsViewModel productSpecsViewModel;
    private ProductRelatedViewModel productRelatedViewModel;
    private ProductAttributeHeaderViewModel productAttributeHeaderViewModel;
    private ProductAttributeDetailViewModel productAttributeDetailViewModel;
    private BasketViewModel basketViewModel;
    private FavouriteViewModel favouriteViewModel;
    private TouchCountViewModel touchCountViewModel;
    private PSDialogMsg psDialogMsg;
    private String additionPrice;
    private ProductParameterHolder productParameterHolder = new ProductParameterHolder();
    private boolean available = true;
    private boolean twist = false;
    private String shopPhoneNumber = "";

    @VisibleForTesting
    private AutoClearedValue<ProductDetailListPagerAdapter> pagerAdapter;
    private AutoClearedValue<FragmentProductDetailBinding> binding;
    public AutoClearedValue<ProductDetailListPagerAdapter> adapter;
    private AutoClearedValue<ProductColorAdapter> colorAdapter;
    private AutoClearedValue<ProductDetailSpecsAdapter> specsAdapter;
    private AutoClearedValue<ProductDetailTagAdapter> tabAdapter;
    private AutoClearedValue<ProductAttributeHeaderAdapter> headerAdapter;
    private AutoClearedValue<List<TabObject>> tabObjectList;
    private AutoClearedValue<ProductHorizontalListAdapter> relatedAdapter;
    private AutoClearedValue<BottomSheetDialogExpanded> mBottomSheetDialog;
    private AutoClearedValue<BottomBoxBasketAndBuyBinding> bottomBoxLayoutBinding;
    AutoClearedValue<ViewPager> imageViewPager;
    AutoClearedValue<LinearLayout> pageIndicatorLayout;
    AutoClearedValue<Button> addToCartButton;
    AutoClearedValue<Button> buyNowButton;

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentProductDetailBinding dataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_product_detail,
                container,
                false,
                dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        setHasOptionsMenu(true);

        List<TabObject> tabObjectList1 = new ArrayList<>();
        tabObjectList = new AutoClearedValue<>(this, tabObjectList1);


//        Toast.makeText(getContext(),"Shop phone number is ::::: "+shopPhoneNumber,Toast.LENGTH_SHORT).show();
        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initUIAndActions() {

        shopPhoneNumber = pref.getString(Constants.SHOP_PHONE_NUMBER, "");

//        Toast.makeText(getContext(), "Shop phone number is ::::: " + shopPhoneNumber, Toast.LENGTH_SHORT).show();
        psDialogMsg = new PSDialogMsg(getActivity(), false);

        hideFloatingButton();

        if (getContext() != null) {

            //mBottomSheetDialog2.setCanceledOnTouchOutside(false);
            mBottomSheetDialog = new AutoClearedValue<>(this, new BottomSheetDialogExpanded(getContext()));

            BottomBoxBasketAndBuyBinding bottomBoxLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.bottom_box_basket_and_buy, null, false, dataBindingComponent);

            this.bottomBoxLayoutBinding = new AutoClearedValue<>(this, bottomBoxLayoutBinding);

            mBottomSheetDialog.get().setContentView(this.bottomBoxLayoutBinding.get().getRoot());
        }

        if (imageViewPager != null && imageViewPager.get() != null && pageIndicatorLayout.get() != null) {
            imageViewPager.get().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    if (pageIndicatorLayout.get() != null) {
                        setupSliderPagination();
                    }

                    for (int i = 0; i < dotsCount; i++) {
                        if (dots != null && dots.length > i) {
                            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                        }
                    }

                    if (dots != null && dots.length > position) {
                        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        binding.get().phoneFloatingActionButton.setOnClickListener(v -> {
            if (!(shopPhoneNumber.trim().isEmpty() || shopPhoneNumber.trim().equals("-"))) {
                Utils.callPhone(ProductDetailFragment.this, shopPhoneNumber);
            }
        });

        binding.get().mainFloatingActionButton.setOnClickListener(v -> {
            twist = Utils.twistFab(v, !twist);

            if (messenger.isEmpty()) {
                if (twist) {
                    Utils.showFab(binding.get().whatsappFloatingActionButton);
                    Utils.showFab(binding.get().whatsAppTextView);
                    Utils.showFab(binding.get().phoneFloatingActionButton);
                    Utils.showFab(binding.get().phoneTextView);
                } else {
                    Utils.hideFab(binding.get().whatsappFloatingActionButton);
                    Utils.hideFab(binding.get().whatsAppTextView);
                    Utils.hideFab(binding.get().phoneFloatingActionButton);
                    Utils.hideFab(binding.get().phoneTextView);
                }
            } else if (whatsappNo.isEmpty()) {
                if (twist) {
                    Utils.showFab(binding.get().messengerFloatingActionButton);
                    Utils.showFab(binding.get().messengerTextView);
                    Utils.showFab(binding.get().phoneFloatingActionButton);
                    Utils.showFab(binding.get().phoneTextView);
                } else {
                    Utils.hideFab(binding.get().messengerFloatingActionButton);
                    Utils.hideFab(binding.get().messengerTextView);
                    Utils.hideFab(binding.get().phoneFloatingActionButton);
                    Utils.hideFab(binding.get().phoneTextView);
                }
            } else if (shopPhoneNumber.isEmpty()) {
                Utils.psLog("******** " + shopPhoneNumber);
                if (twist) {
                    Utils.showFab(binding.get().messengerFloatingActionButton);
                    Utils.showFab(binding.get().messengerTextView);
                    Utils.showFab(binding.get().whatsappFloatingActionButton);
                    Utils.showFab(binding.get().whatsAppTextView);
                } else {
                    Utils.hideFab(binding.get().messengerFloatingActionButton);
                    Utils.hideFab(binding.get().messengerTextView);
                    Utils.hideFab(binding.get().whatsappFloatingActionButton);
                    Utils.hideFab(binding.get().whatsAppTextView);
                }
            } else {
                if (twist) {
                    Utils.showFab(binding.get().messengerFloatingActionButton);
                    Utils.showFab(binding.get().whatsappFloatingActionButton);
                    Utils.showFab(binding.get().phoneFloatingActionButton);
                    Utils.showFab(binding.get().messengerTextView);
                    Utils.showFab(binding.get().phoneTextView);
                    Utils.showFab(binding.get().whatsAppTextView);
                } else {
                    Utils.hideFab(binding.get().messengerFloatingActionButton);
                    Utils.hideFab(binding.get().whatsappFloatingActionButton);
                    Utils.hideFab(binding.get().phoneFloatingActionButton);
                    Utils.hideFab(binding.get().messengerTextView);
                    Utils.hideFab(binding.get().phoneTextView);
                    Utils.hideFab(binding.get().whatsAppTextView);
                }
            }

        });

        binding.get().messengerFloatingActionButton.setOnClickListener(v -> {

            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setPackage("com.facebook.orca");
                intent.setData(Uri.parse("https://m.me/" + messenger));
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.product_detail__install_messenger_app), Toast.LENGTH_SHORT).show();
            }

        });

        binding.get().whatsappFloatingActionButton.setOnClickListener(v -> {
            try {
                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(whatsappNo) + "@s.whatsapp.net");//phone number without "+" prefix
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.product_detail__install_whatsapp_app), Toast.LENGTH_SHORT).show();
            }

        });

        binding.get().starTextView.setOnClickListener(v -> navigationController.navigateToRatingList(ProductDetailFragment.this.getActivity(), productDetailViewModel.productId));

        binding.get().ratingBar.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {

                navigationController.navigateToRatingList(ProductDetailFragment.this.getActivity(), productDetailViewModel.productId);
            }
            return true;
        });

        addToCartButton.get().setOnClickListener(view -> {

            if (available) {
                // NOS PASAMOS DIRECTO A AGREGAR EL PRODUCTO
                confirmBox();
                // QUITANDO LOS ATRIBUTOS DEL PRODUCTO A ELEGIR
                //productDetailViewModel.isAddtoCart = true;
                //bottomBoxLayoutBinding.get().lowestButton.setText(getString(R.string.product_detail__add_to_busket));
                //mBottomSheetDialog.get().show();
            } else {
                psDialogMsg.showWarningDialog(getString(R.string.product_detail__not_available), getString(R.string.app__ok));
                psDialogMsg.show();
            }


        });

        buyNowButton.get().setOnClickListener(view -> {

            // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("David Almeyda")
                    .setTitle("BruFat");

            // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            AlertDialog dialog = builder.create();

            dialog.show();

            if (available) {
                productDetailViewModel.isAddtoCart = false;

                bottomBoxLayoutBinding.get().lowestButton.setText(getString(R.string.product_detail__buy));

                mBottomSheetDialog.get().show();
            } else {
                psDialogMsg.showWarningDialog(getString(R.string.product_detail__not_available), getString(R.string.app__ok));
                psDialogMsg.show();
            }

        });


        /*binding.get().seeAllFactButton.setOnClickListener(view -> navigationController.navigateToTermsAndConditionsActivity(getActivity(), Constants.SHOP_TERMS));

        binding.get().refundPolicyButton.setOnClickListener(view -> navigationController.navigateToTermsAndConditionsActivity(getActivity(), Constants.SHOP_REFUND));*/

    }

    private void hideFloatingButton() {
        Utils.hideFirstFab(binding.get().messengerFloatingActionButton);
        Utils.hideFirstFab(binding.get().whatsappFloatingActionButton);
        Utils.hideFirstFab(binding.get().phoneFloatingActionButton);
        Utils.hideFab(binding.get().phoneTextView);
        Utils.hideFab(binding.get().whatsAppTextView);
        Utils.hideFab(binding.get().messengerTextView);
    }

    private void setupSliderPagination() {

        dotsCount = pagerAdapter.get().getCount();


        if (dotsCount > 0) {

            dots = new ImageView[dotsCount];

            if (pageIndicatorLayout.get() != null) {
                if (pageIndicatorLayout.get().getChildCount() > 0) {
                    pageIndicatorLayout.get().removeAllViewsInLayout();
                }
            }

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(getContext());
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pageIndicatorLayout.get().addView(dots[i], params);
            }

            int currentItem = imageViewPager.get().getCurrentItem();
            if (currentItem > 0) {
                dots[currentItem].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            } else {
                dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            }

        }

        if (dotsCount == 1) {
            pageIndicatorLayout.get().setVisibility(View.INVISIBLE);
        } else {
            pageIndicatorLayout.get().setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initViewModels() {
        imageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageViewModel.class);
        productDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductDetailViewModel.class);
        productColorViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductColorViewModel.class);
        productSpecsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductSpecsViewModel.class);
        productRelatedViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductRelatedViewModel.class);
        productAttributeHeaderViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductAttributeHeaderViewModel.class);
        productAttributeDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductAttributeDetailViewModel.class);
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavouriteViewModel.class);
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel.class);

    }

    @Override
    protected void initAdapters() {
        //view pager slide
        ProductDetailListPagerAdapter pagerAdapter = new ProductDetailListPagerAdapter(dataBindingComponent, (view, obj, position) -> navigationController.navigateToGalleryActivity(getActivity(), Constants.IMAGE_TYPE_PRODUCT, productDetailViewModel.productId));
        this.pagerAdapter = new AutoClearedValue<>(this, pagerAdapter);
        adapter = new AutoClearedValue<>(this, this.pagerAdapter.get());

        if (imageViewPager != null) {
            imageViewPager.get().setAdapter(adapter.get());
        }

        //color
        ProductColorAdapter nvcolorAdapter = new ProductColorAdapter(dataBindingComponent,
                (productColor, seletedColorId, selectetColorValue) -> {

                    productColorViewModel.colorSelectId = seletedColorId;
                    productColorViewModel.colorSelectValue = selectetColorValue;

                    productColorViewModel.proceededColorListData = processDataList(productColorViewModel.proceededColorListData);
                    replaceProductColorData(productColorViewModel.proceededColorListData);
                });
        this.colorAdapter = new AutoClearedValue<>(this, nvcolorAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.get().colorRecyclerView.setLayoutManager(layoutManager);


        binding.get().colorRecyclerView.setAdapter(colorAdapter.get());


        //color in popup

        FlexboxLayoutManager layoutManager2 = new FlexboxLayoutManager(getContext());
        layoutManager2.setFlexDirection(FlexDirection.ROW);
        layoutManager2.setJustifyContent(JustifyContent.FLEX_START);
        bottomBoxLayoutBinding.get().colorRecycler.setLayoutManager(layoutManager2);
        bottomBoxLayoutBinding.get().colorRecycler.setAdapter(colorAdapter.get());

        //tab layout
        ProductDetailTagAdapter tabAdapter = new ProductDetailTagAdapter(dataBindingComponent,
                (tabObject, selectedTabId) -> {

                    switch (tabObject.field_name) {
                        case Constants.CATEGORY:

                            productParameterHolder.resetTheHolder();
                            productParameterHolder.catId = tabObjectList.get().get(0).tag_id;
                            navigationController.navigateToHomeFilteringActivity(getActivity(), productParameterHolder, tabObjectList.get().get(0).tag_name);
                            break;

                        case Constants.SUBCATEGORY:

                            productParameterHolder.resetTheHolder();
                            productParameterHolder.catId = tabObjectList.get().get(0).tag_id;
                            productParameterHolder.subCatId = tabObjectList.get().get(1).tag_id;
                            navigationController.navigateToHomeFilteringActivity(getActivity(), productParameterHolder, tabObjectList.get().get(1).tag_name);

                            break;
                        case Constants.PRODUCT_TAG:

                            productParameterHolder.resetTheHolder();
                            productParameterHolder.search_term = tabObject.tag_name;
                            navigationController.navigateToHomeFilteringActivity(getActivity(), productParameterHolder, tabObject.tag_name);
                            break;
                    }
                });
        this.tabAdapter = new AutoClearedValue<>(this, tabAdapter);
        //binding.get().tabRecyclerView.setAdapter(tabAdapter);

        //specs
        ProductDetailSpecsAdapter specsAdapter = new ProductDetailSpecsAdapter(dataBindingComponent, productSpecs -> {
        });

        this.specsAdapter = new AutoClearedValue<>(this, specsAdapter);
        binding.get().factsRecyclerView.setAdapter(specsAdapter);

        //header attribute
        ProductAttributeHeaderAdapter headerAdapter = new ProductAttributeHeaderAdapter(dataBindingComponent, productAttributeHeaderViewModel.basketItemHolderHashMap,
                productAttributeDetail -> {

                    productAttributeHeaderViewModel.productAttributeDetail = productAttributeDetail;

                    productAttributeHeaderViewModel.basketItemHolderHashMap.put(productAttributeDetail.headerId, productAttributeDetail.name);

                    additionPrice = productAttributeDetail.additionalPrice;

                    productAttributeHeaderViewModel.attributeHeaderHashMap.put(productAttributeDetail.headerId, Integer.parseInt(additionPrice));

                    productAttributeHeaderViewModel.priceAfterAttribute = productAttributeHeaderViewModel.price;
                    productAttributeHeaderViewModel.originalPriceAfterAttribute = productAttributeHeaderViewModel.originalPrice;

                    for (int item : productAttributeHeaderViewModel.attributeHeaderHashMap.values()) {
                        productAttributeHeaderViewModel.priceAfterAttribute += item;
                        productAttributeHeaderViewModel.originalPriceAfterAttribute += item;
                    }

                    if (binding != null
                            && binding.get() != null
                            && binding.get().priceTextView != null) {
                        binding.get().priceTextView.setText(String.valueOf(Utils.format(productAttributeHeaderViewModel.priceAfterAttribute)));
                        binding.get().originalPriceTextView.setText(getString(R.string.product_detail__original_price, productDetailViewModel.currencySymbol, String.valueOf(Utils.format(productAttributeHeaderViewModel.originalPriceAfterAttribute))));
                    }

                    if (bottomBoxLayoutBinding != null
                            && bottomBoxLayoutBinding.get() != null
                            && bottomBoxLayoutBinding.get().priceTextView != null) {
                        bottomBoxLayoutBinding.get().priceTextView.setText(String.valueOf(Utils.format(productAttributeHeaderViewModel.priceAfterAttribute)));
                        bottomBoxLayoutBinding.get().originalPriceTextView.setText(getString(R.string.product_detail__original_price, productDetailViewModel.currencySymbol, String.valueOf(Utils.format(productAttributeHeaderViewModel.originalPriceAfterAttribute))));
                    }

                }) {
        };
        this.headerAdapter = new AutoClearedValue<>(this, headerAdapter);

        //spinner in popup
        bottomBoxLayoutBinding.get().attributeHeaderRecycler.setAdapter(headerAdapter);

        ProductHorizontalListAdapter homeScreenAdapter1 = new ProductHorizontalListAdapter(dataBindingComponent, new ProductHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(ProductDetailFragment.this.getActivity(), product);
//                if (ProductDetailFragment.this.getActivity() != null) {
//                    ProductDetailFragment.this.getActivity().finish();
//                }
            }

            @Override
            public void onFavLikeClick(Product product, LikeButton likeButton) {

                favFunction(product, likeButton);

            }

            @Override
            public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                unFavFunction(product, likeButton);
            }
        });

        this.relatedAdapter = new AutoClearedValue<>(this, homeScreenAdapter1);
        //binding.get().alsoBuyRecyclerView.setAdapter(homeScreenAdapter1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!(shopPhoneNumber.trim().isEmpty() || shopPhoneNumber.trim().equals("-"))) {
            Utils.phoneCallPermissionResult(requestCode, grantResults, this, shopPhoneNumber);
        }
    }

    @Override
    protected void initData() {

        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    productDetailViewModel.productId = getActivity().getIntent().getExtras().getString(Constants.PRODUCT_ID);
                    productDetailViewModel.historyFlag = getActivity().getIntent().getExtras().getString(Constants.HISTORY_FLAG);

                    productDetailViewModel.basketFlag = getActivity().getIntent().getExtras().getString(Constants.BASKET_FLAG);
                    if (productDetailViewModel.basketFlag != null && productDetailViewModel.basketFlag.equals(Constants.ONE)) {

                        productDetailViewModel.price = getActivity().getIntent().getExtras().getString(Constants.PRODUCT_PRICE);
                        productDetailViewModel.attributes = getActivity().getIntent().getExtras().getString(Constants.PRODUCT_ATTRIBUTE);
                        productDetailViewModel.count = getActivity().getIntent().getExtras().getString(Constants.PRODUCT_COUNT);
                        productDetailViewModel.colorId = getActivity().getIntent().getExtras().getString(Constants.PRODUCT_SELECTED_COLOR);
                        productDetailViewModel.basketId = getActivity().getIntent().getExtras().getInt(Constants.BASKET_ID);

                        if (productDetailViewModel.colorId != null) {
                            productColorViewModel.colorSelectId = productDetailViewModel.colorId;
                        }

                        String attribute = productDetailViewModel.attributes;

                        productAttributeHeaderViewModel.basketItemHolderHashMap = new Gson().fromJson(attribute, new TypeToken<HashMap<String, String>>() {
                        }.getType());

                        headerAdapter.get().basketItemHolderHashMap = productAttributeHeaderViewModel.basketItemHolderHashMap;
                    }
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
        LoadData();

        //check floating button action
        if (messenger.isEmpty() && whatsappNo.isEmpty() && shopPhoneNumber.isEmpty()) {
            Utils.psLog("*** " + shopPhoneNumber);
            Utils.hideFirstFab(binding.get().mainFloatingActionButton);
        }

        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });


        basketViewModel.getBasketSavedData().observe(this, resourse -> {
            if (resourse != null) {
                if (resourse.status == Status.SUCCESS) {

                    if (basketViewModel.isDirectCheckout) {

                        basketViewModel.isDirectCheckout = false;

                        navigationController.navigateToBasketList(getActivity());

                        productDetailViewModel.count = Constants.ZERO;
//                        productColorViewModel.colorSelectId = "";


                    } else {

                        if (getContext() != null) {
                            // QUITANDO MENSAJE DE AGREGADO A LA CESTA
                            /*psDialogMsg.showSuccessDialog(getString(R.string.product_detail__successfully_added), getString(R.string.app__ok));

                            psDialogMsg.show();*/

                        }
                    }
                } else {

                    psDialogMsg.showWarningDialog(getString(R.string.error), getString(R.string.app__ok));
                    psDialogMsg.show();
                }
            }
        });


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


        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ProductDetailFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }

                } else if (result.status == Status.ERROR) {
                    if (ProductDetailFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }
                }
            }
        });

        //get touch count post method
        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ProductDetailFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (ProductDetailFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });


    }

    private void showOrHideSpecs() {

        if (productSpecsViewModel.isSpecsData) {
            binding.get().factsRecyclerView.setVisibility(View.VISIBLE);
            binding.get().detailFactTextView.setVisibility(View.VISIBLE);
        } else {
            binding.get().factsRecyclerView.setVisibility(View.GONE);
            binding.get().detailFactTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();
        if (loginUserId != null) {
            productDetailViewModel.setProductDetailObj(productDetailViewModel.productId, selectedShopId, productDetailViewModel.historyFlag, loginUserId);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__PRODUCT_FRAGMENT
                && resultCode == Constants.RESULT_CODE__REFRESH_COMMENT_LIST) {
            Utils.psLog("Here");

            productDetailViewModel.setProductDetailObj(data.getStringExtra(Constants.PRODUCT_ID), selectedShopId, productDetailViewModel.historyFlag, loginUserId);

        }
    }

    private void LoadData() {
        // Load image

        LiveData<Resource<List<Image>>> news = imageViewModel.getImageListLiveData();

        if (news != null) {
            news.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);

                            }

                            imageViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            imageViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    imageViewModel.setLoadingState(false);
                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                }

            });
        }

        // Load Product detail

        productDetailViewModel.setProductDetailObj(productDetailViewModel.productId, selectedShopId, productDetailViewModel.historyFlag, loginUserId);

        setTouchCount();
        AtomicBoolean cargo= new AtomicBoolean(false);
        LiveData<Resource<Product>> productDetail = productDetailViewModel.getProductDetailData();
        if (productDetail != null) {
            productDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                productDetailViewModel.productContainer = listResource.data;

                                headerAdapter.get().currencySymbol = listResource.data.currencySymbol;
                                productDetailViewModel.currencySymbol = listResource.data.currencySymbol;

                                productColorViewModel.setProductColorListObj(productDetailViewModel.productId);
                                productSpecsViewModel.setProductSpecsListObj(productDetailViewModel.productId);
                                productAttributeHeaderViewModel.setProductAttributeHeaderListObj(productDetailViewModel.productId);

                                replaceProductDetailData(listResource.data);
                                pref.edit().putString(Constants.SHOP_ID, listResource.data.shopId).apply();
                                bindingRatingData(listResource.data);
                                bindingFavoriteData(listResource.data);

                                bindProductDetailInfo(listResource.data);

                                selectedShopId = listResource.data.shopId;
                                // Load Related Data
                                imageViewModel.setImageParentId(Constants.IMAGE_TYPE_PRODUCT, productDetailViewModel.productId);
                                cargo.set(true);
                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                productDetailViewModel.productContainer = listResource.data;

                                headerAdapter.get().currencySymbol = listResource.data.currencySymbol;
                                productDetailViewModel.currencySymbol = listResource.data.currencySymbol;

                                productColorViewModel.setProductColorListObj(productDetailViewModel.productId);
                                productSpecsViewModel.setProductSpecsListObj(productDetailViewModel.productId);
                                productAttributeHeaderViewModel.setProductAttributeHeaderListObj(productDetailViewModel.productId);

                                // Update the data
                                replaceProductDetailData(listResource.data);
                                pref.edit().putString(Constants.SHOP_ID, listResource.data.shopId).apply();
                                setTagData(listResource.data);

                                bindingRatingData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                selectedShopId = listResource.data.shopId;

                                bindProductDetailInfo(listResource.data);

                                productRelatedViewModel.setProductRelatedListObj(productDetailViewModel.productId, listResource.data.catId, selectedShopId);
                                cargo.set(true);
                            }

                            productDetailViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            // Error State
                            productDetailViewModel.setLoadingState(false);

                            break;

                        default:
                            // Default

                            break;
                    }
                    // productDetailViewModel.isProductDetailData=true;

                    // David AGREGAMOS AL CARRITO UNA VEZ QUE CARGO LA PAGINA Y SE CARGARON DATOS EN EL OBSERVADOR
                   if(cargo.get()){
                       ProductDetailFragment.this.setSaveToBasket();
                       // David boton de retroceso
                       getActivity().onBackPressed();
                   }
                } else {

                    //productDetailViewModel.isProductDetailData=false;
                    productDetailViewModel.setLoadingState(false);
                    // Init Object or Empty Data

                }
            });
        }

        // Load Product related

        LiveData<Resource<List<Product>>> productRelated = productRelatedViewModel.getProductRelatedData();
        if (productRelated != null) {
            productRelated.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceProductRelatedData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceProductRelatedData(listResource.data);

                            }

                            productRelatedViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            productRelatedViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    productRelatedViewModel.setLoadingState(false);
                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");
                }
            });
        }

        //load product color

        LiveData<List<ProductColor>> productColor = productColorViewModel.getProductColorData();
        if (productColor != null) {
            productColor.observe(this, listResource -> {
                if (listResource != null && listResource.size() > 0) {

                    productColorViewModel.proceededColorListData = processDataList(listResource);
                    replaceProductColorData(productColorViewModel.proceededColorListData);
                    productColorViewModel.isColorData = true;
                } else {
                    productColorViewModel.isColorData = false;

                }
                showOrHideColor();

            });
        }


        //load product specs

        LiveData<List<ProductSpecs>> productSpecs = productSpecsViewModel.getProductSpecsListData();
        if (productSpecs != null) {
            productSpecs.observe(this, listResource -> {
                if (listResource != null && listResource.size() > 0) {

                    ProductDetailFragment.this.replaceProductSpecsData(listResource);
                    productSpecsViewModel.isSpecsData = true;

                } else {
                    productSpecsViewModel.isSpecsData = false;

                }
                showOrHideSpecs();
            });
        }


        //load product header

        LiveData<List<ProductAttributeHeader>> productHeader = productAttributeHeaderViewModel.getProductAttributeHeaderListData();
        if (productHeader != null) {
            productHeader.observe(this, listResource -> {
                if (listResource != null && listResource.size() > 0) {

                    replaceProductAttributeHeaderData(listResource);

                    productAttributeHeaderViewModel.headerIdList.clear();
                    for (int i = 0; i < listResource.size(); i++) {
                        productAttributeHeaderViewModel.headerIdList.add(listResource.get(i).id);
                    }

                    productAttributeHeaderViewModel.isHeaderData = true;

                } else {
                    productAttributeHeaderViewModel.isHeaderData = false;
                }

            });
        }

        //load product header detail

        LiveData<List<ProductAttributeDetail>> productHeaderDetail = productAttributeDetailViewModel.getProductAttributeDetailListData();
        if (productHeaderDetail != null) {
            productHeaderDetail.observe(this, listResource -> {
                if (listResource != null && listResource.size() > 0) {
                    replaceProductAttributeDetailData();
                }

            });
        }

    }

    private void showOrHideColor() {
        if (productColorViewModel.isColorData) {
            binding.get().cangetColorTextView.setVisibility(View.VISIBLE);
            binding.get().colorRecyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.get().cangetColorTextView.setVisibility(View.GONE);
            binding.get().colorRecyclerView.setVisibility(View.GONE);
        }
    }

    private void bindProductDetailInfo(Product product) {

        if (product.minimumOrder == null || product.minimumOrder.equals("0") || product.minimumOrder.isEmpty()) {
            binding.get().productMinOrderValueTextView.setVisibility(View.GONE);
            binding.get().productMinOrderTextView.setVisibility(View.GONE);
        } else {
            binding.get().productMinOrderValueTextView.setText(product.minimumOrder);
        }

        if (product.productUnit == null || product.productUnit.equals("0") || product.productUnit.isEmpty()) {
            binding.get().productUnitValueTextView.setVisibility(View.GONE);
            binding.get().productUnitTextView.setVisibility(View.GONE);
        } else {
            binding.get().productUnitValueTextView.setText(product.productUnit);
        }

        if (product.productMeasurement == null || product.productMeasurement.equals("0") || product.productMeasurement.isEmpty()) {
            binding.get().productMeasurementValueTextView.setVisibility(View.GONE);
            binding.get().productMeasurementTextView.setVisibility(View.GONE);
        } else {
            binding.get().productMeasurementValueTextView.setText(product.productMeasurement);
        }

        if (product.shippingCost == null || product.shippingCost.equals("0") || product.shippingCost.isEmpty()) {
            binding.get().productShippingCostTextView.setVisibility(View.GONE);
            binding.get().productShippingCostValueTextView.setVisibility(View.GONE);
        } else {
            String shippingCostString = product.currencySymbol + Constants.SPACE_STRING + product.shippingCost;
            binding.get().productShippingCostValueTextView.setText(shippingCostString);
        }

    }

    private void bindingFavoriteData(Product product) {
        if (product.isFavourited.equals(Constants.ONE)) {
            binding.get().heartButton.setLiked(true);
        } else {
            binding.get().heartButton.setLiked(false);
        }
    }

    private void bindingRatingData(Product product) {

        if (product.ratingDetails.totalRatingValue == 0.0) {
            binding.get().starTextView.setText(getString(R.string.product_detail__rating));
        } else {
            binding.get().starTextView.setText(getString(R.string.rating__total_count_n_value, product.ratingDetails.totalRatingValue + "", product.ratingDetails.totalRatingCount + ""));
        }
        binding.get().ratingBar.setRating(product.ratingDetails.totalRatingValue);


    }

    //touch count
    private void setTouchCount() {

        if (connectivity.isConnected()) {
            touchCountViewModel.setTouchCountPostDataObj(loginUserId, productDetailViewModel.productId, Constants.FILTERING_TYPE_NAME, selectedShopId);
        }

    }

    private List<ProductColor> processDataList(List<ProductColor> listResource) {

        List<ProductColor> tmpColorList = new ArrayList<>();

        for (int i = 0; i < listResource.size(); i++) {

            try {
                tmpColorList.add((ProductColor) listResource.get(i).clone());
                tmpColorList.get(i).isColorSelect = tmpColorList.get(i).id.equals(productColorViewModel.colorSelectId);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        return tmpColorList;
    }


    private void replaceData(List<Image> imageList) {
        pagerAdapter.get().setImageList(imageList);
        setupSliderPagination();
        binding.get().executePendingBindings();
    }

    private void replaceProductColorData(List<ProductColor> productColorList) {
        colorAdapter.get().replace(productColorList);
        binding.get().executePendingBindings();
    }

    private void replaceProductSpecsData(List<ProductSpecs> productSpecsList) {
        specsAdapter.get().replace(productSpecsList);
        binding.get().executePendingBindings();
    }

    private void replaceProductAttributeHeaderData(List<ProductAttributeHeader> productAttributeHeaderList) {
        headerAdapter.get().replace(productAttributeHeaderList);
        binding.get().executePendingBindings();
    }

    private void replaceProductAttributeDetailData() {
        binding.get().executePendingBindings();
    }

    private void replaceProductTabData(List<TabObject> tabObject) {
        tabAdapter.get().replace(tabObject);
        binding.get().executePendingBindings();
    }

    private void replaceProductRelatedData(List<Product> productList) {
        relatedAdapter.get().replace(productList);
        binding.get().executePendingBindings();
    }

    private void replaceProductDetailData(Product product) {
        binding.get().setProduct(product);

        if (product.isAvailable.equals(Constants.ONE)) {
            binding.get().inStockTextView.setText(getString(R.string.product_detail__in_stock));
            binding.get().inStockTextView.setTextColor(getResources().getColor(R.color.md_green_700));

            this.available = true;
        } else if (product.isAvailable.equals(Constants.ZERO)) {
            binding.get().inStockTextView.setText(getString(R.string.product_detail__item_not_available));
            binding.get().inStockTextView.setTextColor(getResources().getColor(R.color.button__primary_bg));

            this.available = false;
        }

        //basket to detail
        if (productDetailViewModel.basketFlag.equals(Constants.ONE)) {
            binding.get().priceTextView.setText(Utils.priceFormat(Utils.round(Float.parseFloat(productDetailViewModel.price), 2)));

            bottomBoxLayoutBinding.get().priceTextView.setText(Utils.priceFormat(Utils.round(Float.parseFloat(productDetailViewModel.price), 2)));
            num = Integer.parseInt(productDetailViewModel.count);

            if (productDetailViewModel.count.equals(Constants.EMPTY_STRING) || productDetailViewModel.count.equals(Constants.ZERO)) {
                productDetailViewModel.count = Constants.ONE;
            } else {
                bottomBoxLayoutBinding.get().qtyEditText.setText(productDetailViewModel.count);
            }
            binding.get().qtyEditText.setText(productDetailViewModel.count);

            binding.get().colorRecyclerView.setSelected(true);//color

        } else {
            binding.get().priceTextView.setText(String.valueOf(Utils.format(product.unitPrice)));

            bottomBoxLayoutBinding.get().priceTextView.setText(String.valueOf(Utils.format(product.unitPrice)));

            if (product.minimumOrder != null && !product.minimumOrder.equals("0")) {
                num = Integer.valueOf(product.minimumOrder);
                bottomBoxLayoutBinding.get().qtyEditText.setText(product.minimumOrder);
            } else {
                num = 1;
            }
        }

        if (product.minimumOrder != null && !product.minimumOrder.equals("0")) {
            minOrder = Integer.valueOf(product.minimumOrder);
        }

        binding.get().priceCurrencyTextView.setText(product.currencySymbol);
        bottomBoxLayoutBinding.get().priceCurrencyTextView.setText(product.currencySymbol);
        //endregion

        //detail pop AGREGAR A CESTA Y COMPRA
        bottomBoxLayoutBinding.get().lowestButton.setOnClickListener(view -> {
            mBottomSheetDialog.get().setState(BottomSheetBehavior.STATE_EXPANDED);
            if (productDetailViewModel.isAddtoCart) {
                confirmBox();
            } else {
                basketViewModel.isDirectCheckout = true;
                confirmBox();
            }
        });


        if (productColorViewModel.isColorData) {
            bottomBoxLayoutBinding.get().cangetColorTextView.setVisibility(View.VISIBLE);

        } else {
            bottomBoxLayoutBinding.get().cangetColorTextView.setVisibility(View.GONE);
        }

        if (productAttributeHeaderViewModel.isHeaderData) {
            bottomBoxLayoutBinding.get().attributeTitleTextView.setVisibility(View.VISIBLE);

        } else {
            bottomBoxLayoutBinding.get().attributeTitleTextView.setVisibility(View.GONE);
        }

        if (product.isDiscount.equals(Constants.ONE)) {
            String originalPriceStr = product.currencySymbol + Constants.SPACE_STRING + Utils.format(product.originalPrice);
            bottomBoxLayoutBinding.get().originalPriceTextView.setText(originalPriceStr);
            bottomBoxLayoutBinding.get().originalPriceTextView.setPaintFlags(binding.get().originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            bottomBoxLayoutBinding.get().originalPriceTextView.setVisibility(View.GONE);
        }

        bottomBoxLayoutBinding.get().setImagePath(product.defaultPhoto.imgPath);

        bottomBoxLayoutBinding.get().prodNameTextView.setText(product.name);

        bottomBoxLayoutBinding.get().colorRecycler.setNestedScrollingEnabled(false);
        bottomBoxLayoutBinding.get().attributeHeaderRecycler.setNestedScrollingEnabled(false);

        bottomBoxLayoutBinding.get().floatingbtnMinus.setOnClickListener(view -> {
            if (num != 1) {
                if (minOrder != 0) {
                    if (num <= minOrder) {
                        Toast.makeText(getContext(), getString(R.string.product_detail__min_order_error) + Constants.SPACE_STRING + minOrder, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                num -= 1;
                bottomBoxLayoutBinding.get().qtyEditText.setText(String.valueOf(num));
            }
        });

        bottomBoxLayoutBinding.get().floatingbtnAdd.setOnClickListener(view -> {
            num += 1;
            bottomBoxLayoutBinding.get().qtyEditText.setText(String.valueOf(num));
        });

        String originalPriceStr = product.currencySymbol + Constants.SPACE_STRING + Utils.format(product.originalPrice);
        binding.get().originalPriceTextView.setText(originalPriceStr);

        binding.get().commentCountTextView.setText(String.valueOf(product.commentHeaderCount));
        binding.get().favCountTextView.setText(String.valueOf(product.favouriteCount));
        binding.get().touchCountTextView.setText(Utils.numberFormat(product.touchCount));

        productAttributeHeaderViewModel.price = product.unitPrice;
        productAttributeHeaderViewModel.originalPrice = product.originalPrice;

        binding.get().heartButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                Product product = binding.get().getProduct();
                if (product != null) {
                    favFunction(product, likeButton);
                }

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                Product product = binding.get().getProduct();
                if (product != null) {
                    unFavFunction(product, likeButton);
                }
            }

        });

        /*binding.get().seeCommentButton.setOnClickListener(view -> navigationController.navigateToCommentListActivity(getActivity(), product));
        binding.get().writeCommentButton.setOnClickListener(view -> navigationController.navigateToCommentListActivity(getActivity(), product));*/

//        binding.get().likeImageView.setOnClickListener(view -> sendLikePostData());
//        binding.get().likeTextView.setOnClickListener(view -> sendLikePostData());

        if (!product.isFeatured.equals(Constants.ONE)) {
            binding.get().featureTextView.setVisibility(View.GONE);
            binding.get().featureIconImageView.setVisibility(View.GONE);
        }

        if (!product.isDiscount.equals(Constants.ONE)) {
            binding.get().originalPriceTextView.setVisibility(View.GONE);
            binding.get().discountPercentButton.setVisibility(View.INVISIBLE);
        } else {
            binding.get().originalPriceTextView.setVisibility(View.VISIBLE);
            binding.get().discountPercentButton.setVisibility(View.VISIBLE);
            int discountValue = (int) product.discountPercent;
            String discountValueStr = "-" + discountValue + "%";
            binding.get().discountPercentTextView.setText(discountValueStr);
            binding.get().originalPriceTextView.setPaintFlags(binding.get().originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (product.highlightInformation.equals("")) {
            binding.get().constraintLayout5.setVisibility(View.GONE);
            binding.get().highlightTextview.setVisibility(View.GONE);
        } else {
            binding.get().constraintLayout5.setVisibility(View.VISIBLE);
            binding.get().highlightTextview.setVisibility(View.VISIBLE);
        }

        binding.get().executePendingBindings();

        if (product.description.isEmpty()) {
            binding.get().desc.setVisibility(View.GONE);
            binding.get().descTextView.setVisibility(View.GONE);
            binding.get().descUpDownImageView.setVisibility(View.GONE);
        } else {
            binding.get().descTextView.setText(product.description);
            //up and down
            binding.get().descUpDownImageView.setOnClickListener(this::toggleDescription);
            binding.get().desc.setOnClickListener((View v) -> {
                boolean show = Utils.toggleUpDownWithAnimation(binding.get().descUpDownImageView);
                if (show) {
                    ViewAnimationUtil.expand(binding.get().descTextView);
                } else {
                    ViewAnimationUtil.collapse(binding.get().descTextView);
                }
            });
        }

        binding.get().choiceUpDownImageView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandChoiceFunction();
            } else {
                collapseChoiceFunction();
            }
        });

        binding.get().textView39.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().choiceUpDownImageView);
            if (show) {
                expandChoiceFunction();

            } else {
                collapseChoiceFunction();
            }
        });

        /*binding.get().tabUpDownImageView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandTabFunction();
            } else {
                collapseTabFunction();
            }
        });

        binding.get().textview56.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().tabUpDownImageView);
            if (show) {
                expandTabFunction();
            } else {
                collapseTabFunction();
            }
        });*/


        binding.get().detailUpDownImageView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandDetailFunction();
            } else {
                collapseDetailFunction();
            }
        });
        binding.get().textView25.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().detailUpDownImageView);
            if (show) {
                expandDetailFunction();
            } else {
                collapseDetailFunction();
            }
        });


        /*binding.get().strictUpDownImageView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandStrictFunction();
            } else {
                collapseStrictFunction();
            }
        });
        binding.get().textview51.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().strictUpDownImageView);
            if (show) {
                expandStrictFunction();
            } else {
                collapseStrictFunction();
            }
        });*/


        /*binding.get().seeMoreImageView.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                expandSenceFunction(product);
            } else {
                collapseSenceFunction();
            }
        });
        binding.get().textview54.setOnClickListener((View v) -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().seeMoreImageView);
            if (show) {
                expandSenceFunction(product);
            } else {
                collapseSenceFunction();
            }
        });*/

        //endregion

    }

    private void setSaveToBasket() {

        // convert map to JSON String
        Gson gsonObj = new Gson();

        String jsonStr = gsonObj.toJson(productAttributeHeaderViewModel.basketItemHolderHashMap);

        String priceStr = gsonObj.toJson(productAttributeHeaderViewModel.attributeHeaderHashMap);

        //validation
        if (validateBasket()) {

            // save
            if (productAttributeHeaderViewModel.priceAfterAttribute == 0) {
                basketViewModel.setSaveToBasketListObj(
                        productDetailViewModel.basketId,
                        productDetailViewModel.productId,
                        num,
                        jsonStr,
                        productColorViewModel.colorSelectId,
                        productColorViewModel.colorSelectValue,
                        additionPrice,
                        productAttributeHeaderViewModel.price,
                        productAttributeHeaderViewModel.originalPrice,
                        selectedShopId,
                        priceStr
                );
            } else {
                basketViewModel.setSaveToBasketListObj(
                        productDetailViewModel.basketId,
                        productDetailViewModel.productId,
                        num,
                        jsonStr,
                        productColorViewModel.colorSelectId,
                        productColorViewModel.colorSelectValue,
                        additionPrice,
                        productAttributeHeaderViewModel.priceAfterAttribute,
                        productAttributeHeaderViewModel.originalPriceAfterAttribute,
                        selectedShopId,
                        priceStr);
            }

        }
    }

    private boolean validateBasket() {
        // color
        if (productColorViewModel.isColorData) {
            if (productColorViewModel.colorSelectId.equals(Constants.EMPTY_STRING) &&
                    productColorViewModel.colorSelectValue.equals(Constants.EMPTY_STRING)) {

                psDialogMsg.showWarningDialog(getString(R.string.product_detail__warning_select_attr), getString(R.string.app__ok));
                psDialogMsg.show();
                return false;

            }
        }
        // attribute
        List<String> key = new ArrayList<>(productAttributeHeaderViewModel.basketItemHolderHashMap.values());

        List<Integer> value = new ArrayList<>(productAttributeHeaderViewModel.attributeHeaderHashMap.values());

        if (productAttributeHeaderViewModel.isHeaderData) {
            for (int i = 0; i < key.size(); i++) {
                if (value.get(i) == 0) {
                    psDialogMsg.showWarningDialog(key.get(i), getString(R.string.app__ok));
                    psDialogMsg.show();
                    return false;
                }
            }
        }

        return true;
    }


    private void confirmBox() {

        ProductDetailFragment.this.setSaveToBasket();

    }

    private void toggleDescription(View v) {
        boolean show = Utils.toggleUpDownWithAnimation(v);
        if (show) {
            ViewAnimationUtil.expand(binding.get().descTextView);
        } else {
            ViewAnimationUtil.collapse(binding.get().descTextView);
        }
    }

    private void expandChoiceFunction() {
        ViewAnimationUtil.expand(binding.get().howManyTextView);
        ViewAnimationUtil.expand(binding.get().qtyEditText);
        ViewAnimationUtil.expand(binding.get().floatingAddButton);
        ViewAnimationUtil.expand(binding.get().floatingMinusButton);
        binding.get().attributeHeaderRecycler.setVisibility(View.VISIBLE);
        if (productAttributeHeaderViewModel.isHeaderData) {
            ViewAnimationUtil.expand(binding.get().otherFactsTextView);
        }
        binding.get().attributeHeaderRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        binding.get().floatingMinusButton.setOnClickListener(view -> {

            if (num != 1) {

                if (minOrder != 0) {
                    if (num <= minOrder) {
                        Toast.makeText(getContext(), getString(R.string.product_detail__min_order_error) + Constants.SPACE_STRING + minOrder, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                num -= 1;
                binding.get().qtyEditText.setText(String.valueOf(num));
            }
        });
        binding.get().floatingAddButton.setOnClickListener(view -> {
            num += 1;

            binding.get().qtyEditText.setText(String.valueOf(num));
        });

    }

    private void collapseChoiceFunction() {
        ViewAnimationUtil.collapse(binding.get().howManyTextView);
        ViewAnimationUtil.collapse(binding.get().qtyEditText);
        ViewAnimationUtil.collapse(binding.get().floatingAddButton);
        ViewAnimationUtil.collapse(binding.get().floatingMinusButton);
        ViewAnimationUtil.collapse(binding.get().otherFactsTextView);
        binding.get().attributeHeaderRecycler.setVisibility(View.GONE);
    }

    /*private void expandTabFunction() {
        ViewAnimationUtil.expand(binding.get().findBySimilarFactTextView);
        binding.get().tabRecyclerView.setVisibility(View.VISIBLE);
        ViewAnimationUtil.expand(binding.get().alsoBuyTextView);
        binding.get().alsoBuyRecyclerView.setVisibility(View.VISIBLE);
        binding.get().alsoBuyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        binding.get().tabRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void collapseTabFunction() {
        binding.get().tabRecyclerView.setVisibility(View.GONE);
        ViewAnimationUtil.collapse(binding.get().findBySimilarFactTextView);

        binding.get().alsoBuyRecyclerView.setVisibility(View.GONE);
        ViewAnimationUtil.collapse(binding.get().alsoBuyTextView);
    }*/

    private void expandDetailFunction() {
        ViewAnimationUtil.expand(binding.get().productNameTextView);
        ViewAnimationUtil.expand(binding.get().brandTextView);
        ViewAnimationUtil.expand(binding.get().productUnitTextView);
        ViewAnimationUtil.expand(binding.get().productUnitValueTextView);
        ViewAnimationUtil.expand(binding.get().productMeasurementTextView);
        ViewAnimationUtil.expand(binding.get().productMeasurementValueTextView);
        ViewAnimationUtil.expand(binding.get().productMinOrderTextView);
        ViewAnimationUtil.expand(binding.get().productMinOrderValueTextView);
        ViewAnimationUtil.expand(binding.get().productShippingCostTextView);
        ViewAnimationUtil.expand(binding.get().productShippingCostValueTextView);
        if (productColorViewModel.isColorData) {
            ViewAnimationUtil.expand(binding.get().cangetColorTextView);
            ViewAnimationUtil.expand(binding.get().colorRecyclerView);

        } else {
            ViewAnimationUtil.collapse(binding.get().cangetColorTextView);
            ViewAnimationUtil.collapse(binding.get().colorRecyclerView);
        }
        if (productSpecsViewModel.isSpecsData) {
            ViewAnimationUtil.expand(binding.get().factsRecyclerView);
            ViewAnimationUtil.expand(binding.get().detailFactTextView);

        } else {
            ViewAnimationUtil.collapse(binding.get().factsRecyclerView);
            ViewAnimationUtil.collapse(binding.get().detailFactTextView);
        }

        if (!productColorViewModel.isColorData && !productSpecsViewModel.isSpecsData) {
            binding.get().viewDetailInfo.setVisibility(View.VISIBLE);
        }

        bindProductDetailInfo(productDetailViewModel.productContainer);

    }

    private void collapseDetailFunction() {
        ViewAnimationUtil.collapse(binding.get().productNameTextView);
        ViewAnimationUtil.collapse(binding.get().brandTextView);
        ViewAnimationUtil.collapse(binding.get().productUnitTextView);
        ViewAnimationUtil.collapse(binding.get().productUnitValueTextView);
        ViewAnimationUtil.collapse(binding.get().productMeasurementTextView);
        ViewAnimationUtil.collapse(binding.get().productMeasurementValueTextView);
        ViewAnimationUtil.collapse(binding.get().productMinOrderTextView);
        ViewAnimationUtil.collapse(binding.get().productMinOrderValueTextView);
        ViewAnimationUtil.collapse(binding.get().productShippingCostTextView);
        ViewAnimationUtil.collapse(binding.get().productShippingCostValueTextView);
        ViewAnimationUtil.collapse(binding.get().cangetColorTextView);
        ViewAnimationUtil.collapse(binding.get().detailFactTextView);
        ViewAnimationUtil.collapse(binding.get().colorRecyclerView);
        ViewAnimationUtil.collapse(binding.get().factsRecyclerView);
        binding.get().viewDetailInfo.setVisibility(View.GONE);

    }

    /*private void expandStrictFunction() {

        ViewAnimationUtil.expand(binding.get().seeAllFactButton);
        ViewAnimationUtil.expand(binding.get().refundPolicyButton);
    }

    private void collapseStrictFunction() {

        ViewAnimationUtil.collapse(binding.get().seeAllFactButton);
        ViewAnimationUtil.collapse(binding.get().refundPolicyButton);

    }*/

   /* private void expandSenceFunction(Product product) {
        binding.get().noScenceTextview.setVisibility(View.GONE);

        ViewAnimationUtil.expand(binding.get().seeCommentButton);
        ViewAnimationUtil.expand(binding.get().writeCommentButton);

        binding.get().writeCommentButton.setOnClickListener(view -> navigationController.navigateToCommentListActivity(ProductDetailFragment.this.getActivity(), product));

    }

    private void collapseSenceFunction() {
        ViewAnimationUtil.collapse(binding.get().noScenceTextview);
        ViewAnimationUtil.collapse(binding.get().writeCommentButton);
        ViewAnimationUtil.collapse(binding.get().seeCommentButton);
    }*/


    private void setTagData(Product listResource) {

        if (tabObjectList.get().size() > 0) {
            tabObjectList.get().clear();
        }

        tabObjectList.get().add(new TabObject(Constants.CATEGORY, listResource.catId, listResource.category.name));
        tabObjectList.get().add(new TabObject(Constants.SUBCATEGORY, listResource.subCatId, listResource.subCategory.name));

        if (!listResource.searchTag.isEmpty()) {
            String[] tags = listResource.searchTag.split(",");
            for (String tag : tags) {
                tabObjectList.get().add(new TabObject(Constants.PRODUCT_TAG, tag, tag));
            }
        }
        replaceProductTabData(tabObjectList.get());
    }

    private void setBasketMenuItemVisible(Boolean isVisible) {
        if (basketMenuItem != null) {
            basketMenuItem.setVisible(isVisible);
        }
    }

    private MenuItem basketMenuItem;

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

    void callBasket() {
        if (productDetailViewModel.basketFlag.equals(Constants.ONE)) {

            refreshBasketData();

        } else {

            navigationController.navigateToBasketList(getActivity());

        }
    }

    void refreshBasketData() {
        if (productDetailViewModel.basketFlag.equals(Constants.ONE)) {

            if (getActivity() != null) {
                navigationController.navigateBackToBasketActivity(getActivity());
                getActivity().finish();
            }
        }
    }

    private void unFavFunction(Product product, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(product.id, loginUserId, selectedShopId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }

        });

    }

    private void favFunction(Product product, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(product.id, loginUserId, selectedShopId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null));
            }

        });

    }


}
