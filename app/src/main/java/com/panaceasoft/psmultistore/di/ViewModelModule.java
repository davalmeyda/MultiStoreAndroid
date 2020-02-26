package com.panaceasoft.psmultistore.di;

import com.panaceasoft.psmultistore.viewmodel.aboutus.AboutUsViewModel;
import com.panaceasoft.psmultistore.viewmodel.blog.BlogViewModel;
import com.panaceasoft.psmultistore.viewmodel.category.CategoryViewModel;
import com.panaceasoft.psmultistore.viewmodel.city.CityViewModel;
import com.panaceasoft.psmultistore.viewmodel.clearalldata.ClearAllDataViewModel;
import com.panaceasoft.psmultistore.viewmodel.apploading.AppLoadingViewModel;
import com.panaceasoft.psmultistore.viewmodel.country.CountryViewModel;
import com.panaceasoft.psmultistore.viewmodel.subcategory.SubCategoryViewModel;
import com.panaceasoft.psmultistore.viewmodel.collection.ProductCollectionProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.collection.ProductCollectionViewModel;
import com.panaceasoft.psmultistore.viewmodel.comment.CommentDetailListViewModel;
import com.panaceasoft.psmultistore.viewmodel.comment.CommentListViewModel;
import com.panaceasoft.psmultistore.viewmodel.common.NotificationViewModel;
import com.panaceasoft.psmultistore.viewmodel.common.PSNewsViewModelFactory;
import com.panaceasoft.psmultistore.viewmodel.contactus.ContactUsViewModel;
import com.panaceasoft.psmultistore.viewmodel.coupondiscount.CouponDiscountViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeFeaturedProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeLatestProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeSearchProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeTrendingCategoryListViewModel;
import com.panaceasoft.psmultistore.viewmodel.homelist.HomeTrendingProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.image.ImageViewModel;
import com.panaceasoft.psmultistore.viewmodel.notification.NotificationsViewModel;
import com.panaceasoft.psmultistore.viewmodel.paypal.PaypalViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.BasketViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.FavouriteViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.HistoryProductViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductAttributeDetailViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductAttributeHeaderViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductColorViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductDetailViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductFavouriteViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductListByCatIdViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductRelatedViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.ProductSpecsViewModel;
import com.panaceasoft.psmultistore.viewmodel.product.TouchCountViewModel;
import com.panaceasoft.psmultistore.viewmodel.rating.RatingViewModel;
import com.panaceasoft.psmultistore.viewmodel.shippingmethod.ShippingMethodViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.FeaturedShopViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.LatestShopViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.PopularShopViewModel;
import com.panaceasoft.psmultistore.viewmodel.shop.ShopViewModel;
import com.panaceasoft.psmultistore.viewmodel.shoptag.ShopTagViewModel;
import com.panaceasoft.psmultistore.viewmodel.transaction.TransactionListViewModel;
import com.panaceasoft.psmultistore.viewmodel.transaction.TransactionOrderViewModel;
import com.panaceasoft.psmultistore.viewmodel.user.UserViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Panacea-Soft on 11/16/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module
abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PSNewsViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsViewModel.class)
    abstract ViewModel bindAboutUsViewModel(AboutUsViewModel aboutUsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel.class)
    abstract ViewModel bindImageViewModel(ImageViewModel imageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RatingViewModel.class)
    abstract ViewModel bindRatingViewModel(RatingViewModel ratingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel.class)
    abstract ViewModel bindNotificationViewModel(NotificationViewModel notificationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel.class)
    abstract ViewModel bindContactUsViewModel(ContactUsViewModel contactUsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CountryViewModel.class)
    abstract ViewModel bindCountryViewModel(CountryViewModel countryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CityViewModel.class)
    abstract ViewModel bindCityViewModel(CityViewModel cityViewModel);

  /*  @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    abstract ViewModel bindProductViewModel(ProductViewModel productViewModel);*/

    @Binds
    @IntoMap
    @ViewModelKey(CommentListViewModel.class)
    abstract ViewModel bindCommentViewModel(CommentListViewModel commentListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CommentDetailListViewModel.class)
    abstract ViewModel bindCommentDetailViewModel(CommentDetailListViewModel commentDetailListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailViewModel.class)
    abstract ViewModel bindProductDetailViewModel(ProductDetailViewModel productDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteViewModel.class)
    abstract ViewModel bindFavouriteViewModel(FavouriteViewModel favouriteViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TouchCountViewModel.class)
    abstract ViewModel bindTouchCountViewModel(TouchCountViewModel touchCountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductColorViewModel.class)
    abstract ViewModel bindProductColorViewModel(ProductColorViewModel productColorViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductSpecsViewModel.class)
    abstract ViewModel bindProductSpecsViewModel(ProductSpecsViewModel productSpecsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BasketViewModel.class)
    abstract ViewModel bindBasketViewModel(BasketViewModel basketViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HistoryProductViewModel.class)
    abstract ViewModel bindHistoryProductViewModel(HistoryProductViewModel historyProductViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductAttributeHeaderViewModel.class)
    abstract ViewModel bindProductAttributeHeaderViewModel(ProductAttributeHeaderViewModel productAttributeHeaderViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductAttributeDetailViewModel.class)
    abstract ViewModel bindProductAttributeDetailViewModel(ProductAttributeDetailViewModel productAttributeDetailViewModel);
/*

    @Binds
    @IntoMap
    @ViewModelKey(DiscountProductViewModel.class)
    abstract ViewModel bindDiscountProductViewModel(DiscountProductViewModel discountProductViewModel);
*/

    @Binds
    @IntoMap
    @ViewModelKey(ProductRelatedViewModel.class)
    abstract ViewModel bindRelatedProductViewModel(ProductRelatedViewModel productRelatedViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductFavouriteViewModel.class)
    abstract ViewModel bindProductFavouriteViewModel(ProductFavouriteViewModel productFavouriteViewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(ProductLikedViewModel.class)
//    abstract ViewModel bindProductLikedViewModel(ProductLikedViewModel productLikedViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel.class)
    abstract ViewModel bindCategoryViewModel(CategoryViewModel categoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SubCategoryViewModel.class)
    abstract ViewModel bindSubCategoryViewModel(SubCategoryViewModel subCategoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductListByCatIdViewModel.class)
    abstract ViewModel bindProductListByCatIdViewModel(ProductListByCatIdViewModel productListByCatIdViewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(TrendingProductsViewModel.class)
//    abstract ViewModel bindTrendingProductsViewModel(TrendingProductsViewModel trendingProductsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeLatestProductViewModel.class)
    abstract ViewModel bindHomeLatestProductViewModel(HomeLatestProductViewModel homeLatestProductViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeSearchProductViewModel.class)
    abstract ViewModel bindHomeFeaturedProductViewModel(HomeSearchProductViewModel homeSearchProductViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeTrendingProductViewModel.class)
    abstract ViewModel bindHomeTrendingProductViewModel(HomeTrendingProductViewModel homeTrendingProductViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeFeaturedProductViewModel.class)
    abstract ViewModel bindHomeCategory1ProductViewModel(HomeFeaturedProductViewModel homeFeaturedProductViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductCollectionViewModel.class)
    abstract ViewModel bindProductCollectionViewModel(ProductCollectionViewModel productCollectionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel.class)
    abstract ViewModel bindNotificationListViewModel(NotificationsViewModel notificationListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransactionListViewModel.class)
    abstract ViewModel bindTransactionListViewModel(TransactionListViewModel transactionListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransactionOrderViewModel.class)
    abstract ViewModel bindTransactionOrderViewModel(TransactionOrderViewModel transactionOrderViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeTrendingCategoryListViewModel.class)
    abstract ViewModel bindHomeTrendingCategoryListViewModel(HomeTrendingCategoryListViewModel transactionListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductCollectionProductViewModel.class)
    abstract ViewModel bindProductCollectionProductViewModel(ProductCollectionProductViewModel productCollectionProductViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FeaturedShopViewModel.class)
    abstract ViewModel bindFeaturedShopViewModel(FeaturedShopViewModel shopViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShopViewModel.class)
    abstract ViewModel bindShopViewModel(ShopViewModel shopViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LatestShopViewModel.class)
    abstract ViewModel bindRecentShopViewModel(LatestShopViewModel latestShopViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PopularShopViewModel.class)
    abstract ViewModel bindNewShopViewModel(PopularShopViewModel popularShopViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShopTagViewModel.class)
    abstract ViewModel bindShopCategoryViewModel(ShopTagViewModel shopTagViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel.class)
    abstract ViewModel bindNewsFeedViewModel(BlogViewModel blogViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ShippingMethodViewModel.class)
    abstract ViewModel bindShippingMethodViewModel(ShippingMethodViewModel shippingMethodViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PaypalViewModel.class)
    abstract ViewModel bindPaypalViewModel(PaypalViewModel paypalViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CouponDiscountViewModel.class)
    abstract ViewModel bindCouponDiscountViewModel(CouponDiscountViewModel couponDiscountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AppLoadingViewModel.class)
    abstract ViewModel bindPSAppInfoViewModel(AppLoadingViewModel psAppInfoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ClearAllDataViewModel.class)
    abstract ViewModel bindClearAllDataViewModel(ClearAllDataViewModel clearAllDataViewModel);
}


