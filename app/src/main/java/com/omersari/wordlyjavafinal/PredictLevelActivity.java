package com.omersari.wordlyjavafinal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.omersari.wordlyjavafinal.adapter.MessageAdapter;
import com.omersari.wordlyjavafinal.databinding.ActivityPredictLevelBinding;
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

public class PredictLevelActivity extends AppCompatActivity {
    private ActivityPredictLevelBinding binding;

    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    Button doneButton;
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
        binding = ActivityPredictLevelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        messageList = new ArrayList<>();
        conversationHistory  = new ArrayList<>();
        binding.doneButton.setVisibility(View.GONE);

        callAPI("Test my english level with a little conversation between us. Start conversation with 'Hello i will test your english level.'");

        recyclerView = binding.recyclerView;
        welcomeTextView = binding.welcomeText;
        messageEditText = binding.messageEditText;
        sendButton = binding.sendBtn;
        doneButton = binding.doneButton;


        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);

            /*
            while(conversationHistory.size()>8) {
                conversationHistory.remove(0);
            }


             */
            if(conversationHistory.size()>10) {
                conversationHistory.clear();
                callAPI("Rate my level based on the conversation with me(A1, A2, B1, B2, C1, C2). End the Conversation");
                messageEditText.setVisibility(View.GONE);
                sendButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


        void addToChat (String message, String sentBy){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageList.add(new Message(message, sentBy));
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                }
            });
        }

        void addResponse (String response){
            messageList.remove(messageList.size() - 1);
            addToChat(response, Message.SENT_BY_BOT);

        }

        void callAPI (String question){
            //okhttp
            messageList.add(new Message("Typing... ", Message.SENT_BY_BOT));
            conversationHistory.add(question);

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("model", "gpt-3.5-turbo");

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

                jsonBody.put("messages", messageArr);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            Request request = new Request.Builder()
                    .url("\n" +
                            "https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer sk-proj-KL5NWhzXii8IgqIYZDH3T3BlbkFJCujj1ikqwE9bJCWsbsA2")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    addResponse("Failed to load response due to " + e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = null;
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




                    } else {
                        addResponse("Failed to load response due to " + response.body().toString());
                    }
                }
            });


        }
    }
