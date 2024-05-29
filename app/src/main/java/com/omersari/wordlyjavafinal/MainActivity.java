package com.omersari.wordlyjavafinal;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.omersari.wordlyjavafinal.bottom_nav_fragments.HomeFragment;
import com.omersari.wordlyjavafinal.bottom_nav_fragments.TrainingFragment;
import com.omersari.wordlyjavafinal.databinding.ActivityMainBinding;
import com.omersari.wordlyjavafinal.model.Category;
import com.omersari.wordlyjavafinal.model.CategoryManager;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private ActivityMainBinding binding;
    private boolean mState;


    CategoryManager categoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //toolbar initialization
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        mState = true;

        categoryManager = CategoryManager.getInstance();

        categoryManager.getCategories(this, new CategoryManager.GetCategoriesCallback() {
            @Override
            public void onSuccess(ArrayList<Category> categoryArrayList) {
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setBackground(ContextCompat.getDrawable(this, R.drawable.bottombar_shape));

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            bottomNavigationView.setItemTextColor(getResources().getColorStateList(R.color.lightGray, getTheme()));
            bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.lightGray, getTheme()));
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.secondary_dark, getTheme()));
        } else {
            bottomNavigationView.setItemTextColor(getResources().getColorStateList(R.color.lightGray, getTheme()));
            bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.lightGray, getTheme()));
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.secondary, getTheme()));
        }


        bottomNavBar();





    }

    //bottom nav bar

    private void bottomNavBar() {
        //binding.bottomNavigationView.setItemIconTintList(null);

        openFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if(R.id.bottom_home == item.getItemId()){
                            openFragment(new HomeFragment());
                            mState = true;
                            item.setChecked(true);
                            invalidateOptionsMenu();
                        } else if(R.id.bottom_training == item.getItemId()){
                            openFragment(new TrainingFragment());
                            mState= false;
                            item.setChecked(true);
                            invalidateOptionsMenu();
                        }
                        return false;
                    }
                }
        );
    }


    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_action_menu,menu);

        if (!mState)
        {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return true;
    }
    //toolbar actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_addWord){
            Intent intent = new Intent(MainActivity.this, AddNewWordActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_addList){
            addNewCategory();
        }

        return false;
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager1.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

    public void addNewCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View customLayout = getLayoutInflater().inflate(R.layout.add_list_custom,null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        EditText categoryEditText = customLayout.findViewById(R.id.categoryEditText);
        Button categoryaddButton = customLayout.findViewById(R.id.addCategoryButton);
        categoryaddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = categoryEditText.getText().toString();
                Category category = new Category();
                category.setCategoryName(name);
                categoryManager.addCategory(MainActivity.this, category, new CategoryManager.AddCategoryCallback() {
                    @Override
                    public void onSuccess() {
                        dialog.cancel();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        dialog.show();
    }



    /*
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

     */

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}