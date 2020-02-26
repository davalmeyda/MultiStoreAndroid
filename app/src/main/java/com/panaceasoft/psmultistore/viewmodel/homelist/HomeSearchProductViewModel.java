package com.panaceasoft.psmultistore.viewmodel.homelist;

import com.panaceasoft.psmultistore.repository.product.ProductRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.holder.ProductParameterHolder;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class HomeSearchProductViewModel extends PSViewModel {

    private final LiveData<Resource<List<Product>>> getProductListByKeyData;
    private final MutableLiveData<HomeSearchProductViewModel.TmpDataHolder> getProductListByKeyObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> getNextPageProductListByKeyData;
    private final MutableLiveData<HomeSearchProductViewModel.TmpDataHolder> getNextPageProductListByKeyObj = new MutableLiveData<>();

    public ProductParameterHolder holder = new ProductParameterHolder().getDiscountParameterHolder();

    @Inject
    HomeSearchProductViewModel(ProductRepository repository){
        Utils.psLog("Inside HomeSearchProductViewModel");

        getProductListByKeyData = Transformations.switchMap(getProductListByKeyObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getProductListByKey(obj.productParameterHolder, obj.loginUserId, obj.limit, obj.offset);
        });

        getNextPageProductListByKeyData = Transformations.switchMap(getNextPageProductListByKeyObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageProductListByKey(obj.productParameterHolder, obj.loginUserId, obj.limit, obj.offset);
        });
    }

    public void setGetProductListByKeyObj(ProductParameterHolder parameterHolder, String userId, String limit, String offset)
    {
        HomeSearchProductViewModel.TmpDataHolder tmpDataHolder = new HomeSearchProductViewModel.TmpDataHolder();
        tmpDataHolder.productParameterHolder = parameterHolder;
        tmpDataHolder.limit = limit;
        tmpDataHolder.offset = offset;
        tmpDataHolder.loginUserId = userId;

        setLoadingState(true);

        this.getProductListByKeyObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Product>>> getGetProductListByKeyData()
    {
        return getProductListByKeyData;
    }

    public void setGetNextPageProductListByKeyObj(ProductParameterHolder parameterHolder, String userId, String limit, String offset)
    {
        HomeSearchProductViewModel.TmpDataHolder tmpDataHolder = new HomeSearchProductViewModel.TmpDataHolder();
        tmpDataHolder.productParameterHolder = parameterHolder;
        tmpDataHolder.limit =limit;
        tmpDataHolder.offset = offset;
        tmpDataHolder.loginUserId = userId;

        setLoadingState(true);

        this.getNextPageProductListByKeyObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getGetNextPageProductListByKeyData()
    {
        return getNextPageProductListByKeyData;
    }

    class TmpDataHolder {
        public String productId = "";
        public String loginUserId = "";
        public String offset = "";
        public String catId = "";
        public String limit = "";
        public Boolean isConnected = false;
        public String apiKey = "";
        public String shopId = "";
        ProductParameterHolder productParameterHolder = new ProductParameterHolder();
    }

}
