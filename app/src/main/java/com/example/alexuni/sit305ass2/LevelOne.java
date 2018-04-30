package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LevelOne extends AppCompatActivity {

    JSONArray ja; //JSON array
    JSONArray ba; //Boss array
    JSONObject jo; //JSON object
    JSONObject pd; //Player data object
    JSONArray wd; //Weapon data array
    JSONObject weapon;
    int i = 0; //Counter for JSON object
    int ID = 0;




    boolean enemyDead = false;
    boolean lvlClear = false;
    boolean playerTurn = true;
    boolean forwardErr;
    boolean boss = false;

    Random random;

    ProgressBar healthBar;
    ProgressBar enemyHealthBar;

    String textName;
    String playerName;
    String enemyName;
    String text1JSON;
    String text2JSON;
    String exitText;
    String enemyDeadText;
    String forwardErrText;

    TextView nameJSON, textJSON;

    TextView playerNameText, playerHealthText, playerStatsText, playerPotionsText, goldText;
    TextView enemyNameText, enemyHealthText, enemyStatsText;

    ImageView playerImage, enemyImage, textImage;

    Button forwardBtn, backBtn;
    Button attBtn;

    int level = 1;

    int playerHealth, playerMaxHealth, potions;
    int wepEquipped, weaponAttMin, weaponAttMax;
    int baseAttMin, baseAttMax, totalAttMin, totalAttMax;
    int baseDef;
    int totalDef;

    //
    int enemyID;
    int attValue;
    int enemyAttack;
    int enemyAttMin;
    int enemyAttMax;
    int enemyDef;

    int goldCount;
    int goldMin, goldMax;


    int image;
    int enemyHealth;
    int enemyAtt;
    int stepNum;

    int bossCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        //HEALTH BARS
        healthBar = findViewById(R.id.healthBar);
        enemyHealthBar = findViewById(R.id.enemyHealthBar);
        healthBar.setMax(playerHealth);

        random = new Random();

        forwardBtn = findViewById(R.id.row1Btn1);
        backBtn = findViewById(R.id.row1Btn2);
        attBtn = findViewById(R.id.attBtn);

        goldCount = GameActivity.goldCount;

        nameJSON = findViewById(R.id.nameJSON);
        textJSON = findViewById(R.id.textJSON);

        playerImage = findViewById(R.id.playerImage);
        enemyImage = findViewById(R.id.enemyImage);

        playerNameText = findViewById(R.id.playerNameText);
        playerHealthText = findViewById(R.id.playerHealthText);
        playerStatsText = findViewById(R.id.playerStatsText);
        playerPotionsText = findViewById(R.id.potionText);
        goldText = findViewById(R.id.goldText);

        enemyNameText = findViewById(R.id.enemyNameText);
        enemyHealthText = findViewById(R.id.enemyHealthText);
        enemyStatsText = findViewById(R.id.enemyStatsText);

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

    /* #############################################################################################
     loadJSON() is responsible for grabbing all content from the specified JSON file and converting
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
    //##############################################################################################


    /* #############################################################################################
       The following methods are responsible for loading/getting data and assigning it to values.
     */
    public void loadStats() throws JSONException {
        JSONObject gameData = new JSONObject(loadJSON());
        pd = gameData.getJSONObject("PlayerData");
        wd = gameData.getJSONArray("Weapons");

        playerName = pd.getString("name");
        playerHealth = pd.getInt("baseHealth");
        potions = pd.getInt("potions");

        baseAttMin = pd.getInt("baseAttackMin");
        baseAttMax = pd.getInt("baseAttackMax");
        baseDef = pd.getInt("baseDefence");

        ID = wepEquipped;
        weapon = wd.getJSONObject(ID);

        weaponAttMin = weapon.getInt("attMin");
        weaponAttMax = weapon.getInt("attMax");

        totalAttMin = weaponAttMin + baseAttMin;
        totalAttMax = weaponAttMax + baseAttMax;

        totalDef = baseDef;
        playerMaxHealth = playerHealth;

        // Assigning variables to widgets
        playerNameText.setText(playerName);
        playerStatsText.setText("ATT: " + String.valueOf(totalAttMin) + "-" + String.valueOf(totalAttMax) + " DEF: " + String.valueOf(baseDef));
        playerHealthText.setText(String.valueOf(playerHealth));
        playerPotionsText.setText(String.valueOf(potions));
        goldText.setText(String.valueOf(goldCount));
        healthBar.setMax(playerHealth);
        healthBar.setProgress(playerHealth);

    }

    public void getText() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("Level1");

        stepNum = jo.getInt("stepNum");
        text1JSON = jo.getString("text1");
        text2JSON = jo.getString("text2");
        exitText = jo.getString("exitText");
        enemyDeadText = jo.getString("enemyDead");
        forwardErrText = jo.getString("enemyNotDead");
        textName = jo.getString("character1");
        updateText();
    }


    public void getEnemy() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        ja = obj.getJSONArray("Enemies");
        ba = obj.getJSONArray("Bosses");

        //Checks how many steps remaining and will make the last encounter a boss fight.
        if(stepNum >=2) {
            boss = false;
        } else if (stepNum == 1) {
            boss = true;
        }

        //Gets random enemy from the array
        if(!boss) {
            i = random.nextInt(ja.length());
            jo = ja.getJSONObject(i);
        } else {
            i = bossCount;
            jo = ba.getJSONObject(i);
            boss = false;
        }


        enemyID = jo.getInt("ID");
        enemyName = jo.getString("name");
        enemyHealth = jo.getInt("health");
        enemyAttMin = jo.getInt("attMin");
        enemyAttMax = jo.getInt("attMax");
        enemyDef = jo.getInt("defence");



        enemyNameText.setText(enemyName);
        enemyStatsText.setText("ATT: " + String.valueOf(enemyAttMin) + "-" + String.valueOf(enemyAttMax) + " DEF: " + String.valueOf(enemyDef));
        enemyHealthText.setText(String.valueOf(enemyHealth));
        enemyHealthBar.setMax(enemyHealth);
        enemyHealthBar.setProgress(enemyHealth);

        getEnemyImage();

    }

    public void getEnemyImage() {
        switch (enemyID) {
            case 0:
                    enemyImage.setBackgroundResource(R.drawable.c_archerbones);
                break;
            case 1:
                enemyImage.setBackgroundResource(R.drawable.c_henryvillager);
                break;
            case 2:
                enemyImage.setBackgroundResource(R.drawable.cave);
                break;
            case 3:
                enemyImage.setBackgroundResource(R.drawable.logo);
                break;
            case 4:
                enemyImage.setBackgroundResource(R.drawable.shop);
                break;
        }
    }

    //##############################################################################################

    /* #############################################################################################
        The following methods are responsible for updating data based on events
     */
    public void updateText() {
        textImage.setBackgroundResource(R.drawable.c_henryvillager);
        nameJSON.setText(textName);
        if (forwardErr == false) {
            if (stepNum >= 1) {
                textJSON.setText(text1JSON + String.valueOf(stepNum) + text2JSON);
            } else {
                textJSON.setText(exitText);
            }
        } else {
            textJSON.setText(forwardErrText);
            forwardErr = false;
        }

        if(enemyDead) {
            textJSON.setText(enemyDeadText);
        }


    }

    public void updatePlayer() {
        playerHealthText.setText(String.valueOf(playerHealth));
        healthBar.setProgress(playerHealth);
    }

    public void goldReward() throws JSONException {
        int goldvalue;
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("Level1");

        goldMin = jo.getInt("goldMin");
        goldMax = jo.getInt("goldMax");

        goldvalue = (random.nextInt(goldMax - goldMin) + goldMin);
        goldCount = goldCount + goldvalue;
        goldText.setText(String.valueOf(goldCount));
    }

    public void updateEnemy() throws JSONException {
        enemyHealthText.setText(String.valueOf(enemyHealth));
        enemyHealthBar.setProgress(enemyHealth);

        if(enemyHealth <= 0) {
            enemyNameText.setText("You have defeated " + enemyName +"!!");
            enemyDead = true;
            goldReward();

        }
        updateText();
    }

    public void showToast() {
        Context context = getApplicationContext();
        CharSequence text;
        final Toast toast;
        int duration = Toast.LENGTH_SHORT;

        if (playerTurn) {
            text = "You did " + String.valueOf(attValue) + " damage against " + enemyName;
        } else {
            text = "You received " + String.valueOf(enemyAttack) + " damage from " + enemyName;
        }

        toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    //##############################################################################################

    public void onAttack(View view) throws JSONException {
        int damage;
        attValue = (random.nextInt((totalAttMax+1) - (totalAttMin-1)) + totalAttMin) - enemyDef;

        if(attValue <= 0) {
            attValue = 1;
        }

        if (enemyDead == false && playerTurn == true) {
            enemyHealth = enemyHealth - attValue;

            showToast();
            updateEnemy();

            playerTurn = false;
            enemyResponse();
        }

    }

    public void enemyResponse() {
        Timer timer = new Timer();
        Random random = new Random();
        enemyAttack = (random.nextInt((enemyAttMax+1) - (enemyAttMin-1)) + enemyAttMin) - totalDef;
        if(enemyAttack <= 0) {
            enemyAttack = 1;
        }

        if(!enemyDead && !playerTurn) {
            /* A timer and within it is another runnable runOnUIThread to allow for the toast to be
            called, as it can only be called from a UI thread.
             */
             timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            playerHealth = playerHealth - enemyAttack;
                            updatePlayer();
                            showToast();
                            playerTurn = true;
                        }
                    });
                }
            }, 2000);
        }
    }

    public void onForward(View view) throws JSONException {
        if(enemyDead) {
            stepNum--;
            getEnemy();
            enemyDead = false;
            playerTurn = true;
        } else {
            forwardErr = true;
        }

        updateText();
    }

    public void onBack(View view) {

        updateText();

    }

    public void onHeal(View view) {
        if (potions >= 1) {
            playerHealth = playerHealth + 50;
            potions--;
            playerPotionsText.setText(String.valueOf(potions));
            if(playerHealth > playerMaxHealth) {
                playerHealth = playerMaxHealth;
            }
            updatePlayer();

        }
    }




}
