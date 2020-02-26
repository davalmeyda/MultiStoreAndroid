package com.panaceasoft.psmultistore.viewmodel.product;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.product.ProductRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ProductFavouriteViewModel extends PSViewModel {

    //for product favourite list
    private final LiveData<Resource<List<Product>>> productFavouriteData;
    private MutableLiveData<ProductFavouriteViewModel.TmpDataHolder> productFavouriteListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageFavouriteLoadingData;
    private MutableLiveData<ProductFavouriteViewModel.TmpDataHolder> nextPageLoadingFavouriteObj = new MutableLiveData<>();
    //endregion
    //region Constructor

    @Inject
    public ProductFavouriteViewModel(ProductRepository productRepository) {
        //  product detail List
        productFavouriteData = Transformations.switchMap(productFavouriteListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("productFavouriteData");
            return productRepository.getFavouriteList(Config.API_KEY, obj.loginUserId, obj.offset);
        });

        nextPageFavouriteLoadingData = Transformations.switchMap(nextPageLoadingFavouriteObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("nextPageFavouriteLoadingData");
            return productRepository.getNextPageFavouriteProductList(obj.loginUserId, obj.offset);
        });

    }

    //endregion

    //region Getter And Setter for product detail List
    public void setProductFavouriteListObj(String loginUserId, String offset) {
        if (!isLoading) {
            ProductFavouriteViewModel.TmpDataHolder tmpDataHolder = new ProductFavouriteViewModel.TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.offset = offset;
            productFavouriteListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Product>>> getProductFavouriteData() {
        return productFavouriteData;
    }
    //endregion


    //Get Favourite Next Page
    public void setNextPageLoadingFavouriteObj(String offset, String loginUserId) {

        if (!isLoading) {
            ProductFavouriteViewModel.TmpDataHolder tmpDataHolder = new ProductFavouriteViewModel.TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.offset = offset;
            nextPageLoadingFavouriteObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageFavouriteLoadingData() {
        return nextPageFavouriteLoadingData;
    }

    //region Holder
    class TmpDataHolder {
        public String loginUserId = "";
        public String offset = "";
        public String limit = "";
        public Boolean isConnected = false;
        public String shopId = "";
    }
    //endregion

}
