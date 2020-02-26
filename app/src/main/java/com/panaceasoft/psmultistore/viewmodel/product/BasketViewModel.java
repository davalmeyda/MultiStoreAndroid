package com.panaceasoft.psmultistore.viewmodel.product;

import com.panaceasoft.psmultistore.repository.basket.BasketRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.Basket;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class BasketViewModel extends PSViewModel {

    //for basket

    private final LiveData<List<Basket>> basketListData;
    private MutableLiveData<BasketViewModel.TmpDataHolder> basketListObj = new MutableLiveData<>();

    private final LiveData<List<Basket>> basketListWithProductData;
    private MutableLiveData<BasketViewModel.TmpDataHolder> basketListWithProductObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> basketSavedData;
    private MutableLiveData<BasketViewModel.TmpDataHolder> basketSavedStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> basketUpdateData;
    private MutableLiveData<BasketViewModel.TmpDataHolder> basketUpdateStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> basketDeleteData;
    private MutableLiveData<BasketViewModel.TmpDataHolder> basketDeleteStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> wholeBasketDeleteData;
    private MutableLiveData<BasketViewModel.TmpDataHolder> wholeBasketDeleteStateObj = new MutableLiveData<>();

    public float totalPrice = 0;
    public int basketCount = 0;

    public boolean isDirectCheckout = false;

    //region Constructor

    @Inject
    public BasketViewModel(BasketRepository basketRepository) {
        //  basket List

        basketListData = Transformations.switchMap(basketListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return basketRepository.getAllBasketList(obj.shopId);
        });

        basketListWithProductData = Transformations.switchMap(basketListWithProductObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return basketRepository.getAllBasketWithProduct(obj.shopId);
        });

        //save
        basketSavedData = Transformations.switchMap(basketSavedStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return basketRepository.saveProduct(obj.id,
                    obj.productId,
                    obj.count,
                    obj.selectedAttributes,
                    obj.selectedColorId,
                    obj.selectedColorValue,
                    obj.selectedAttributeTotalPrice,
                    obj.basketPrice,
                    obj.basketOriginalPrice,
                    obj.shopId,
                    obj.selectedAttributesPrice);
        });

        //update
        basketUpdateData = Transformations.switchMap(basketUpdateStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return basketRepository.updateProduct(obj.id, obj.count);
        });

        //delete
        basketDeleteData = Transformations.switchMap(basketDeleteStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return basketRepository.deleteProduct(obj.id);
        });

        wholeBasketDeleteData = Transformations.switchMap(wholeBasketDeleteStateObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return basketRepository.deleteStoredBasket();
        });

    }
    //endregion

    //region Getter And Setter for basket List

    public void setBasketListObj(String shopId) {
        BasketViewModel.TmpDataHolder tmpDataHolder = new BasketViewModel.TmpDataHolder();
        tmpDataHolder.shopId = shopId;

        basketListObj.setValue(tmpDataHolder);
    }

    public LiveData<List<Basket>> getAllBasketList() {
        return basketListData;
    }

    public void setBasketListWithProductObj(String shopId) {
        BasketViewModel.TmpDataHolder tmpDataHolder = new BasketViewModel.TmpDataHolder();
        tmpDataHolder.shopId = shopId;

        basketListWithProductObj.setValue(tmpDataHolder);
    }

    public LiveData<List<Basket>> getAllBasketWithProductList() {
        return basketListWithProductData;
    }

    public void setWholeBasketDeleteStateObj() {
        BasketViewModel.TmpDataHolder tmpDataHolder = new BasketViewModel.TmpDataHolder();

        wholeBasketDeleteStateObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getWholeBasketDeleteData() {
        return wholeBasketDeleteData;
    }

    //endregion

    //save basket
    public void setSaveToBasketListObj(int id, String productId, int count, String selectedAttributes, String selectedColorId, String selectedColorValue, String selectedAttributeTotalPrice, float basketPrice, float basketOriginalPrice, String shopId, String priceStr) {

        BasketViewModel.TmpDataHolder tmpDataHolder = new BasketViewModel.TmpDataHolder();
        tmpDataHolder.id = id;
        tmpDataHolder.count = count;
        tmpDataHolder.productId = productId;
        tmpDataHolder.selectedAttributes = selectedAttributes;
        tmpDataHolder.selectedColorId = selectedColorId;
        tmpDataHolder.selectedColorValue = selectedColorValue;
        tmpDataHolder.basketPrice = basketPrice;
        tmpDataHolder.basketOriginalPrice = basketOriginalPrice;
        tmpDataHolder.shopId = shopId;
        tmpDataHolder.selectedAttributeTotalPrice = selectedAttributeTotalPrice;
        tmpDataHolder.selectedAttributesPrice = priceStr;
        basketSavedStateObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<Boolean>> getBasketSavedData() {
        return basketSavedData;
    }
    //endregion

    //update basket
    public void setUpdateToBasketListObj(int id, int count) {

        BasketViewModel.TmpDataHolder tmpDataHolder = new BasketViewModel.TmpDataHolder();

        tmpDataHolder.count = count;
        tmpDataHolder.id = id;
        basketUpdateStateObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<Boolean>> getBasketUpdateData() {
        return basketUpdateData;
    }
    //endregion

    //delete basket
    public void setDeleteToBasketListObj(int id) {

        BasketViewModel.TmpDataHolder tmpDataHolder = new BasketViewModel.TmpDataHolder();
        tmpDataHolder.id = id;
        basketDeleteStateObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<Boolean>> getBasketDeleteData() {
        return basketDeleteData;
    }
    //endregion


    //region Holder
    class TmpDataHolder {
        public int id = 0;
        public String productId = "";
        public int count = 0;
        public String selectedAttributes = "";
        public String shopId = "";
        public String selectedColorId = "";
        public String selectedColorValue = "";
        public float basketPrice = 0;
        private float basketOriginalPrice = 0;
        public Boolean isConnected = false;
        private String selectedAttributeTotalPrice = "";
        private String selectedAttributesPrice = "";
    }
    //endregion
}
