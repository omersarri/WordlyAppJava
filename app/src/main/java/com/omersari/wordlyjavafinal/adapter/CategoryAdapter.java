package com.omersari.wordlyjavafinal.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.omersari.wordlyjavafinal.bottom_nav_fragments.HomeFragment;
import com.omersari.wordlyjavafinal.databinding.RecyclerRowCategoryBinding;
import com.omersari.wordlyjavafinal.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    public CategoryAdapter(List<Category> categoryList, RecyclerViewInterface recyclerViewInterface) {
        this.categoryList = categoryList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    private final List<Category> categoryList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowCategoryBinding recyclerRowCategoryBinding = RecyclerRowCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(recyclerRowCategoryBinding, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.textView12.setText(categoryList.get(position).getCategoryName());
        if(categoryList.get(position).id == HomeFragment.selectedCategoryId){
            holder.binding.textView12.setBackgroundColor(Color.parseColor("#9A76A5"));
        }else{
            holder.binding.textView12.setBackgroundColor(Color.parseColor("#C89DD8"));
        }


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerRowCategoryBinding binding;
        public ViewHolder(RecyclerRowCategoryBinding binding, RecyclerViewInterface recyclerViewInterface) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onListItemClick(pos);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onListItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });

        }
    }


}
