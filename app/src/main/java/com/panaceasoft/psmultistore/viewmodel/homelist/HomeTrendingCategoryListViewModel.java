package com.panaceasoft.psmultistore.viewmodel.homelist;

import com.panaceasoft.psmultistore.repository.category.CategoryRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.Category;
import com.panaceasoft.psmultistore.viewobject.common.Resource;
import com.panaceasoft.psmultistore.viewobject.holder.CategoryParameterHolder;
import com.panaceasoft.psmultistore.viewobject.holder.ProductParameterHolder;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class HomeTrendingCategoryListViewModel extends PSViewModel {

    private final LiveData<Resource<List<Category>>> homeTrendingCategoryListData;
    private MutableLiveData<HomeTrendingCategoryListViewModel.TmpDataHolder> homeTrendingCategoryListobj = new MutableLiveData<HomeTrendingCategoryListViewModel.TmpDataHolder>();

    private final LiveData<Resource<Boolean>> homeTrendingCategoryLoadNetworkData;
    private final MutableLiveData<HomeTrendingCategoryListViewModel.TmpDataHolder> homeTrendingCategoryLoadNetworkListObj = new MutableLiveData<HomeTrendingCategoryListViewModel.TmpDataHolder>();

    public ProductParameterHolder productParameterHolder = new ProductParameterHolder();

    public CategoryParameterHolder categoryParameterHolder = new CategoryParameterHolder().getTrendingCategories();

    @Inject
    HomeTrendingCategoryListViewModel(CategoryRepository repository) {
        Utils.psLog("Inside ProductViewModel");

        homeTrendingCategoryListData = Transformations.switchMap(homeTrendingCategoryListobj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getAllSearchCategory(obj.categoryParameterHolder, obj.limit, obj.offset);
        });

        homeTrendingCategoryLoadNetworkData = Transformations.switchMap(homeTrendingCategoryLoadNetworkListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextSearchCategory(obj.limit, obj.offset, obj.categoryParameterHolder);
        });
    }

    public void setHomeTrendingCatrgoryListDataObj(CategoryParameterHolder categoryParameterHolder, String limit, String offset) {

        HomeTrendingCategoryListViewModel.TmpDataHolder tmpDataHolder = new HomeTrendingCategoryListViewModel.TmpDataHolder();
        tmpDataHolder.limit = limit;
        tmpDataHolder.offset = offset;
        tmpDataHolder.categoryParameterHolder = categoryParameterHolder;
        homeTrendingCategoryListobj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<List<Category>>> getHomeTrendingCategoryListData() {
        return homeTrendingCategoryListData;
    }


    public void setHomeTrendingCategoryLoadNetworkObj(String loginUserId, String limit, String offset, CategoryParameterHolder categoryParameterHolder) {
        if (!isLoading) {
            HomeTrendingCategoryListViewModel.TmpDataHolder tmpDataHolder = new HomeTrendingCategoryListViewModel.TmpDataHolder();
            tmpDataHolder.loginUserId = loginUserId;
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.categoryParameterHolder = categoryParameterHolder;
            homeTrendingCategoryLoadNetworkListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getHomeTrendingCategoryLoadNetworkData() {
        return homeTrendingCategoryLoadNetworkData;
    }

    class TmpDataHolder {
        public String loginUserId = "";
        public String limit = "";
        public String offset = "";
        public String shopId = "";
        public String orderBy = "";
        public CategoryParameterHolder categoryParameterHolder;

    }
}
