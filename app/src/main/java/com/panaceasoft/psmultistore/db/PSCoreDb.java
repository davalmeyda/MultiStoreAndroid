package com.panaceasoft.psmultistore.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.panaceasoft.psmultistore.db.common.Converters;
import com.panaceasoft.psmultistore.viewobject.AboutUs;
import com.panaceasoft.psmultistore.viewobject.Basket;
import com.panaceasoft.psmultistore.viewobject.Blog;
import com.panaceasoft.psmultistore.viewobject.Category;
import com.panaceasoft.psmultistore.viewobject.CategoryMap;
import com.panaceasoft.psmultistore.viewobject.City;
import com.panaceasoft.psmultistore.viewobject.Comment;
import com.panaceasoft.psmultistore.viewobject.CommentDetail;
import com.panaceasoft.psmultistore.viewobject.Country;
import com.panaceasoft.psmultistore.viewobject.DeletedObject;
import com.panaceasoft.psmultistore.viewobject.DiscountProduct;
import com.panaceasoft.psmultistore.viewobject.FavouriteProduct;
import com.panaceasoft.psmultistore.viewobject.FeaturedProduct;
import com.panaceasoft.psmultistore.viewobject.HistoryProduct;
import com.panaceasoft.psmultistore.viewobject.Image;
import com.panaceasoft.psmultistore.viewobject.LatestProduct;
import com.panaceasoft.psmultistore.viewobject.LikedProduct;
import com.panaceasoft.psmultistore.viewobject.Noti;
import com.panaceasoft.psmultistore.viewobject.PSAppInfo;
import com.panaceasoft.psmultistore.viewobject.PSAppVersion;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.ProductAttributeDetail;
import com.panaceasoft.psmultistore.viewobject.ProductAttributeHeader;
import com.panaceasoft.psmultistore.viewobject.ProductCollection;
import com.panaceasoft.psmultistore.viewobject.ProductCollectionHeader;
import com.panaceasoft.psmultistore.viewobject.ProductColor;
import com.panaceasoft.psmultistore.viewobject.ProductListByCatId;
import com.panaceasoft.psmultistore.viewobject.ProductMap;
import com.panaceasoft.psmultistore.viewobject.ProductSpecs;
import com.panaceasoft.psmultistore.viewobject.Rating;
import com.panaceasoft.psmultistore.viewobject.RelatedProduct;
import com.panaceasoft.psmultistore.viewobject.ShippingMethod;
import com.panaceasoft.psmultistore.viewobject.Shop;
import com.panaceasoft.psmultistore.viewobject.ShopByTagId;
import com.panaceasoft.psmultistore.viewobject.ShopMap;
import com.panaceasoft.psmultistore.viewobject.ShopTag;
import com.panaceasoft.psmultistore.viewobject.SubCategory;
import com.panaceasoft.psmultistore.viewobject.TransactionDetail;
import com.panaceasoft.psmultistore.viewobject.TransactionObject;
import com.panaceasoft.psmultistore.viewobject.User;
import com.panaceasoft.psmultistore.viewobject.UserLogin;


/**
 * Created by Panacea-Soft on 11/20/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Database(entities = {
        Image.class,
        Category.class,
        User.class,
        UserLogin.class,
        AboutUs.class,
        Product.class,
        LatestProduct.class,
        DiscountProduct.class,
        FeaturedProduct.class,
        SubCategory.class,
        ProductListByCatId.class,
        Comment.class,
        CommentDetail.class,
        ProductColor.class,
        ProductSpecs.class,
        RelatedProduct.class,
        FavouriteProduct.class,
        LikedProduct.class,
        ProductAttributeHeader.class,
        ProductAttributeDetail.class,
        Noti.class,
        TransactionObject.class,
        ProductCollectionHeader.class,
        ProductCollection.class,
        TransactionDetail.class,
        Basket.class,
        HistoryProduct.class,
        Shop.class,
        ShopTag.class,
        Blog.class,
        Rating.class,
        ShippingMethod.class,
        ShopByTagId.class,
        ProductMap.class,
        ShopMap.class,
        CategoryMap.class,
        PSAppInfo.class,
        PSAppVersion.class,
        DeletedObject.class,
        Country.class,
        City.class

}, version = 6, exportSchema = false)
//V2.6 = DBV 6
//V2.4 = DBV 6
//V2.3 = DBV 6
//V2.2 = DBV 6
//V2.1 = DBV 6
//V2.0 = DBV 5
//V1.6 = DBV 4
//V1.5 = DBV 4
//V1.4 = DBV 3
//V1.3 = DBV 2


@TypeConverters({Converters.class})

public abstract class PSCoreDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public ProductColorDao productColorDao();

    abstract public ProductSpecsDao productSpecsDao();

    abstract public ProductAttributeHeaderDao productAttributeHeaderDao();

    abstract public ProductAttributeDetailDao productAttributeDetailDao();

    abstract public BasketDao basketDao();

    abstract public HistoryDao historyDao();

    abstract public AboutUsDao aboutUsDao();

    abstract public ImageDao imageDao();

    abstract public RatingDao ratingDao();

    abstract public CommentDao commentDao();

    abstract public CommentDetailDao commentDetailDao();

    abstract public ProductDao productDao();

    abstract public CategoryDao categoryDao();

    abstract public CountryDao countryDao();

    abstract public CityDao cityDao();

    abstract public SubCategoryDao subCategoryDao();

    abstract public NotificationDao notificationDao();

    abstract public ProductCollectionDao productCollectionDao();

    abstract public TransactionDao transactionDao();

    abstract public TransactionOrderDao transactionOrderDao();

    abstract public ShopDao shopDao();

    abstract public ShopTagDao shopCategoryDao();

    abstract public BlogDao blogDao();

    abstract public ShippingMethodDao shippingMethodDao();

    abstract public ShopListByTagIdDao shopListByTagIdDao();

    abstract public ProductMapDao productMapDao();

    abstract public ShopMapDao shopMapDao();

    abstract public CategoryMapDao categoryMapDao();

    abstract public PSAppInfoDao psAppInfoDao();

    abstract public PSAppVersionDao psAppVersionDao();

    abstract public DeletedObjectDao deletedObjectDao();


//    /**
//     * Migrate from:
//     * version 1 - using Room
//     * to
//     * version 2 - using Room where the {@link } has an extra field: addedDateStr
//     */
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE news "
//                    + " ADD COLUMN addedDateStr INTEGER NOT NULL DEFAULT 0");
//        }
//    };

    /* More migration write here */
}