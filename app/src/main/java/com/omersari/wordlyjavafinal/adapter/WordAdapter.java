package com.omersari.wordlyjavafinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.omersari.wordlyjavafinal.databinding.RecyclerRowBinding;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.util.Util;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    public WordAdapter(List<Word> wordList, RecyclerViewInterface recyclerViewInterface) {
        this.wordList = wordList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    private List<Word> wordList;

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WordHolder(recyclerRowBinding, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder holder, int position) {

        holder.binding.textView1.setText(wordList.get(position).getName());
        holder.binding.textView2.setText(wordList.get(position).getMeaning());
        holder.binding.textView4.setText(wordList.get(position).getType());
        Util util = new Util();
        int succesRate = util.calculateSuccessRate(wordList.get(position).getStats());
        holder.binding.textView3.setText("%"+ succesRate);
        holder.binding.cardView.setCardBackgroundColor(wordList.get(position).getColor());


    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }


    public class WordHolder extends RecyclerView.ViewHolder {
        private RecyclerRowBinding binding;

        public WordHolder(RecyclerRowBinding binding, RecyclerViewInterface recyclerViewInterface) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });

        }
    }


}
