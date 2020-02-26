package com.panaceasoft.psmultistore.viewmodel.coupondiscount;

import com.panaceasoft.psmultistore.repository.coupondiscount.CouponDiscountRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.CouponDiscount;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class CouponDiscountViewModel extends PSViewModel {

    private final LiveData<Resource<CouponDiscount>> couponDiscountData;
    private final MutableLiveData<CouponDiscountViewModel.TmpDataHolder> couponDiscountObj = new MutableLiveData<>();

    @Inject
    CouponDiscountViewModel(CouponDiscountRepository repository) {

        couponDiscountData = Transformations.switchMap(couponDiscountObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getCouponDiscount(obj.code, obj.shopId);
        });
    }

    public void setCouponDiscountObj(String code, String shopId) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.code = code;
        tmpDataHolder.shopId = shopId;

        couponDiscountObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<CouponDiscount>> getCouponDiscountData() {
        return couponDiscountData;
    }


    class TmpDataHolder {

        public String code = "";
        public String shopId = "";
    }

}
