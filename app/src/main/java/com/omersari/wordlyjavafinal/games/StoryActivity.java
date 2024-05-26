package com.omersari.wordlyjavafinal.games;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.JsonReader;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.omersari.wordlyjavafinal.R;
import com.omersari.wordlyjavafinal.adapter.QuestionAdapter;
import com.omersari.wordlyjavafinal.databinding.ActivityStoryBinding;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.model.WordManager;
import com.omersari.wordlyjavafinal.model.gpt_response.Question;
import com.omersari.wordlyjavafinal.model.gpt_response.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class StoryActivity extends AppCompatActivity {
    private ActivityStoryBinding binding;

    private TextToSpeech textToSpeech;

    private ArrayList<Word> wordList;
    private ArrayList<Question> questionArrayList;
    private WordManager wordManager;
    private int selectedCategoryId;

    private QuestionAdapter questionAdapter;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient().newBuilder()
            .readTimeout(60, TimeUnit.SECONDS).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        wordList = new ArrayList<>();
        questionArrayList = new ArrayList<>();
        wordManager = WordManager.getInstance();

        selectedCategoryId = getIntent().getIntExtra("selectedCategoryId", -1);
        getWords();
        hideStory();
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }

        });

    }


    private void hideStory() {
        binding.storyTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.storyText.getVisibility() == View.VISIBLE) {
                    binding.hideButton.setImageResource(R.drawable.baseline_arrow_right_24);
                    YoYo.with(Techniques.SlideOutUp)
                            .duration(1000)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    binding.storyText.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            })
                            .playOn(binding.storyText);
                } else {
                    binding.hideButton.setImageResource(R.drawable.baseline_arrow_drop_down_30);
                    binding.storyText.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInDown)
                            .duration(1000)
                            .playOn(binding.storyText);
                }
            }
        });
    }

    public void getWords() {
        wordList.clear();
        wordManager.getWords(StoryActivity.this, new WordManager.GetWordsCallback() {
            @Override
            public void onSuccess(ArrayList<Word> wordArrayList) {

                for (Word word : wordArrayList) {
                    if (word.getCategoryId() == selectedCategoryId) {
                        wordList.add(word);
                    } else {
                        if (selectedCategoryId == -1) {
                            wordList = wordArrayList;
                        }
                    }
                }
                System.out.println(wordList.size());
                String words = "";
                for (Word word : wordList) {
                    words += word.getName() + ", ";
                }
                String question1 = words + " kelimelerinden uygun olanları içinde barındıran bir kısa hikaye oluştur " +
                        "json formatında olsun sana örnek bir json da veriyorum ona uygun şekilde yanıt ver." +
                        "{\n" +
                        "    \"storyTitle\": \"The Teacher who Kicked a Ball\",\n" +
                        "    \"story\": \"Once upon a time, there was a teacher who loved to play soccer. One day, during recess, he decided to join his students in a game. As he was dribbling the ball, he accidentally kicked it too hard and it flew over the schoolyard fence. The teacher knew he would have to go and retrieve the ball before someone got hurt, so he quickly ran to the other side of the fence. Luckily, he found the ball and returned it to the students, who were amazed by their teacher's soccer skills. From that day on, the teacher became known as the soccer star of the school.\",\n" +
                        "    \"question1\": {\n" +
                        "        \"question\": \"Why did the teacher have to go retrieve the ball?\",\n" +
                        "        \"answers\": [\n" +
                        "            \"Because he kicked it too hard\",\n" +
                        "            \"Because he wanted to play soccer\",\n" +
                        "            \"Because he was bored\",\n" +
                        "            \"Because he was teaching a lesson\"\n" +
                        "        ],\n" +
                        "        \"correctAnswer\": \"Because he kicked it too hard\"\n" +
                        "    },\n" +
                        "    \"question2\": {\n" +
                        "        \"question\": \"What did the students think of their teacher?\",\n" +
                        "        \"answers\": [\n" +
                        "            \"He was boring\",\n" +
                        "            \"He was a soccer star\",\n" +
                        "            \"He was too old to play\",\n" +
                        "            \"He was mean\"\n" +
                        "        ],\n" +
                        "        \"correctAnswer\": \"He was a soccer star\"\n" +
                        "    },\n" +
                        "    \"question3\": {\n" +
                        "        \"question\": \"What happened when the ball flew over the fence?\",\n" +
                        "        \"answers\": [\n" +
                        "            \"The teacher left it there\",\n" +
                        "            \"The teacher ignored it\",\n" +
                        "            \"The teacher went to retrieve it\",\n" +
                        "            \"The teacher bought a new one\"\n" +
                        "        ],\n" +
                        "        \"correctAnswer\": \"The teacher went to retrieve it\"\n" +
                        "    }\n" +
                        "}";

                String question = words + " kelimelerinden uygun olanları içinde barındıran bir kısa hikaye oluştur " +
                        "json formatında olsun Başlıkları ve tipleri=> storyTitle (String), story (String), " +
                        "Bunlara ek olarak bu hikayeyle alakalı 3 tane soru oluşturmanı istiyorum bunu da json formatının " +
                        "içinde ver başlıklar: question1, question2, question3, her sorunun altında answers adında bir liste olsun" +
                        "bu listenin 4 elemanı olsun ve jsonda correctAnswer adında bir eleman olsun burada sorunun cevabını ver.";
                callAPI(question1);
                System.out.println(question);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(StoryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SpannableString highlightKeywords(String fullText, ArrayList<Word> wordList) {
        SpannableString spannableString = new SpannableString(fullText);

        for (Word word : wordList) {
            String keyword = word.getName().toLowerCase();
            int startIndex = fullText.indexOf(keyword);
            while (startIndex != -1) {
                int endIndex = startIndex + keyword.length();
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                startIndex = fullText.indexOf(keyword, endIndex);
            }
        }

        return spannableString;
    }


    void addToChat(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(message);
                Gson gson = new Gson();
                JsonElement jsonElement = gson.fromJson(message, JsonElement.class);
                Story story = gson.fromJson(jsonElement, Story.class);

                questionArrayList.clear();
                questionArrayList.add(story.getQuestion1());
                questionArrayList.add(story.getQuestion2());
                questionArrayList.add(story.getQuestion3());
                System.out.println(questionArrayList.size() + "questionsize");

                binding.storyTitle.setText(story.getStoryTitle());
                binding.storyText.setText(highlightKeywords(story.getStory(), wordList));
                binding.infoText.setVisibility(View.GONE);

                if (questionAdapter == null) {
                    binding.questionsRecyclerView.setLayoutManager(new LinearLayoutManager(StoryActivity.this, LinearLayoutManager.VERTICAL, false));
                    questionAdapter = new QuestionAdapter(questionArrayList);
                    binding.questionsRecyclerView.setAdapter(questionAdapter);
                } else {
                    questionAdapter.notifyDataSetChanged();
                }


            }
        });
    }

    void addResponse(String response) {
        addToChat(response);
    }

    void callAPI(String question) {

        binding.infoText.setText("Lütfen Bekleyiniz...");
        //okhttp

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");

            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role", "user");
            obj.put("content", question);
            messageArr.put(obj);

            jsonBody.put("messages", messageArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer sk-proj-KL5NWhzXii8IgqIYZDH3T3BlbkFJCujj1ikqwE9bJCWsbsA2")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        addResponse(result.trim());
                        System.out.println(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    addResponse("Failed to load response due to " + response.body().string());
                }
            }
        });


    }

    public void listenStory(View view) {
        textToSpeech.setSpeechRate(0.7f);
        if (textToSpeech.isSpeaking()) {
            binding.listenButton.setImageResource(R.drawable.baseline_play_circle_24);
            textToSpeech.stop();
        } else {
            textToSpeech.speak(binding.storyText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
            binding.listenButton.setImageResource(R.drawable.baseline_stop_circle_24);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeech.shutdown();
    }

    public void backClick(View view) {
        finish();
    }

    public void hideQuizButton(View view) {
        if (binding.questionsRecyclerView.getVisibility() == View.VISIBLE) {
            binding.questionsHideButton.setImageResource(R.drawable.baseline_arrow_right_24);

            YoYo.with(Techniques.SlideOutUp)
                    .duration(1000)
                    .withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            binding.questionsRecyclerView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    })
                    .playOn(binding.questionsRecyclerView);
        } else {
            binding.questionsHideButton.setImageResource(R.drawable.baseline_arrow_drop_down_30);
            binding.questionsRecyclerView.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.SlideInDown)
                    .duration(1000)
                    .playOn(binding.questionsRecyclerView);
        }
    }
}