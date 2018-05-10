package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class ShopActivity extends AppCompatActivity {

    JSONArray wd;
    JSONObject c;

    int id, shopID, price;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // listView = findViewById(R.id.listView);




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
        // Getting JSON Array node
        wd = gameData.getJSONArray("Weapons");
        int i = shopID;
        c = wd.getJSONObject(i);

        id = c.getInt("ID");
        name = c.getString("name");
        price = c.getInt("price");

    }

    public void buyHandler(View v) throws JSONException {
        


    }
}
