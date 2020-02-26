package com.panaceasoft.psmultistore.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.api.PSApiService;
import com.panaceasoft.psmultistore.db.AboutUsDao;
import com.panaceasoft.psmultistore.db.BasketDao;
import com.panaceasoft.psmultistore.db.BlogDao;
import com.panaceasoft.psmultistore.db.CategoryDao;
import com.panaceasoft.psmultistore.db.CategoryMapDao;
import com.panaceasoft.psmultistore.db.CityDao;
import com.panaceasoft.psmultistore.db.CommentDao;
import com.panaceasoft.psmultistore.db.CommentDetailDao;
import com.panaceasoft.psmultistore.db.CountryDao;
import com.panaceasoft.psmultistore.db.DeletedObjectDao;
import com.panaceasoft.psmultistore.db.HistoryDao;
import com.panaceasoft.psmultistore.db.ImageDao;
import com.panaceasoft.psmultistore.db.NotificationDao;
import com.panaceasoft.psmultistore.db.PSAppInfoDao;
import com.panaceasoft.psmultistore.db.PSAppVersionDao;
import com.panaceasoft.psmultistore.db.PSCoreDb;
import com.panaceasoft.psmultistore.db.ProductAttributeDetailDao;
import com.panaceasoft.psmultistore.db.ProductAttributeHeaderDao;
import com.panaceasoft.psmultistore.db.ProductCollectionDao;
import com.panaceasoft.psmultistore.db.ProductColorDao;
import com.panaceasoft.psmultistore.db.ProductDao;
import com.panaceasoft.psmultistore.db.ProductMapDao;
import com.panaceasoft.psmultistore.db.ProductSpecsDao;
import com.panaceasoft.psmultistore.db.RatingDao;
import com.panaceasoft.psmultistore.db.ShippingMethodDao;
import com.panaceasoft.psmultistore.db.ShopDao;
import com.panaceasoft.psmultistore.db.ShopListByTagIdDao;
import com.panaceasoft.psmultistore.db.ShopTagDao;
import com.panaceasoft.psmultistore.db.SubCategoryDao;
import com.panaceasoft.psmultistore.db.TransactionDao;
import com.panaceasoft.psmultistore.db.TransactionOrderDao;
import com.panaceasoft.psmultistore.db.UserDao;
import com.panaceasoft.psmultistore.utils.AppLanguage;
import com.panaceasoft.psmultistore.utils.Connectivity;
import com.panaceasoft.psmultistore.utils.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import androidx.room.Room;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    PSApiService providePSApiService() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl(Config.APP_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(PSApiService.class);

    }

    @Singleton
    @Provides
    PSCoreDb provideDb(Application app) {
        return Room.databaseBuilder(app, PSCoreDb.class, "PSApp.db")
                //.addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    Connectivity provideConnectivity(Application app) {
        return new Connectivity(app);
    }

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
    }

    @Singleton
    @Provides
    UserDao provideUserDao(PSCoreDb db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    AppLanguage provideCurrentLanguage(SharedPreferences sharedPreferences) {
        return new AppLanguage(sharedPreferences);
    }

    @Singleton
    @Provides
    AboutUsDao provideAboutUsDao(PSCoreDb db) {
        return db.aboutUsDao();
    }

    @Singleton
    @Provides
    ImageDao provideImageDao(PSCoreDb db) {
        return db.imageDao();
    }

    @Singleton
    @Provides
    CountryDao provideCountryDao(PSCoreDb db) {
        return db.countryDao();
    }

    @Singleton
    @Provides
    CityDao provideCityDao(PSCoreDb db) {
        return db.cityDao();
    }

    @Singleton
    @Provides
    ProductDao provideProductDao(PSCoreDb db) {
        return db.productDao();
    }

    @Singleton
    @Provides
    ProductColorDao provideProductColorDao(PSCoreDb db) {
        return db.productColorDao();
    }

    @Singleton
    @Provides
    ProductSpecsDao provideProductSpecsDao(PSCoreDb db) {
        return db.productSpecsDao();
    }

    @Singleton
    @Provides
    ProductAttributeHeaderDao provideProductAttributeHeaderDao(PSCoreDb db) {
        return db.productAttributeHeaderDao();
    }

    @Singleton
    @Provides
    ProductAttributeDetailDao provideProductAttributeDetailDao(PSCoreDb db) {
        return db.productAttributeDetailDao();
    }

    @Singleton
    @Provides
    BasketDao provideBasketDao(PSCoreDb db) {
        return db.basketDao();
    }

    @Singleton
    @Provides
    HistoryDao provideHistoryDao(PSCoreDb db) {
        return db.historyDao();
    }

    @Singleton
    @Provides
    CategoryDao provideCategoryDao(PSCoreDb db) {
        return db.categoryDao();
    }

    @Singleton
    @Provides
    RatingDao provideRatingDao(PSCoreDb db) {
        return db.ratingDao();
    }

    @Singleton
    @Provides
    SubCategoryDao provideSubCategoryDao(PSCoreDb db) {
        return db.subCategoryDao();
    }

    @Singleton
    @Provides
    CommentDao provideCommentDao(PSCoreDb db) {
        return db.commentDao();
    }

    @Singleton
    @Provides
    CommentDetailDao provideCommentDetailDao(PSCoreDb db) {
        return db.commentDetailDao();
    }

    @Singleton
    @Provides
    NotificationDao provideNotificationDao(PSCoreDb db) {
        return db.notificationDao();
    }

    @Singleton
    @Provides
    ProductCollectionDao provideProductCollectionDao(PSCoreDb db) {
        return db.productCollectionDao();
    }

    @Singleton
    @Provides
    TransactionDao provideTransactionDao(PSCoreDb db) {
        return db.transactionDao();
    }

    @Singleton
    @Provides
    TransactionOrderDao provideTransactionOrderDao(PSCoreDb db) {
        return db.transactionOrderDao();
    }

//    @Singleton
//    @Provides
//    TrendingCategoryDao provideTrendingCategoryDao(PSCoreDb db){return db.trendingCategoryDao();}

    @Singleton
    @Provides
    ShopDao provideShopDao(PSCoreDb db) {
        return db.shopDao();
    }

    @Singleton
    @Provides
    ShopTagDao provideShopCategoryDao(PSCoreDb db) {
        return db.shopCategoryDao();
    }

    @Singleton
    @Provides
    BlogDao provideNewsFeedDao(PSCoreDb db) {
        return db.blogDao();
    }

    @Singleton
    @Provides
    ShippingMethodDao provideShippingMethodDao(PSCoreDb db) {
        return db.shippingMethodDao();
    }

    @Singleton
    @Provides
    ShopListByTagIdDao provideShopCategoryByIdDao(PSCoreDb db) {
        return db.shopListByTagIdDao();
    }

    @Singleton
    @Provides
    ProductMapDao provideProductMapDao(PSCoreDb db) {
        return db.productMapDao();
    }

    @Singleton
    @Provides
    CategoryMapDao provideCategoryMapDao(PSCoreDb db) {
        return db.categoryMapDao();
    }

    @Singleton
    @Provides
    PSAppInfoDao providePSAppInfoDao(PSCoreDb db) {
        return db.psAppInfoDao();
    }

    @Singleton
    @Provides
    PSAppVersionDao providePSAppVersionDao(PSCoreDb db) {
        return db.psAppVersionDao();
    }

    @Singleton
    @Provides
    DeletedObjectDao provideDeletedObjectDao(PSCoreDb db) {
        return db.deletedObjectDao();
    }
}
