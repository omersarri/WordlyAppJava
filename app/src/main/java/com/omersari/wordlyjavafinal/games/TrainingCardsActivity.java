package com.omersari.wordlyjavafinal.games;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.omersari.wordlyjavafinal.databinding.ActivityCardsTrainingBinding;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.model.WordManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainingCardsActivity extends AppCompatActivity{

    private ActivityCardsTrainingBinding binding;


    private List<Word> wordList;
    private WordManager wordManager;
    private int selectedCategoryId;

    private CountDownTimer countDownTimer;


    private Word selectedWord;

    private SharedPreferences sharedPreferences;
    private int score = 0;
    private int bestScore = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardsTrainingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPreferences = getSharedPreferences("best_score", Context.MODE_PRIVATE);
        bestScore = sharedPreferences.getInt("best_score", 0);
        binding.bestScoreText.setText("Best Score: " + bestScore);

        binding.meaningCardText.setVisibility(View.INVISIBLE);
        binding.correctButton.setVisibility(View.INVISIBLE);
        binding.wrongButton.setVisibility(View.INVISIBLE);

        wordManager = WordManager.getInstance();

        selectedCategoryId = getIntent().getIntExtra("selectedCategoryId", -1);
        wordList =  new ArrayList<>();
        getWords();
        countDownTimer();






    }

    private void countDownTimer() {
        long startTimeInMillis = 180000;

        countDownTimer = new CountDownTimer(startTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                binding.timeText.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                binding.timeText.setText("00:00");
                showAlertDialog();
            }
        };

        countDownTimer.start();
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
            binding.wordCardText.setText(selectedWord.getName());
            binding.meaningCardText.setText(selectedWord.getMeaning());
        }

    }

    public void getWords() {
        wordList.clear();
        wordManager.getWords(TrainingCardsActivity.this, new WordManager.GetWordsCallback() {
            @Override
            public void onSuccess(ArrayList<Word> wordArrayList) {
                for(Word word : wordArrayList){
                    if(word.getCategoryId() == selectedCategoryId){

                        wordList.add(word);
                    } else{
                        if(selectedCategoryId == -1){
                            wordList = wordArrayList;
                        }
                    }
                }
                setQuestion(wordList);


            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(TrainingCardsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oyun Bitti")
                .setMessage("Süresiz olarak devam edebilirsiniz fakat bu istatistiklere eklenmez. Devam etmek istiyor musunuz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aktiviteyi yeniden başlat
                        countDownTimer.cancel();
                        bestScore = 100000;
                    }
                })
                .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aktiviteyi bitir
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void cardClick(View view) {
        YoYo.with(Techniques.BounceInDown).playOn(binding.meaningCardText);
        YoYo.with(Techniques.Tada).playOn(binding.correctButton);
        YoYo.with(Techniques.Tada).playOn(binding.wrongButton);
        binding.meaningCardText.setVisibility(View.VISIBLE);
        binding.correctButton.setVisibility(View.VISIBLE);
        binding.wrongButton.setVisibility(View.VISIBLE);
    }

    public void wrongButtonClick(View view) {
        binding.meaningCardText.setVisibility(View.INVISIBLE);
        binding.correctButton.setVisibility(View.INVISIBLE);
        binding.wrongButton.setVisibility(View.INVISIBLE);
        selectedWord.addStat(false);
        if(selectedWord.getStats().size() >= 21){
            selectedWord.removeStat(0);
            wordManager.addWord(TrainingCardsActivity.this, selectedWord, new WordManager.AddWordCallback() {
                @Override
                public void onSuccess() {
                    setQuestion(wordList);

                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(TrainingCardsActivity.this, "We cant store data right now.", Toast.LENGTH_SHORT).show();
                }
            });
        } else{

            wordManager.addWord(TrainingCardsActivity.this, selectedWord, new WordManager.AddWordCallback() {
                @Override
                public void onSuccess() {
                    setQuestion(wordList);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(TrainingCardsActivity.this, "We cant store data right now.", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public void correctButtonClick(View view) {

        binding.meaningCardText.setVisibility(View.INVISIBLE);
        binding.correctButton.setVisibility(View.INVISIBLE);
        binding.wrongButton.setVisibility(View.INVISIBLE);
        selectedWord.addStat(true);
        if(selectedWord.getStats().size() >= 21){
            selectedWord.removeStat(0);
            wordManager.addWord(TrainingCardsActivity.this, selectedWord, new WordManager.AddWordCallback() {
                @Override
                public void onSuccess() {
                    setQuestion(wordList);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(TrainingCardsActivity.this, "We cant store data right now.", Toast.LENGTH_SHORT).show();
                }
            });
        } else{

            wordManager.addWord(TrainingCardsActivity.this, selectedWord, new WordManager.AddWordCallback() {
                @Override
                public void onSuccess() {
                    setQuestion(wordList);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(TrainingCardsActivity.this, "We cant store data right now.", Toast.LENGTH_SHORT).show();
                }
            });

        }


        score++;
        String scoreText = "Score: " + score;
        binding.scoreText.setText(scoreText);
    }

    public void backClick(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(score>bestScore){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("best_score", score);
            editor.apply();

        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
















