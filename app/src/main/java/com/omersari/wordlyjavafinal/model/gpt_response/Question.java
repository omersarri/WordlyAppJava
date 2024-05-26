package com.omersari.wordlyjavafinal.model.gpt_response;

import java.util.List;

public class Question {
    private String question;
    private List<String> answers;
    private String correctAnswer;

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
