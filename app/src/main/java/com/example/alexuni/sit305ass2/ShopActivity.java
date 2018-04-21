package com.example.alexuni.sit305ass2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    JSONArray wd;
    JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        loadJSON();
        try {
            loadItems();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

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

    public void loadItems() throws JSONException {
        JSONObject gameData = new JSONObject(loadJSON());
        wd = gameData.getJSONArray("Weapons");

        /*
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < wd.length(); i++){
            String name = wd.getJSONObject(i).getString("name");
            String price = wd.getJSONObject(i).getString("price");
            list.add(name);
            list.add(price);

        } */


    }
}
