package com.omersari.wordlyjavafinal.domain;

import com.omersari.wordlyjavafinal.model.dictionary.Dictionary;
import com.omersari.wordlyjavafinal.model.dictionary.DictionaryItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApiInterface {

    @GET("v2/entries/en/{word}")
    Call<Dictionary> getDictionary(
            @Path("word") String word
    );
}
