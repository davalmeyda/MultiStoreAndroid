package com.panaceasoft.psmultistore.ui.product.filtering;

import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.viewobject.Category;
import com.panaceasoft.psmultistore.viewobject.SubCategory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Grouping {

    public String catId,subCatId;
    public LinkedHashMap<Category,List<SubCategory>> map = new LinkedHashMap<>();

    public LinkedHashMap<Category,List<SubCategory>> group(List<Category> categoryList,List<SubCategory> subCategoryList)
    {
        map.clear();


        for (int i = 0;i < categoryList.size();i++) {

            List<SubCategory> subCategories = new ArrayList<>();

            SubCategory subCategory = new SubCategory(Constants.ZERO,"",Constants.CATEGORY_ALL,Constants.CATEGORY_ALL,"","","","","","","" +
                    "","",null,null);
            subCategories.add(0,subCategory);

            catId = categoryList.get(i).id;

            for (int j = 0;j < subCategoryList.size();j++)
            {
                subCatId = subCategoryList.get(j).catId;

                if (catId.equals(subCatId))
                {
                    subCategories.add(subCategoryList.get(j));
                }
            }

            if(!subCategories.isEmpty())
            {

                map.put(categoryList.get(i),subCategories);
            }
        }

        return map;
    }
}
