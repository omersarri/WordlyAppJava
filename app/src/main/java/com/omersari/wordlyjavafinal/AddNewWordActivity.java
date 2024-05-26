package com.omersari.wordlyjavafinal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.omersari.wordlyjavafinal.bottom_nav_fragments.HomeFragment;
import com.omersari.wordlyjavafinal.databinding.ActivityAddNewWordBinding;
import com.omersari.wordlyjavafinal.model.Word;
import com.omersari.wordlyjavafinal.model.WordManager;


public class AddNewWordActivity extends AppCompatActivity {

    private ActivityAddNewWordBinding binding;
    private int selectedColor = Color.parseColor("#B2BEB5");
    private String selectedType;

    private WordManager wordManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewWordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        wordManager = WordManager.getInstance();



    }

    public void onColorRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if(view.getId() == R.id.greyButton){
            if(checked){
                selectedColor = Color.parseColor("#B2BEB5");
            }
        } else if(view.getId() == R.id.blueButton){
            if(checked){
                selectedColor = Color.parseColor("#42a4f5");
            }
        } else if(view.getId() == R.id.redButton){
            if(checked){
                selectedColor = Color.parseColor("#f5424e");
            }
        } else if(view.getId() == R.id.greenButton){
            if(checked){
                selectedColor = Color.parseColor("#42f569");
            }
        } else if(view.getId() == R.id.yellowButton){
            if(checked){
                selectedColor = Color.parseColor("#ecf542");
            }
        }
    }

    public void onTypeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if(view.getId() == R.id.nounButton){
            if(checked){
                selectedType = "Noun";
            }
        } else if(view.getId() == R.id.pronounButton){
            if(checked){
                selectedType= "Pronoun";
            }
        } else if(view.getId() == R.id.verbButton){
            if(checked){
                selectedType = "Verb";
            }
        } else if(view.getId() == R.id.adjectiveButton){
            if(checked){
                selectedType = "Adjective";
            }
        } else if(view.getId() == R.id.adverbButton){
            if(checked){
                selectedType = "Adverb";
            }
        } else if(view.getId() == R.id.prepositionButton){
            if(checked){
                selectedType = "Preposition";
            }
        } else if(view.getId() == R.id.conjunctionButton){
            if(checked){
                selectedType = "Conjunction";
            }
        } else if(view.getId() == R.id.interjectionButton){
            if(checked){
                selectedType = "Interjection";
            }
        }
    }



    public void saveButtonClicked(View view) {
        if(binding.wordText.getText().toString().equals("") ||
                binding.meaningText.getText().toString().equals("")){
            Toast.makeText(this, "Fill the blanks", Toast.LENGTH_SHORT).show();
        }else {
            Word word = new Word();
            word.setColor(selectedColor);
            word.setName(binding.wordText.getText().toString().substring(0, 1).toUpperCase() + binding.wordText.getText().toString().substring(1).toLowerCase());
            word.setMeaning(binding.meaningText.getText().toString().substring(0, 1).toUpperCase() + binding.meaningText.getText().toString().substring(1).toLowerCase());
            word.setType(selectedType);
            word.setCategoryId(HomeFragment.selectedCategoryId);

            wordManager.addWord(AddNewWordActivity.this, word, new WordManager.AddWordCallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(AddNewWordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(AddNewWordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}