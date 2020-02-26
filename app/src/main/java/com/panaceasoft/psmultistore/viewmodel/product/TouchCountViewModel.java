package com.panaceasoft.psmultistore.viewmodel.product;

import com.panaceasoft.psmultistore.repository.product.ProductRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class TouchCountViewModel extends PSViewModel {
    private final LiveData<Resource<Boolean>> sendTouchCountPostData;
    private MutableLiveData<TouchCountViewModel.TmpDataHolder> sendTouchCountDataPostObj = new MutableLiveData<>();

    @Inject
    TouchCountViewModel(ProductRepository productRepository) {
        sendTouchCountPostData = Transformations.switchMap(sendTouchCountDataPostObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return productRepository.uploadTouchCountPostToServer(obj.userId, obj.typeId, obj.typeName, obj.shopId);
        });
    }

    public void setTouchCountPostDataObj(String userId, String typeId, String typeName, String shopId) {

        TouchCountViewModel.TmpDataHolder tmpDataHolder = new TouchCountViewModel.TmpDataHolder();
        tmpDataHolder.userId = userId;
        tmpDataHolder.typeId = typeId;
        tmpDataHolder.typeName = typeName;
        tmpDataHolder.shopId = shopId;

        sendTouchCountDataPostObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<Boolean>> getTouchCountPostData() {
        return sendTouchCountPostData;
    }

    class TmpDataHolder {
        public String userId = "";
        String typeId = "";
        String typeName = "";
        String shopId = "";
    }
}
