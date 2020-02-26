package com.panaceasoft.psmultistore.di;


import com.panaceasoft.psmultistore.MainActivity;
import com.panaceasoft.psmultistore.ui.apploading.AppLoadingActivity;
import com.panaceasoft.psmultistore.ui.apploading.AppLoadingFragment;
import com.panaceasoft.psmultistore.ui.basket.BasketListActivity;
import com.panaceasoft.psmultistore.ui.basket.BasketListFragment;
import com.panaceasoft.psmultistore.ui.blog.detail.BlogDetailActivity;
import com.panaceasoft.psmultistore.ui.blog.detail.BlogDetailFragment;
import com.panaceasoft.psmultistore.ui.blog.list.BlogListActivity;
import com.panaceasoft.psmultistore.ui.blog.list.BlogListFragment;
import com.panaceasoft.psmultistore.ui.blog.listbyshopid.BlogListByShopIdActivity;
import com.panaceasoft.psmultistore.ui.blog.listbyshopid.BlogListByShopIdFragment;
import com.panaceasoft.psmultistore.ui.category.CategoryListActivity;
import com.panaceasoft.psmultistore.ui.category.CategoryListFragment;
import com.panaceasoft.psmultistore.ui.category.TrendingCategoryFragment;
import com.panaceasoft.psmultistore.ui.checkout.CheckoutActivity;
import com.panaceasoft.psmultistore.ui.checkout.CheckoutFragment1;
import com.panaceasoft.psmultistore.ui.checkout.CheckoutFragment2;
import com.panaceasoft.psmultistore.ui.checkout.CheckoutFragment3;
import com.panaceasoft.psmultistore.ui.checkout.CheckoutStatusFragment;
import com.panaceasoft.psmultistore.ui.privacyandpolicy.PrivacyAndPolicyActivity;
import com.panaceasoft.psmultistore.ui.privacyandpolicy.PrivacyAndPolicyFragment;
import com.panaceasoft.psmultistore.ui.product.search.SearchCityListFragment;
import com.panaceasoft.psmultistore.ui.product.search.SearchCountryListFragment;
import com.panaceasoft.psmultistore.ui.stripe.StripeActivity;
import com.panaceasoft.psmultistore.ui.collection.CollectionActivity;
import com.panaceasoft.psmultistore.ui.collection.CollectionFragment;
import com.panaceasoft.psmultistore.ui.collection.productCollectionHeader.ProductCollectionHeaderListActivity;
import com.panaceasoft.psmultistore.ui.collection.productCollectionHeader.ProductCollectionHeaderListFragment;
import com.panaceasoft.psmultistore.ui.comment.detail.CommentDetailActivity;
import com.panaceasoft.psmultistore.ui.comment.detail.CommentDetailFragment;
import com.panaceasoft.psmultistore.ui.comment.list.CommentListActivity;
import com.panaceasoft.psmultistore.ui.comment.list.CommentListFragment;
import com.panaceasoft.psmultistore.ui.contactus.ContactUsFragment;
import com.panaceasoft.psmultistore.ui.dashboard.DashBoardShopListFragment;
import com.panaceasoft.psmultistore.ui.forceupdate.ForceUpdateActivity;
import com.panaceasoft.psmultistore.ui.forceupdate.ForceUpdateFragment;
import com.panaceasoft.psmultistore.ui.gallery.GalleryActivity;
import com.panaceasoft.psmultistore.ui.gallery.GalleryFragment;
import com.panaceasoft.psmultistore.ui.gallery.detail.GalleryDetailActivity;
import com.panaceasoft.psmultistore.ui.gallery.detail.GalleryDetailFragment;
import com.panaceasoft.psmultistore.ui.language.LanguageFragment;
import com.panaceasoft.psmultistore.ui.notification.detail.NotificationActivity;
import com.panaceasoft.psmultistore.ui.notification.detail.NotificationFragment;
import com.panaceasoft.psmultistore.ui.notification.list.NotificationListActivity;
import com.panaceasoft.psmultistore.ui.notification.list.NotificationListFragment;
import com.panaceasoft.psmultistore.ui.notification.setting.NotificationSettingFragment;
import com.panaceasoft.psmultistore.ui.product.detail.ProductActivity;
import com.panaceasoft.psmultistore.ui.product.detail.ProductDetailFragment;
import com.panaceasoft.psmultistore.ui.product.favourite.FavouriteListActivity;
import com.panaceasoft.psmultistore.ui.product.favourite.FavouriteListFragment;
import com.panaceasoft.psmultistore.ui.product.filtering.CategoryFilterFragment;
import com.panaceasoft.psmultistore.ui.product.filtering.FilterFragment;
import com.panaceasoft.psmultistore.ui.product.filtering.FilteringActivity;
import com.panaceasoft.psmultistore.ui.product.history.HistoryFragment;
import com.panaceasoft.psmultistore.ui.product.history.UserHistoryListActivity;
import com.panaceasoft.psmultistore.ui.product.list.ProductListActivity;
import com.panaceasoft.psmultistore.ui.product.list.ProductListFragment;
import com.panaceasoft.psmultistore.ui.product.productbycatId.ProductListByCatIdActivity;
import com.panaceasoft.psmultistore.ui.product.productbycatId.ProductListByCatIdFragment;
import com.panaceasoft.psmultistore.ui.product.search.SearchByCategoryActivity;
import com.panaceasoft.psmultistore.ui.product.search.SearchCategoryFragment;
import com.panaceasoft.psmultistore.ui.product.search.SearchFragment;
import com.panaceasoft.psmultistore.ui.product.search.SearchSubCategoryFragment;
import com.panaceasoft.psmultistore.ui.rating.RatingListActivity;
import com.panaceasoft.psmultistore.ui.rating.RatingListFragment;
import com.panaceasoft.psmultistore.ui.setting.AppInfoActivity;
import com.panaceasoft.psmultistore.ui.setting.AppInfoFragment;
import com.panaceasoft.psmultistore.ui.setting.TermsAndConditionsActivity;
import com.panaceasoft.psmultistore.ui.setting.NotificationSettingActivity;
import com.panaceasoft.psmultistore.ui.setting.SettingActivity;
import com.panaceasoft.psmultistore.ui.setting.SettingFragment;
import com.panaceasoft.psmultistore.ui.shop.detail.ShopFragment;
import com.panaceasoft.psmultistore.ui.shop.list.ShopListActivity;
import com.panaceasoft.psmultistore.ui.shop.list.ShopListFragment;
import com.panaceasoft.psmultistore.ui.shop.listbytagid.ShopListByTagIdActivity;
import com.panaceasoft.psmultistore.ui.shop.listbytagid.ShopListByTagIdFragment;
import com.panaceasoft.psmultistore.ui.shop.menu.ShopMenuFragment;
import com.panaceasoft.psmultistore.ui.shop.selectedshop.SelectedShopActivity;
import com.panaceasoft.psmultistore.ui.shop.selectedshop.SelectedShopFragment;
import com.panaceasoft.psmultistore.ui.shop.tag.ShopTagListActivity;
import com.panaceasoft.psmultistore.ui.shop.tag.ShopTagListFragment;
import com.panaceasoft.psmultistore.ui.stripe.StripeFragment;
import com.panaceasoft.psmultistore.ui.terms.TermsAndConditionsFragment;
import com.panaceasoft.psmultistore.ui.transaction.detail.TransactionActivity;
import com.panaceasoft.psmultistore.ui.transaction.detail.TransactionFragment;
import com.panaceasoft.psmultistore.ui.transaction.list.TransactionListActivity;
import com.panaceasoft.psmultistore.ui.transaction.list.TransactionListFragment;
import com.panaceasoft.psmultistore.ui.user.PasswordChangeActivity;
import com.panaceasoft.psmultistore.ui.user.PasswordChangeFragment;
import com.panaceasoft.psmultistore.ui.user.ProfileEditActivity;
import com.panaceasoft.psmultistore.ui.user.ProfileEditFragment;
import com.panaceasoft.psmultistore.ui.user.ProfileFragment;
import com.panaceasoft.psmultistore.ui.user.UserForgotPasswordActivity;
import com.panaceasoft.psmultistore.ui.user.UserForgotPasswordFragment;
import com.panaceasoft.psmultistore.ui.user.UserLoginActivity;
import com.panaceasoft.psmultistore.ui.user.UserLoginFragment;
import com.panaceasoft.psmultistore.ui.user.UserRegisterActivity;
import com.panaceasoft.psmultistore.ui.user.UserRegisterFragment;
import com.panaceasoft.psmultistore.ui.user.verifyemail.VerifyEmailActivity;
import com.panaceasoft.psmultistore.ui.user.verifyemail.VerifyEmailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = TransactionModule.class)
    abstract TransactionListActivity contributeTransactionActivity();

    @ContributesAndroidInjector(modules = FavouriteListModule.class)
    abstract FavouriteListActivity contributeFavouriteListActivity();

    @ContributesAndroidInjector(modules = UserHistoryModule.class)
    abstract UserHistoryListActivity contributeUserHistoryListActivity();

    @ContributesAndroidInjector(modules = UserRegisterModule.class)
    abstract UserRegisterActivity contributeUserRegisterActivity();

    @ContributesAndroidInjector(modules = UserForgotPasswordModule.class)
    abstract UserForgotPasswordActivity contributeUserForgotPasswordActivity();

    @ContributesAndroidInjector(modules = UserLoginModule.class)
    abstract UserLoginActivity contributeUserLoginActivity();

    @ContributesAndroidInjector(modules = PasswordChangeModule.class)
    abstract PasswordChangeActivity contributePasswordChangeActivity();

    @ContributesAndroidInjector(modules = ProductListByCatIdModule.class)
    abstract ProductListByCatIdActivity productListByCatIdActivity();

    @ContributesAndroidInjector(modules = FilteringModule.class)
    abstract FilteringActivity filteringActivity();

    @ContributesAndroidInjector(modules = CommentDetailModule.class)
    abstract CommentDetailActivity commentDetailActivity();

    @ContributesAndroidInjector(modules = DiscountDetailModule.class)
    abstract ProductActivity discountDetailActivity();

    @ContributesAndroidInjector(modules = NotificationModule.class)
    abstract NotificationListActivity notificationActivity();

    @ContributesAndroidInjector(modules = HomeFilteringActivityModule.class)
    abstract ProductListActivity contributehomeFilteringActivity();

    @ContributesAndroidInjector(modules = NotificationDetailModule.class)
    abstract NotificationActivity notificationDetailActivity();

    @ContributesAndroidInjector(modules = TransactionDetailModule.class)
    abstract TransactionActivity transactionDetailActivity();

    @ContributesAndroidInjector(modules = CheckoutActivityModule.class)
    abstract CheckoutActivity checkoutActivity();

    @ContributesAndroidInjector(modules = CommentListActivityModule.class)
    abstract CommentListActivity commentListActivity();

    @ContributesAndroidInjector(modules = BasketlistActivityModule.class)
    abstract BasketListActivity basketListActivity();

    @ContributesAndroidInjector(modules = GalleryDetailActivityModule.class)
    abstract GalleryDetailActivity galleryDetailActivity();

    @ContributesAndroidInjector(modules = GalleryActivityModule.class)
    abstract GalleryActivity galleryActivity();

    @ContributesAndroidInjector(modules = SearchByCategoryActivityModule.class)
    abstract SearchByCategoryActivity searchByCategoryActivity();

    @ContributesAndroidInjector(modules = TermsAndConditionsModule.class)
    abstract com.panaceasoft.psmultistore.ui.terms.TermsAndConditionsActivity termsAndConditionsActivity();

    @ContributesAndroidInjector(modules = EditSettingModule.class)
    abstract SettingActivity editSettingActivity();

    @ContributesAndroidInjector(modules = LanguageChangeModule.class)
    abstract NotificationSettingActivity languageChangeActivity();

    @ContributesAndroidInjector(modules = ProfileEditModule.class)
    abstract ProfileEditActivity contributeProfileEditActivity();

    @ContributesAndroidInjector(modules = TermsAndConditionsModule.class)
    abstract TermsAndConditionsActivity ConditionsAndTermsActivity();

    @ContributesAndroidInjector(modules = AppInfoModule.class)
    abstract AppInfoActivity AppInfoActivity();

    @ContributesAndroidInjector(modules = ProductCollectionModule.class)
    abstract ProductCollectionHeaderListActivity productCollectionHeaderListActivity();

    @ContributesAndroidInjector(modules = CategoryListActivityAppInfoModule.class)
    abstract CategoryListActivity categoryListActivity();

    @ContributesAndroidInjector(modules = RatingListActivityModule.class)
    abstract RatingListActivity ratingListActivity();

    @ContributesAndroidInjector(modules = ShopListModule.class)
    abstract SelectedShopActivity selectedShopActivity();

    @ContributesAndroidInjector(modules = CollectionModule.class)
    abstract CollectionActivity collectionActivity();

    @ContributesAndroidInjector(modules = StripeModule.class)
    abstract StripeActivity stripeActivity();

    @ContributesAndroidInjector(modules = SelectedShopListModule.class)
    abstract ShopListActivity selectedShopListActivity();

    @ContributesAndroidInjector(modules = SelectedShopListBlogModule.class)
    abstract BlogListActivity selectedShopListBlogActivity();

    @ContributesAndroidInjector(modules = BlogDetailModule.class)
    abstract BlogDetailActivity blogDetailActivity();

    @ContributesAndroidInjector(modules = ShopCategoryDetailModule.class)
    abstract ShopListByTagIdActivity shopCategoryDetailActivity();

    @ContributesAndroidInjector(modules = ShopCategoryViewAllModule.class)
    abstract ShopTagListActivity shopCategoryViewAllActivity();

    @ContributesAndroidInjector(modules = forceUpdateModule.class)
    abstract ForceUpdateActivity forceUpdateActivity();

    @ContributesAndroidInjector(modules = blogListByShopIdActivityModule.class)
    abstract BlogListByShopIdActivity forceBlogListByShopIdActivity();

    @ContributesAndroidInjector(modules = appLoadingActivityModule.class)
    abstract AppLoadingActivity forceAppLoadingActivity();

    @ContributesAndroidInjector(modules = PrivacyAndPolicyActivityModule.class)
    abstract PrivacyAndPolicyActivity privacyAndPolicyActivity();

    @ContributesAndroidInjector(modules = VerifyEmailModule.class)
    abstract VerifyEmailActivity contributeVerifyEmailActivity();
}

@Module
abstract class CheckoutActivityModule {

    @ContributesAndroidInjector
    abstract CheckoutFragment1 checkoutFragment1();

    @ContributesAndroidInjector
    abstract LanguageFragment languageFragment();

    @ContributesAndroidInjector
    abstract CheckoutFragment2 checkoutFragment2();

    @ContributesAndroidInjector
    abstract CheckoutFragment3 checkoutFragment3();

    @ContributesAndroidInjector
    abstract CheckoutStatusFragment checkoutStatusFragment();
}

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract ContactUsFragment contributeContactUsFragment();

    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();

    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();

    @ContributesAndroidInjector
    abstract NotificationSettingFragment contributeNotificationSettingFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract LanguageFragment contributeLanguageFragment();

    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteListFragment();

    @ContributesAndroidInjector
    abstract TransactionListFragment contributTransactionListFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributEditSettingFragment();

    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ContributesAndroidInjector
    abstract NotificationListFragment contributeNotificationFragment();


    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();

    @ContributesAndroidInjector
    abstract DashBoardShopListFragment contributeShopListFragment();

    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();
}


@Module
abstract class ProfileEditModule {
    @ContributesAndroidInjector
    abstract ProfileEditFragment contributeProfileEditFragment();
}

@Module
abstract class TransactionModule {
    @ContributesAndroidInjector
    abstract TransactionListFragment contributeTransactionListFragment();
}

@Module
abstract class FavouriteListModule {
    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteFragment();
}

@Module
abstract class UserRegisterModule {
    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();
}

@Module
abstract class UserForgotPasswordModule {
    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();
}

@Module
abstract class UserLoginModule {
    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();
}

@Module
abstract class PasswordChangeModule {
    @ContributesAndroidInjector
    abstract PasswordChangeFragment contributePasswordChangeFragment();
}

@Module
abstract class CommentDetailModule {
    @ContributesAndroidInjector
    abstract CommentDetailFragment commentDetailFragment();
}

@Module
abstract class DiscountDetailModule {
    @ContributesAndroidInjector
    abstract ProductDetailFragment discountDetailFragment();
}

@Module
abstract class NotificationModule {
    @ContributesAndroidInjector
    abstract NotificationListFragment notificationFragment();


}


@Module
abstract class NotificationDetailModule {
    @ContributesAndroidInjector
    abstract NotificationFragment notificationDetailFragment();
}

@Module
abstract class TransactionDetailModule {
    @ContributesAndroidInjector
    abstract TransactionFragment transactionDetailFragment();
}

@Module
abstract class UserHistoryModule {
    @ContributesAndroidInjector
    abstract HistoryFragment contributeHistoryFragment();
}

@Module
abstract class AppInfoModule {
    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();
}

@Module
abstract class ProductCollectionModule {
    @ContributesAndroidInjector
    abstract ProductCollectionHeaderListFragment contributeProductCollectionHeaderListFragment();
}

@Module
abstract class CategoryListActivityAppInfoModule {
    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract TrendingCategoryFragment contributeTrendingCategoryFragment();
}

@Module
abstract class RatingListActivityModule {
    @ContributesAndroidInjector
    abstract RatingListFragment contributeRatingListFragment();
}

@Module
abstract class TermsAndConditionsModule {
    @ContributesAndroidInjector
    abstract TermsAndConditionsFragment TermsAndConditionsFragment();
}

@Module
abstract class EditSettingModule {
    @ContributesAndroidInjector
    abstract SettingFragment EditSettingFragment();
}

@Module
abstract class LanguageChangeModule {
    @ContributesAndroidInjector
    abstract NotificationSettingFragment notificationSettingFragment();
}

@Module
abstract class EditProfileModule {
    @ContributesAndroidInjector
    abstract ProfileFragment ProfileFragment();
}

@Module
abstract class ProductListByCatIdModule {
    @ContributesAndroidInjector
    abstract ProductListByCatIdFragment contributeProductListByCatIdFragment();

}

@Module
abstract class FilteringModule {

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract FilterFragment contributeSpecialFilteringFragment();
}

@Module
abstract class HomeFilteringActivityModule {
    @ContributesAndroidInjector
    abstract ProductListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract CollectionFragment contributeCollectionFragment();
}

@Module
abstract class CommentListActivityModule {
    @ContributesAndroidInjector
    abstract CommentListFragment contributeCommentListFragment();
}

@Module
abstract class BasketlistActivityModule {
    @ContributesAndroidInjector
    abstract BasketListFragment contributeBasketListFragment();
}

@Module
abstract class GalleryDetailActivityModule {
    @ContributesAndroidInjector
    abstract GalleryDetailFragment contributeGalleryDetailFragment();
}

@Module
abstract class GalleryActivityModule {
    @ContributesAndroidInjector
    abstract GalleryFragment contributeGalleryFragment();
}

@Module
abstract class SearchByCategoryActivityModule {

    @ContributesAndroidInjector
    abstract SearchCategoryFragment contributeSearchCategoryFragment();

    @ContributesAndroidInjector
    abstract SearchSubCategoryFragment contributeSearchSubCategoryFragment();

    @ContributesAndroidInjector
    abstract SearchCountryListFragment contributeSearchCountryListFragment();

    @ContributesAndroidInjector
    abstract SearchCityListFragment contributeSearchCityListFragment();

}

@Module
abstract class ShopListModule {

    @ContributesAndroidInjector
    abstract ShopFragment contributeAboutUsFragmentFragment();

    @ContributesAndroidInjector
    abstract BasketListFragment basketFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract SelectedShopFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract ProductCollectionHeaderListFragment contributeProductCollectionHeaderListFragment();

    @ContributesAndroidInjector
    abstract ShopMenuFragment contributeShopMenuFragment();


}

@Module
abstract class CollectionModule {

    @ContributesAndroidInjector
    abstract CollectionFragment contributeCollectionFragment();

}

@Module
abstract class StripeModule {

    @ContributesAndroidInjector
    abstract StripeFragment contributeStripeFragment();

}

@Module
abstract class SelectedShopListModule {

    @ContributesAndroidInjector
    abstract ShopListFragment contributeSelectedShopListFragment();

}

@Module
abstract class SelectedShopListBlogModule {

    @ContributesAndroidInjector
    abstract BlogListFragment contributeSelectedShopListBlogFragment();

}

@Module
abstract class BlogDetailModule {

    @ContributesAndroidInjector
    abstract BlogDetailFragment contributeBlogDetailFragment();
}

@Module
abstract class ShopCategoryDetailModule {

    @ContributesAndroidInjector
    abstract ShopListByTagIdFragment contributeShopCategoryDetailFragment();
}

@Module
abstract class ShopCategoryViewAllModule {

    @ContributesAndroidInjector
    abstract ShopTagListFragment contributeShopCategoryViewAllFragment();
}


@Module
abstract class forceUpdateModule {

    @ContributesAndroidInjector
    abstract ForceUpdateFragment contributeForceUpdateFragment();
}

@Module
abstract class blogListByShopIdActivityModule {

    @ContributesAndroidInjector
    abstract BlogListByShopIdFragment contributeBlogListByShopIdFragment();
}

@Module
abstract class appLoadingActivityModule {

    @ContributesAndroidInjector
    abstract AppLoadingFragment contributeAppLoadingFragment();
}
@Module
abstract class PrivacyAndPolicyActivityModule {

    @ContributesAndroidInjector
    abstract PrivacyAndPolicyFragment contributePrivacyAndPolicyFragment();

}
@Module
abstract class VerifyEmailModule {
    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();
}

