package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
// https://www.javatpoint.com/android-json-parsing-tutorial
// http://www.vogella.com/tutorials/AndroidJSON/article.html

        TextView textJSON = findViewById(R.id.textJSON);
        
        String jsonString = loadJSONFromAsset();
        try {
            JSONObject json = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            Context context = null;
            InputStream is = context.getAssets().open("test.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}



