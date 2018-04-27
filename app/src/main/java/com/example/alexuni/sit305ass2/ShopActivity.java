package com.example.alexuni.sit305ass2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
    int i;
    private ListView listView;

    ArrayList<HashMap<String, String>> shopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shopList = new ArrayList<>();

        listView = findViewById(R.id.listView);

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
        for (int i = 0; i < wd.length(); i++) {
            c = wd.getJSONObject(i);

            String id = c.getString("ID");
            String name = c.getString("name");
            String price = c.getString("price");


            // tmp hash map for single contact
            HashMap<String, String> item = new HashMap<>();

            // adding each child node to HashMap key => value
            item.put("id", id);
            item.put("name", name);
            item.put("price", price);

            // adding contact to contact list
            shopList.add(item);
        }

        ListAdapter adapter = new SimpleAdapter(
                ShopActivity.this, shopList,
                R.layout.shop_layout, new String[]{"name",
                "price"}, new int[]{
                R.id.name, R.id.price});

        listView.setAdapter(adapter);
        //https://www.youtube.com/watch?v=ZEEYYvVwJGY

    }

    public void buyHandler(View v) throws JSONException {
        String price = c.getString("price");


    }
}
