package com.omersari.wordlyjavafinal.games;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.omersari.wordlyjavafinal.adapter.MessageAdapter;
import com.omersari.wordlyjavafinal.databinding.ActivityPracticeAiactivityBinding;
import com.omersari.wordlyjavafinal.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PracticeAIActivity extends AppCompatActivity {
    private ActivityPracticeAiactivityBinding binding;

    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    List<String> conversationHistory;
    MessageAdapter messageAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPracticeAiactivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        messageList = new ArrayList<>();
        conversationHistory = new ArrayList<>();

        recyclerView = binding.recyclerView;
        welcomeTextView = binding.welcomeText;
        messageEditText = binding.messageEditText;
        sendButton = binding.sendBtn;
        messageEditText.setVisibility(View.GONE);
        sendButton.setVisibility(View.GONE);
        scenarios();


        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question,Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);
            while(conversationHistory.size()>8) {
                conversationHistory.remove(0);
            }
        });


    }

    void scenarios() {

        binding.freeTalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI("Seninle ingilizce konuşma pratiği yapacağız. konuşmaya 'Hello, how are you?' ile başla.");
                binding.scenariosLayout.setVisibility(View.GONE);
                messageEditText.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
                welcomeTextView.setText("Talk about anything");
            }
        });
        binding.busTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI("Seninle ingilizce konuşma pratiği yapacağız. konuşmaya 'Excuse me is this seat taken?' ile başla.");
                binding.scenariosLayout.setVisibility(View.GONE);
                messageEditText.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
                welcomeTextView.setText("You are in a bus trip");
            }
        });
        binding.taxiDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI("Seninle ingilizce konuşma pratiği yapacağız. konuşmaya 'Welcome, where do you want to go ?' ile başla. Sen bir taksicisin");
                binding.scenariosLayout.setVisibility(View.GONE);
                messageEditText.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
                welcomeTextView.setText("You are in a taxi");
            }
        });
        binding.touristButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI("Seninle ingilizce konuşma pratiği yapacağız. konuşmaya 'Excuse me, i'm here for one day and i don't know where should i visit. Do you have any idea?' ile başla. Sen bir turistsin");
                binding.scenariosLayout.setVisibility(View.GONE);
                messageEditText.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
                welcomeTextView.setText("A tourist asks you where to go");
            }
        });
        binding.lostChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI("Seninle ingilizce konuşma pratiği yapacağız. konuşmaya 'Excuse me, i think i lost my mother by any chance you see her?' ile başla. Sen markette annesini kaybetmiş bir çocuksun.");
                binding.scenariosLayout.setVisibility(View.GONE);
                messageEditText.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
                welcomeTextView.setText("You saw a lost child");
            }
        });
        binding.dateWithSomeoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI("Seninle ingilizce konuşma pratiği yapacağız. konuşmaya 'So, do you come here often?' ile başla. Sen hoşlandığı kişi ile ilk defa randevuya çıkan birisin. İlgi çekmeye çalış.");
                binding.scenariosLayout.setVisibility(View.GONE);
                messageEditText.setVisibility(View.VISIBLE);
                sendButton.setVisibility(View.VISIBLE);
                welcomeTextView.setText("You are in a date with someone");
            }
        });
    }


    void addToChat(String message,String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message,sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response,Message.SENT_BY_BOT);
    }

    void callAPI(String question){
        //okhttp
        messageList.add(new Message("Typing... ",Message.SENT_BY_BOT));
        conversationHistory.add(question);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model","gpt-3.5-turbo");

            JSONArray messageArr = new JSONArray();
            for (String message : conversationHistory) {
                JSONObject obj = new JSONObject();
                obj.put("role", "user");
                obj.put("content", message);
                messageArr.put(obj);
            }
                /*
                JSONObject obj = new JSONObject();
                obj.put("role", "user");
                obj.put("content", question);
                messageArr.put(obj);

                 */

            jsonBody.put("messages",messageArr);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("\n" +
                        "https://api.openai.com/v1/chat/completions")
                .header("Authorization","Bearer sk-proj-KL5NWhzXii8IgqIYZDH3T3BlbkFJCujj1ikqwE9bJCWsbsA2")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject  jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        conversationHistory.add(result.trim());
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else{
                    addResponse("Failed to load response due to "+response.body().toString());
                }
            }
        });





    }
}