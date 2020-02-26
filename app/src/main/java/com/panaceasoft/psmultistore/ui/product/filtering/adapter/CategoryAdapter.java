package com.panaceasoft.psmultistore.ui.product.filtering.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemCategoryFilterBinding;
import com.panaceasoft.psmultistore.databinding.ItemSubCategoryFilterBinding;
import com.panaceasoft.psmultistore.ui.product.filtering.Grouping;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.Category;
import com.panaceasoft.psmultistore.viewobject.SubCategory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.databinding.DataBindingUtil;

public class CategoryAdapter extends BaseExpandableListAdapter {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final filteringClickCallback callback;
    private List<Category> categoryList = new ArrayList<>();
    private List<SubCategory> subCategoryListOrg = new ArrayList<>();
    private LinkedHashMap<Category, List<SubCategory>> map = new LinkedHashMap<>();
    private Grouping grouping = new Grouping();
    private TextView selectedView = null;
    private String catId, subCatId;


    public CategoryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                           filteringClickCallback callback, String catId, String subCatId) {

        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.catId = catId;
        this.subCatId = subCatId;
    }

    public void replaceCategory(List<Category> categoryList) {
        this.categoryList = categoryList;
        if (categoryList != null && categoryList.size() != 0) {
            map = grouping.group(categoryList, subCategoryListOrg);
        }

    }

    public void replaceSubCategory(List<SubCategory> subCategoryList) {
        this.subCategoryListOrg = subCategoryList;
        if (categoryList.size() != 0 && subCategoryListOrg.size() != 0) {

            map = grouping.group(categoryList, subCategoryListOrg);
        }
    }

    @Override
    public int getGroupCount() {
        if (categoryList == null) {
            return 0;
        }
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<SubCategory> sub;
        if (categoryList != null) {
            sub = this.map.get(this.categoryList.get(i));
            if (sub != null) {
                return sub.size();
            }
        }

        return 0;
    }


    @Override
    public Object getGroup(int i) {
        return categoryList.get(i);
    }

    @Override
    public SubCategory getChild(int i, int i1) {
        List<SubCategory> subCategoryList = this.map.get(this.categoryList.get(i));
        if (subCategoryList != null) {
            return subCategoryList.get(i1);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {

            ItemCategoryFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_category_filter, viewGroup, false, dataBindingComponent);

            binding.setCategory(categoryList.get(i));

            if (categoryList.get(i).id.equals(catId)) {
                binding.groupview.setBackgroundColor(viewGroup.getResources().getColor(R.color.md_green_50));
            }

            if (isExpanded) {
                Utils.toggleUporDown(binding.dropdownimage);
            }

            return binding.getRoot();

    }

    @Override
    public View getChildView(int i, int childPosition, boolean b, View view, ViewGroup viewGroup) {

        ItemSubCategoryFilterBinding subCategoryItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_sub_category_filter, viewGroup, false, dataBindingComponent);

        SubCategory subCategory = getChild(i, childPosition);
        subCategoryItemBinding.setSubcategory(subCategory);

        if (categoryList.get(i).id.equals(catId) && subCategory.id.equals(subCatId)) {
            subCategoryItemBinding.subcategoryItem.setCompoundDrawablesWithIntrinsicBounds(null, null, viewGroup.getResources().getDrawable(R.drawable.baseline_check_green_24), null);
            selectedView = subCategoryItemBinding.subcategoryItem;
        }

        subCategoryItemBinding.getRoot().setOnClickListener(view1 -> {

            if (selectedView != null) {
                selectedView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            }

            subCategoryItemBinding.subcategoryItem.setCompoundDrawablesWithIntrinsicBounds(null, null, viewGroup.getResources().getDrawable(R.drawable.baseline_check_green_24), null);

            catId = (categoryList.get(i).id);

            if (childPosition != 0) {
                subCatId = subCategory.id;
            } else {
                subCatId = Constants.ZERO;
            }

            callback.onClick(catId, subCatId);

            ((Activity) viewGroup.getContext()).finish();
        });

        return subCategoryItemBinding.getRoot();

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public interface filteringClickCallback {
        void onClick(String catId, String subCatId);
    }

}
