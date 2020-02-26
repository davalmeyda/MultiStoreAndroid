package com.panaceasoft.psmultistore.ui.shop.selectedshop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.like.LikeButton;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemProductCollectionRowAdapterBinding;
import com.panaceasoft.psmultistore.ui.product.adapter.ProductHorizontalListAdapter;
import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.ProductCollectionHeader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ProductCollectionRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private List<ProductCollectionHeader> productCollectionHeaderList;
    public NewsClickCallback callback;

    public ProductCollectionRowAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, NewsClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    public void replaceCollectionHeader(List<ProductCollectionHeader> productCollectionHeaderList) {
        this.productCollectionHeaderList = productCollectionHeaderList;
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductCollectionRowAdapterBinding binding;

        private MyViewHolder(ItemProductCollectionRowAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductCollectionHeader productCollectionHeader) {
            binding.setProductCollection(productCollectionHeader);
            binding.executePendingBindings();
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemProductCollectionRowAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product_collection_row_adapter, parent, false, dataBindingComponent);

        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            ProductCollectionHeader productCollectionHeader = productCollectionHeaderList.get(position);

            ((MyViewHolder) holder).binding.titleTextView.setText(productCollectionHeader.name);

            ((MyViewHolder) holder).binding.setProductCollection(productCollectionHeader);

            ((MyViewHolder) holder).binding.viewAllTextView.setOnClickListener(view -> callback.onViewAllClick(productCollectionHeaderList.get(position)));

            if (productCollectionHeader.productList.size() != 0) {

                ProductHorizontalListAdapter homeScreenAdapter = new ProductHorizontalListAdapter(dataBindingComponent, new ProductHorizontalListAdapter.NewsClickCallback() {
                    @Override
                    public void onClick(Product product) {
                        callback.onClick(product);
                    }

                    @Override
                    public void onFavLikeClick(Product product, LikeButton likeButton) {
                        callback.onFavLikeClick(product, likeButton);
                    }

                    @Override
                    public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                        callback.onFavUnlikeClick(product, likeButton);
                    }
                });

                ((MyViewHolder) holder).binding.collectionList.setAdapter(homeScreenAdapter);
                homeScreenAdapter.replace(productCollectionHeaderList.get(position).productList);
            }
        }

    }

    public interface NewsClickCallback {
        void onClick(Product product);

        void onViewAllClick(ProductCollectionHeader productCollectionHeader);

        void onFavLikeClick(Product product, LikeButton likeButton);

        void onFavUnlikeClick(Product product, LikeButton likeButton);
    }

    @Override
    public int getItemCount() {
        if (productCollectionHeaderList != null && productCollectionHeaderList.size() > 0) {
            return productCollectionHeaderList.size();
        } else {
            return 0;
        }
    }
}
