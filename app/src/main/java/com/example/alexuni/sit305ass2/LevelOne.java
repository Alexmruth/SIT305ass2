package com.example.alexuni.sit305ass2;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class LevelOne extends AppCompatActivity {

    JSONArray ja; //Short for JSON array
    JSONObject jo; //Short for JSON object
    int i = 0; //Counter for JSON object
    Boolean lvlClear = false;

    String name;
    String text1JSON;
    String text2JSON;
    String exitText;

    TextView textJSON;
    TextView text1;
    TextView text2;
    TextView text3;

    Button forwardBtn;
    Button backBtn;
    Button attBtn;

    int enemyHealth;
    int stepNum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);
        //test = findViewById(R.id.test);


        forwardBtn = findViewById(R.id.row1Btn1);
        backBtn = findViewById(R.id.row1Btn2);
        attBtn = findViewById(R.id.attBtn);

        textJSON = findViewById(R.id.textJSON);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);

        try {
            getEnemy();
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSON() {
        String json = null;
        try {
            InputStream file = getAssets().open("levelData.json");
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

    public void getText() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        jo = obj.getJSONObject("Level1");

        stepNum = jo.getInt("stepNum");
        text1JSON = jo.getString("text1");
        text2JSON = jo.getString("text2");
        exitText = jo.getString("exitText");
        updateText();
    }
    public void updateText() {
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

        name = jo.getString("name");
        enemyHealth = jo.getInt("health");

        text1.setText(name);
        text2.setText(String.valueOf(enemyHealth));

    }

    public void updateEnemy() {
        text2.setText(String.valueOf(enemyHealth));
        if(enemyHealth <= 0) {
            text1.setText("You have defeated " + name +"!!");
        }

    }

    public void onForward(View view) {
        stepNum--;

        updateText();
    }
    public void onBack(View view) {

        updateText();

    }

    public void onAttack(View view) {
        enemyHealth = enemyHealth - 10;
        updateEnemy();

    }
}
