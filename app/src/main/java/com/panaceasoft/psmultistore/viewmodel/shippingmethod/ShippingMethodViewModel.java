package com.panaceasoft.psmultistore.viewmodel.shippingmethod;

import com.panaceasoft.psmultistore.repository.shippingmethod.ShippingMethodRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.ShippingCost;
import com.panaceasoft.psmultistore.viewobject.ShippingCostContainer;
import com.panaceasoft.psmultistore.viewobject.ShippingMethod;
import com.panaceasoft.psmultistore.viewobject.ShippingProductContainer;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ShippingMethodViewModel extends PSViewModel {

    private final LiveData<Resource<List<ShippingMethod>>> shippingMethodsData;
    private MutableLiveData<String> shippingMethodsObj = new MutableLiveData<>();

    private final LiveData<Resource<ShippingCost>> shippingCostByCountryAndCityData;
    private MutableLiveData<ShippingMethodViewModel.TmpDataHolder> shippingCostByCountryAndCityDataObj = new MutableLiveData<>();

    public List<ShippingProductContainer> shippingProductContainer = new ArrayList<>();
    @Inject
    ShippingMethodViewModel(ShippingMethodRepository repository) {

        shippingMethodsData = Transformations.switchMap(shippingMethodsObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getAllShippingMethods(obj);

        });

        shippingCostByCountryAndCityData = Transformations.switchMap(shippingCostByCountryAndCityDataObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.postShippingByCountryAndCity(obj.shippingCostContainer);
        });

    }

    public void setShippingMethodsObj(String shopId) {

        this.shippingMethodsObj.setValue(shopId);
    }

    public LiveData<Resource<List<ShippingMethod>>> getShippingMethodsData() {
        return shippingMethodsData;
    }

    //get shipping cost by zone
    public void setshippingCostByCountryAndCityObj(ShippingCostContainer shippingCostContainer) {

        ShippingMethodViewModel.TmpDataHolder tmpDataHolder = new ShippingMethodViewModel.TmpDataHolder();
        tmpDataHolder.shippingCostContainer = shippingCostContainer;
        shippingCostByCountryAndCityDataObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<ShippingCost>> getshippingCostByCountryAndCityData() {
        return shippingCostByCountryAndCityData;
    }

    class TmpDataHolder {
        public ShippingCostContainer shippingCostContainer;

    }

}

