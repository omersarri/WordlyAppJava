package com.omersari.wordlyjavafinal.bottom_nav_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.omersari.wordlyjavafinal.MainActivity;
import com.omersari.wordlyjavafinal.adapter.CategoryAdapter;
import com.omersari.wordlyjavafinal.adapter.WordAdapter;
import com.omersari.wordlyjavafinal.games.PracticeAIActivity;
import com.omersari.wordlyjavafinal.games.StoryActivity;
import com.omersari.wordlyjavafinal.games.TrainingCardsActivity;
import com.omersari.wordlyjavafinal.databinding.FragmentTrainingBinding;
import com.omersari.wordlyjavafinal.games.TrainingTestActivity;
import com.omersari.wordlyjavafinal.games.TrainingWritingActivity;
import com.omersari.wordlyjavafinal.model.Category;
import com.omersari.wordlyjavafinal.model.CategoryManager;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.model.WordManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class TrainingFragment extends Fragment {

    private FragmentTrainingBinding binding;
    private CompositeDisposable compositeDisposable =new CompositeDisposable();

    private CategoryManager categoryManager;
    private WordManager wordManager;

    private List<Word> wordList;
    private List<Category> categoryList;
    private List<String> categoryNames;

    private int selectedCategoryId = -1;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryManager = CategoryManager.getInstance();
        wordManager = WordManager.getInstance();

        wordList = new ArrayList<>();
        categoryList = new ArrayList<>();
        categoryNames = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrainingBinding.inflate(inflater, container, false);

        cardViewCardsClicked();
        cardViewPracticeClicked();
        cardViewStoryClicked();
        cardViewWritingClicked();
        cardViewTestClicked();
        getCategories();
        selectListSpinner();





        return binding.getRoot();

    }

    private void selectListSpinner() {



        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("All")){
                    selectedCategoryId=-1;
                }else {
                    selectedCategoryId = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cardViewCardsClicked() {
        CardView cardsButton = binding.cardViewCards;
        cardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToCardsTraining = new Intent(getContext(), TrainingCardsActivity.class);
                startActivity(intentToCardsTraining);
            }
        });
    }

    private void cardViewPracticeClicked() {
        CardView cardsButton = binding.cardViewChat;
        cardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToPracticeTraining = new Intent(getContext(), PracticeAIActivity.class);
                startActivity(intentToPracticeTraining);
            }
        });
    }

    private void cardViewStoryClicked() {
        CardView cardsButton = binding.cardViewStory;
        cardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToStoryTraining = new Intent(getContext(), StoryActivity.class);
                intentToStoryTraining.putExtra("selectedCategoryId",selectedCategoryId);
                startActivity(intentToStoryTraining);
            }
        });
    }

    private void cardViewWritingClicked() {
        CardView cardsButton = binding.cardViewWriting;
        cardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToWritingTraining = new Intent(getContext(), TrainingWritingActivity.class);
                intentToWritingTraining.putExtra("selectedCategoryId",selectedCategoryId);
                startActivity(intentToWritingTraining);
            }
        });
    }

    private void cardViewTestClicked() {
        CardView cardsButton = binding.cardViewTest;
        cardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToTestTraining = new Intent(getContext(), TrainingTestActivity.class);
                intentToTestTraining.putExtra("selectedCategoryId",selectedCategoryId);
                startActivity(intentToTestTraining);
            }
        });
    }





    public void getCategories() {
        categoryList.clear();
        categoryManager.getCategories(getActivity(), new CategoryManager.GetCategoriesCallback() {
            @Override
            public void onSuccess(ArrayList<Category> categoryArrayList) {
                categoryList = categoryArrayList;
                categoryNames.add("All");

                categoryList.forEach(category -> {
                    categoryNames.add(category.getCategoryName());
                });

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinner.setAdapter(adapter);


            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}