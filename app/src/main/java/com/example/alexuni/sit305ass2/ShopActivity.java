package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.content.SharedPreferences;
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
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    ImageView shopImage1, shopImage2, shopImage3, shopImage4;
    TextView shopName1, shopName2, shopName3, shopName4;
    TextView shopPrice1, shopPrice2, shopPrice3, shopPrice4;
    Button buyBtn1, buyBtn2, buyBtn3, buyBtn4;

    int i, id, price, weapEquipped;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        prefs = getSharedPreferences("playerSaveData", MODE_PRIVATE);
        editor = prefs.edit();

        shopImage1 = findViewById(R.id.shopImage1);
        shopImage2 = findViewById(R.id.shopImage2);
        shopImage3 = findViewById(R.id.shopImage3);
        shopImage4 = findViewById(R.id.shopImage4);

        shopName1 = findViewById(R.id.shopName1);
        shopPrice1 = findViewById(R.id.shopPrice1);
        buyBtn1 = findViewById(R.id.buyBtn1);

        shopName2 = findViewById(R.id.shopName2);
        shopPrice2 = findViewById(R.id.shopPrice2);
        buyBtn2 = findViewById(R.id.buyBtn2);

        shopName3 = findViewById(R.id.shopName3);
        shopPrice3 = findViewById(R.id.shopPrice3);
        buyBtn3 = findViewById(R.id.buyBtn3);

        shopName4 = findViewById(R.id.shopName4);
        shopPrice4 = findViewById(R.id.shopPrice4);
        buyBtn4 = findViewById(R.id.buyBtn4);



        loadJSON();
        try {
            loadImages();
            loadItems();
            loadButtons();
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

    public void getItem() throws JSONException {
        JSONObject gameData = new JSONObject(loadJSON());
        // Getting JSON Array node
        wd = gameData.getJSONArray("Weapons");

        c = wd.getJSONObject(i);

        id = c.getInt("ID");
        name = c.getString("name");
        price = c.getInt("price");

    }

    public void loadItems() throws JSONException {

        getItem();
        i++;
        shopName1.setText(name);
        shopPrice1.setText(String.valueOf(price));
        getItem();
        i++;
        shopName2.setText(name);
        shopPrice2.setText(String.valueOf(price));
        getItem();
        i++;
        shopName3.setText(name);
        shopPrice3.setText(String.valueOf(price));
        getItem();
        i++;
        shopName4.setText(name);
        shopPrice4.setText(String.valueOf(price));
    }
    public void loadImages() {
        shopImage2.setBackgroundResource(R.drawable.sword001);
        shopImage3.setBackgroundResource(R.drawable.sword002);
        shopImage4.setBackgroundResource(R.drawable.sword003);
    }
    public void loadButtons() {
        if(weapEquipped == 0) {
            buyBtn1.setText("Equipped");
            buyBtn2.setText("Buy");
            buyBtn3.setText("Buy");
        }
        if(weapEquipped == 1) {
            buyBtn1.setText("Buy");
            buyBtn2.setText("Equipped");
            buyBtn3.setText("Buy");
        }
    }

    public void buyHandler(View v) throws JSONException {
        if(v.getId() == R.id.buyBtn1) {
           //if(buyBtn1.getText())
        }


    }
}
