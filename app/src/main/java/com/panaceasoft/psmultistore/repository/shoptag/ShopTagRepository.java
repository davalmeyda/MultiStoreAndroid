package com.panaceasoft.psmultistore.repository.shoptag;

import com.panaceasoft.psmultistore.AppExecutors;
import com.panaceasoft.psmultistore.api.ApiResponse;
import com.panaceasoft.psmultistore.api.PSApiService;
import com.panaceasoft.psmultistore.db.PSCoreDb;
import com.panaceasoft.psmultistore.db.ShopTagDao;
import com.panaceasoft.psmultistore.repository.common.NetworkBoundResource;
import com.panaceasoft.psmultistore.repository.common.PSRepository;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.ShopTag;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

@Singleton
public class ShopTagRepository extends PSRepository {

    //region Variables

    private final ShopTagDao shopTagDao;

    //endregion


    //region constructor

    @Inject
    ShopTagRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ShopTagDao shopTagDao) {
        super(psApiService, appExecutors, db);
        this.shopTagDao = shopTagDao;
    }

    //endregion


    //region Shop Tag List

    public LiveData<Resource<List<ShopTag>>> getShopTagList(String apiKey, String limit, String offset) {
        return new NetworkBoundResource<List<ShopTag>, List<ShopTag>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ShopTag> itemList) {
                Utils.psLog("SaveCallResult of getShopTagList");

                db.beginTransaction();

                try {

                    shopTagDao.deleteALl();

                    shopTagDao.insertList(itemList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getShopTagList.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ShopTag> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<ShopTag>> loadFromDb() {
                Utils.psLog("Load getShopTagList From Db");

                return shopTagDao.getAllShopCategories();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ShopTag>>> createCall() {
                Utils.psLog("Call API Service to getShopTagList.");

                return psApiService.getShopTagList(apiKey, limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getShopTagList) : " + message);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextShopTagList(String apiKey, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<ShopTag>>> apiResponse = psApiService.getShopTagList(apiKey, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    try {

                        db.beginTransaction();

                        db.shopCategoryDao().insertList(response.body);

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

    //endregion


}
