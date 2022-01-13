package com.example.td8;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public Button btnGuess1, btnGuess2, btnGuess3, btnNext;
    public ImageView image;
    public TextView question, correctAnswer;
    private AssetManager assets;
    public String[] imageList;
    private String correctChoice;
    private int counter = 0;
    private int current = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGuess1 = findViewById(R.id.btnGuess1);
        btnGuess2 = findViewById(R.id.btnGuess2);
        btnGuess3 = findViewById(R.id.btnGuess3);
        btnNext = findViewById(R.id.nextQuestionBtn);
        image = findViewById(R.id.image);
        question = findViewById(R.id.questionNbr);
        correctAnswer = findViewById(R.id.answerTXT);
        btnNext.setEnabled(false);
        assets = this.getAssets();
        try {
            imageList = assets.list("png");
        } catch (IOException e) {
            Toast.makeText(this, "No such file.png exists here...", Toast.LENGTH_LONG).show();
        }
        fillQuestion();

    }

    public void fillQuestion() {

        int rand1, rand2, rand3;
        rand1 = (int) (Math.random() * imageList.length);
        do {
            rand2 = (int) (Math.random() * imageList.length);
        } while (rand1 == rand2);
        do {
            rand3 = (int) (Math.random() * imageList.length);
        } while (rand1 == rand3 || rand2 == rand3);
        String imagePath = "png/" + imageList[rand1];
        String imageName = imageList[rand1].split(".png")[0].split("-")[1];
        correctChoice = imageName;
        Drawable img = null;
        try {
            img = Drawable.createFromStream(assets.open(imagePath), imageName);
        } catch (IOException e) {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_LONG).show();
        }
        this.image.setImageDrawable(img);
        String choice2 = imageList[rand2].split(".png")[0].split("-")[1];
        String choice3 = imageList[rand3].split(".png")[0].split("-")[1];
        String[] choices = {imageName, choice2, choice3};
        int c1, c2, c3;
        c1 = (int) (Math.random() * choices.length);
        do {
            c2 = (int) (Math.random() * choices.length);
        } while (c2 == c1);
        do {
            c3 = (int) (Math.random() * choices.length);
        } while (c3 == c2 || c3 == c1);
        btnGuess1.setText(choices[c1]);
        btnGuess2.setText(choices[c2]);
        btnGuess3.setText(choices[c3]);
    }

    @SuppressLint("SetTextI18n")
    public void checkAnswer(View v) {
        String selectedBtn = ((Button) v).getText().toString();
        if (selectedBtn.equalsIgnoreCase(correctChoice)) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            counter++;
        }
        correctAnswer.setText("Current Points : " + counter + " / 10");
        current++;
        btnNext.setEnabled(true);
        btnGuess1.setEnabled(false);
        btnGuess2.setEnabled(false);
        btnGuess3.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    public void toNextQuestion(View v) {
        if (current <= 10) {
            question.setText("Question " + current + " of 10");
            fillQuestion();
            btnNext.setEnabled(false);
            btnGuess1.setEnabled(true);
            btnGuess2.setEnabled(true);
            btnGuess3.setEnabled(true);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Finish")
                    .setMessage("Congrats! You've got " + counter + " points out of 10\nDo you want to try again?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        counter = 0;
                        current = 1;
                        question.setText("Question " + current + " of 10");
                        correctAnswer.setText("Current Points :");
                        fillQuestion();
                        btnNext.setEnabled(false);
                        btnGuess1.setEnabled(true);
                        btnGuess2.setEnabled(true);
                        btnGuess3.setEnabled(true);
                    })
                    .setNegativeButton("NO", (dialog, which) -> {
                        Toast.makeText(this, "Tank you!", Toast.LENGTH_LONG).show();
                        finish();
                    })
                    .create().show();
        }
    }
}