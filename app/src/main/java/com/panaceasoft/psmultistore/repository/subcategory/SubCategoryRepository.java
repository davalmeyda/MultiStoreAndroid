package com.panaceasoft.psmultistore.repository.subcategory;

import com.panaceasoft.psmultistore.AppExecutors;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.api.ApiResponse;
import com.panaceasoft.psmultistore.api.PSApiService;
import com.panaceasoft.psmultistore.db.PSCoreDb;
import com.panaceasoft.psmultistore.db.SubCategoryDao;
import com.panaceasoft.psmultistore.repository.common.NetworkBoundResource;
import com.panaceasoft.psmultistore.repository.common.PSRepository;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.SubCategory;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

@Singleton
public class SubCategoryRepository extends PSRepository {

    private final SubCategoryDao subCategoryDao;

    @Inject
    SubCategoryRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, SubCategoryDao subCategoryDao) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside SubCategoryRepository");

        this.subCategoryDao = subCategoryDao;
    }

    public LiveData<Resource<List<SubCategory>>> getAllSubCategoryList(String apiKey, String shopId) {
        return new NetworkBoundResource<List<SubCategory>, List<SubCategory>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<SubCategory> itemList) {

                Utils.psLog("SaveCallResult of getAllSubCategoryList.");

                try {
                    db.beginTransaction();

                    subCategoryDao.deleteAllSubCategory();

                    subCategoryDao.insertAll(itemList);

                    for (SubCategory item : itemList) {
                        subCategoryDao.insert(new SubCategory(item.id, item.catId,item.shopId, item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr, item.defaultPhoto, item.defaultIcon));
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllSubCategoryList.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SubCategory> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<SubCategory>> loadFromDb() {
                return subCategoryDao.getAllSubCategory();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<SubCategory>>> createCall() {
                return psApiService.getAllSubCategoryList(apiKey, shopId);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<List<SubCategory>>> getSubCategoryList(String apiKey, String shopId, String limit, String offset) {
        return new NetworkBoundResource<List<SubCategory>, List<SubCategory>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<SubCategory> itemList) {

                Utils.psLog("SaveCallResult of getSubCategoryList.");

                try {
                    db.beginTransaction();

                    subCategoryDao.deleteAllSubCategory();

                    subCategoryDao.insertAll(itemList);

                    for (SubCategory item : itemList) {
                        subCategoryDao.insert(new SubCategory(item.id, item.catId, item.shopId,item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr,  item.defaultPhoto, item.defaultIcon));
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of recent product list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SubCategory> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<SubCategory>> loadFromDb() {
                return subCategoryDao.getAllSubCategory();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<SubCategory>>> createCall() {
                return psApiService.getSubCategoryList(apiKey, shopId, limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageSubCategory(String shopId, String limit, String offset) {
        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<SubCategory>>> apiResponse = psApiService.getSubCategoryList(Config.API_KEY, shopId, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            for (SubCategory news : response.body) {
                                db.subCategoryDao().insert(new SubCategory(news.id, news.catId,news.shopId, news.name, news.status, news.ordering, news.addedDate, news.addedUserId, news.updatedDate, news.updatedUserId, news.updatedFlag, news.addedDateStr, news.defaultPhoto, news.defaultIcon));
                            }

                            db.subCategoryDao().insertAll(response.body);
                        }

                        db.setTransactionSuccessful();
                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.success(true));
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;
    }


    public LiveData<Resource<List<SubCategory>>> getSubCategoriesWithCatId(String loginUserId, String offset, String catId) {
        return new NetworkBoundResource<List<SubCategory>, List<SubCategory>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<SubCategory> itemList) {

                Utils.psLog("SaveCallResult of Sub Category.");

                try {
                    db.beginTransaction();

                    subCategoryDao.insertAll(itemList);

                    for (SubCategory item : itemList) {
                        subCategoryDao.insert(new SubCategory(item.id, item.catId,item.shopId, item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr, item.defaultPhoto, item.defaultIcon));
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of recent product list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SubCategory> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<SubCategory>> loadFromDb() {
                return subCategoryDao.getSubCategoryList(catId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<SubCategory>>> createCall() {
                return psApiService.getSubCategoryListWithCatId(Config.API_KEY, Utils.checkUserId(loginUserId), catId, "", offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageSubCategoriesWithCatId(String loginUserId, String limit, String offset, String catId) {
        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<SubCategory>>> apiResponse = psApiService.getSubCategoryListWithCatId(Config.API_KEY, Utils.checkUserId(loginUserId), catId, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            for (SubCategory news : response.body) {
                                db.subCategoryDao().insert(new SubCategory(news.id, news.catId,news.shopId, news.name, news.status, news.ordering, news.addedDate, news.addedUserId, news.updatedDate, news.updatedUserId, news.updatedFlag, news.addedDateStr, news.defaultPhoto, news.defaultIcon));
                            }

                            db.subCategoryDao().insertAll(response.body);
                        }

                        db.setTransactionSuccessful();
                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.success(true));
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;
    }

}
