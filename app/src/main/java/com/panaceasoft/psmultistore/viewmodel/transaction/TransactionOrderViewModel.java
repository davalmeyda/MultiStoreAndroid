package com.panaceasoft.psmultistore.viewmodel.transaction;

import com.panaceasoft.psmultistore.Config;
import com.panaceasoft.psmultistore.repository.transaction.TransactionOrderRepository;
import com.panaceasoft.psmultistore.utils.AbsentLiveData;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewmodel.common.PSViewModel;
import com.panaceasoft.psmultistore.viewobject.TransactionDetail;
import com.panaceasoft.psmultistore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class TransactionOrderViewModel extends PSViewModel {

    private final LiveData<Resource<List<TransactionDetail>>> transactionOrderListData;
    private MutableLiveData<TransactionOrderViewModel.TmpDataHolder> transactionOrderListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageTransactionOrderLoadingData;
    private MutableLiveData<TransactionOrderViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();


    @Inject
    public TransactionOrderViewModel(TransactionOrderRepository transactionOrderRepository) {
        // Transaction Order List
        transactionOrderListData = Transformations.switchMap(transactionOrderListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Transaction List.");
            return transactionOrderRepository.getTransactionOrderList(Config.API_KEY, obj.transactionHeaderId, obj.offset);
        });


        nextPageTransactionOrderLoadingData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Transaction List.");
            return transactionOrderRepository.getNextPageTransactionOrderList(obj.transactionHeaderId, obj.offset);
        });
    }


    public void setTransactionOrderListObj(String offset, String transactionHeaderId) {
        if (!isLoading) {
            TransactionOrderViewModel.TmpDataHolder tmpDataHolder = new TransactionOrderViewModel.TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.transactionHeaderId = transactionHeaderId;
            transactionOrderListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }


    public LiveData<Resource<List<TransactionDetail>>> getTransactionListData() {
        return transactionOrderListData;
    }


    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageTransactionOrderLoadingData;
    }


    public void setNextPageLoadingStateObj(String offset, String transactionHeaderId) {

        if (!isLoading) {
            TransactionOrderViewModel.TmpDataHolder tmpDataHolder = new TransactionOrderViewModel.TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.transactionHeaderId = transactionHeaderId;
            nextPageLoadingStateObj.setValue(tmpDataHolder);
            // start loading
            setLoadingState(true);
        }
    }


    class TmpDataHolder {
        public String offset = "";
        public String transactionHeaderId = "";
        public Boolean isConnected = false;
    }
}
