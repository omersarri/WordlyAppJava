package com.omersari.wordlyjavafinal.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omersari.wordlyjavafinal.databinding.QuestionRowBinding;
import com.omersari.wordlyjavafinal.model.gpt_response.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionHolder> {
    private final List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionRowBinding questionRowBinding = QuestionRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuestionHolder(questionRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
        holder.bind(questionList.get(position), position);
        System.out.println(questionList.size()+ "listsize");
        System.out.println(questionList.get(position).getQuestion()+ "question");
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {
        private final QuestionRowBinding binding;
        private final List<RadioButton> radioButtons = new ArrayList<>();
        private int selectedAnswerIndex = -1;

        public QuestionHolder(QuestionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            radioButtons.add(binding.choiceARadioButton);
            radioButtons.add(binding.choiceBRadioButton);
            radioButtons.add(binding.choiceCRadioButton);
            radioButtons.add(binding.choiceDRadioButton);

            for (RadioButton radioButton : radioButtons) {
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedAnswerIndex = radioButtons.indexOf(radioButton);
                    }
                });
            }
        }

        public void bind(Question question, int position) {
            if (question != null) {
                binding.questionTextView.setText(question.getQuestion());
                binding.choiceARadioButton.setText(question.getAnswers().get(0));
                binding.choiceBRadioButton.setText(question.getAnswers().get(1));
                binding.choiceCRadioButton.setText(question.getAnswers().get(2));
                binding.choiceDRadioButton.setText(question.getAnswers().get(3));

                binding.submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedAnswerIndex == -1) {
                            Toast.makeText(v.getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String selectedAnswer = question.getAnswers().get(selectedAnswerIndex);
                        if (selectedAnswer.equals(question.getCorrectAnswer())) {
                            radioButtons.get(selectedAnswerIndex).setTextColor(Color.GREEN);
                        } else {
                            radioButtons.get(selectedAnswerIndex).setTextColor(Color.RED);
                            // Doğru cevabı yeşil yap
                            int correctAnswerIndex = question.getAnswers().indexOf(question.getCorrectAnswer());
                            radioButtons.get(correctAnswerIndex).setTextColor(Color.GREEN);
                        }

                        // Submit butonunu devre dışı bırak
                        binding.submitBtn.setEnabled(false);
                    }
                });
            }
        }
    }


}



