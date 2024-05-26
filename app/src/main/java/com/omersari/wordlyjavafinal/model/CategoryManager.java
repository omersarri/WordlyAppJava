package com.omersari.wordlyjavafinal.model;

import android.content.Context;

import androidx.room.Room;


import com.omersari.wordlyjavafinal.database.CategoryDao;
import com.omersari.wordlyjavafinal.database.CategoryDatabase;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CategoryManager {
    private static CategoryManager instance;

    private CategoryDao categoryDao;
    private CategoryDatabase categoryDatabase;
    private CompositeDisposable compositeDisposable;

    private ArrayList<Category> categories = new ArrayList<>();

    private CategoryManager() {

    }

    public interface AddCategoryCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public interface GetCategoriesCallback {
        void onSuccess(ArrayList<Category> categoriesArrayList);
        void onFailure(String errorMessage);
    }
    public interface DeleteCategoryCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public interface UpdateCategoryCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }


    public void addCategory(Context context, Category category, AddCategoryCallback addCategoryCallback) {
        categoryDatabase = Room.databaseBuilder(context, CategoryDatabase.class, "Category").build();
        categoryDao = categoryDatabase.categoryDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(categoryDao.insert(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->{
                            addCategoryCallback.onSuccess();
                            //favorites.add(recipe);
                        },
                        throwable -> addCategoryCallback.onFailure("Error while adding category")));

    }

    public void getCategories(Context context, GetCategoriesCallback getCategoriesCallback) {
        ArrayList<Category> categoryTemp = new ArrayList<>();
        categoryDatabase = Room.databaseBuilder(context, CategoryDatabase.class, "Category").build();
        categoryDao = categoryDatabase.categoryDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(categoryDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryList ->{
                            categoryTemp.addAll(categoryList);
                            categories = categoryTemp;
                            getCategoriesCallback.onSuccess(categoryTemp);
                        },
                        throwable -> getCategoriesCallback.onFailure("Error while fetching categories")
                ));

    }

    public void deleteCategory(Context context, Category category, DeleteCategoryCallback deleteCategoryCallback) {
        categoryDatabase = Room.databaseBuilder(context, CategoryDatabase.class, "Category").build();
        categoryDao = categoryDatabase.categoryDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(categoryDao.delete(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->{
                            deleteCategoryCallback.onSuccess();
                            //favorites.remove(recipe);
                        },
                        throwable -> deleteCategoryCallback.onFailure("Error while deleting category")));

    }




    public ArrayList<Category> getCategoriesList(){
        return categories;
    }







    public static synchronized CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }
}
