package com.omersari.wordlyjavafinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omersari.wordlyjavafinal.databinding.CardsRowBinding;
import com.omersari.wordlyjavafinal.model.Word;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.WordHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    public CardAdapter(List<Word> wordList, RecyclerViewInterface recyclerViewInterface) {
        this.wordList = wordList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    private List<Word> wordList;

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardsRowBinding recyclerRowBinding = CardsRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WordHolder(recyclerRowBinding, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder holder, int position) {
        /*
        holder.binding.wordCardText.setText(wordList.get(position).name);
        holder.binding.meaningCardText.setText(wordList.get(position).meaning);
        holder.binding.exampleCardText.setText(wordList.get(position).example);
        holder.binding.typeCardText.setText(wordList.get(position).type);

         */


    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }


    public class WordHolder extends RecyclerView.ViewHolder {
        private CardsRowBinding binding;
        public WordHolder(CardsRowBinding binding, RecyclerViewInterface recyclerViewInterface) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }


}
