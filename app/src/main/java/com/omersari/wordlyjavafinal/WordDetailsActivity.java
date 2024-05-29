package com.omersari.wordlyjavafinal;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.omersari.wordlyjavafinal.adapter.DictionaryAdapter;
import com.omersari.wordlyjavafinal.databinding.ActivityWordDetailsBinding;
import com.omersari.wordlyjavafinal.domain.DictionaryApiInterface;
import com.omersari.wordlyjavafinal.model.dictionary.Dictionary;
import com.omersari.wordlyjavafinal.model.dictionary.DictionaryItem;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WordDetailsActivity extends AppCompatActivity {

    TextToSpeech textToSpeech;
    private final  String DICTIONARY_BASE_URL = "https://api.dictionaryapi.dev/api/";

    private ActivityWordDetailsBinding binding;
    private DictionaryAdapter dictionaryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        String word = getIntent().getStringExtra("word");
        String meaning = getIntent().getStringExtra("meaning");

        binding.wordTextView.setText(word);
        binding.meaningTextView.setText(meaning);
        binding.listenWordTextView.setText(word);

        getData(word, meaning);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.US);
                }
            }

        });
    }








    private void getData(String word, String meaning){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DICTIONARY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DictionaryApiInterface dictionaryApiInterface = retrofit.create(DictionaryApiInterface.class);
        Call<Dictionary> call = dictionaryApiInterface.getDictionary(word);

        call.enqueue(new Callback<Dictionary>() {
            @Override
            public void onResponse(Call<Dictionary> call, Response<Dictionary> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(WordDetailsActivity.this, "No data for this word.", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Dictionary dictionary = response.body();
                    binding.phoneticTextView.setText(dictionary.get(0).getPhonetic());


                    if(dictionary != null) {
                        binding.meaningRecyclerView.setLayoutManager(new LinearLayoutManager(WordDetailsActivity.this));
                        dictionaryAdapter = new DictionaryAdapter(dictionary);
                        binding.meaningRecyclerView.setAdapter(dictionaryAdapter);
                    }else{
                        Toast.makeText(WordDetailsActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onFailure(Call<Dictionary> call, Throwable t) {
                Toast.makeText(WordDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
            }
        });

    }

    public void listenWordClick(View view) {
        textToSpeech.speak(binding.listenWordTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void backClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
    }
}