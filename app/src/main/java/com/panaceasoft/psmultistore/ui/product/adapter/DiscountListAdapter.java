package com.panaceasoft.psmultistore.ui.product.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.like.LikeButton;
import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemDiscountListAdapterBinding;
import com.panaceasoft.psmultistore.databinding.ItemDiscountListAdapterViewAllBinding;
import com.panaceasoft.psmultistore.utils.Constants;
import com.panaceasoft.psmultistore.utils.Utils;
import com.panaceasoft.psmultistore.viewobject.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.databinding.DataBindingUtil.inflate;

public class DiscountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final DiscountListAdapter.NewsClickCallback callback;
    private List<Product> discountList;

    public DiscountListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, NewsClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    public void replaceDiscount(List<Product> discountList) {
        this.discountList = discountList;
        this.notifyDataSetChanged();

    }

    public class MyViewAllHolder extends RecyclerView.ViewHolder {
        final ItemDiscountListAdapterViewAllBinding viewAllBinding;

        private MyViewAllHolder(ItemDiscountListAdapterViewAllBinding viewAllBinding) {

            super(viewAllBinding.getRoot());
            this.viewAllBinding = viewAllBinding;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemDiscountListAdapterBinding binding;

        private MyViewHolder(ItemDiscountListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.setProduct(product);
            binding.executePendingBindings();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //  AQUI SE CREA LA LISTA DE PRODUCTOS EN PROMOCION
        if (viewType == 0) {
            ItemDiscountListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_discount_list_adapter, parent, false, dataBindingComponent);

            binding.originalPriceTextView.setPaintFlags(binding.originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            return new MyViewHolder(binding);

        } else {
            ItemDiscountListAdapterViewAllBinding viewAllBinding = inflate(LayoutInflater.from(parent.getContext()), R.layout.item_discount_list_adapter_view_all, parent, false, dataBindingComponent);

            return new MyViewAllHolder(viewAllBinding);


        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewAllHolder) {

            holder.itemView.setOnClickListener(view -> callback.onViewAllClick());

        } else if (holder instanceof MyViewHolder) {
            if (discountList != null && discountList.size() > 0) {
                Product product = discountList.get(position - 1);

                ((MyViewHolder) holder).binding.ratingBar.setRating(product.ratingDetails.totalRatingValue);

                ((MyViewHolder) holder).binding.ratingBarTextView.setText(((MyViewHolder) holder).binding.getRoot().getResources().getString(R.string.discount__rating5,
                        String.valueOf(product.ratingDetails.totalRatingValue),
                        String.valueOf(product.ratingDetails.totalRatingCount)));

                ((MyViewHolder) holder).binding.priceTextView.setText(String.valueOf(Utils.format(product.unitPrice)));

                String originalPriceStr = product.currencySymbol + Constants.SPACE_STRING + String.valueOf(Utils.format(product.originalPrice));

                ((MyViewHolder) holder).binding.originalPriceTextView.setText(originalPriceStr);

                if (product.isFeatured.equals(Constants.ZERO)) {

                    ((MyViewHolder) holder).binding.featuredIconImageView.setVisibility(View.GONE);

                } else {

                    ((MyViewHolder) holder).binding.featuredIconImageView.setVisibility(View.VISIBLE);

                }

                if (product.isDiscount.equals(Constants.ZERO)) {
                    ((MyViewHolder) holder).binding.originalPriceTextView.setVisibility(View.GONE);
                    ((MyViewHolder) holder).binding.discountTextView.setVisibility(View.GONE);

                } else {

                    ((MyViewHolder) holder).binding.setProduct(product);
                    ((MyViewHolder) holder).binding.originalPriceTextView.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).binding.discountTextView.setVisibility(View.VISIBLE);

                    int discountValue = (int) product.discountPercent;
                    String discountValueStr = "-" + discountValue + "%";
                    ((MyViewHolder) holder).binding.discountTextView.setText(discountValueStr);

//                    if (product.isFavourited.equals(Constants.RATING_ONE)) {
//                        ((MyViewHolder) holder).binding.heartButton.setLiked(true);
//                    } else {
//                        ((MyViewHolder) holder).binding.heartButton.setLiked(false);
//                    }

                }

                holder.itemView.setOnClickListener(view -> {

                    //productDetailFragment = new ProductDetailFragment();
                    //guardarCarrito(product.id, product.unitPrice, product.originalPrice, product.shopId);
                    this.callback.onClick(product);
                });

//                ((MyViewHolder) holder).binding.heartButton.setOnLikeListener(new OnLikeListener() {
//                    @Override
//                    public void liked(LikeButton likeButton) {
//
//                        Product product = ((MyViewHolder) holder).binding.getProduct();
//                        if (product != null && callback != null) {
//                            callback.onFavLikeClick(product, ((MyViewHolder) holder).binding.heartButton);
//                        }
//
//                    }
//
//                    @Override
//                    public void unLiked(LikeButton likeButton) {
//
//                        Product product = ((MyViewHolder) holder).binding.getProduct();
//                        if (product != null && callback != null) {
//                            callback.onFavUnlikeClick(product, ((MyViewHolder) holder).binding.heartButton);
//                        }
//                    }
//                });
            }
        }
    }

    /*public void guardarCarrito(String productId, float price, float originalPrice, String selectedShopId){

        basketViewModel.setSaveToBasketListObj(
                0,
                productId,
                1,
                "{}",
                "",
                "",
                null,
                price,
                originalPrice,
                selectedShopId,
                "{}"
        );
    }*/

    public interface NewsClickCallback {
        void onClick(Product product);

        void onViewAllClick();

        void onFavLikeClick(Product product, LikeButton likeButton);

        void onFavUnlikeClick(Product product, LikeButton likeButton);
    }

    @Override
    public int getItemCount() {
        if (discountList != null && discountList.size() > 0) {
            return discountList.size() + 1;
        } else {
            return 0;
        }
    }
}
