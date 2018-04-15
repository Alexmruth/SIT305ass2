package com.example.alexuni.sit305ass2;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    String text;
    TextView test;
    ImageView testImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);
        //test = findViewById(R.id.test);
        testImage = findViewById(R.id.imageTest);

        try {
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
        ja = obj.getJSONArray("Enemies");
        jo = ja.getJSONObject(0);
        text = jo.getString("name");

        //testImage.setBackground(Drawable.createFromPath(text));


    }
}
