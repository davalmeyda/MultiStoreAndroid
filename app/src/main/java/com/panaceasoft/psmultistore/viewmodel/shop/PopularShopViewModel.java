package com.panaceasoft.psmultistore.viewmodel.shop;

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

public class PopularShopViewModel extends PSViewModel {


    //region Variables
    private final LiveData<Resource<List<Shop>>> popularShopListData;
    private MutableLiveData<TmpDataHolder> popularShopListObj = new MutableLiveData<>();

    public ShopParameterHolder shopParameterHolder = new ShopParameterHolder().getPopularParameterHolder();
    //endregion


    //region Constructor
    @Inject
    PopularShopViewModel(ShopRepository repository) {

        popularShopListData = Transformations.switchMap(popularShopListObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getShopList(obj.apiKey, obj.limit, obj.offset, obj.parameterHolder);

        });

    }

    //endregion


    //region Popular Shop List

    public void setPopularShopListObj(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(apiKey, limit, offset, parameterHolder);

        this.popularShopListObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Shop>>> getPopularShopListData() {
        return popularShopListData;
    }

    //endregion


    //region Holders

    class TmpDataHolder {

        private String apiKey, limit, offset;
        private ShopParameterHolder parameterHolder;

        public TmpDataHolder(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {
            this.apiKey = apiKey;
            this.limit = limit;
            this.offset = offset;
            this.parameterHolder = parameterHolder;
        }
    }

    //endregion
}
