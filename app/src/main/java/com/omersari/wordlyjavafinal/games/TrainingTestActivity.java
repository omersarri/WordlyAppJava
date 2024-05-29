package com.omersari.wordlyjavafinal.games;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.omersari.wordlyjavafinal.adapter.QuestionAdapter;
import com.omersari.wordlyjavafinal.databinding.ActivityTrainingTestBinding;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.model.WordManager;
import com.omersari.wordlyjavafinal.model.gpt_response.Question;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class TrainingTestActivity extends AppCompatActivity {

    ActivityTrainingTestBinding binding;
    private ArrayList<Word> wordList;
    private ArrayList<Button> buttons;
    private Word selectedWord;
    private String selectedAnswer;
    private WordManager wordManager;
    private int selectedCategoryId;
    private boolean isTrue = false;

    private int health = 3;
    private int score = 0;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient().newBuilder()
            .readTimeout(60, TimeUnit.SECONDS).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainingTestBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        wordList = new ArrayList<>();
        buttons = new ArrayList<>();
        wordManager = WordManager.getInstance();
        buttons.add(binding.answer1Button);
        buttons.add(binding.answer2Button);
        buttons.add(binding.answer3Button);
        buttons.add(binding.answer4Button);

        selectedCategoryId = getIntent().getIntExtra("selectedCategoryId", -1);
        getWords();
        checkAnswer();



    }

    public void getWords() {
        wordList.clear();
        wordManager.getWords(TrainingTestActivity.this, new WordManager.GetWordsCallback() {
            @Override
            public void onSuccess(ArrayList<Word> wordArrayList) {

                if(wordArrayList.size()<5){
                    Toast.makeText(TrainingTestActivity.this, "Not enough words", Toast.LENGTH_SHORT).show();
                    finish();
                }

                for (Word word : wordArrayList) {
                    if (word.getCategoryId() == selectedCategoryId) {
                        wordList.add(word);
                    } else {
                        if (selectedCategoryId == -1) {
                            wordList = wordArrayList;
                        }
                    }
                }
                setQuestion();

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(TrainingTestActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setQuestion() {
        if (wordList.isEmpty()) {
            // Kelime listesi boşsa hata mesajı göster
            Toast.makeText(this, "No words available", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            for (Button button : buttons) {
                button.setVisibility(View.VISIBLE); // Butonları görünür yap
                System.out.println("görünür yapıldı");
            }

            Random random = new Random();
            int randomIndex = random.nextInt(wordList.size());
            selectedWord = wordList.get(randomIndex);
            binding.questionTextView.setText(selectedWord.getName());
            setAnswers();
        }
    }

    private void setAnswers() {

        ArrayList<Button> tempButtons = new ArrayList<>(buttons);
        ArrayList<Word> tempWords = new ArrayList<>(wordList);
        tempWords.remove(selectedWord);
        Random random = new Random();
        int randomButtonIndex = random.nextInt(tempButtons.size());
        tempButtons.get(randomButtonIndex).setText(selectedWord.getMeaning());
        selectedAnswer = selectedWord.getMeaning();
        tempButtons.remove(randomButtonIndex);
        for (int i = 0; i < 3; i++) {
            int randomIndex = random.nextInt(tempWords.size());
            Word word = tempWords.get(randomIndex);
            tempButtons.get(i).setText(word.getMeaning());
            tempWords.remove(randomIndex);
        }
    }

    private void checkAnswer() {
        for (Button button : buttons) {
            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (button.getText().toString().equals(selectedAnswer)) {
                                score++;
                                binding.scoreText.setText("Score: " + score);
                                isTrue = true;

                            } else {
                                health--;
                                binding.healthText.setText(""+health);
                                isTrue = false;
                                YoYo.with(Techniques.Shake).duration(500).playOn(binding.cardView);
                            }
                            selectedWord.addStat(isTrue);
                            wordManager.addWord(TrainingTestActivity.this, selectedWord, new WordManager.AddWordCallback() {
                                @Override
                                public void onSuccess() {
                                    if (health > 0) {
                                        setQuestion();
                                    } else {
                                        showAlertDialog();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Toast.makeText(TrainingTestActivity.this, "We cant store data right now.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            if (selectedWord.getStats().size() >= 21) {
                                selectedWord.removeStat(0);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(TrainingTestActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Log.e("Button Error", "Button is null");
            }
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

    public void audienceJokerButton(View view) {
        for(Button button : buttons){
            if(button.getText().toString().equals(selectedAnswer)){
                YoYo.with(Techniques.Flash).duration(1000).playOn(button);
                view.setVisibility(View.GONE);
            }
        }
    }

    public void fiftyPercentJokerButton(View view) {
        int sayac = 0;
        for (Button button : buttons) {
            if (!button.getText().toString().equals(selectedAnswer) && sayac<2) {
                button.setVisibility(View.INVISIBLE);
                sayac++;
            }
        }
        view.setVisibility(View.GONE);
    }

    public void backClick(View view) {
        finish();
    }
}