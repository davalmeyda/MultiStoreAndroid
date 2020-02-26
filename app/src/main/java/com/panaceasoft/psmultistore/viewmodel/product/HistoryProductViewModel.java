package com.panaceasoft.psmultistore.viewmodel.product;

import com.panaceasoft.psmultistore.repository.product.ProductRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.HistoryProduct;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class HistoryProductViewModel extends PSViewModel {
    private final LiveData<List<HistoryProduct>> historyListData;
    private MutableLiveData<HistoryProductViewModel.TmpDataHolder> historyListObj = new MutableLiveData<>();


    @Inject
    public HistoryProductViewModel(ProductRepository productRepository) {
        //  basket List

        historyListData = Transformations.switchMap(historyListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("get basket");
            return productRepository.getAllHistoryList(obj.offset);
        });


    }
    //endregion
    //region Getter And Setter for basket List

    public void setHistoryProductListObj(String offset) {
        HistoryProductViewModel.TmpDataHolder tmpDataHolder = new HistoryProductViewModel.TmpDataHolder();
        tmpDataHolder.offset = offset;
        historyListObj.setValue(tmpDataHolder);
    }

    public LiveData<List<HistoryProduct>> getAllHistoryProductList() {
        return historyListData;
    }

    //endregion


    //region Holder
    class TmpDataHolder {
        public int id = 0;
        public String productId = "";
        public String loginUserId = "";
        public String offset = "";
        public Boolean isConnected = false;
    }
//endregion
}