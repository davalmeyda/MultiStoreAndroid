package com.panaceasoft.psmultistore.viewmodel.product;

import com.panaceasoft.psmultistore.repository.product.ProductRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.ProductAttributeDetail;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ProductAttributeDetailViewModel extends PSViewModel {

    //for product attribute detail list

    private final LiveData<List<ProductAttributeDetail>> ProductAttributeDetailListData;
    private MutableLiveData<ProductAttributeDetailViewModel.TmpDataHolder> ProductAttributeDetailObj = new MutableLiveData<>();

    //endregion

    //region Constructor

    @Inject
    public ProductAttributeDetailViewModel(ProductRepository productRepository) {
        //  product attribute detail List
        ProductAttributeDetailListData = Transformations.switchMap(ProductAttributeDetailObj, (ProductAttributeDetailViewModel.TmpDataHolder obj) -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("product attribute detail List.");
            return productRepository.getProductAttributeDetail(obj.productId, obj.headerId);
        });

    }

    //endregion
    //region Getter And Setter for product attribute detail List

    public LiveData<List<ProductAttributeDetail>> getProductAttributeDetailListData() {
        return ProductAttributeDetailListData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        public String offset = "";
        public String productId = "";
        public String headerId = "";
        public Boolean isConnected = false;
    }
    //endregion
}
