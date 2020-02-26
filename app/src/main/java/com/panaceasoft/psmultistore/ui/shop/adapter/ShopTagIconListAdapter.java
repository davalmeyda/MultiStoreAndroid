package com.panaceasoft.psmultistore.ui.shop.adapter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemShopTagIconListAdapterBinding;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.ShopTag;

import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ShopTagIconListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private ShopTagIconListAdapter.NewsClickCallback callback;

    @Nullable
    private List<ShopTag> shopCategories;
    // each time data is set, we update this variable so that if DiffUtil calculation returns
    // after repetitive updates, we can ignore the old calculation
    private int dataVersion = 0;


    public ShopTagIconListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, NewsClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemShopTagIconListAdapterBinding binding;

        private MyViewHolder(ItemShopTagIconListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ShopTag ShopTag) {
            binding.setShopTag(ShopTag);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemShopTagIconListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shop_tag_icon_list_adapter, parent, false, dataBindingComponent);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            if (shopCategories != null && shopCategories.size() > 0) {
                ShopTag shopTag = shopCategories.get(position);

                ((MyViewHolder) holder).binding.setShopTag(shopTag);

                ((MyViewHolder) holder).binding.shopTagNameTextView.setText(shopTag.name);

                holder.itemView.setOnClickListener(view -> callback.onClick(shopTag));
            }
        }

    }

    public interface NewsClickCallback {
        void onClick(ShopTag ShopTag);
    }

    @Override
    public int getItemCount() {
        if (shopCategories != null && shopCategories.size() > 0) {
            return shopCategories.size();
        } else {
            return 0;
        }
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replaceCategories(List<ShopTag> update) {
        dataVersion++;
        if (shopCategories == null) {
            if (update == null) {
                return;
            }
            shopCategories = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = shopCategories.size();
            shopCategories = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = dataVersion;
            final List<ShopTag> oldItems = shopCategories;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @SuppressLint("WrongThread")
                @Override
                protected DiffUtil.DiffResult doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            ShopTag oldItem = oldItems.get(oldItemPosition);
                            ShopTag newItem = update.get(newItemPosition);

                            return Objects.equals(oldItem.id, newItem.id)
                                    && oldItem.name.equals(newItem.name);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            ShopTag oldItem = oldItems.get(oldItemPosition);
                            ShopTag newItem = update.get(newItemPosition);
                            return Objects.equals(oldItem.id, newItem.id)
                                    && oldItem.name.equals(newItem.name);
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return;
                    }
                    shopCategories = update;
                    diffResult.dispatchUpdatesTo(ShopTagIconListAdapter.this);

                }
            }.execute();
        }
    }
}

