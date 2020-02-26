package com.panaceasoft.psmultistore.viewmodel.subcategory;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.subcategory.SubCategoryRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.SubCategory;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class SubCategoryViewModel extends PSViewModel {

    private LiveData<Resource<List<SubCategory>>> allSubCategoryListData;
    private MutableLiveData<TmpDataHolder> allSubCategoryListObj = new MutableLiveData<>();

    private LiveData<Resource<List<SubCategory>>> subCategoryListData;
    private MutableLiveData<TmpDataHolder> subCategoryListObj = new MutableLiveData<>();

    private LiveData<Resource<Boolean>> nextPageLoadingStateData;
    private MutableLiveData<TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    private LiveData<Resource<List<SubCategory>>> subCategoryListWithCatIdData;
    private MutableLiveData<TmpDataHolder> subCategoryListWithCatIdObj = new MutableLiveData<>();

    private LiveData<Resource<Boolean>> nextPageLoadingStateWithCatIdData;
    private MutableLiveData<TmpDataHolder> nextPageLoadingStateWithCatIdObj = new MutableLiveData<>();


    @Inject
    SubCategoryViewModel(SubCategoryRepository repository) {
        Utils.psLog("Inside SubCategoryViewModel");

        allSubCategoryListData = Transformations.switchMap(allSubCategoryListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("allSubCategoryListData");
            return repository.getAllSubCategoryList(Config.API_KEY, obj.shopId);
        });

        subCategoryListData = Transformations.switchMap(subCategoryListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("subCategoryListData");
            return repository.getSubCategoryList(Config.API_KEY, obj.shopId, obj.limit, obj.offset);
        });

        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("nextPageLoadingStateData");
            return repository.getNextPageSubCategory(obj.shopId, obj.limit, obj.offset);
        });

        subCategoryListWithCatIdData = Transformations.switchMap(subCategoryListWithCatIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getSubCategoriesWithCatId(obj.loginUserId, obj.offset, obj.catId);
        });

        nextPageLoadingStateWithCatIdData = Transformations.switchMap(nextPageLoadingStateWithCatIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageSubCategoriesWithCatId(obj.loginUserId, obj.limit, obj.offset, obj.catId);
        });
    }

    public void setSubCategoryListWithCatIdObj(String loginUserId, String offset, String catId) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.loginUserId = loginUserId;
        tmpDataHolder.offset = offset;
        tmpDataHolder.catId = catId;

        subCategoryListWithCatIdObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<SubCategory>>> getsubCategoryListWithCatIdData() {

        return subCategoryListWithCatIdData;
    }

    public LiveData<Resource<Boolean>> getnextPageLoadingStateWithCatIdData() {

        return nextPageLoadingStateWithCatIdData;
    }


    public void setNextPageLoadingStateWithCatIdObj(String loginUserId, String limit, String offset, String catId) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.catId = catId;

            nextPageLoadingStateWithCatIdObj.setValue(tmpDataHolder);

            setLoadingState(true);
        }
    }


    public void setAllSubCategoryListObj(String shopId) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.shopId = shopId;
            allSubCategoryListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<SubCategory>>> getAllSubCategoryListData() {
        return allSubCategoryListData;
    }


    public void setSubCategoryListData(String shopId, String limit, String offset) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.shopId = shopId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            subCategoryListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<SubCategory>>> getSubCategoryListData() {
        return subCategoryListData;
    }

    public void setNextPageLoadingStateObj(String shopId, String limit, String offset) {

        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.shopId = shopId;
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageLoadingStateData;
    }


    class TmpDataHolder {
        public String loginUserId = "";
        public String shopId = "";
        public String offset = "";
        public String limit = "";
        public String catId = "";
        public Boolean isConnected = false;


    }
}
