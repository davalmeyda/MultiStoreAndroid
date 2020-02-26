package com.panaceasoft.psmultistore.viewmodel.shoptag;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.shoptag.ShopTagRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.ShopTag;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ShopTagViewModel extends PSViewModel {

    //region Variables

    private final LiveData<Resource<List<ShopTag>>> shopTagData;
    private MutableLiveData<ShopTagViewModel.TmpDataHolder> shopTagObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageShopTagData;
    private MutableLiveData<ShopTagViewModel.TmpDataHolder> nextPageShopTagObj = new MutableLiveData<>();

    public String toolbarName = "";

    //endregion


    //region Constructor
    @Inject
    ShopTagViewModel(ShopTagRepository repository) {

        shopTagData = Transformations.switchMap(shopTagObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getShopTagList(obj.apiKey, obj.limit, obj.offset);
        });

        nextPageShopTagData = Transformations.switchMap(nextPageShopTagObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextShopTagList(obj.apiKey, obj.limit, obj.offset);
        });

    }

    //endregion

    public void setShopTagObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(Config.API_KEY, limit, offset);

        this.shopTagObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<ShopTag>>> getShopTagData() {
        return shopTagData;
    }

    public void setNextPageShopTagObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(Config.API_KEY, limit, offset);

        this.nextPageShopTagObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageShopTagData() {
        return nextPageShopTagData;
    }

    class TmpDataHolder {

        String apiKey, limit, offset;

        public TmpDataHolder(String apiKey, String limit, String offset) {
            this.apiKey = apiKey;
            this.limit = limit;
            this.offset = offset;

        }
    }
}

