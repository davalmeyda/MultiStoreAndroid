package com.panaceasoft.psmultistore.viewmodel.comment;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.comment.CommentRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.Comment;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class CommentListViewModel extends PSViewModel {

    //for recent comment list

    public final String PRODUCT_ID_KEY = "product_id";
    public String productId = "";

    private final LiveData<Resource<List<Comment>>> commentListData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> commentListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageCommentLoadingData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> sendCommentHeaderPostData;
    private MutableLiveData<com.panaceasoft.psmultistore.viewmodel.comment.CommentListViewModel.TmpDataHolder> sendCommentHeaderPostDataObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> commentCountLoadingData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> commentCountLoadingStateObj = new MutableLiveData<>();
    //region Constructor

    @Inject
    public CommentListViewModel(CommentRepository commentRepository) {
        // Latest comment List
        commentListData = Transformations.switchMap(commentListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getCommentList(Config.API_KEY, obj.productId, obj.limit, obj.offset);
        });

        nextPageCommentLoadingData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getNextPageCommentList(obj.productId, obj.limit, obj.offset);
        });

        sendCommentHeaderPostData = Transformations.switchMap(sendCommentHeaderPostDataObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return commentRepository.uploadCommentHeaderToServer(
                    obj.productId,
                    obj.userId,
                    obj.headerComment,
                    obj.shopId);
        });

        commentCountLoadingData = Transformations.switchMap(commentCountLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getCommentDetailReplyCount(obj.comment_id);
        });
    }

    //endregion
    public void setSendCommentHeaderPostDataObj(String product_id,
                                                String userId,
                                                String headerComment,
                                                String shopId
    ) {
        if (!isLoading) {
            com.panaceasoft.psmultistore.viewmodel.comment.CommentListViewModel.TmpDataHolder tmpDataHolder = new com.panaceasoft.psmultistore.viewmodel.comment.CommentListViewModel.TmpDataHolder();
            tmpDataHolder.productId = product_id;
            tmpDataHolder.userId = userId;
            tmpDataHolder.headerComment = headerComment;
            tmpDataHolder.shopId = shopId;
            sendCommentHeaderPostDataObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getsendCommentHeaderPostData() {
        return sendCommentHeaderPostData;
    }


    //region Getter And Setter for Comment List

    public void setCommentListObj(String limit, String offset, String productId) {
        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.productId = productId;
            commentListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Comment>>> getCommentListData() {
        return commentListData;
    }

    //Get Comment Next Page
    public void setNextPageCommentLoadingObj(String product_id, String limit, String offset) {

        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.productId = product_id;
            tmpDataHolder.offset = offset;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public void setCommentCountLoadingObj(String comment_id) {

        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.comment_id = comment_id;
            commentCountLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getCommentCountLoadingStateData() {
        return commentCountLoadingData;
    }


    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageCommentLoadingData;
    }
/*

    public void setSendCommentHeaderPostDataObj(String productId, String loginUserId, String description) {
    }
*/

    //endregion

    //region Holder
    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public String productId = "";
        public String userId = "";
        public String headerComment = "";
        public String comment_id = "";
        public Boolean isConnected = false;
        public String shopId;
    }
    //endregion
}
