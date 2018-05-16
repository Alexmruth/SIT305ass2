package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Timer;

import static java.lang.String.*;

public class GameActivity extends AppCompatActivity {

    boolean introText;
    int jc = 0; //short for JSON counter
    int LLcount = 0;
    int i = 0;
    int continueGame, goldCount, potionCount, playerHealth, playerMaxHealth, attMin, attMax, def, weapEquipped, healthUpgradeCost, healthUpgrade;
    ProgressBar healthBar;
    LinearLayout LL1, LL3;
    Button nextBtn;
    TextView textJSON, nameJSON;
    TextView playerStatsText, playerStatsText2, playerHealthText, goldText, potionsText, buyHealthText;
    ImageView textImage;
    String dialogue, charName;
    JSONObject o;
    JSONArray ja;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //SharedPreferences ----------------------------------------
        prefs = getSharedPreferences("playerSaveData", MODE_PRIVATE);
        editor = prefs.edit();
        continueGame = prefs.getInt("continueGame", 0); //if 0, new game - if 1, continue game
        weapEquipped = prefs.getInt("weapEquipped", 0); //loads current weapon equipped
        //LinearLayouts --------------------------------------------
        LL1 = findViewById(R.id.LL1);
        LL3 = findViewById(R.id.LL3);
        //Health bars ----------------------------------------------
        healthBar = findViewById(R.id.homeHealthBar);
        //Buttons --------------------------------------------------
        nextBtn = findViewById(R.id.nextBtn);
        //Images ---------------------------------------------------
        textImage = findViewById(R.id.textImage);
        //TextViews ------------------------------------------------
            //Player stats text
        playerStatsText = findViewById(R.id.playerStatsText);
        playerStatsText2 = findViewById(R.id.playerStatsText2);
        playerHealthText = findViewById(R.id.playerHealthText);
        potionsText = findViewById(R.id.homePotionText);
        goldText = findViewById(R.id.homeGoldText);
            //Upgrades text
        buyHealthText = findViewById(R.id.upgradeHealthText);
            //Dialogue text
        textJSON = findViewById(R.id.textJSON);
        nameJSON = findViewById(R.id.nameJSON);
        //----------------------------------------------------------


        //Decides if game shows intro text (only displayed when starting a new game)
        if (continueGame == 1) {
            LLcount = 1;
            introText = false;
        } else {
            //When a new game starts, introText = true and all SharedPreferences data is wiped.
            LL1.setVisibility(View.INVISIBLE);
            LL3.setVisibility(View.INVISIBLE);
            introText = true;
            editor.clear();
            editor.apply();
        }


        //Calls important methods, surrounded with try/catch to prevent crashes
        try {
            loadPlayerData(); //loads all player data
            getText(); //loads intro text etc
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /* loadJSON() is responsible for grabbing all content from the specified JSON file and converting
    it into a string to be used as a JSONObject within the getText() method. */
    public String loadJSON() {
        String json = null;
        try {
            InputStream file = getAssets().open("dialogue.json");
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
    /* loadGameData() is responsible for grabbing all content from the specified JSON file and converting
    it into a string to be used as a JSONObject in the loadPlayerData() method. */
    public String loadGameData() {
        String json = null;
        try {
            InputStream file = getAssets().open("gameData.json");
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
    /* Method loadPlayerData() is responsible for grabbing data from both JSON and SharedPreferences files
    and assigning them to values within the activity. Only to be loaded once at beginning of activity
    to avoid resetting any values that have since changed from beginning of actvitiy. */
    public void loadPlayerData() throws JSONException {
        JSONObject obj = new JSONObject(loadGameData()); // creates a JSONObject using the return value of loadGameData()
        JSONObject jo = obj.getJSONObject("PlayerData");
        /* These values below are primarily used for default or beginner values, which then get written over
        when it is updated in SharedPreferences */
        int baseAttMin = jo.getInt("baseAttMin");
        int baseAttMax = jo.getInt("baseAttMax");
        int baseDef = jo.getInt("baseDefence");
        int baseHealth = jo.getInt("baseHealth");
        int basePotions = jo.getInt("basePotions");
        int baseGold = jo.getInt("baseGold");
        int baseHealthUpgradeCost = jo.getInt("healthUpgradeCost");
        healthUpgrade = jo.getInt("healthUpgrade");
        //------------------------------------------------------------------------------------------
        // These values below are grabbed from the SharedPreferences file, and use the JSON values as defaults
        playerMaxHealth = prefs.getInt("playerMaxHealth", baseHealth);
        healthUpgradeCost = prefs.getInt("healthUpgradeCost", baseHealthUpgradeCost);
        goldCount = prefs.getInt("gold", baseGold);
        potionCount = prefs.getInt("potions", basePotions);
        playerHealth = prefs.getInt("health", baseHealth);
        attMin = prefs.getInt("attMin", baseAttMin);
        attMax = prefs.getInt("attMax", baseAttMax);
        def = prefs.getInt("def", baseDef);
        //------------------------------------------------------------------------------------------
        updatePlayerText(); // calls method which uses this data and assigns it to TextViews etc.

    }
    // Method updataPlayerText() responsible for assigning values to TextViews and progress bars
    public void updatePlayerText() {
        playerStatsText.setText(String.format("ATT: %s-%s", valueOf(attMin), valueOf(attMax)));
        playerStatsText2.setText(String.format("DEF: %s", valueOf(def)));
        playerHealthText.setText(String.format("HEALTH: %s/%s", valueOf(playerHealth), valueOf(playerMaxHealth)));
        healthBar.setMax(playerMaxHealth);
        healthBar.setProgress(playerHealth);
        goldText.setText(valueOf(goldCount));
        potionsText.setText(valueOf(potionCount));
        buyHealthText.setText(String.format("Upgrade health (%s Gold)", valueOf(healthUpgradeCost)));
    }

    public void onMenuBtnClick(View v) {
        if(v.getId() == R.id.button1) {
            Intent intent = new Intent(this, LevelsActivity.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.shopBtn) {
            Intent intent = new Intent(this, ShopActivity.class);
            startActivity(intent);
        }
    }

    public void talkHenry(View v) {
        jc = 1;

        textJSON.setText("Coming soon!");
        try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void nextDialogue(View view) throws JSONException {
        if (i <= 8 && introText) {
            i++;
            getText();
        } else {
            continueGame = 1;
            editor.putInt("continueGame", continueGame);
            editor.commit();
            introText = false;
            LLcount = 1;
            nameJSON.setText("");
            textJSON.setText("");
            textImage.setBackground(null);
        }

        if(LLcount == 1){
            LL1.setVisibility(View.VISIBLE);
            LL3.setVisibility(View.VISIBLE);
        }

    }


    // This method is a working switch statement and JSON reader
    public void getText() throws JSONException {

        JSONObject obj = new JSONObject(loadJSON()); //defines JSONObject as the result of loadJSON method
        ja = obj.getJSONArray("IntroText");

        if(jc == 0) {
            o = ja.getJSONObject(i);
        } else if(jc ==1) {
            o = obj.getJSONObject("HenryText");
        }

        charName = o.getString("character");
        dialogue = o.getString("text");

        if (introText) {
            nameJSON.setText(charName);
            textJSON.setText(dialogue);

            if(o.getString("character").equals("Henry the Villager")) {
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
            } else if (o.getString("character").equals("Archer Bones (you)")) {
                textImage.setBackgroundResource(R.drawable.c_archerbones);
            }
        }

    }

    public void fullHeal(View v) {
        if (goldCount >= 20) {
            goldCount = goldCount - 20;
            playerHealth = playerMaxHealth;

            editor.putInt("gold", goldCount);
            editor.putInt("health", playerHealth);
            editor.commit();
            updatePlayerText();

        } else {
            showToast();
        }
    }

    public void showToast() {
        Context context = getApplicationContext();
        CharSequence text;
        final Toast toast;
        int duration = Toast.LENGTH_LONG;
        text = "Not enough gold!";
        toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onQuit(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void buyPotion(View view) {
        if (goldCount >= 15) {
            goldCount = goldCount - 15;
            potionCount++;

            editor.putInt("gold", goldCount);
            editor.putInt("potions", potionCount);
            editor.commit();
            updatePlayerText();

        } else {
            showToast();
        }
    }

    public void buyHealth(View view) {
        if(goldCount >= healthUpgradeCost) {
            goldCount = goldCount - healthUpgradeCost;
            playerMaxHealth = playerMaxHealth + healthUpgrade;
            healthUpgradeCost = healthUpgradeCost * 2;

            editor.putInt("gold", goldCount);
            editor.putInt("playerMaxHealth", playerMaxHealth);
            editor.putInt("healthUpgradeCost", healthUpgradeCost);
            editor.commit();

            updatePlayerText();

        }
    }
}



