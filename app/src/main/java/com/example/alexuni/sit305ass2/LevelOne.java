package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    JSONObject encounterJSON;
    JSONArray wd; //Weapon data array
    JSONObject weapon;
    int i = 0; //Counter for JSON object
    int e; //Counter for encounter dialog
    int ec; // Counter for encounter
    int ID = 0;
    double lvlDifficulty;

    LinearLayout ll3;
    LinearLayout llOptions;

// https://stackoverflow.com/questions/41080424/how-to-output-a-interactive-game-map-from-an-array-in-android-studios
    // https://gamedev.stackexchange.com/questions/26346/whole-map-design-vs-tiles-array-design


    boolean enemyDead = false;
    boolean lvlClear = false;
    boolean playerTurn = true;
    boolean forwardErr;
    boolean boss = false;
    boolean npcEncounter = false;

    Random random;

    ProgressBar healthBar;
    ProgressBar enemyHealthBar;

    String textName,playerName, enemyName;
    String text1JSON, text2JSON, text3JSON;
    String exitText;
    String enemyDeadText;
    String forwardErrText;
    String replyOne, replyTwo;

    TextView nameJSON, textJSON;

    TextView option1Text, option2Text;

    TextView playerNameText, playerHealthText, playerStatsText, playerPotionsText, goldText;
    TextView enemyNameText, enemyHealthText, enemyStatsText;

    ImageView playerImage, enemyImage, textImage;

    Button forwardBtn, backBtn, option1Btn, option2Btn;
    Button attBtn;

    int currentLevel = LevelsActivity.currentLevel;

    int playerHealth, playerMaxHealth, potions;
    int wepEquipped, weaponAttMin, weaponAttMax;
    int baseAttMin, baseAttMax, totalAttMin, totalAttMax;
    int baseDef;
    int totalDef;

    //
    int enemyID;
    int attValue;

    int enemyAttack, enemyAttMin, enemyAttMax, enemyDef;

    int goldCount;
    int goldMin, goldMax;


    int image;
    int enemyHealth;
    int stepNum;

    int bossCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        random = new Random();
        SharedPreferences prefs = getSharedPreferences("playerSaveData", MODE_PRIVATE);

        playerHealth = prefs.getInt("health", 0);
        goldCount = prefs.getInt("gold", 0);

        //LinearLayouts --------------------------------------------
        ll3 = findViewById(R.id.LL3);
        llOptions = findViewById(R.id.llOptions);

        //Health bars ----------------------------------------------
        healthBar = findViewById(R.id.healthBar);
        enemyHealthBar = findViewById(R.id.enemyHealthBar);
        healthBar.setMax(playerHealth);

        //Buttons --------------------------------------------------
        forwardBtn = findViewById(R.id.row1Btn1);
        backBtn = findViewById(R.id.row1Btn2);
        attBtn = findViewById(R.id.attBtn);
        option1Btn = findViewById(R.id.option1Btn);
        option2Btn = findViewById(R.id.option2Btn);

        //Images ---------------------------------------------------
        playerImage = findViewById(R.id.playerImage);
        enemyImage = findViewById(R.id.enemyImage);
        textImage = findViewById(R.id.textImage);
        playerImage.setBackgroundResource(R.drawable.c_archerbones);

        //TextViews ------------------------------------------------
            //Options
        option1Text = findViewById(R.id.option1Text);
        option2Text = findViewById(R.id.option2Text);
            //Dialogue text
        nameJSON = findViewById(R.id.nameJSON);
        textJSON = findViewById(R.id.textJSON);
            //Player text
        playerNameText = findViewById(R.id.playerNameText);
        playerHealthText = findViewById(R.id.playerHealthText);
        playerStatsText = findViewById(R.id.playerStatsText);
        playerPotionsText = findViewById(R.id.potionText);
        goldText = findViewById(R.id.goldText);
            //Enemy text
        enemyNameText = findViewById(R.id.enemyNameText);
        enemyHealthText = findViewById(R.id.enemyHealthText);
        enemyStatsText = findViewById(R.id.enemyStatsText);



        //Essentially starts the level, loads player stats, gets encounter and gets text
        try {
            loadLevelStats();
            loadStats();
            getText();
            getEncounter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // getEncounter() is responsible for handling what to show and when during the course of completing the level
    public void getEncounter() throws JSONException {

        if (currentLevel == 1 && stepNum == 3) npcEncounter = true;
        if (currentLevel == 2 && stepNum == 4) npcEncounter = true;

        if (stepNum >= 1) {
            if (!npcEncounter) {
                getEnemy();
            } else {
                getText();
                getNPC();
            }
        } else {
            lvlClear = true;
            forwardBtn.setClickable(false);
            attBtn.setClickable(false);
        }

        updateText();
    }
    public void loadLevelStats() throws JSONException {
        JSONObject gameData = new JSONObject(loadJSON());
        jo = gameData.getJSONObject("LevelData");
        if(currentLevel == 1) {
            encounterJSON = gameData.getJSONObject("Encounter1");
            bossCount = 0;  // Boss number
            ec = 0;  // Encounter counter
            stepNum = jo.getInt("stepNumLvl1"); // Number of steps for level
            lvlDifficulty = jo.getDouble("lvl1Difficulty");  // Difficulty multiplyer
            goldMin = jo.getInt("goldMin1");
            goldMax = jo.getInt("goldMax1");
        }
        if(currentLevel == 2) {
            encounterJSON = gameData.getJSONObject("Encounter2");
            bossCount = 1;
            ec = 1;  // Encounter counter
            stepNum = jo.getInt("stepNumLvl2");
            lvlDifficulty = jo.getDouble("lvl2Difficulty");
            goldMin = jo.getInt("goldMin2");
            goldMax = jo.getInt("goldMax2");
        }
        if(currentLevel == 3) {
            bossCount = 2;
            ec = 2;  // Encounter counter
            stepNum = jo.getInt("stepNumLvl3");
            lvlDifficulty = jo.getDouble("lvl3Difficulty");
            goldMin = jo.getInt("goldMin3");
            goldMax = jo.getInt("goldMax3");
        }
    }

    /* loadJSON() is responsible for grabbing all content from the specified JSON file and converting
    it into a string to be used as a JSONObject. */
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

    // loadStats() is responsible for loading JSON player data and assigning it to variables in-game.
    public void loadStats() throws JSONException {
        JSONObject gameData = new JSONObject(loadJSON());
        pd = gameData.getJSONObject("PlayerData");
        wd = gameData.getJSONArray("Weapons");

        playerName = pd.getString("name");
        playerHealth = pd.getInt("baseHealth");
        potions = pd.getInt("basePotions");

        baseAttMin = pd.getInt("baseAttMin");
        baseAttMax = pd.getInt("baseAttMax");
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
        playerHealthText.setText(String.valueOf(playerHealth) + "/" + String.valueOf(playerMaxHealth));
        playerPotionsText.setText(String.valueOf(potions));
        goldText.setText(String.valueOf(goldCount));
        healthBar.setMax(playerHealth);
        healthBar.setProgress(playerHealth);

    }
    // getEnemy() is responsible for loading JSON enemy data and assigning it to variables in-game.
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
        double enemyHealthDbl = Math.round(jo.getDouble("health") * lvlDifficulty);
        double enemyAttMinDbl = Math.round(jo.getDouble("attMin") * lvlDifficulty);
        double enemyAttMaxDbl = Math.round(jo.getDouble("attMax") * lvlDifficulty);
        double enemyDefDbl = Math.round(jo.getDouble("defence") * lvlDifficulty);
        enemyHealth = (int) enemyHealthDbl;
        enemyAttMin = (int) enemyAttMinDbl;
        enemyAttMax = (int) enemyAttMaxDbl;
        enemyDef = (int) enemyDefDbl;

        enemyNameText.setText(enemyName);
        enemyStatsText.setText("ATT: " + String.valueOf(enemyAttMin) + "-" + String.valueOf(enemyAttMax) + " DEF: " + String.valueOf(enemyDef));
        enemyHealthText.setText(String.valueOf(enemyHealth));
        enemyHealthBar.setMax(enemyHealth);
        enemyHealthBar.setProgress(enemyHealth);

        getEnemyImage();
    }
    // getEnemyImage is responsible for assigning an image to an enemy based on their ID
    public void getEnemyImage() {
        switch (enemyID) {
            case 0:
                enemyImage.setBackgroundResource(R.drawable.e_rat_minion2);
                break;
            case 1:
                enemyImage.setBackgroundResource(R.drawable.e_troll);
                break;
            case 2:
                enemyImage.setBackgroundResource(R.drawable.e_skeleton);
                break;
            case 3:
                enemyImage.setBackgroundResource(R.drawable.e_rat_minion);
                break;
            case 4:
                enemyImage.setBackgroundResource(R.drawable.e_eye);
                break;
            case 5:
                enemyImage.setBackgroundResource(R.drawable.e_spider);
                break;
            case 6:
                enemyImage.setBackgroundResource(R.drawable.e_dragon_minion3);
                break;
            case 10:
                enemyImage.setBackgroundResource(R.drawable.eb_troll_boss);
        }
    }

    public void getText() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("LevelData");
        if(!npcEncounter) {
            text1JSON = jo.getString("text1");
            text2JSON = jo.getString("text2");
            exitText = jo.getString("exitText");
            enemyDeadText = jo.getString("enemyDead");
            forwardErrText = jo.getString("enemyNotDead");
            textName = jo.getString("character1");
            updateText();
        } else {
            ja = encounterJSON.getJSONArray("dialog");
            JSONObject d = ja.getJSONObject(e);
            text1JSON = d.getString("text");
            textName = encounterJSON.getString("name");
            text2JSON = d.getString("option1");
            text3JSON = d.getString("option2");
        }
    }
    public void updateText() {

        if (!npcEncounter) {
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

        } else if(npcEncounter) {
            ll3.setVisibility(View.INVISIBLE);
            llOptions.setVisibility(View.VISIBLE);

            if (text2JSON.contains("Null")) {
                option1Btn.setVisibility(View.INVISIBLE);
                option1Text.setVisibility(View.INVISIBLE);
            }

            if (text3JSON.contains("Null")) {
                option2Btn.setVisibility(View.INVISIBLE);
                option2Text.setVisibility(View.INVISIBLE);
            }

            if(currentLevel == 1) {
                textImage.setBackgroundResource(R.drawable.e_troll);
            }
            if(currentLevel == 2) {
                textImage.setBackgroundResource(R.drawable.e_rat_minion);
            }


            option1Text.setText(text2JSON);
            option2Text.setText(text3JSON);

            nameJSON.setText(textName);
            textJSON.setText(text1JSON);
            enemyNameText.setText(enemyName);
            enemyStatsText.setText("ATT: " + String.valueOf(enemyAttMin) + "-" + String.valueOf(enemyAttMax) + " DEF: " + String.valueOf(enemyDef));
            enemyHealthText.setText(String.valueOf(enemyHealth));
            enemyHealthBar.setMax(enemyHealth);
            enemyHealthBar.setProgress(enemyHealth);
        }

    }

    public void getNPC() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());

        enemyName = encounterJSON.getString("name");
        enemyHealth = encounterJSON.getInt("health");
        enemyAttMin = encounterJSON.getInt("attMin");
        enemyAttMax = encounterJSON.getInt("attMax");
        enemyDef = encounterJSON.getInt("defence");


        enemyNameText.setText(enemyName);
        enemyStatsText.setText("ATT: " + String.valueOf(enemyAttMin) + "-" + String.valueOf(enemyAttMax) + " DEF: " + String.valueOf(enemyDef));
        enemyHealthText.setText(String.valueOf(enemyHealth));
        enemyHealthBar.setMax(enemyHealth);
        enemyHealthBar.setProgress(enemyHealth);
        enemyImage.setBackgroundResource(R.drawable.e_troll);
    }

    public void onOption(View v) throws JSONException {
        if (v.getId() == R.id.option1Btn) {
            if (text2JSON.contains("Fight")) {
                npcEncounter = false;
                ll3.setVisibility(View.VISIBLE);
                llOptions.setVisibility(View.INVISIBLE);
            } else {
                e++;
            }
        } else if(v.getId() == R.id.option2Btn) {
            if(currentLevel == 1) {
                if (text3JSON.contains("Give")) {
                    stepNum--;
                    npcEncounter = false;
                    ll3.setVisibility(View.VISIBLE);
                    llOptions.setVisibility(View.INVISIBLE);
                    getEnemy();
                } else {
                        e = 2;
                }
            }
            if(currentLevel == 2) {
                    e++;
            }
        }
        getText();
        updateText();
    }


    public void onNext(View view) throws JSONException {

    }


    public void updatePlayer() {
        playerHealthText.setText(String.valueOf(playerHealth));
        healthBar.setProgress(playerHealth);
    }

    public void goldReward() throws JSONException {
        int goldvalue;
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("LevelData");

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
            textJSON.setText(enemyDeadText);
        }

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
            getEncounter();
            enemyDead = false;
            playerTurn = true;
        } else {
            forwardErr = true;
        }

        updateText();
    }

    public void onExit(View view) throws JSONException {
        if (lvlClear) {
            save();
            goToGameActivity();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit?");
            builder.setMessage("If you exit before you finish the level, all progress is lost!");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    goToGameActivity();
                }
            });
            builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();
        }
    }
    public void goToGameActivity() {
        Intent intent = new Intent (this, GameActivity.class);
        startActivity(intent);
    }

    public void save() {
        SharedPreferences prefs = getSharedPreferences("playerSaveData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("gold", goldCount);
        editor.putInt("health", playerHealth);
        editor.putInt("potions", potions);
        editor.putInt("attMin", totalAttMin);
        editor.putInt("attMax", totalAttMax);
        editor.putInt("def", totalDef);
        editor.commit();
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
