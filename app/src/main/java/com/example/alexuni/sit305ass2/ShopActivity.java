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

    ImageView shopImage1, shopImage2, shopImage3, shopImage4, shopImage5, shopImage6, shopImage7;
    TextView shopName1, shopName2, shopName3, shopName4, shopName5, shopName6, shopName7;
    TextView shopPrice1, shopPrice2, shopPrice3, shopPrice4, shopPrice5, shopPrice6, shopPrice7;
    Button buyBtn1, buyBtn2, buyBtn3, buyBtn4, buyBtn5, buyBtn6, buyBtn7;
    boolean item1Purchased, item2Purchased, item3Purchased, item4Purchased, item5Purchased, item6Purchased, item7Purchased;
    TextView goldText;

    String btn1Text, btn2Text, btn3Text, btn4Text, btn5Text, btn6Text, btn7Text;

    int i, id, price, weapEquipped, goldCount;
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
        shopImage5 = findViewById(R.id.shopImage5);
        shopImage6 = findViewById(R.id.shopImage6);
        shopImage7 = findViewById(R.id.shopImage7);

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

        shopName5 = findViewById(R.id.shopName5);
        shopPrice5 = findViewById(R.id.shopPrice5);
        buyBtn5 = findViewById(R.id.buyBtn5);

        shopName6 = findViewById(R.id.shopName6);
        shopPrice6 = findViewById(R.id.shopPrice6);
        buyBtn6 = findViewById(R.id.buyBtn6);

        shopName7 = findViewById(R.id.shopName7);
        shopPrice7 = findViewById(R.id.shopPrice7);
        buyBtn7 = findViewById(R.id.buyBtn7);

        goldText = findViewById(R.id.shopGoldText);

        item2Purchased = prefs.getBoolean("item2Purchased", false);
        item3Purchased = prefs.getBoolean("item3Purchased", false);
        item4Purchased = prefs.getBoolean("item4Purchased", false);
        item5Purchased = prefs.getBoolean("item5Purchased", false);
        item6Purchased = prefs.getBoolean("item6Purchased", false);
        item7Purchased = prefs.getBoolean("item7Purchased", false);
        weapEquipped = prefs.getInt("weapEquipped", 0);
        goldCount = prefs.getInt("gold", 0);
        goldText.setText(String.valueOf(goldCount));

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
        i = 0;
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
        getItem();
        i++;
        shopName5.setText(name);
        shopPrice5.setText(String.valueOf(price));
        getItem();
        i++;
        shopName6.setText(name);
        shopPrice6.setText(String.valueOf(price));
        getItem();
        i++;
        shopName7.setText(name);
        shopPrice7.setText(String.valueOf(price));
    }
    public void loadImages() {
        shopImage2.setBackgroundResource(R.drawable.sword001);
        shopImage3.setBackgroundResource(R.drawable.sword002);
        shopImage4.setBackgroundResource(R.drawable.sword003);
        shopImage5.setBackgroundResource(R.drawable.sword004);
        shopImage6.setBackgroundResource(R.drawable.sword005);
        shopImage7.setBackgroundResource(R.drawable.sword006);
    }
    public void btnText1Value() {
        if(weapEquipped != 0 ) {
            btn1Text = "Equip";
        } else {
            btn1Text = "Equipped";
        }
        buyBtn1.setText(btn1Text);
    }

    public void btnText2Value() {
        if(item2Purchased) {
            if(weapEquipped != 1 ) {
                btn2Text = "Equip";
            } else {
                btn2Text = "Equipped";
            }
        } else {
            btn2Text = "Buy";
        }
        buyBtn2.setText(btn2Text);
    }
    public void btnText3Value() {
        if(item3Purchased) {
            if(weapEquipped != 2 ) {
                btn3Text = "Equip";
            } else {
                btn3Text = "Equipped";
            }
        } else {
            btn3Text = "Buy";
        }
        buyBtn3.setText(btn3Text);
    }
    public void btnText4Value() {
        if(item4Purchased) {
            if(weapEquipped != 3 ) {
                btn4Text = "Equip";
            } else {
                btn4Text = "Equipped";
            }
        } else {
            btn4Text = "Buy";
        }
        buyBtn4.setText(btn4Text);
    }

    public void loadButtons() {
        btnText1Value();
        btnText2Value();
        btnText3Value();
        btnText4Value();
    }

    public void buyHandler(View v) throws JSONException {
        if(v.getId() == R.id.buyBtn1) {
            if(btn1Text.contains("Equip")) {
                weapEquipped = 0;
            }
        }
        if(v.getId() == R.id.buyBtn2) {
            c = wd.getJSONObject(1);
            price = c.getInt("price");
            if(btn2Text.contains("Buy")) {
                if(goldCount >= price) {
                    goldCount = goldCount - price;
                    item2Purchased = true;
                    editor.putBoolean("item2Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 1;
            }
            if(btn2Text.contains("Equip")) {
                weapEquipped = 1;
            }
        }
        if(v.getId() == R.id.buyBtn3) {
            c = wd.getJSONObject(2);
            price = c.getInt("price");
            if(btn3Text.contains("Buy")) {
                if(goldCount >= price) {
                    goldCount = goldCount - price;
                    item3Purchased = true;
                    editor.putBoolean("item3Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 2;
            }
            if(btn3Text.contains("Equip")) {
                weapEquipped = 2;
            }
        }
        if(v.getId() == R.id.buyBtn4) {
            c = wd.getJSONObject(3);
            price = c.getInt("price");
            if(btn4Text.contains("Buy")) {
                if(goldCount >= price) {
                    goldCount = goldCount - price;
                    item4Purchased = true;
                    editor.putBoolean("item4Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 3;
            }
            if(btn3Text.contains("Equip")) {
                weapEquipped = 3;
            }
        }
        loadButtons();
        goldText.setText(String.valueOf(goldCount));
        editor.putInt("gold", goldCount);
        editor.putInt("weapEquipped", weapEquipped);
        editor.commit();
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
}
