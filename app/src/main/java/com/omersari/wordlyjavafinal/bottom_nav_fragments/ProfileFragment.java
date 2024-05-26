package com.omersari.wordlyjavafinal.bottom_nav_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.omersari.wordlyjavafinal.PredictLevelActivity;
import com.omersari.wordlyjavafinal.databinding.FragmentProfileBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient().newBuilder()
            .readTimeout(60, TimeUnit.SECONDS).build();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.predictLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToPredictLevel = new Intent(getActivity(), PredictLevelActivity.class);
                startActivity(intentToPredictLevel);
            }
        });
        String question = /*words*/ ""+ " kelimelerinden uygun olanları içinde barındıran bir kısa hikaye oluştur json formatında olsun Başlıkları ve tipleri=> storyTitle (String), story (String)";

        callAPI("Car, Person, House kelimelerinden uygun olanları içinde barındıran bir kısa hikaye oluştur " +
                "json formatında olsun Başlıkları ve tipleri=> storyTitle (String), story (String), " +
                "Bunlara ek olarak bu hikayeyle alakalı 3 tane soru oluşturmanı istiyorum bunu da json formatının " +
                "içinde ver başlıklar: question1, question2, question3, her sorunun altında answers adında bir liste olsun" +
                "bu listenin 4 elemanı olsun ve jsonda correctAnswer adında bir eleman olsun burada sorunun cevabını ver.");


        return view;
    }



    void callAPI(String question) {

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

                        System.out.println(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                }
            }
        });


    }
}