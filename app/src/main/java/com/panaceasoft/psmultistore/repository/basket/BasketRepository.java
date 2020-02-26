package com.panaceasoft.psmultistore.repository.basket;

import com.panaceasoft.psmultistore.AppExecutors;
import com.panaceasoft.psmultistore.api.PSApiService;
import com.panaceasoft.psmultistore.db.BasketDao;
import com.panaceasoft.psmultistore.db.PSCoreDb;
import com.panaceasoft.psmultistore.repository.common.PSRepository;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.Basket;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

@Singleton
public class BasketRepository extends PSRepository {


    //region Variables

    private final BasketDao basketDao;


    //endregion


    //region Constructor

    @Inject
    BasketRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, BasketDao basketDao) {
        super(psApiService, appExecutors, db);

        this.basketDao = basketDao;
    }

    //endregion


    //    region Basket Repository Functions for ViewModel
    // insert Product
    public LiveData<Resource<Boolean>> saveProduct(int basketId, String productId, int count, String selectedAttributes, String selectedColorId, String selectedColorValue, String selectedAttributePrice, float basketPrice, float basketOriginalPrice, String shopId, String priceStr) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.diskIO().execute(() -> {

            try {
                db.beginTransaction();

                if(basketId == 0) {
                    int id = basketDao.getBasketId(productId, selectedAttributes, selectedColorId);
                    if (id > 0) {
                        basketDao.updateBasketById(id, count);
                    } else {
                        basketDao.insert(new Basket(productId, count, selectedAttributes, selectedColorId, selectedColorValue, selectedAttributePrice, basketPrice, basketOriginalPrice,shopId, priceStr));
                    }
                }else {
                    Basket basket = new Basket(productId, count, selectedAttributes, selectedColorId, selectedColorValue,selectedAttributePrice, basketPrice, basketOriginalPrice,shopId, priceStr);
                    basket.id = basketId;
                    basketDao.update(basket);
                }

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
    //endregion

    // update Product by id
    public LiveData<Resource<Boolean>> updateProduct(int id, int count) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.diskIO().execute(() -> {

            try {
                db.beginTransaction();

                basketDao.updateBasketById(id, count);

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
    //endregion

    // delete Product by id
    public LiveData<Resource<Boolean>> deleteProduct(int id) {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.diskIO().execute(() -> {

            try {
                db.beginTransaction();

                basketDao.deleteBasketById(id);

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
    //endregion


    public LiveData<Resource<Boolean>> deleteStoredBasket() {

        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.diskIO().execute(() -> {

            try {
                db.beginTransaction();

                basketDao.deleteAllBasket();

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


    //Get basket

    public LiveData<List<Basket>> getAllBasketList(String shopId) {

        return basketDao.getAllBasketList(shopId);

    }

    public LiveData<List<Basket>> getAllBasketWithProduct(String shopId) {

        MutableLiveData<List<Basket>> basketList = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> {
            List<Basket> groupList = basketDao.getAllBasketWithProduct(shopId);

            appExecutors.mainThread().execute(() ->
                    basketList.setValue(groupList)
            );
        });

        return basketList;
    }
    //endregion


}
