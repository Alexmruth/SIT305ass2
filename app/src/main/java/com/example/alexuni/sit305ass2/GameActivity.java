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

public class GameActivity extends AppCompatActivity {

    boolean introText, henrySwitch;
    int jc = 0; //short for JSON counter
    int LLcount = 0;
    int continueGame;
    int goldCount, potionCount, playerHealth, playerMaxHealth, attMin, attMax, def, weapEquipped;
    ProgressBar healthBar;
    LinearLayout LL1;
    LinearLayout LL3;
    Button nextBtn;
    TextView textJSON, nameJSON;
    TextView playerStatsText, playerStatsText2, playerHealthText, goldText, potionsText;
    ImageView textImage;
    String dialogue;
    String charName;
    JSONObject o;
    JSONArray ja;
    int i = 0;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private Timer timer = new Timer();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        prefs = getSharedPreferences("playerSaveData", MODE_PRIVATE);
        editor = prefs.edit();

        healthBar = findViewById(R.id.homeHealthBar);
        playerStatsText = findViewById(R.id.playerStatsText);
        playerStatsText2 = findViewById(R.id.playerStatsText2);
        playerHealthText = findViewById(R.id.playerHealthText);
        potionsText = findViewById(R.id.homePotionText);
        textJSON = findViewById(R.id.textJSON);
        nextBtn = findViewById(R.id.nextBtn);
        nameJSON = findViewById(R.id.nameJSON);
        textImage = findViewById(R.id.textImage);
        goldText = findViewById(R.id.homeGoldText);
        LL1 = findViewById(R.id.LL1);
        LL3 = findViewById(R.id.LL3);
        continueGame = prefs.getInt("continueGame", 0);
        weapEquipped = prefs.getInt("weapEquipped", 0);

        if (continueGame == 1) {
            LLcount = 1;
            introText = false;
        } else {
            LL1.setVisibility(View.INVISIBLE);
            LL3.setVisibility(View.INVISIBLE);
            introText = true;
            editor.clear();
            editor.apply();
        }


        //Opens the getText method
        try {
            loadPlayerData();
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void loadPlayerData() throws JSONException {
        JSONObject obj = new JSONObject(loadGameData());
        JSONObject jo = obj.getJSONObject("PlayerData");
        int baseAttMin = jo.getInt("baseAttMin");
        int baseAttMax = jo.getInt("baseAttMax");
        int baseDef = jo.getInt("baseDefence");
        int baseHealth = jo.getInt("baseHealth");
        int basePotions = jo.getInt("basePotions");
        int baseGold = jo.getInt("baseGold");
        playerMaxHealth = baseHealth;
        goldCount = prefs.getInt("gold", baseGold);
        potionCount = prefs.getInt("potions", basePotions);
        playerHealth = prefs.getInt("health", baseHealth);
        attMin = prefs.getInt("attMin", baseAttMin);
        attMax = prefs.getInt("attMax", baseAttMax);
        def = prefs.getInt("def", baseDef);

        playerStatsText.setText("ATT: " + String.valueOf(attMin) + "-" + String.valueOf(attMax));
        playerStatsText2.setText( "DEF: " + String.valueOf(def));
        playerHealthText.setText("HEALTH: " + String.valueOf(playerHealth) + "/" + String.valueOf(playerMaxHealth));
        healthBar.setMax(100);
        healthBar.setProgress(playerHealth);
        goldText.setText(String.valueOf(goldCount));
        potionsText.setText(String.valueOf(potionCount));
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
        henrySwitch = true;

        textJSON.setText("Coming soon!");
        try {
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
            goldText.setText(String.valueOf(goldCount));
            playerHealth = 100;
            playerHealthText.setText("HEALTH: " + String.valueOf(playerHealth) + "/" + String.valueOf(playerMaxHealth));
            healthBar.setMax(100);
            healthBar.setProgress(playerHealth);

            editor.putInt("gold", goldCount);
            editor.putInt("health", playerHealth);
            editor.commit();

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

}



