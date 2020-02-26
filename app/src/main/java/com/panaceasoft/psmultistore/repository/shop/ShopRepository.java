package com.panaceasoft.psmultistore.repository.shop;

import android.content.SharedPreferences;

import com.panaceasoft.psmultistore.AppExecutors;
import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.api.ApiResponse;
import com.panaceasoft.psmultistore.api.PSApiService;
import com.panaceasoft.psmultistore.db.PSCoreDb;
import com.panaceasoft.psmultistore.db.ShopDao;
import com.panaceasoft.psmultistore.repository.common.NetworkBoundResource;
import com.panaceasoft.psmultistore.repository.common.PSRepository;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.Shop;
import com.panaceasoft.psmultistore.viewobject.ShopByTagId;
import com.panaceasoft.psmultistore.viewobject.ShopMap;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.holder.ShopParameterHolder;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ShopRepository extends PSRepository {

    private final ShopDao shopDao;
    //endregion


    //region Constructor

    @Inject
    protected SharedPreferences pref;

    @Inject
    public ShopRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ShopDao shopDao) {
        super(psApiService, appExecutors, db);

        Utils.psLog("Inside ShopRepository");

        this.shopDao = shopDao;
    }

    public LiveData<Resource<List<Shop>>> getShopList(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {

        return new NetworkBoundResource<List<Shop>, List<Shop>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Shop> itemList) {
                Utils.psLog("SaveCallResult of getShopList.");

                db.beginTransaction();

                try {

                    String mapKey = parameterHolder.getShopMapKey();

                    db.shopMapDao().deleteByMapKey(mapKey);

                    shopDao.insertAll(itemList);

                    String dateTime = Utils.getDateTime();

                    for (int i = 0 ; i < itemList.size(); i++) {
                        db.shopMapDao().insert(new ShopMap(mapKey+itemList.get(i).id, mapKey, itemList.get(i).id, i + 1 , dateTime));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getShopList.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Shop> data) {

                // Recent news always load from server
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<Shop>> loadFromDb() {
                Utils.psLog("getShopList From Db");

                String mapKey = parameterHolder.getShopMapKey();

                return shopDao.getShopListByMapKey(mapKey);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Shop>>> createCall() {
                Utils.psLog("Call API Service to getShopList.");

                return psApiService.getShopList(apiKey, limit, offset, parameterHolder.isFeature, parameterHolder.orderBy, parameterHolder.orderType);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getShopList) : " + message);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageShopList(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<Shop>>> apiResponse = psApiService.getShopList(apiKey,
                limit,
                offset,
                parameterHolder.isFeature,
                parameterHolder.orderBy,
                parameterHolder.orderType);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    if(response.body != null) {
                        try {

                            db.beginTransaction();

                            db.shopDao().insertAll(response.body);

                            int finalIndex = db.shopMapDao().getMaxSortingByValue(parameterHolder.getShopMapKey());

                            int startIndex = finalIndex + 1;

                            String mapKey = parameterHolder.getShopMapKey();
                            String dateTime = Utils.getDateTime();

                            for (int i = 0; i < response.body.size(); i++) {
                                db.shopMapDao().insert(new ShopMap(mapKey + response.body.get(i).id, mapKey, response.body.get(i).id, startIndex + i, dateTime));
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

                    }else {
                        statusLiveData.postValue(Resource.error(response.errorMessage, null));
                    }
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }
        });

        return statusLiveData;

    }


    public LiveData<Resource<Shop>> getShop(String api_key, String shopId) {
        return new NetworkBoundResource<Shop, Shop>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull Shop itemList) {
                Utils.psLog("SaveCallResult of recent products.");

                db.beginTransaction();

                try {

                    db.shopDao().insert(itemList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Shop data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<Shop> loadFromDb() {
                Utils.psLog("Load discount From Db");

                return shopDao.getShopById(shopId);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Shop>> createCall() {
                Utils.psLog("Call API Service to get discount.");

                return psApiService.getShopById(api_key, shopId);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getDiscount) : " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<List<Shop>>> getShopByTagId(String tagId, String limit, String offset) {
        return new NetworkBoundResource<List<Shop>, List<Shop>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Shop> itemList) {
                Utils.psLog("SaveCallResult of Shop.");

                db.beginTransaction();

                try {

                    db.shopListByTagIdDao().deleteAllByTagId(tagId);

                    db.shopDao().insertAll(itemList);

                    for(int i = 0; i < itemList.size(); i++ ) {
                        db.shopListByTagIdDao().insert(new ShopByTagId(itemList.get(i).id, i, tagId));
                    }

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getShopByTagId.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Shop> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<Shop>> loadFromDb() {
                Utils.psLog("Load getShopByTagId From Db");

                return db.shopDao().getShopListByTagId(tagId);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Shop>>> createCall() {
                Utils.psLog("Call API Service to getShopByTagId.");

                return psApiService.getShopListByTagId(Config.API_KEY, tagId, limit, offset);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getShopByTagId) : " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextShopListByTagId(String tagId, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<Shop>>> apiResponse = psApiService.getShopListByTagId(Config.API_KEY, tagId, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    if(response.body != null) {
                        try {

                            db.beginTransaction();

                            int finalIndex = db.shopListByTagIdDao().getMaxSortingByValue(tagId);

                            int startIndex = finalIndex + 1;

                            for (int i = 0; i < response.body.size(); i++) {
                                db.shopListByTagIdDao().insert(new ShopByTagId(response.body.get(i).id, startIndex + i, tagId));
                            }

                            db.shopDao().insertAll(response.body);

                            db.setTransactionSuccessful();

                        } catch (NullPointerException ne) {
                            Utils.psErrorLog("Null Pointer Exception : ", ne);
                        } catch (Exception e) {
                            Utils.psErrorLog("Exception : ", e);
                        } finally {
                            db.endTransaction();
                        }

                        statusLiveData.postValue(Resource.success(true));

                    }else {

                        statusLiveData.postValue(Resource.error(response.errorMessage, null));

                    }
                });

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }
        });

        return statusLiveData;

    }

}
