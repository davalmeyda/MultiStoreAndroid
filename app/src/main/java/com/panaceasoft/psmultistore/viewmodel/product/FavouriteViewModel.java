package com.panaceasoft.psmultistore.viewmodel.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.panaceasoft.psmultistore.repository.product.ProductRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import javax.inject.Inject;

public class FavouriteViewModel extends PSViewModel {
    private final LiveData<Resource<Boolean>> sendFavouritePostData;
    private MutableLiveData<FavouriteViewModel.TmpDataHolder> sendFavouriteDataPostObj = new MutableLiveData<>();

    @Inject
    public FavouriteViewModel(ProductRepository productRepository) {
        sendFavouritePostData = Transformations.switchMap(sendFavouriteDataPostObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return productRepository.uploadFavouritePostToServer(obj.productId, obj.userId, obj.shopId);
        });
    }

    public void setFavouritePostDataObj(String product_id, String userId, String shopId) {

        if (!isLoading) {
            FavouriteViewModel.TmpDataHolder tmpDataHolder = new FavouriteViewModel.TmpDataHolder();
            tmpDataHolder.productId = product_id;
            tmpDataHolder.userId = userId;
            tmpDataHolder.shopId = shopId;

            sendFavouriteDataPostObj.setValue(tmpDataHolder);
            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getFavouritePostData() {
        return sendFavouritePostData;
    }

    class TmpDataHolder {
        public String productId = "";
        public String userId = "";
        public String shopId = "";
    }
}
