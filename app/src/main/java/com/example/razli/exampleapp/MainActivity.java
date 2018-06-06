package com.example.razli.exampleapp;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    // Views
    TextView winningNotification;
    Button restartButton;

    // 0: Yellow , 1: Red , 2: Empty
    int activePlayer = 0;
    int[] gameState = {2, 2, 2,
            2, 2, 2,
            2, 2, 2};

    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};

    boolean gameActive = true;
    boolean hasWinner = false;

    public void dropIn(View view) {

        ImageView button = (ImageView) view;

        int tappedBox = Integer.parseInt(button.getTag().toString());

        // Player can only press if box is empty (ie. gameState[x] == 2)
        if (gameState[tappedBox] == 2 && gameActive) {

            // Sets the index in array to value of active player
            gameState[Integer.parseInt(button.getTag().toString())] = activePlayer;
            Log.d("GameState", "arr: " + Arrays.toString(gameState));

            button.setTranslationY(-1500);

            if (activePlayer == 0) {
                button.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            } else {
                button.setImageResource(R.drawable.red);
                activePlayer = 0;
            }

            button.animate().translationYBy(1500).rotation(1800).setDuration(300);

            // Check for winning condition
            for (int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {
                    hasWinner = true;
                    String winner = gameState[winningPosition[0]] == 0 ? "Yellow" : "Red";
                    winningNotification.setText(winner + " wins!");
                    winningNotification.setVisibility(View.VISIBLE);
                    restartButton.setVisibility(View.VISIBLE);
                    gameActive = false;
                }
            }

            // Check if nobody has won even if grid is filled. Then restart
            if (isGridFull(gameState) && !hasWinner) {
                Log.i("Msg", "Its a draw LOG");
                winningNotification.setText("Stalemate!");
                winningNotification.setVisibility(View.VISIBLE);
                restartButton.setVisibility(View.VISIBLE);
                gameActive = false;
            }
        }
    }

    public void restartGame(View view) {
        winningNotification.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);
        hasWinner = false;

        android.support.v7.widget.GridLayout gridLayout = (android.support.v7.widget.GridLayout) findViewById(R.id.gridLayout);

        // Clear all images
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setImageDrawable(null);
        }

        // Reset game variable defaults
        activePlayer = 0;
        gameActive = true;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        winningNotification = (TextView) findViewById(R.id.winningNotificationTextView);
        restartButton = (Button) findViewById(R.id.restartButton);

    }

    private boolean isGridFull(int[] grid) {
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 2) {
                return false;
            }
        }
        return true;
    }
}