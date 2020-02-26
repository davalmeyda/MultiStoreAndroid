package com.panaceasoft.psmultistore.viewmodel.blog;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.blog.BlogRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.Blog;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class BlogViewModel extends PSViewModel {
    private final LiveData<Resource<List<Blog>>> newsFeedData;
    private MutableLiveData<BlogViewModel.TmpDataHolder> newsFeedObj = new MutableLiveData<>();

    private final LiveData<Resource<List<Blog>>> newsFeedByShopIdData;
    private MutableLiveData<BlogViewModel.NewFeedByShopIdTmpDataHolder> newsFeedByShopIdObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageNewsFeedData;
    private MutableLiveData<BlogViewModel.TmpDataHolder> nextPageNewsFeedObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageNewsFeedByShopIdData;
    private MutableLiveData<BlogViewModel.TmpDataHolderByShopId> nextPageNewsFeedByShopIdObj = new MutableLiveData<>();

    private final LiveData<Resource<Blog>> blogByIdData;
    private MutableLiveData<BlogViewModel.BlogByIdTmpDataHolder> blogByIdObj = new MutableLiveData<>();

    public String shopName, shopId;

    @Inject
    BlogViewModel(BlogRepository repository) {

        newsFeedData = Transformations.switchMap(newsFeedObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNewsFeedList(obj.limit, obj.offset);

        });

        newsFeedByShopIdData = Transformations.switchMap(newsFeedByShopIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNewsFeedListByShopId(obj.shopId, obj.limit, obj.offset);

        });

        nextPageNewsFeedData = Transformations.switchMap(nextPageNewsFeedObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageNewsFeedList(Config.API_KEY, obj.limit, obj.offset);

        });

        nextPageNewsFeedByShopIdData = Transformations.switchMap(nextPageNewsFeedByShopIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageNewsFeedByShopIdList(Config.API_KEY, obj.shopId, obj.limit, obj.offset);

        });

        blogByIdData = Transformations.switchMap(blogByIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getBlogById(obj.id, obj.shopId);

        });

    }

    public void setNewsFeedObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.newsFeedObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Blog>>> getNewsFeedData() {
        return newsFeedData;
    }

    public void setNewsFeedByShopIdObj(String shopId, String limit, String offset) {
        NewFeedByShopIdTmpDataHolder tmpDataHolder = new NewFeedByShopIdTmpDataHolder(shopId, limit, offset);

        this.newsFeedByShopIdObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Blog>>> getNewsFeedByShopIdData() {
        return newsFeedByShopIdData;
    }


    public void setNextPageNewsFeedObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.nextPageNewsFeedObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageNewsFeedData() {
        return nextPageNewsFeedData;
    }

    //new feeds next page by shop id
    public void setNextPageNewsFeedByShopIdObj(String shopId,String limit, String offset) {
        TmpDataHolderByShopId tmpDataHolder = new TmpDataHolderByShopId(shopId, limit, offset);

        this.nextPageNewsFeedByShopIdObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageNewsFeedByShopIdData() {
        return nextPageNewsFeedByShopIdData;
    }

    public void setBlogByIdObj(String id, String shopId) {
        BlogByIdTmpDataHolder blogByIdTmpDataHolder = new BlogByIdTmpDataHolder(id, shopId);

        this.blogByIdObj.setValue(blogByIdTmpDataHolder);
    }

    public LiveData<Resource<Blog>> getBlogByIdData() {
        return blogByIdData;
    }

    class NewFeedByShopIdTmpDataHolder {

        String limit, offset,  shopId;

        private NewFeedByShopIdTmpDataHolder(String shopId, String limit, String offset) {
            this.limit = limit;
            this.offset = offset;
            this.shopId = shopId;
        }
    }

    class TmpDataHolder {

        String  limit, offset;

        public TmpDataHolder(String limit, String offset) {
            this.limit = limit;
            this.offset = offset;
        }

    }
    class TmpDataHolderByShopId {

        String  limit, offset, shopId;

        private TmpDataHolderByShopId(String shopId,String limit, String offset) {
            this.shopId = shopId;
            this.limit = limit;
            this.offset = offset;
        }

    }

    class BlogByIdTmpDataHolder {

        String id, shopId;

        private BlogByIdTmpDataHolder(String id, String shopId) {
            this.id = id;
            this.shopId = shopId;
        }
    }
}
