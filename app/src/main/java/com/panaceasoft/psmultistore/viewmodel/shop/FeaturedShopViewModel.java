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

public class FeaturedShopViewModel extends PSViewModel {


    //region Variables

    private final LiveData<Resource<List<Shop>>> featuredShopListData;
    private MutableLiveData<FeaturedShopListTmpDataHolder> featuredShopListObj = new MutableLiveData<>();

    public ShopParameterHolder shopParameterHolder = new ShopParameterHolder().getFeaturedParameterHolder();

    //endregion


    //region Constructor

    @Inject
    FeaturedShopViewModel(ShopRepository repository) {

        featuredShopListData = Transformations.switchMap(featuredShopListObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getShopList(obj.apiKey, obj.limit, obj.offset, obj.parameterHolder);

        });

    }

    //endregion


    //region Featured Shop List

    public void setFeaturedShopListObj(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {
        FeaturedShopListTmpDataHolder tmpDataHolder = new FeaturedShopListTmpDataHolder(apiKey, limit, offset, parameterHolder);

        this.featuredShopListObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Shop>>> getFeaturedShopListData() {
        return featuredShopListData;
    }

    //endregion


    //region Holders

    class FeaturedShopListTmpDataHolder {

        private String apiKey, limit, offset;
        private ShopParameterHolder parameterHolder;

        FeaturedShopListTmpDataHolder(String apiKey, String limit, String offset, ShopParameterHolder parameterHolder) {
            this.apiKey = apiKey;
            this.limit = limit;
            this.offset = offset;
            this.parameterHolder = parameterHolder;
        }
    }

    //endregion


}
