package com.panaceasoft.psmultistore.viewmodel.shop;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.shop.ShopRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.Shop;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.holder.ShopParameterHolder;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * Created by Panacea-Soft on 3/19/19.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class ShopViewModel extends PSViewModel {


    //region Variables
    private final LiveData<Resource<List<Shop>>> shopListData;
    private MutableLiveData<TmpDataHolder> shopListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageShopListData;
    private MutableLiveData<TmpDataHolder> nextPageShopListObj = new MutableLiveData<>();

    private final LiveData<Resource<Shop>> shopData;
    private MutableLiveData<ShopProfileTmpDataHolder> shopObj = new MutableLiveData<>();

    private final LiveData<Resource<List<Shop>>> shopByTagIdData;
    private MutableLiveData<TmpDataHolder> shopCategoryByIdObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageShopByTagIdData;
    private MutableLiveData<TmpDataHolder> nextPageShopByTagIdObj = new MutableLiveData<>();

    public ShopParameterHolder shopParameterHolder = new ShopParameterHolder();

    public String selectedShopId;
    public String flag;
    public String stripePublishableKey;

    //endregion


    //region Constructors

    @Inject
    ShopViewModel(ShopRepository repository) {

        shopListData = Transformations.switchMap(shopListObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getShopList(obj.apiKey, obj.limit, obj.offset, obj.parameterHolder);

        });

        nextPageShopListData = Transformations.switchMap(nextPageShopListObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageShopList(obj.apiKey, obj.limit, obj.offset, obj.parameterHolder);
        });

        shopData = Transformations.switchMap(shopObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getShop(obj.apiKey, obj.shopId);

        });

        shopByTagIdData = Transformations.switchMap(shopCategoryByIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getShopByTagId(obj.id, obj.limit, obj.offset);

        });

        nextPageShopByTagIdData = Transformations.switchMap(nextPageShopByTagIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextShopListByTagId(obj.id, obj.limit, obj.offset);
        });
    }

    //endregion


    //region Normal Shop List

    //region Shop List

    public void setShopListObj(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(apiKey, limit, offset, parameterHolder);

        this.shopListObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Shop>>> getShopListData() {
        return shopListData;
    }

    //endregion


    //region Next Page Shop List

    public void setNextPageShopListObj(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(apiKey, limit, offset, parameterHolder);

        this.nextPageShopListObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageShopListData() {
        return nextPageShopListData;
    }

    //endregion

    //endregion


    //region Shop Detail

    public void setShopObj(String apiKey, String shopId) {
        ShopProfileTmpDataHolder tmpDataHolder = new ShopProfileTmpDataHolder(apiKey, shopId);

        this.shopObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Shop>> getShopData() {
        return shopData;
    }

    //endregion


    //region Shop List By Tag Id

    public void setShopListByTagIdObj(String ShopCategoryId, String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(Config.API_KEY, ShopCategoryId, limit, offset);

        this.shopCategoryByIdObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Shop>>> getShopListByTagIdData() {
        return shopByTagIdData;
    }

    public void setNextPageShopListByTagIdObj(String ShopCategoryId, String limit, String offset) {

        TmpDataHolder tmpDataHolder = new TmpDataHolder(Config.API_KEY, ShopCategoryId, limit, offset);

        this.nextPageShopByTagIdObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageShopByTagIdData() {
        return nextPageShopByTagIdData;
    }

    //endregion

    //region Holders

    class TmpDataHolder {

        private String apiKey, id, limit, offset;
        private ShopParameterHolder parameterHolder;

        public TmpDataHolder(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {
            this.apiKey = apiKey;
            this.limit = limit;
            this.offset = offset;
            this.parameterHolder = parameterHolder;
        }

        public TmpDataHolder(String apiKey, String id, String limit, String offset) {
            this.apiKey = apiKey;
            this.limit = limit;
            this.id = id;
            this.offset = offset;
        }
    }

    class ShopProfileTmpDataHolder {
        String apiKey, shopId;

        ShopProfileTmpDataHolder(String apiKey, String shopId) {
            this.apiKey = apiKey;
            this.shopId = shopId;
        }
    }
    //endregion


}
