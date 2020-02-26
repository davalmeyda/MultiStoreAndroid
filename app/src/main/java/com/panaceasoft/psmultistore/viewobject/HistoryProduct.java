package com.panaceasoft.psmultistore.viewobject;

import androidx.room.Entity;
import androidx.annotation.NonNull;

@Entity(primaryKeys = "id")
public class HistoryProduct {

    @NonNull
    public final String id;

    public  String historyName;

    public String historyUrl;

    public String historyDate;

    public HistoryProduct(String id, String historyName, String historyUrl, String historyDate) {
        this.id = id;
        this.historyName = historyName;
        this.historyUrl = historyUrl;
        this.historyDate = historyDate;
    }
}
