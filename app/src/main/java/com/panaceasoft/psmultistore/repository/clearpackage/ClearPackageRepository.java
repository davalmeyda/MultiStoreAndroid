package com.panaceasoft.psmultistore.repository.clearpackage;

import com.panaceasoft.psmultistore.AppExecutors;
import com.panaceasoft.psmultistore.api.PSApiService;
import com.panaceasoft.psmultistore.db.PSCoreDb;
import com.panaceasoft.psmultistore.repository.common.PSRepository;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ClearPackageRepository extends PSRepository {

    @Inject
    ClearPackageRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside CategoryRepository");
    }

    public LiveData<Resource<Boolean>> clearAllTheData() {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {
                db.beginTransaction();

                db.shopDao().deleteAll();
                db.productDao().deleteAll();
                db.blogDao().deleteAll();
                db.categoryDao().deleteAllCategory();
                db.commentDao().deleteAll();
                db.productAttributeDetailDao().deleteAll();
                db.basketDao().deleteAllBasket();
                db.categoryMapDao().deleteAll();
                db.commentDetailDao().deleteAll();
                db.deletedObjectDao().deleteAll();
                db.imageDao().deleteTable();
                db.notificationDao().deleteAllNotificationList();
                db.productAttributeHeaderDao().deleteAll();
                db.productCollectionDao().deleteAll();
                db.productColorDao().deleteAll();
                db.productMapDao().deleteAll();
                db.productSpecsDao().deleteAll();
                db.psAppInfoDao().deleteAll();
                db.psAppVersionDao().deleteAll();
                db.ratingDao().deleteAll();
                db.shippingMethodDao().deleteAll();
                db.shopListByTagIdDao().deleteAll();
                db.shopMapDao().deleteAll();
                db.shopCategoryDao().deleteALl();
                db.subCategoryDao().deleteAllSubCategory();
                db.transactionDao().deleteAllTransactionList();
                db.transactionOrderDao().deleteAll();

                db.setTransactionSuccessful();
            } catch (NullPointerException ne) {
                Utils.psErrorLog("Null Pointer Exception : ", ne);

                statusLiveData.postValue(Resource.error(ne.getMessage(), false));
            } catch (Exception e) {
                Utils.psErrorLog("Exception : ", e);

                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            } finally {
                db.endTransaction();
            }

            statusLiveData.postValue(Resource.success(true));


        });

        return statusLiveData;
    }

}
