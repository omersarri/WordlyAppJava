package com.omersari.wordlyjavafinal.games;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.omersari.wordlyjavafinal.R;
import com.omersari.wordlyjavafinal.databinding.ActivityTrainingPronunciationBinding;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.model.WordManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TrainingPronunciationActivity extends AppCompatActivity {

    private ActivityTrainingPronunciationBinding binding;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private SpeechRecognizer speechRecognizer;
    private Snackbar listeningSnackbar;

    private WordManager wordManager;
    private int selectedCategoryId;
    private Word selectedWord;
    private ArrayList<Word> wordList;

    private TextToSpeech textToSpeech;
    private int score = 0;
    private int health = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainingPronunciationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        wordList = new ArrayList<>();
        wordManager = WordManager.getInstance();
        selectedCategoryId = getIntent().getIntExtra("selectedCategoryId", -1);
        getWords();

        binding.micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMicrophonePermission();
            }
        });

        // Initialize the SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }

        });

    }

    public void listenWord(View view) {
        textToSpeech.setSpeechRate(0.9f);
        textToSpeech.speak(binding.wordCardText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);



    }

    private void setQuestion() {
        if (wordList.isEmpty()) {
            // Kelime listesi boşsa hata mesajı göster
            Toast.makeText(this, "No words available", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Random random = new Random();
            int randomIndex = random.nextInt(wordList.size());
            selectedWord = wordList.get(randomIndex);
            binding.wordCardText.setText(selectedWord.getName());
        }

    }

    public void getWords() {
        wordList.clear();
        wordManager.getWords(TrainingPronunciationActivity.this, new WordManager.GetWordsCallback() {
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
                setQuestion();


            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(TrainingPronunciationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setStat(Boolean isTrue) {
        selectedWord.addStat(isTrue);
        wordManager.addWord(TrainingPronunciationActivity.this, selectedWord, new WordManager.AddWordCallback() {
            @Override
            public void onSuccess() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (health > 0) {
                            setQuestion();
                        } else {
                            showAlertDialog();
                        }
                    }
                }, 2000);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(TrainingPronunciationActivity.this, "We cant store data right now.", Toast.LENGTH_SHORT).show();
            }
        });

        if (selectedWord.getStats().size() >= 21) {
            selectedWord.removeStat(0);
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Oyun Bitti")
                .setMessage("Süresiz olarak devam edebilirsiniz fakat bu istatistiklere eklenmez. Devam etmek istiyor musunuz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Aktiviteyi yeniden başlat
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
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


    private void requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            startSpeechRecognition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSpeechRecognition();
            } else {
                Toast.makeText(this, "Microphone permission is required for this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString());
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                showListeningSnackbar();
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
                if (listeningSnackbar != null && listeningSnackbar.isShown()) {
                    listeningSnackbar.dismiss();
                }
            }

            @Override
            public void onError(int error) {
                if (listeningSnackbar != null && listeningSnackbar.isShown()) {
                    listeningSnackbar.dismiss();
                }
                Toast.makeText(TrainingPronunciationActivity.this, "Error recognizing speech. Please try again.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                handleSpeechResults(results);
                System.out.println(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // Partial results are ignored
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        speechRecognizer.startListening(intent);
    }

    private void handleSpeechResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (matches != null && !matches.isEmpty()) {
            String spokenText = matches.get(0);
            highlightPronunciation(spokenText);
        }
    }

    private void highlightPronunciation(String spokenText) {
        String word = selectedWord.getName().trim();
        SpannableString spannableString = new SpannableString(word);

        if (word.equalsIgnoreCase(spokenText.trim())) {
            spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            score++;
            binding.scoreText.setText("Score: " + score);
            setStat(true);
        } else {
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (health > 0) {
                health--;
            }
            binding.healthText.setText("" + health);
            setStat(false);
        }

        binding.wordCardText.setText(spannableString);
    }

    private void showListeningSnackbar() {
        listeningSnackbar = Snackbar.make(binding.getRoot(), "Listening...", Snackbar.LENGTH_INDEFINITE);
        listeningSnackbar.setAction("Bitir", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speechRecognizer != null) {
                    speechRecognizer.stopListening();
                }
                if (listeningSnackbar != null && listeningSnackbar.isShown()) {
                    listeningSnackbar.dismiss();
                }
            }
        });
        listeningSnackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    public void backClick(View view) {
        finish();
    }
}
