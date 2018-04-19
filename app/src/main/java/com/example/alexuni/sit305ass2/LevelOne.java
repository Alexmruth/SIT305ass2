package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LevelOne extends AppCompatActivity {

    JSONArray ja; //Short for JSON array
    JSONObject jo; //Short for JSON object
    JSONObject pd; //Player data JSON object
    JSONArray wd; //Weapon data JSON Array
    JSONObject weapon;
    int i = 0; //Counter for JSON object
    int ID = 0;

    Boolean enemyDead = false;
    Boolean lvlClear = false;
    Boolean playerTurn = true;

    ProgressBar healthBar;

    String name;
    String enemyName;
    String text1JSON;
    String text2JSON;
    String exitText;

    TextView nameJSON;
    TextView textJSON;

    TextView playerNameText;
    TextView playerHealthText;
    TextView playerStatsText;
    TextView enemyNameText;
    TextView enemyHealthText;

    ImageView playerImage;
    ImageView textImage;

    Button forwardBtn;
    Button backBtn;
    Button attBtn;

    int wepEquipped;
    int attMin;
    int attMax;
    int playerHealth;

    int baseAttMin;
    int baseAttMax;
    int totalAttMin;
    int totalAttMax;

    int baseDef;
    int enemyHealth;
    int enemyAtt;
    int stepNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        healthBar = findViewById(R.id.healthBar);

        healthBar.setMax(playerHealth);
        healthBar.setProgress(70);
        forwardBtn = findViewById(R.id.row1Btn1);
        backBtn = findViewById(R.id.row1Btn2);
        attBtn = findViewById(R.id.attBtn);

        nameJSON = findViewById(R.id.nameJSON);
        textJSON = findViewById(R.id.textJSON);

        playerImage = findViewById(R.id.playerImage);

        playerNameText = findViewById(R.id.playerNameText);
        playerHealthText = findViewById(R.id.playerHealthText);
        playerStatsText = findViewById(R.id.playerStatsText);

        enemyNameText = findViewById(R.id.enemyNameText);
        enemyHealthText = findViewById(R.id.enemyHealthText);

        textImage = findViewById(R.id.textImage);

        playerImage.setBackgroundResource(R.drawable.c_archerbones);

        try {
            loadStats();
            getEnemy();
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /* loadJSON() is responsible for grabbing all content from the specified JSON file and converting
    it into a string to be used as a JSONObject within the getText() method. */
    public String loadJSON() {
        String json = null;
        try {
            InputStream file = getAssets().open("gameData.json"); //JSON file
            int size = file.available();
            byte[] buffer = new byte[size];
            file.read(buffer);
            file.close();
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


    public void loadStats() throws JSONException {
        JSONObject gameData = new JSONObject(loadJSON());
        pd = gameData.getJSONObject("PlayerData");
        wd = gameData.getJSONArray("Weapons");

        playerHealth = pd.getInt("baseHealth");

        baseAttMin = pd.getInt("baseAttackMin");
        baseAttMax = pd.getInt("baseAttackMax");
        baseDef = pd.getInt("baseDefence");
        wepEquipped = pd.getInt("weaponEquipped");

        ID = wepEquipped;
        weapon = wd.getJSONObject(ID);

        attMin = weapon.getInt("attMin");
        attMax = weapon.getInt("attMax");

        totalAttMin = attMin + baseAttMin;
        totalAttMax = attMax + baseAttMax;
        playerStatsText.setText("ATT: " + String.valueOf(totalAttMin) + "-" + String.valueOf(totalAttMax));
        playerHealthText.setText(String.valueOf(playerHealth));


    }

    public void getText() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("Level1");

        stepNum = jo.getInt("stepNum");
        text1JSON = jo.getString("text1");
        text2JSON = jo.getString("text2");
        exitText = jo.getString("exitText");
        name = jo.getString("character1");
        updateText();
    }
    public void updateText() {
        textImage.setBackgroundResource(R.drawable.c_henryvillager);
        nameJSON.setText(name);
        if(stepNum >= 1) {
            textJSON.setText(text1JSON + String.valueOf(stepNum) + text2JSON);
        } else {
            textJSON.setText(exitText);
        }
    }

    public void getEnemy() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        ja = obj.getJSONArray("Enemies");
        jo = ja.getJSONObject(i);

        enemyName = jo.getString("name");
        enemyHealth = jo.getInt("health");
        enemyAtt = jo.getInt("attack");

        enemyNameText.setText(enemyName);
        enemyHealthText.setText(String.valueOf(enemyHealth));

    }

    public void updateEnemy() {
        enemyHealthText.setText(String.valueOf(enemyHealth));
        if(enemyHealth <= 0) {
            enemyNameText.setText("You have defeated " + enemyName +"!!");
            enemyDead = true;
        }

    }

    public void enemyResponse() {
        Timer timer = new Timer();
        final int enemyAttack = enemyAtt;

        if(enemyDead == false) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    playerHealth = playerHealth - enemyAttack;
                    updatePlayer();

                    playerTurn = true;
                }
            }, 2000);
            Context context = getApplicationContext();
            CharSequence text = "You received " + String.valueOf(enemyAttack) + " damage from " + enemyName;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    public void updatePlayer() {
        playerHealthText.setText(String.valueOf(playerHealth));
    }


    public void onForward(View view) {
        stepNum--;

        updateText();
    }
    public void onBack(View view) {

        updateText();

    }

    public void onAttack(View view) {
        Random random = new Random();
        int attValue = random.nextInt(totalAttMax - totalAttMin) + totalAttMin;

        if (enemyDead == false && playerTurn == true) {
            enemyHealth = enemyHealth - attValue;
            updateEnemy();


            Context context = getApplicationContext();
            CharSequence text = "You did " + String.valueOf(attValue) + " damage against " + enemyName;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            playerTurn = false;
            enemyResponse();
        }





    }
}
