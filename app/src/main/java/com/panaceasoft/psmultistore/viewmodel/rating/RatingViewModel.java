package com.panaceasoft.psmultistore.viewmodel.rating;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.rating.RatingRepository;

import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;

import com.panaceasoft.psmultistore.viewobject.Rating;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class RatingViewModel extends PSViewModel {

    //region variables

    private final LiveData<Resource<List<Rating>>> ratingList;
    private MutableLiveData<RatingViewModel.TmpDataHolder> ratingListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageLoadingStateData;
    private MutableLiveData<RatingViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> ratingPostData;
    private MutableLiveData<RatingViewModel.TmpDataHolder> ratingPostObj = new MutableLiveData<>();

    public String productId;
    public float numStar;
    public boolean isData = false;

    //region Constructor
    @Inject
    RatingViewModel(RatingRepository repository) {
        Utils.psLog("Inside RatingViewModel");

        //Latest Rating List
        ratingList = Transformations.switchMap(ratingListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("ratingList");

            return repository.getRatingListByProductId(Config.API_KEY, obj.productId, obj.limit, obj.offset);
        });

        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("nextPageLoadingStateData");
            return repository.getNextPageRatingListByProductId(obj.productId, obj.limit, obj.offset);
        });

        ratingPostData = Transformations.switchMap(ratingPostObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("ratingPostData");

            return repository.uploadRatingPostToServer(obj.title, obj.description, obj.rating, obj.shopId, obj.loginUserId, obj.productId);
        });

    }
    //endregion

    //Get Latest Rating
    public void setRatingListObj(String productId, String limit, String offset) {
        if (!isLoading) {
            RatingViewModel.TmpDataHolder tmpDataHolder = new RatingViewModel.TmpDataHolder();
            tmpDataHolder.productId = productId;
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            ratingListObj.setValue(tmpDataHolder);

            //start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Rating>>> getRatingList() {
        return ratingList;
    }

    //Get Latest Rating Next Page
    public void setNextPageLoadingStateObj(String productId, String limit, String offset) {

        if (!isLoading) {
            RatingViewModel.TmpDataHolder tmpDataHolder = new RatingViewModel.TmpDataHolder();
            tmpDataHolder.productId = productId;
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            //start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageLoadingStateData;
    }

    //endregion

    //Get Rating post
    public void setRatingPostObj(String title, String description, String rating, String shopId, String userId, String productId) {

        if (!isLoading) {
            RatingViewModel.TmpDataHolder tmpDataHolder = new RatingViewModel.TmpDataHolder();
            tmpDataHolder.productId = productId;
            tmpDataHolder.loginUserId = userId;
            tmpDataHolder.shopId = shopId;
            tmpDataHolder.rating = rating;
            tmpDataHolder.description = description;
            tmpDataHolder.title = title;

            ratingPostObj.setValue(tmpDataHolder);

            //start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getRatingPostData() {
        return ratingPostData;
    }

    //endregion


    //region Holder

    class TmpDataHolder {
        public String productId = "";
        public String loginUserId = "";
        public String offset = "";
        public String limit = "";
        public String categoryId = "";
        public Boolean isConnected = false;
        public String title = "";
        public String description = "";
        public String rating = "";
        public String shopId = "";

    }

}
