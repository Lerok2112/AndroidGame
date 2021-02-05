package com.example.a3_in_a_row;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView playerScore;
    private TextView color;
    private Button[] buttons = new Button[9];
    private Button resetGame;
    private int index_color;
    private int playerScoreCont, startNum;

    private int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //строки
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //столбцы
            {0, 4, 8}, {2, 4, 6} //диагональ
    };
    private int[] colors = {Color.parseColor("#00FF00"), //зеленый
            Color.parseColor("#E61616"),  //красный
            Color.parseColor("#FFFF00")}; //желтый

    private String[] textColors = {"Зеленый", "Красный", "Желтый"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerScore = (TextView) findViewById(R.id.playerScore);
        resetGame = (Button) findViewById(R.id.resetGame);
        color = (TextView) findViewById(R.id.Color);
        index_color = random();
        color = setTextAndColor(index_color);
        color.setText(textColors[index_color]);


        for (int i = 0; i < buttons.length; i++) {
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
            gameState[i] = 0;
        }
        playerScoreCont = 0;
    }

    @Override
    public void onClick(View v) {

        String buttonID = v.getResources().getResourceEntryName(v.getId()); // btn_2

        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length() - 1, buttonID.length())); // 2

        //Log.i("k=", Integer.toString(index_color));

        if (index_color == 0) {
            ((Button) v).setBackgroundColor(colors[0]);
            gameState[gameStatePointer] = 1;
        }//зеленый

        if (index_color == 1) {
            ((Button) v).setBackgroundColor(colors[1]); //красный
            gameState[gameStatePointer] = 2;
        }
        if (index_color == 2) {
            ((Button) v).setBackgroundColor(colors[2]); //желтый
            gameState[gameStatePointer] = 3;
        }

        index_color = random();
        color = setTextAndColor(index_color);

        if (checkWinner()) {
            playerScoreCont++;
            updatePlayerScore();
        }
        if (!checkEmptyField()) {
            resetGame.callOnClick(); //очищаем поле
            Toast.makeText(this, "Пустые клетки закончились! Попробуй заново.", Toast.LENGTH_SHORT).show();
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                playerScoreCont = 0;
                updatePlayerScore();
            }
        });
    }

    public int random() {
        //1, 2 or 3
        int min = 0;
        int max = 2;
        int diff = max - min;
        Random rnd = new Random();
        int k = rnd.nextInt(diff + 1);
        k += min;
        return k;
    }

    public TextView setTextAndColor(int k) {
        color.setText(textColors[k]);
        color.setTextColor(colors[k]);
        return color;
    }

    public boolean checkWinner() {
        boolean winnerResult = false;
        for (int[] winnerPosition : winningPositions) {
            if (gameState[winnerPosition[0]] == gameState[winnerPosition[1]] &&
                    gameState[winnerPosition[1]] == gameState[winnerPosition[2]] &&
                    gameState[winnerPosition[0]] != 0 && gameState[winnerPosition[1]] != 0 && gameState[winnerPosition[2]] != 0) {
                winnerResult = true;
                for (int i = 0; i < 3; i++) {
                    buttons[winnerPosition[i]].setBackgroundColor(Color.parseColor("#3E3B3B"));
                    gameState[winnerPosition[i]] = 0;
                }
            }
        }
        return winnerResult;
    }

    public boolean checkEmptyField() {
        int k = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (gameState[i] != 0) k++;
        }
        if (k == 9) return false;
        else return true;
    }

    public void updatePlayerScore() {
        playerScore.setText(Integer.toString(playerScoreCont));
    }

    public void playAgain() {
        for (int i = 0; i < buttons.length; i++) {
            gameState[i] = 0;
            buttons[i].setBackgroundColor(Color.parseColor("#3E3B3B"));
        }
    }
}