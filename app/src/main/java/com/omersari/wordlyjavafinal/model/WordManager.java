package com.omersari.wordlyjavafinal.model;

import android.content.Context;

import androidx.room.Room;


import com.omersari.wordlyjavafinal.database.WordDao;
import com.omersari.wordlyjavafinal.database.WordDatabase;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WordManager {
    private static WordManager instance;

    private WordDao wordDao;
    private WordDatabase wordDatabase;
    private CompositeDisposable compositeDisposable;
    private ArrayList<Word> wordsAll = new ArrayList<>();


    private WordManager() {

    }

    public interface AddWordCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public interface GetWordsCallback {
        void onSuccess(ArrayList<Word> wordArrayList);
        void onFailure(String errorMessage);
    }
    public interface DeleteWordCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    public interface UpdateWordCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }


    public void addWord(Context context, Word word, AddWordCallback addWordCallback) {
        wordDatabase = Room.databaseBuilder(context, WordDatabase.class, "Word").build();
        wordDao = wordDatabase.wordDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(wordDao.insert(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->{
                            addWordCallback.onSuccess();
                            //favorites.add(recipe);
                        },
                        throwable -> addWordCallback.onFailure("Error while adding word")));

    }

    public void getWords(Context context, GetWordsCallback getWordsCallback) {
        ArrayList<Word> words = new ArrayList<>();
        wordDatabase = Room.databaseBuilder(context, WordDatabase.class, "Word").build();
        wordDao = wordDatabase.wordDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(wordDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        wordList ->{
                            words.addAll(wordList);
                            wordsAll = words;
                            getWordsCallback.onSuccess(words);
                        },
                        throwable -> getWordsCallback.onFailure("Error while fetching words")
                ));

    }

    public void deleteWord(Context context, Word word, DeleteWordCallback deleteWordCallback) {
        wordDatabase = Room.databaseBuilder(context, WordDatabase.class, "Word").build();
        wordDao = wordDatabase.wordDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(wordDao.delete(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->{
                            deleteWordCallback.onSuccess();
                            //favorites.remove(recipe);
                        },
                        throwable -> deleteWordCallback.onFailure("Error while deleting word")));

    }

    public void deleteListItems(Context context, int selectedCategoryId, DeleteWordCallback deleteWordCallback) {
        wordDatabase = Room.databaseBuilder(context, WordDatabase.class, "Word").build();
        wordDao = wordDatabase.wordDao();

        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(wordDao.deleteListItems(selectedCategoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () ->{
                            deleteWordCallback.onSuccess();
                            //favorites.remove(recipe);
                        },
                        throwable -> deleteWordCallback.onFailure("Error while deleting words")));

    }




    public ArrayList<Word> getWords(){
        return wordsAll;
    }







    public static synchronized WordManager getInstance() {
        if (instance == null) {
            instance = new WordManager();
        }
        return instance;
    }
}
