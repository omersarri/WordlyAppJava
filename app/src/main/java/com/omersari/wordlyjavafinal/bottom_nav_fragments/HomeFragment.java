package com.omersari.wordlyjavafinal.bottom_nav_fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.omersari.wordlyjavafinal.R;
import com.omersari.wordlyjavafinal.games.StoryActivity;
import com.omersari.wordlyjavafinal.WordDetailsActivity;
import com.omersari.wordlyjavafinal.adapter.CategoryAdapter;
import com.omersari.wordlyjavafinal.adapter.RecyclerViewInterface;
import com.omersari.wordlyjavafinal.adapter.WordAdapter;
import com.omersari.wordlyjavafinal.databinding.FragmentHomeBinding;
import com.omersari.wordlyjavafinal.model.Category;
import com.omersari.wordlyjavafinal.model.CategoryManager;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.model.WordManager;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements RecyclerViewInterface {

    private WordAdapter wordAdapter;

    private CategoryAdapter categoryAdapter;
    private List<Word> wordList;
    private List<Category> categoryList;
    private FragmentHomeBinding binding;
    public static int selectedCategoryId = 1;

    private CategoryManager categoryManager;
    private int nightModeFlags;
    private WordManager wordManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        categoryList = new ArrayList<>();
        wordList = new ArrayList<>();


        categoryManager = CategoryManager.getInstance();
        wordManager = WordManager.getInstance();
        binding.floatingActionButton.setVisibility(View.GONE);
        nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        getWords();
        getCategories();
        floatingButtonClick();

        return binding.getRoot();


    }

    public void getWords() {
        wordList.clear();
        wordManager.getWords(getActivity(), new WordManager.GetWordsCallback() {
            @Override
            public void onSuccess(ArrayList<Word> wordArrayList) {
                for (Word word : wordArrayList) {
                    if (word.getCategoryId() == selectedCategoryId) {
                        wordList.add(word);
                    }
                }
                if (wordList.isEmpty()) {
                    binding.textViewNoItem.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);

                } else {
                    binding.textViewNoItem.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    wordAdapter = new WordAdapter(wordList, HomeFragment.this);
                    binding.recyclerView.setAdapter(wordAdapter);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCategories() {
        categoryList.clear();
        categoryManager.getCategories(getActivity(), new CategoryManager.GetCategoriesCallback() {
            @Override
            public void onSuccess(ArrayList<Category> categoryArrayList) {
                categoryList = categoryArrayList;
                //categoryList.addAll(categoryArrayList);
                if (categoryList.isEmpty()) {
                    Category category = new Category();
                    category.setCategoryName("Main");
                    categoryManager.addCategory(getActivity(), category, new CategoryManager.AddCategoryCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(String errorMessage) {

                        }
                    });
                }
                //this.categoryList = categoryList;
                binding.recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                categoryAdapter = new CategoryAdapter(categoryList, HomeFragment.this, nightModeFlags);
                binding.recyclerView2.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getActivity(), WordDetailsActivity.class);
        intent.putExtra("word", wordList.get(position).getName());
        intent.putExtra("meaning", wordList.get(position).getMeaning());
        startActivity(intent);

        //itemDetailsAlert(position);
    }

    public void onItemLongClick(int position) {
        wordManager.deleteWord(getActivity(), wordList.get(position), new WordManager.DeleteWordCallback() {
            @Override
            public void onSuccess() {
                wordList.remove(position);
                wordAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onListItemClick(int position) {

        selectedCategoryId = categoryList.get(position).id;
        getCategories();
        getWords();


    }

    public void onListItemLongClick(int position) {
        selectedCategoryId = categoryList.get(position).id;
        if (selectedCategoryId == 1) {
            Toast.makeText(getActivity(), "You can't delete main list.", Toast.LENGTH_SHORT).show();
        } else {
            categoryManager.deleteCategory(getActivity(), categoryList.get(position), new CategoryManager.DeleteCategoryCallback() {
                @Override
                public void onSuccess() {
                    categoryList.remove(position);
                    categoryAdapter.notifyItemChanged(position);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            wordManager.deleteListItems(getActivity(), selectedCategoryId, new WordManager.DeleteWordCallback() {
                @Override
                public void onSuccess() {
                    //onListItemClick(1);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    private void floatingButtonClick() {
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoryActivity.class);
                startActivity(intent);
            }
        });
    }

}