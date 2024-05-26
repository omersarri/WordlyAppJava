package com.omersari.wordlyjavafinal.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.omersari.wordlyjavafinal.databinding.DefinitionRowBinding;
import com.omersari.wordlyjavafinal.databinding.DetailsMeaningRowBinding;
import com.omersari.wordlyjavafinal.model.dictionary.Definition;
import com.omersari.wordlyjavafinal.model.dictionary.Dictionary;
import com.omersari.wordlyjavafinal.model.dictionary.DictionaryItem;
import com.omersari.wordlyjavafinal.model.dictionary.Meaning;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MeaningHolder> {
    //private final RecyclerViewInterface recyclerViewInterface;
    private DictionaryItem dictionaryItem;
    public DictionaryAdapter(Dictionary dictionary) {
        this.dictionaryItem = dictionary.get(0);
        if(dictionaryItem != null) {
            dictionaryItem.getMeanings().forEach(meaning -> meaningList.add(meaning));
        }else{
            System.out.println("null");
        }

    }


    private List<Meaning> meaningList = new ArrayList<>();

    @NonNull
    @Override
    public MeaningHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DetailsMeaningRowBinding recyclerRowBinding = DetailsMeaningRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MeaningHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeaningHolder holder, int position) {


        String str = meaningList.get(position).getPartOfSpeech();
        char firstChar = str.charAt(0);
        String upperCaseFirstChar = Character.toUpperCase(firstChar) + str.substring(1);

        holder.binding.type.setText(upperCaseFirstChar);
        holder.binding.definitionsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL,false));
        holder.binding.definitionsRecyclerView.setAdapter(new DefinitionsAdapter(meaningList.get(position)));

        holder.binding.synonymsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL,false));
        holder.binding.synonymsRecyclerView.setAdapter(new SynonymsAdapter(meaningList.get(position), "synonym"));

        holder.binding.antonymsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL,false));
        holder.binding.antonymsRecyclerView.setAdapter(new SynonymsAdapter(meaningList.get(position), "antonyms"));

        /*
        holder.binding.wordCardText.setText(wordList.get(position).name);
        holder.binding.meaningCardText.setText(wordList.get(position).meaning);
        holder.binding.exampleCardText.setText(wordList.get(position).example);
        holder.binding.typeCardText.setText(wordList.get(position).type);

         */


    }

    @Override
    public int getItemCount() {
        return meaningList.size();
    }


    public class MeaningHolder extends RecyclerView.ViewHolder {
        private DetailsMeaningRowBinding binding;
        public MeaningHolder(DetailsMeaningRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            /*
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
            */
        }
    }


}




class DefinitionsAdapter extends RecyclerView.Adapter<DefinitionsAdapter.DefinitionHolder> {
    //private final RecyclerViewInterface recyclerViewInterface;
    public DefinitionsAdapter(Meaning meaning) {
        this.meaning = meaning;
        if(meaning != null) {
            meaning.getDefinitions().forEach(definition -> definitionList.add(definition));
        }
    }

    private Meaning meaning;
    private List<Definition> definitionList = new ArrayList<>();
    private int counter = 1;

    @NonNull
    @Override
    public DefinitionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DefinitionRowBinding recyclerRowBinding = DefinitionRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DefinitionHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DefinitionHolder holder, int position) {

        holder.binding.definitionTextView.setText(counter +"-) "+definitionList.get(position).getDefinition());
        if(definitionList.get(position).getExample() != null){
            holder.binding.exampleTextView.setText("Example: "+definitionList.get(position).getExample());
        }

        counter++;



    }

    @Override
    public int getItemCount() {
        return definitionList.size();
    }


    public class DefinitionHolder extends RecyclerView.ViewHolder {
        private DefinitionRowBinding binding;
        public DefinitionHolder(DefinitionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            /*
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
            */
        }
    }


}

class SynonymsAdapter extends RecyclerView.Adapter<SynonymsAdapter.SynonymHolder> {
    //private final RecyclerViewInterface recyclerViewInterface;
    public SynonymsAdapter(Meaning meaning, String objectType) {
        if(objectType.equals("synonym")){
            this.objectList = Collections.singletonList(meaning.getSynonyms());
        }else{
            this.objectList = Collections.singletonList(meaning.getAntonyms());
        }
    }

    private List<Object> objectList = new ArrayList<>();
    private List<Definition> definitions;
    private int counter = 1;

    @NonNull
    @Override
    public SynonymHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DefinitionRowBinding recyclerRowBinding = DefinitionRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SynonymHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SynonymHolder holder, int position) {
        holder.binding.definitionTextView.setText(counter +"-) "+objectList.get(position));


        counter++;



    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }


    public class SynonymHolder extends RecyclerView.ViewHolder {
        private DefinitionRowBinding binding;
        public SynonymHolder(DefinitionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            /*
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
            */
        }
    }


}
