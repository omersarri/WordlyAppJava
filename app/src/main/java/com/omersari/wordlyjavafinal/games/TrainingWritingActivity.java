package com.omersari.wordlyjavafinal.games;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.omersari.wordlyjavafinal.R;
import com.omersari.wordlyjavafinal.databinding.ActivityTrainingWritingBinding;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.model.WordManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrainingWritingActivity extends AppCompatActivity {

    private ActivityTrainingWritingBinding binding;

    private WordManager wordManager;

    private List<Word> wordList;
    private int selectedCategoryId;

    private Word selectedWord;

    private boolean isTrue = false;

    private int health = 3;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainingWritingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        wordManager = WordManager.getInstance();
        wordList = new ArrayList<>();


        selectedCategoryId = getIntent().getIntExtra("selectedCategoryId", -1);
        getWords();



    }


    private void setQuestion(List<Word> words) {
        if (words.isEmpty()) {
            // Kelime listesi boşsa hata mesajı göster
            Toast.makeText(this, "No words available", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Random random = new Random();
            int randomIndex = random.nextInt(words.size());
            selectedWord = words.get(randomIndex);
            String shuffledWord = shuffleString(selectedWord.getName().toUpperCase());
            binding.wordCardText.setText(shuffledWord);
        }

    }

    private void checkAnswer() {
        String answer = binding.answerEditText.getText().toString().toUpperCase();
        if (answer.equals(selectedWord.getName().toUpperCase())) {
            score++;
            binding.scoreText.setText("Score: " + score);
            binding.answerEditText.setText("");
            isTrue = true;


        }else{
            YoYo.with(Techniques.Shake).duration(700).playOn(binding.cardViewTrainingWriting);
            health--;
            binding.healthText.setText(String.valueOf(health));
            binding.answerEditText.setText("");
            isTrue = false;
        }
    }



    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        Random random = new Random();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }





    public void getWords() {
        wordList.clear();
        wordManager.getWords(TrainingWritingActivity.this, new WordManager.GetWordsCallback() {
            @Override
            public void onSuccess(ArrayList<Word> wordArrayList) {
                System.out.println("catID"+ selectedCategoryId);
                for(Word word : wordArrayList){
                    if(word.getCategoryId() == selectedCategoryId){

                        wordList.add(word);
                    } else{
                        if(selectedCategoryId == -1){
                            wordList = wordArrayList;
                        }
                    }
                }
                System.out.println(wordList.size()+ "wordlistSize");
                setQuestion(wordList);

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(TrainingWritingActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doneButton(View view) {
        checkAnswer();
        selectedWord.addStat(isTrue);
        wordManager.addWord(TrainingWritingActivity.this, selectedWord, new WordManager.AddWordCallback() {
            @Override
            public void onSuccess() {
                if (health > 0) {
                    setQuestion(wordList);
                } else {
                    showAlertDialog();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(TrainingWritingActivity.this, "We cant store data right now.", Toast.LENGTH_SHORT).show();
            }
        });

        if (selectedWord.getStats().size() >= 21) {
            selectedWord.removeStat(0);
        }

    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oyun Bitti")
                .setMessage("Yeniden başlamak ister misiniz?")
                .setPositiveButton("Yeniden Başlat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aktiviteyi yeniden başlat
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Bitir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aktiviteyi bitir
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void backClick(View view) {
        finish();
    }
}