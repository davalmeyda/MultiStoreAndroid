package com.panaceasoft.psmultistore.utils;

/**
 * Created by Panacea-Soft on 3/19/19.
 * Contact Email : teamps.is.cool@gmail.com
 */


public interface Constants {

    //region General

    String EMPTY_STRING = "";
    String SPACE_STRING = " ";
    String ZERO = "0";
    String ONE = "1";
    String NO_DATA = "NO_DATA";
    String DASH = "-";

    //endregion

    // region Shop

    String SHOP_ID = "SHOP_ID";
    String SHIPPING_ID = "SHIPPING_ID";
    String SHOP_NAME = "SHOP_NAME";
    String SHOP = "shop"; // Don't Change
    String SHOP_CATEGORY_ID = "SHOP_CATEGORY_ID";
    String SHOP_HOLDER = "SHOP_HOLDER";
    String SHOP_TITLE = "SHOP_TITLE";
    String SHOP_START_DATE = "SHOP_START_DATE";
    String SHOP_END_DATE = "SHOP_END_DATE";
    String SHOP_TEL = "tel:"; // Don't Change
    String SHOP_TERMS = "SHOP_TERMS";
    String SHOP_REFUND = "SHOP_REFUND";
    String SHOP_TERMS_FLAG = "SHOP_TERMS_FLAG";
    String SHOP_STANDARD_SHIPPING_ENABLE = "SHOP_STANDARD_SHIPPING_ENABLE";
    String SHOP_ZONE_SHIPPING_ENABLE = "SHOP_ZONE_SHIPPING_ENABLE";
    String SHOP_NO_SHIPPING_ENABLE = "SHOP_NO_SHIPPING_ENABLE";

    //endregion

    // region Blog/News

    String BLOG_ID = "BLOG_ID";

    //endregion

    //region Payment

    String PAYMENT_CASH_ON_DELIVERY = "COD";
    String PAYMENT_PAYPAL = "PAYMENT_PAYPAL";
    String PAYMENT_STRIPE = "PAYMENT_STRIPE";
    String PAYMENT_BANK = "PAYMENT_BANK";
    String MESSENGER = "MESSENGER";
    String WHATSAPP = "WHATSAPP";
    String PAYMENT_SHIPPING_TAX = "PAYMENT_SHIPPING_TAX";
    String PAYMENT_OVER_ALL_TAX = "PAYMENT_OVER_ALL_TAX";
    String PAYMENT_SHIPPING_TAX_LABEL = "PAYMENT_SHIPPING_TAX_LABEL";
    String PAYMENT_OVER_ALL_TAX_LABEL = "PAYMENT_OVER_ALL_TAX_LABEL";
    String PAYMENT_TOKEN = "TOKEN";
    String SHOP_PHONE_NUMBER = "SHOPPHONENUMBER";

    //endregion

    //region Rating

    String RATING_ZERO = "0";
    String RATING_ONE = "1";
    String RATING_TWO = "2";
    String RATING_THREE = "3";
    String RATING_FOUR = "4";
    String RATING_FIVE = "5";

    //endregion

    //region REQUEST CODE AND RESULT CODE

    int REQUEST_CODE__MAIN_ACTIVITY = 1000;
    int REQUEST_CODE__SEARCH_FRAGMENT = 1001;
    int REQUEST_CODE__BASKET_FRAGMENT = 1002;
    int REQUEST_CODE__NOTIFICATION_LIST_FRAGMENT = 1003;
    int REQUEST_CODE__PRODUCT_LIST_FRAGMENT = 1004;
    int REQUEST_CODE__COMMENT_LIST_FRAGMENT = 1005;
    int REQUEST_CODE__PRODUCT_FRAGMENT = 1006;
    int REQUEST_CODE__PROFILE_FRAGMENT = 1007;
    int REQUEST_CODE__STRIPE_ACTIVITY = 1008;
    int REQUEST_CODE__PAYPAL = 1444;
    int REQUEST_CODE__PHONE_CALL_PERMISSION = 2030;
    int REQUEST_CODE__GOOGLE_SIGN = 1009;

    int RESULT_CODE__RESTART_MAIN_ACTIVITY = 2000;
    int RESULT_CODE__SEARCH_WITH_CATEGORY = 2001;
    int RESULT_CODE__SEARCH_WITH_SUBCATEGORY = 2002;
    int RESULT_CODE__REFRESH_BASKET_LIST = 2003;
    int RESULT_CODE__REFRESH_NOTIFICATION = 2004;
    int RESULT_CODE__SPECIAL_FILTER = 2005;
    int RESULT_CODE__CATEGORY_FILTER = 2006;
    int RESULT_CODE__REFRESH_COMMENT_LIST = 2007;
    int RESULT_CODE__LOGOUT_ACTIVATED = 2008;
    int RESULT_CODE__STRIPE_ACTIVITY = 2009;
    int RESULT_CODE__SEARCH_WITH_COUNTRY = 2010;
    int RESULT_CODE__SEARCH_WITH_CITY = 2011;
    int RESULT_LOAD_IMAGE = 1;
    int RESULT_OK = -1;

    //endregion

    //region Platform

    String PLATFORM = "android"; // Please don't change!

    //endregion


    //region AppInfo type_name

    String APPINFO_NAME_SHOP = "APPINFO_NAME_SHOP";
    String APPINFO_NAME_PRODUCT = "APPINFO_NAME_PRODUCT";
    String APPINFO_NAME_CATEGORY = "APPINFO_NAME_CATEGORY";
    String APPINFO_PREF_VERSION_NO = "APPINFO_PREF_VERSION_NO";
    String APPINFO_PREF_FORCE_UPDATE = "APPINFO_PREF_FORCE_UPDATE";
    String APPINFO_FORCE_UPDATE_MSG = "APPINFO_FORCE_UPDATE_MSG";
    String APPINFO_FORCE_UPDATE_TITLE = "APPINFO_FORCE_UPDATE_TITLE";


    //endregion

    //region User
    String FACEBOOK_ID = "FACEBOOK_ID";
    String GOOGLE_ID = "GOOGLE_ID";
    String USER_ID = "USER_ID";
    String USER_NAME = "USER_NAME";
    String USER_EMAIL = "USER_EMAIL";
    String USER_NO_USER = "nologinuser"; // Don't Change
    String USER_EMAIL_TO_VERIFY = "USER_EMAIL_TO_VERIFY";
    String USER_PASSWORD_TO_VERIFY = "USER_PASSWORD_TO_VERIFY";
    String USER_NAME_TO_VERIFY = "USER_NAME_TO_VERIFY";
    String USER_ID_TO_VERIFY = "USER_ID_TO_VERIFY";
    String USER_PASSWORD = "password";
    String USER_NO_DEVICE_TOKEN = "nodevicetoken"; // Don't Change


    //endregion

    //region Product

    //    String PRODUCT_LATEST = "latest";
//    String PRODUCT_DISCOUNT = "discount";
//    String PRODUCT_TRENDING = "FILTERING_TRENDING";
    String PRODUCT_FEATURED = "FEATURED";
    String PRODUCT_NAME = "PRODUCT_NAME";
    String PRODUCT_PRICE = "PRODUCT_PRICE";
    String PRODUCT_ATTRIBUTE = "PRODUCT_ATTRIBUTE";
    String PRODUCT_COUNT = "PRODUCT_COUNT";
    String PRODUCT_SELECTED_COLOR = "PRODUCT_SELECTED_COLOR";
    String PRODUCT_TAG = "PRODUCT_TAG";
    String PRODUCT_ID = "product_id";
    String PRODUCT_PARAM_HOLDER_KEY = "PRODUCT_PARAM_HOLDER_KEY";
    //endregion

    //region Collection

    String COLLECTION_COLLECTION = "COLLECTION_COLLECTION";
    String COLLECTION_NAME = "COLLECTION_NAME";
    String COLLECTION_IMAGE = "COLLECTION_IMAGE";
    String COLLECTION_ID = "COLLECTION_ID";

    //endregion

    //region Filtering Don't Change

    String FILTERING_FILTER_NAME = "name"; // Don't Change
    String FILTERING_TYPE_FILTER = "tf"; // Don't Change
    String FILTERING_SPECIAL_FILTER = "sf"; // Don't Change
    String FILTERING_TYPE_NAME = "product"; // Don't Change
    String FILTERING_TYPE_NAME_CAT = "category"; // Don't Change
    String FILTERING_CATEGORY_TYPE_NAME = "category"; // Don't Change
    String FILTERING_INACTIVE = ""; // Don't Change
    String FILTERING_TRENDING = "touch_count"; // Don't Change
    String FILTERING_FEATURE = "featured_date"; // Don't Change
    String FILTERING_PRICE = "unit_price"; // Don't Change
    String FILTERING_ASC = "asc"; // Don't Change
    String FILTERING_DESC = "desc"; // Don't Change
    String FILTERING_ADDED_DATE = "added_date"; // Don't Change
    String FILTERING_HOLDER = "filter_holder"; // Don't Change

    //endregion

    //region Category

    String CATEGORY_NAME = "CATEGORY_NAME";
    String CATEGORY_ID = "CATEGORY_ID";
    String CATEGORY = "CATEGORY";
    String CATEGORY_TYPE = "CATEGORY_TYPE";
    String CATEGORY_TRENDING = "CATEGORY_TRENDING";
    String CATEGORY_ALL = "ALL";
    String CATEGORY_FLAG = "CATEGORY_FLAG";

    //endregion

    //region SubCategory
    String SUBCATEGORY_ID = "SUBCATEGORY_ID";
    String SUBCATEGORY = "SUBCATEGORY";
    String SUBCATEGORY_NAME = "SUBCATEGORY_NAME";

    //endregion

    //Country
    String COUNTRY_NAME = "COUNTRY_NAME";
    String COUNTRY_ID = "COUNTRY_ID";
    String COUNTRY = "COUNTRY";

    //endregion

    //City
    String CITY_NAME = "CITY_NAME";
    String CITY_ID = "CITY_ID";
    String CITY = "CITY";

    //endregion

    //region Image

    String IMAGE_TYPE = "IMAGE_TYPE";
    String IMAGE_PARENT_ID = "IMAGE_PARENT_ID";
    String IMAGE_ID = "IMAGE_ID";
    String IMAGE_TYPE_PRODUCT = "product"; // Don't Change
    String IMAGE_TYPE_ABOUT = "IMAGE_TYPE_ABOUT";

    //endregion

    //region Basket

    String BASKET_FLAG = "BASKET_FLAG";
    String BASKET_ID = "BASKET_ID";
    String HEADER_ID = "-1";

    //endregion

    //region Transaction

    String TRANSACTION_ID = "TRANSACTION_ID";
    String TRANSACTION_NUMBER = "t_number"; // Don't Change
    String TRANSACTON_TEXT = "Text"; // Don't Change
    String TRANSACTION_IS_ZONE_SHIPPING = "TRANSACTION_IS_ZONE_SHIPPING";

    //endregion

    //region Language

    String LANGUAGE_CODE = "Language";
    String LANGUAGE_COUNTRY_CODE ="Language_Country_Code";

    //endregion

    //region Comment

    String COMMENT_ID = "COMMENT_ID";
    String COMMENT_HEADER_ID = "COMMENT_HEADER_ID";

    //endregion


    //regionHistory

    String HISTORY_FLAG = "history_flag";

    //endregion


    //region Noti

    String NOTI_NEW_ID = "NOTI_NEW_ID";
    String NOTI_ID = "NOTI_ID";
    String NOTI_TOKEN = "NOTI_TOKEN";
    String NOTI_EXISTS_TO_SHOW = "IS_NOTI_EXISTS_TO_SHOW";
    String NOTI_MSG = "NOTI_MSG";
    String NOTI_SETTING = "NOTI_SETTING";
    String NOTI_HEADER_ID = "NOTI_HEADER_ID";

    //endregion


    //region FB Register

    String FB_FIELDS = "fields"; // Don't Change
    String FB_EMAILNAMEID = "email,name,id"; // Don't Change
    String FB_NAME_KEY = "name"; // Don't Change
    String FB_EMAIL_KEK = "email"; // Don't Change
    String FB_ID_KEY = "id"; // Don't Change

    //endregion

    //region Email Type

    String EMAIL_TYPE = "plain/text"; // Don't Change
    String HTTP = "http://"; // Don't Change
    String HTTPS = "https://"; // Don't Change

    //endregion

    //region BoostManger

    String GALLERY_BOOST = "android.gestureboost.GestureBoostManager"; // Don't Change
    String GALLERY_GESTURE = "sGestureBoostManager"; // Don't Change
    String GALLERY_ID = "id"; // Don't Change
    String GALLERY_CONTEXT = "mContext"; // Don't Change

    //endregion

    //region Policy

    String PRIVACY_POLICY_NAME = "PRIVACY_POLICY_NAME";

    //endregion
}



