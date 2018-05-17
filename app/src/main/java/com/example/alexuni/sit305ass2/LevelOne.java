package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/* LevelOne is responsible for handling every level, and values within this class change according to
which level button is clicked in the LevelsActivity */
public class LevelOne extends AppCompatActivity {

    JSONArray ja; //JSON array
    JSONArray ba; //Boss array
    JSONObject jo; //JSON object
    JSONObject pd; //Player data object
    JSONObject encounterJSON;
    JSONArray wd; //Weapon data array
    JSONObject weapon;

    LinearLayout ll1, ll3, llOptions;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    boolean enemyDead = false;
    boolean lvlClear = false;
    boolean playerTurn = true;
    boolean forwardErr;
    boolean boss = false;
    boolean npcEncounter = false;
    boolean endGame = false;

    Random random;
    ProgressBar healthBar, enemyHealthBar;
    String textName, playerName, enemyName, text1JSON, text2JSON, text3JSON, exitText, enemyDeadText, forwardErrText;
    TextView nameJSON, textJSON, option1Text, option2Text;
    TextView playerNameText, playerHealthText, playerStatsText, playerPotionsText, goldText;
    TextView enemyNameText, enemyHealthText, enemyStatsText;
    ImageView playerImage, enemyImage, textImage;
    Button forwardBtn, backBtn, option1Btn, option2Btn, attBtn;

    int currentLevel = LevelsActivity.currentLevel;
    int playerHealth, playerMaxHealth, potions;
    int weaponAttMin, weaponAttMax, baseAttMin, baseAttMax, totalAttMin, totalAttMax, baseDef, totalDef;
    int enemyID, attValue, enemyHealth, enemyMaxHealth, enemyAttack, enemyAttMin, enemyAttMax, enemyDef;
    int goldCount, goldMin, goldMax;
    int i = 0; //Counter for JSON object
    int e; //Counter for encounter dialog
    double lvlDifficulty;
    int stepNum, bossCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        //SharedPreferences ----------------------------------------
        prefs = getSharedPreferences("playerSaveData", MODE_PRIVATE);
        editor = prefs.edit();
        playerHealth = prefs.getInt("health", 0);
        goldCount = prefs.getInt("gold", 0);

        //LinearLayouts --------------------------------------------
        ll1 = findViewById(R.id.LL1);
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

        //Misc -----------------------------------------------------
        random = new Random();
        //----------------------------------------------------------
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
        //Changes when npc encounter shows according to level
        if (currentLevel == 1 && stepNum == 3) npcEncounter = true;
        if (currentLevel == 2 && stepNum == 4) npcEncounter = true;
        if (currentLevel == 3 && stepNum == 4) npcEncounter = true;
        if (currentLevel == 4 && stepNum == 5) npcEncounter = true;
        if (currentLevel == 5 && stepNum == 3) npcEncounter = true;
        if (currentLevel == 6 && stepNum == 2) npcEncounter = true;
        //If stepNum is more than or equal to 1, grab enemy, if less than 1, end the level
        if (stepNum >= 1) {
            if (!npcEncounter) {
                getEnemy();
                updateText();
            } else {
                getText();
                getNPC();
                updateText();
            }
        } else {
            if (currentLevel == 6) {
                endGame();
            } else {
                lvlClear = true;
                forwardBtn.setClickable(false);
                attBtn.setClickable(false);
                updateText();
            }
        }
    }

    /*Method loadLevelStats() is responsible for loading the level stats based on what the current level is.
    Sets what encounter, boss, step count, difficulty, and gold dropped from enemies grabbed from JSON file */
    public void loadLevelStats() throws JSONException {
        JSONObject gameData = new JSONObject(loadJSON());
        jo = gameData.getJSONObject("LevelData");
        if (currentLevel == 1) {
            encounterJSON = gameData.getJSONObject("Encounter1");
            bossCount = 0;  // Boss number
            stepNum = jo.getInt("stepNumLvl1"); // Number of steps for level
            lvlDifficulty = jo.getDouble("lvl1Difficulty");  // Difficulty multiplyer
            goldMin = jo.getInt("goldMin1");
            goldMax = jo.getInt("goldMax1");
        }
        if (currentLevel == 2) {
            encounterJSON = gameData.getJSONObject("Encounter2");
            bossCount = 1;
            stepNum = jo.getInt("stepNumLvl2");
            lvlDifficulty = jo.getDouble("lvl2Difficulty");
            goldMin = jo.getInt("goldMin2");
            goldMax = jo.getInt("goldMax2");
        }
        if (currentLevel == 3) {
            encounterJSON = gameData.getJSONObject("Encounter2");
            bossCount = 2;
            stepNum = jo.getInt("stepNumLvl3");
            lvlDifficulty = jo.getDouble("lvl3Difficulty");
            goldMin = jo.getInt("goldMin3");
            goldMax = jo.getInt("goldMax3");
        }
        if (currentLevel == 4) {
            encounterJSON = gameData.getJSONObject("Encounter2");
            bossCount = 3;
            stepNum = jo.getInt("stepNumLvl4");
            lvlDifficulty = jo.getDouble("lvl4Difficulty");
            goldMin = jo.getInt("goldMin4");
            goldMax = jo.getInt("goldMax4");
        }
        if (currentLevel == 5) {
            encounterJSON = gameData.getJSONObject("Encounter2");
            bossCount = 4;
            stepNum = jo.getInt("stepNumLvl5");
            lvlDifficulty = jo.getDouble("lvl5Difficulty");
            goldMin = jo.getInt("goldMin5");
            goldMax = jo.getInt("goldMax5");
        }
        if (currentLevel == 6) {
            encounterJSON = gameData.getJSONObject("Encounter6");
            bossCount = 5;
            stepNum = jo.getInt("stepNumLvl6");
            lvlDifficulty = jo.getDouble("lvl6Difficulty");
            goldMin = jo.getInt("goldMin6");
            goldMax = jo.getInt("goldMax6");
        }
    }

    /* loadJSON() is responsible for grabbing all content from the specified JSON file and converting
    it into a string to be used as a JSONObject across various methods, such as loadStats(). */
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
        /* These values below are primarily used for default or beginner values, which then get written over
        when it is updated in SharedPreferences */
        int baseAttMin1 = pd.getInt("baseAttMin");
        int baseAttMax1 = pd.getInt("baseAttMax");
        int baseDef1 = pd.getInt("baseDefence");
        int baseHealth = pd.getInt("baseHealth");
        int basePotions = pd.getInt("basePotions");
        int baseGold = pd.getInt("baseGold");
        //------------------------------------------------------------------------------------------
        // These values below are grabbed from the SharedPreferences file, and use the JSON values as defaults
        playerMaxHealth = prefs.getInt("playerMaxHealth", baseHealth);
        goldCount = prefs.getInt("gold", baseGold);
        potions = prefs.getInt("potions", basePotions);
        playerHealth = prefs.getInt("health", baseHealth);
        baseAttMin = prefs.getInt("attMin", baseAttMin1);
        baseAttMax = prefs.getInt("attMax", baseAttMax1);
        baseDef = prefs.getInt("def", baseDef1);
        int weapEquipped = prefs.getInt("weapEquipped", 0);
        weapon = wd.getJSONObject(weapEquipped);
        weaponAttMin = weapon.getInt("attMin");
        weaponAttMax = weapon.getInt("attMax");
        totalAttMin = weaponAttMin + baseAttMin1;
        totalAttMax = weaponAttMax + baseAttMax1;

        editor.putInt("attMin", totalAttMin);
        editor.putInt("attMax", totalAttMax);
        editor.commit();
        totalDef = baseDef;
        // Assigning variables to widgets
        playerNameText.setText(playerName);
        playerStatsText.setText("ATT: " + String.valueOf(totalAttMin) + "-" + String.valueOf(totalAttMax) + " DEF: " + String.valueOf(baseDef));
        playerHealthText.setText(String.valueOf(playerHealth) + "/" + String.valueOf(playerMaxHealth));
        playerPotionsText.setText("x" + String.valueOf(potions));
        goldText.setText(String.valueOf(goldCount));
        healthBar.setMax(playerMaxHealth);
        healthBar.setProgress(playerHealth);
    }

    /* getEnemy() is responsible for loading JSON enemy data and assigning it to variables in-game.
    It checks if it needs to load a boss enemy, as well as calling getEnemyImage() to load enemy image */
    public void getEnemy() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        ja = obj.getJSONArray("Enemies");
        ba = obj.getJSONArray("Bosses");

        //Checks how many steps remaining and will make the last encounter a boss fight.
        if (stepNum >= 2) {
            boss = false;
        } else if (stepNum == 1) {
            boss = true;
        }
        //Gets random enemy from the array
        if (!boss) {
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
        enemyMaxHealth = enemyHealth;
        enemyAttMin = (int) enemyAttMinDbl;
        enemyAttMax = (int) enemyAttMaxDbl;
        enemyDef = (int) enemyDefDbl;

        enemyNameText.setText(enemyName);
        enemyStatsText.setText("ATT: " + String.valueOf(enemyAttMin) + "-" + String.valueOf(enemyAttMax) + " DEF: " + String.valueOf(enemyDef));
        enemyHealthText.setText(String.valueOf(enemyHealth) + "/" + String.valueOf(enemyMaxHealth));
        enemyHealthBar.setMax(enemyHealth);
        enemyHealthBar.setProgress(enemyHealth);

        getEnemyImage();
    }

    // getEnemyImage is responsible for assigning an image to an enemy based on their ID. It is called by getEnemy().
    //Uses enemy ID to assign an image to the enemyImage ImageView.
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
                break;
            case 11:
                enemyImage.setBackgroundResource(R.drawable.eb_rat_boss);
                break;
            case 12:
                enemyImage.setBackgroundResource(R.drawable.eb_medusa_boss);
                break;
            case 13:
                enemyImage.setBackgroundResource(R.drawable.eb_mage_boss);
                break;
            case 14:
                enemyImage.setBackgroundResource(R.drawable.eb_demon_boss);
                break;
            case 15:
                enemyImage.setBackgroundResource(R.drawable.eb_dragon_boss);
                break;
            case 20:
                enemyImage.setBackgroundResource(R.drawable.e_troll);
                break;
            case 21:
                enemyImage.setBackgroundResource(R.drawable.e_rat_minion);
                break;
            case 25:
                enemyImage.setBackgroundResource(R.drawable.c_henryvillager);
                break;
        }
    }

    /*The method getText() is used to load dialogue which will then be used in the updateText() method.
    It is called by getEncounter() and others to update text when values are changed.*/
    public void getText() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("LevelData");
        if (!npcEncounter) {
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

    /*The method updateText() is responsible for updating text views each time data is updated by other methods.
    This method is mainly used by getEncounter().*/
    public void updateText() {
        if (!npcEncounter) { //The following happens when it is not an NPC encounter
            textImage.setBackgroundResource(R.drawable.c_henryvillager);
            nameJSON.setText(textName);
            if (!forwardErr) {
                if (stepNum >= 1) {
                    textJSON.setText(String.format("%s%s%s", text1JSON, String.valueOf(stepNum), text2JSON)); //Displays how many steps remaining
                } else {
                    textJSON.setText(exitText); //if at end of level, exit text displayed
                }
            } else {
                textJSON.setText(forwardErrText); //if forwardErr is true, error message displayed
                forwardErr = false;
            }
        }
        if (npcEncounter) { // The following happens when there is currently an NPC encounter
            ll3.setVisibility(View.INVISIBLE); //remove original bottom layout
            llOptions.setVisibility(View.VISIBLE); //replace it with options layout
            if (text2JSON.contains("Null")) { //makes button invisible if it contains "Null"
                option1Btn.setVisibility(View.INVISIBLE);
                option1Text.setVisibility(View.INVISIBLE);
            }
            if (text3JSON.contains("Null")) {
                option2Btn.setVisibility(View.INVISIBLE);
                option2Text.setVisibility(View.INVISIBLE);
            }
            if (currentLevel == 1) {
                textImage.setBackgroundResource(R.drawable.e_troll);
            }
            if (currentLevel == 2) {
                textImage.setBackgroundResource(R.drawable.e_rat_minion);
            }
            option1Text.setText(text2JSON);
            option2Text.setText(text3JSON);
            nameJSON.setText(textName);
            textJSON.setText(text1JSON);
            enemyNameText.setText(enemyName);
            enemyStatsText.setText(String.format("ATT: %s-%s DEF: %s", String.valueOf(enemyAttMin), String.valueOf(enemyAttMax), String.valueOf(enemyDef)));
            enemyHealthText.setText(String.format("%s/%s", String.valueOf(enemyHealth), String.valueOf(enemyMaxHealth)));
            enemyHealthBar.setMax(enemyHealth);
            enemyHealthBar.setProgress(enemyHealth);
        }

    }

    /* The method getNPC() is responsible for replacing normal enemy data with the NPC enemy data, triggered
    by getEncounter() method. It also updates TextViews and progress bars with this data*/
    public void getNPC() throws JSONException {
        enemyName = encounterJSON.getString("name");
        enemyID = encounterJSON.getInt("ID");
        enemyHealth = encounterJSON.getInt("health");
        enemyMaxHealth = enemyHealth;
        enemyAttMin = encounterJSON.getInt("attMin");
        enemyAttMax = encounterJSON.getInt("attMax");
        enemyDef = encounterJSON.getInt("defence");

        enemyNameText.setText(enemyName);
        enemyStatsText.setText(String.format("ATT: %s-%s DEF: %s", String.valueOf(enemyAttMin), String.valueOf(enemyAttMax), String.valueOf(enemyDef)));
        enemyHealthText.setText(String.valueOf(String.valueOf(enemyHealth) + "/" + String.valueOf(enemyMaxHealth)));
        enemyHealthBar.setMax(enemyHealth);
        enemyHealthBar.setProgress(enemyHealth);
        getEnemyImage();
    }

    /* The method onOption(View v) is responsible for handling user decisions when interacting with an NPC
    and there are options displayed. The View is grabbing which button was clicked. */
    public void onOption(View v) throws JSONException {
        if (v.getId() == R.id.option1Btn) { //If option 1 button is clicked. A more simple button as its basically reserved as the agressive 'fight' response
            if (text2JSON.contains("Fight")) {
                npcEncounter = false;
                ll3.setVisibility(View.VISIBLE);
                llOptions.setVisibility(View.INVISIBLE);
            } else {
                e++;
            }
        }
        if (v.getId() == R.id.option2Btn) { //If option 2 button is clicked. Handles for different encounters from different levels
            if (currentLevel == 1) { //Level 1 encounter code
                if (text3JSON.contains("Give")) {
                    stepNum--;
                    npcEncounter = false;
                    goldCount = goldCount - 10;
                    ll3.setVisibility(View.VISIBLE);
                    llOptions.setVisibility(View.INVISIBLE);
                    getEnemy();
                } else {
                    e = 2;
                }
            }
            if (currentLevel == 2) { //level 2 encounter code
                boolean goNext = true;
                if (text3JSON.contains("(Give potion)")) {
                    if (potions > 0) {
                        potions--;
                        playerPotionsText.setText(String.format("x%s", String.valueOf(potions)));
                        e++;
                        goNext = false;
                    } else {
                        goNext = false;
                        Context context = getApplicationContext();
                        CharSequence text;
                        final Toast toast;
                        int duration = Toast.LENGTH_SHORT;
                        text = "Insufficient potions";
                        toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
                if (goNext)
                    e++;
            }
        }
        getText();
        updateText();
    }

    public void onNext(View view) throws JSONException {
        //Next button has no purpose as of yet on this activity.
    }

    /*Method updatePlayer() updates TextViews and progress bars of player after health value is changed.
    It is called by enemyResponse() and onHeal(). */
    public void updatePlayer() throws JSONException {
        playerHealthText.setText(String.valueOf(String.valueOf(playerHealth) + "/" + String.valueOf(playerMaxHealth)));
        healthBar.setProgress(playerHealth);
        if (playerHealth <= 0) { //If player health reaches 0, the player dies and playerDead() is triggered.
            playerDead();
        }
    }

    //The method goldReward() is called by updateEnemy() when an enemy dies, and rewards the player with gold
    public void goldReward() throws JSONException {
        int goldvalue;
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("LevelData");

        goldvalue = (random.nextInt(goldMax - goldMin) + goldMin);
        goldCount = goldCount + goldvalue;
        goldText.setText(String.valueOf(goldCount));
    }

    /*The method updateEnemy() updates Textviews and progress bar of the enemy after its health value is changed
    (getting attacked by player) */
    public void updateEnemy() throws JSONException {
        enemyHealthText.setText(String.format("%s/%s", String.valueOf(enemyHealth), String.valueOf(enemyMaxHealth)));
        enemyHealthBar.setProgress(enemyHealth);

        if (enemyHealth <= 0) { // If enemy health goes below 0, they die
            enemyNameText.setText(String.format("You have defeated %s!!", enemyName));
            enemyDead = true;
            goldReward();
            textJSON.setText(enemyDeadText);
        }
    }

    /*The method showToast() handles what toast message to display based on whether the player or an enemy
    received damage. It displays damage dealt to who and by whom. It is called by onAttack() and enemyResponse()*/
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

    /* The method onAttack() is responsible for calculating what the attack value will be, and delivering
    that to the enemy. */
    public void onAttack(View view) throws JSONException {
        // using player attMin and attMax and subtracting enemy defense, a random value within that range is the attack value
        attValue = (random.nextInt((totalAttMax + 1) - (totalAttMin - 1)) + totalAttMin) - enemyDef;
        if (attValue <= 0) {//If attValue happens to be 0 or lower (if enemy def is too high) it corrects to 1 attack minimum
            attValue = 1;
        }
        if (!enemyDead && playerTurn) {
            enemyHealth = enemyHealth - attValue; // updates enemy health
            showToast();
            updateEnemy(); //update enemy
            playerTurn = false;
            enemyResponse(); // Triggers the enemy attack response
        }
    }

    /* The method enemyResponse() is responsible for responding to a player attack to an enemy, by dealing
    damage back to the player.*/
    public void enemyResponse() {
        Timer timer = new Timer();
        Random random = new Random();
        // using enemy attMin and attMax and subtracting player defense, a random value within that range is the attack value
        enemyAttack = (random.nextInt((enemyAttMax + 1) - (enemyAttMin - 1)) + enemyAttMin) - totalDef;
        if (enemyAttack <= 0) { //If attValue happens to be 0 or lower (if enemy def is too high) it corrects to 1 attack minimum
            enemyAttack = 1;
        }
        //if enemy is not dead, it will deal attack to player
        if (!enemyDead && !playerTurn) {
            /* A timer and within it is another runnable runOnUIThread to allow for the toast to be
            called, as it can only be called from a UI thread.
             */
            timer.schedule(new TimerTask() { //Timer set to delay enemy attack, to give a sense of a real enemy (not computer), response is not instant.
                @Override
                public void run() {
                    runOnUiThread(new TimerTask() {
                        @Override
                        public void run() {
                            playerHealth = playerHealth - enemyAttack; //updates player health
                            try {
                                updatePlayer();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            showToast();
                            playerTurn = true;
                        }
                    });
                }
            }, 1000);
        }
    }

    /* The method playerDead() is responsible for handling when a players health drops below 0, and is
    called by the updatePlayer() method. It stops most interaction with level and makes user exit*/
    public void playerDead() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("LevelData");

        text1JSON = jo.getString("playerDead");
        textJSON.setText(text1JSON);
        forwardBtn.setClickable(false);
        attBtn.setClickable(false);
        playerHealth = playerMaxHealth;
        editor.putInt("health", playerHealth);
        editor.commit();
    }

    /* The method onForward() is responsible for taking the player to the next enemy within the level.
    It is triggered by pressing the forward button.
     */
    public void onForward(View view) throws JSONException {
        if (enemyDead) {
            stepNum--;
            getEncounter();
            enemyDead = false;
            playerTurn = true;
        } else { //if enemy is not dead, forwardErr is true and error text is triggered
            forwardErr = true;
            updateText();
        }
    }

    /* The method onExit() is responsible for exiting the level and taking the user back to GameActivity.
    It is called when the exit button is clicked.*/
    public void onExit(View view) throws JSONException {
        if (lvlClear) { //Saves data and exits level
            save();
            goToGameActivity();
        } else if (endGame) { //When the game has ended, saves data and exits to main menu
            save();
            goToMainMenu();
        } else { //If level is not cleared, alertDialog triggered to warn player that progress will not be saved
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit?");
            builder.setMessage("If you exit before you finish the level, all progress is lost!");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    goToGameActivity(); // Exits activity
                }
            });
            builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();
        }
    }

    // Takes user to MainActivity, called by onExit() when the game is finished.
    public void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Takes user to GameActivity, called every other time the user wants to exit level.
    public void goToGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    /* The method save() saves all user data to SharedPreferences. This is loaded and used across other
    activities and when the player enters another level. It is called by onExit()*/
    public void save() {
        editor.putInt("gold", goldCount);
        editor.putInt("health", playerHealth);
        editor.putInt("potions", potions);
        editor.putInt("attMin", totalAttMin);
        editor.putInt("attMax", totalAttMax);
        editor.putInt("def", totalDef);
        editor.commit();
    }

    // The method onHeal() is called when the heal button is clicked. It increases player health.
    public void onHeal(View view) throws JSONException {
        if (potions >= 1) {
            playerHealth = playerHealth + 50; //increase health
            potions--; //remove a potion
            playerPotionsText.setText(String.format("x%s", String.valueOf(potions)));
            if (playerHealth > playerMaxHealth) {
                playerHealth = playerMaxHealth;
            }
            updatePlayer();
        }
    }

    /* This method is triggered when it is the final level and the final boss is defeated. It is responsible
    for displaying the end text and disabling some buttons */
    public void endGame() throws JSONException {
        endGame = true;
        text1JSON = jo.getString("endText");
        textJSON.setText(text1JSON);
        forwardBtn.setClickable(false);
        attBtn.setClickable(false);
        ll1.setVisibility(View.INVISIBLE);
    }

}
