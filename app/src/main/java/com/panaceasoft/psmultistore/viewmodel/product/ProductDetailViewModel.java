package com.panaceasoft.psmultistore.viewmodel.product;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.product.ProductRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.Basket;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ProductDetailViewModel extends PSViewModel {

    //for product detail list

    public String productId = "";
    public String historyFlag = "";
    public String basketFlag = "";
    public String currencySymbol = "";
    public String price = "";
    public String attributes = "";
    public String count = "";
    public String colorId = "";
    public Basket basket;
    public int basketId = 0;

    public Boolean isAddtoCart = false;
    public Product productContainer;

    private final LiveData<Resource<Product>> productDetailListData;
    private MutableLiveData<ProductDetailViewModel.TmpDataHolder> productDetailObj = new MutableLiveData<>();

    //region Constructor

    @Inject
    ProductDetailViewModel(ProductRepository productRepository) {
        //  product detail List
        productDetailListData = Transformations.switchMap(productDetailObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("product detail List.");
            return productRepository.getProductDetail(Config.API_KEY, obj.productId, obj.shopId, obj.historyFlag, obj.userId);
        });


    }

    //endregion
    //region Getter And Setter for product detail List

    public void setProductDetailObj(String productId, String shopId, String historyFlag, String userId) {
        if (!isLoading) {
            ProductDetailViewModel.TmpDataHolder tmpDataHolder = new ProductDetailViewModel.TmpDataHolder();
            tmpDataHolder.productId = productId;
            tmpDataHolder.historyFlag = historyFlag;
            tmpDataHolder.shopId = shopId;
            tmpDataHolder.userId = userId;
            productDetailObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Product>> getProductDetailData() {
        return productDetailListData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        public String offset = "";
        public String productId = "";
        public String historyFlag = "";
        public String userId = "";
        public String shopId = "";
        public Boolean isConnected = false;
    }
    //endregion
}
