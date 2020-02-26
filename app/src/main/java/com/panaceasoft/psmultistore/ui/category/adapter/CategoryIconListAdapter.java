package com.panaceasoft.psmultistore.ui.category.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.panaceasoft.psmultistore.R;
import com.panaceasoft.psmultistore.databinding.ItemHomeCategoryIconListBinding;
import com.panaceasoft.psmultistore.ui.common.DataBoundListAdapter;
import com.panaceasoft.psmultistore.ui.common.DataBoundViewHolder;
import com.panaceasoft.psmultistore.utils.Objects;
import com.panaceasoft.psmultistore.viewobject.Category;

import androidx.databinding.DataBindingUtil;


/**
 * Created by Panacea-Soft on 9/18/18.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class CategoryIconListAdapter extends DataBoundListAdapter<Category, ItemHomeCategoryIconListBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final CategoryClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    private int lastPosition = -1;


    public CategoryIconListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                           CategoryClickCallback callback,
                           DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemHomeCategoryIconListBinding createBinding(ViewGroup parent) {
        ItemHomeCategoryIconListBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_home_category_icon_list, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Category category = binding.getCat();
            if (category != null && callback != null) {
                callback.onClick(category);
            }
        });
        return binding;
    }

    @Override
    public void bindView(DataBoundViewHolder<ItemHomeCategoryIconListBinding> holder, int position) {
        super.bindView(holder, position);

        //setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemHomeCategoryIconListBinding binding, Category item) {
        binding.setCat(item);
    }

    @Override
    protected boolean areItemsTheSame(Category oldItem, Category newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(Category oldItem, Category newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface CategoryClickCallback {
        void onClick(Category category);
    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else {
            lastPosition = position;
        }
    }
}

/*
Multi Cell Style

public class CategoryIconListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private NewsClickCallback callback;

    @Nullable
    private List<Category> categoryList;
    // each time data is set, we update this variable so that if DiffUtil calculation returns
    // after repetitive updates, we can ignore the old calculation
    private int dataVersion = 0;


    public CategoryIconListAdapter(DataBindingComponent dataBindingComponent, NewsClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    private class MyViewAllHolder extends RecyclerView.ViewHolder {
        private final ItemHomeCategoryIconListViewallBinding viewAllBinding;

        private MyViewAllHolder(ItemHomeCategoryIconListViewallBinding viewAllBinding) {
            super(viewAllBinding.getRoot());
            this.viewAllBinding = viewAllBinding;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemHomeCategoryIconListBinding binding;

        private MyViewHolder(ItemHomeCategoryIconListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Category category) {
            binding.setCat(category);
            binding.executePendingBindings();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (categoryList != null && categoryList.size() > 0) {
            if (position == categoryList.size()) {
                return 0;
            } else {
                return 0;
            }
        } else return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ItemHomeCategoryIconListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_category_icon_list, parent, false, dataBindingComponent);
            return new MyViewHolder(binding);
        } else {
            ItemHomeCategoryIconListViewallBinding viewAllBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_home_category_icon_list_viewall, parent, false, dataBindingComponent);
            return new MyViewAllHolder(viewAllBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewAllHolder) {

            ((MyViewAllHolder) holder).viewAllBinding.iconImageView.setImageResource(R.drawable.viewall_category_default_cell);

            ((MyViewAllHolder) holder).viewAllBinding.nameTextView.setText(R.string.menu__home_category_view_All_un);

            holder.itemView.setOnClickListener(view -> callback.onViewAllClick());

        } else if (holder instanceof MyViewHolder) {

            if (categoryList != null && categoryList.size() > 0) {
                Category category = categoryList.get(position);

                ((MyViewHolder) holder).binding.setCat(category);

                holder.itemView.setOnClickListener(view -> callback.onClick(category));
            }
        }


    }

    public interface NewsClickCallback {
        void onClick(Category category);

        void onViewAllClick();
    }

    @Override
    public int getItemCount() {
        if (categoryList != null && categoryList.size() > 0) {
            return categoryList.size();
        } else {
            return 0;
        }
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replaceCategories(List<Category> update) {
        dataVersion++;
        if (categoryList == null) {
            if (update == null) {
                return;
            }
            categoryList = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = categoryList.size();
            categoryList = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = dataVersion;
            final List<Category> oldItems = categoryList;
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
                            Category oldItem = oldItems.get(oldItemPosition);
                            Category newItem = update.get(newItemPosition);

                            return Objects.equals(oldItem.id, newItem.id)
                                    && oldItem.name.equals(newItem.name);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            Category oldItem = oldItems.get(oldItemPosition);
                            Category newItem = update.get(newItemPosition);
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
                    categoryList = update;
                    diffResult.dispatchUpdatesTo(CategoryIconListAdapter.this);

                }
            }.execute();
        }
    }
}

*/