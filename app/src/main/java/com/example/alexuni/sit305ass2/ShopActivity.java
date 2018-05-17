package com.example.alexuni.sit305ass2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/* The class ShopActivity is responsible for displaying shop items to purchase, as well as already
purchased items where the player can equip and unequip items */
public class ShopActivity extends AppCompatActivity {

    JSONArray wd;
    JSONObject c;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    ImageView shopImage1, shopImage2, shopImage3, shopImage4, shopImage5, shopImage6, shopImage7;
    TextView shopName1, shopName2, shopName3, shopName4, shopName5, shopName6, shopName7;
    TextView shopPrice1, shopPrice2, shopPrice3, shopPrice4, shopPrice5, shopPrice6, shopPrice7;
    Button buyBtn1, buyBtn2, buyBtn3, buyBtn4, buyBtn5, buyBtn6, buyBtn7;
    boolean item2Purchased, item3Purchased, item4Purchased, item5Purchased, item6Purchased, item7Purchased;
    TextView goldText;

    String btn1Text, btn2Text, btn3Text, btn4Text, btn5Text, btn6Text, btn7Text;

    int i, id, price, weapEquipped, goldCount, baseGold;
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
        try {
            JSONObject obj = new JSONObject(loadJSON());
            JSONObject jo = obj.getJSONObject("PlayerData");
            baseGold = jo.getInt("baseGold");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        goldCount = prefs.getInt("gold", baseGold);
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

    /* loadJSON() is responsible for grabbing all content from the specified JSON file and converting
        it into a string to be used as a JSONObject across various methods, such as getItem(). */
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

    /* The method getItem() is responsible for grabbing item ID, name and price from an array based on
    the value of i. This method is called by loadItems() to loop through each item. */
    public void getItem() throws JSONException {
        JSONObject gameData = new JSONObject(loadJSON());
        // Getting JSON Array node
        wd = gameData.getJSONArray("Weapons");

        c = wd.getJSONObject(i);

        id = c.getInt("ID");
        name = c.getString("name");
        price = c.getInt("price");

    }

    /* The method loadItems() is responsible for looping through each line in the JSON array, grab
    the data and assign it to the given TextView. */
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

    //loadImages() sets each ImageView to a set weapon image
    public void loadImages() {
        shopImage2.setBackgroundResource(R.drawable.sword001);
        shopImage3.setBackgroundResource(R.drawable.sword002);
        shopImage4.setBackgroundResource(R.drawable.sword003);
        shopImage5.setBackgroundResource(R.drawable.sword004);
        shopImage6.setBackgroundResource(R.drawable.sword005);
        shopImage7.setBackgroundResource(R.drawable.sword006);
    }

    /*The methods below btnText1Value() all the way to btnText7Value() are responsible for handing
    the button text, based on if that item is either purchased, equipped or unequipped.
     */
    public void btnText1Value() {
        if (weapEquipped != 0) {
            btn1Text = getString(R.string.equip);
        } else {
            btn1Text = getString(R.string.equipped);
        }
        buyBtn1.setText(btn1Text);
    }

    public void btnText2Value() {
        if (item2Purchased) {
            if (weapEquipped != 1) {
                btn2Text = getString(R.string.equip);
            } else {
                btn2Text = getString(R.string.equipped);
            }
        } else {
            btn2Text = "Buy";
        }
        buyBtn2.setText(btn2Text);
    }

    public void btnText3Value() {
        if (item3Purchased) {
            if (weapEquipped != 2) {
                btn3Text = getString(R.string.equip);
            } else {
                btn3Text = getString(R.string.equipped);
            }
        } else {
            btn3Text = "Buy";
        }
        buyBtn3.setText(btn3Text);
    }

    public void btnText4Value() {
        if (item4Purchased) {
            if (weapEquipped != 3) {
                btn4Text = getString(R.string.equip);
            } else {
                btn4Text = getString(R.string.equipped);
            }
        } else {
            btn4Text = "Buy";
        }
        buyBtn4.setText(btn4Text);
    }

    public void btnText5Value() {
        if (item5Purchased) {
            if (weapEquipped != 4) {
                btn5Text = getString(R.string.equip);
            } else {
                btn5Text = getString(R.string.equipped);
            }
        } else {
            btn5Text = "Buy";
        }
        buyBtn5.setText(btn5Text);
    }

    public void btnText6Value() {
        if (item6Purchased) {
            if (weapEquipped != 5) {
                btn6Text = getString(R.string.equip);
            } else {
                btn6Text = getString(R.string.equipped);
            }
        } else {
            btn6Text = "Buy";
        }
        buyBtn6.setText(btn6Text);
    }

    public void btnText7Value() {
        if (item7Purchased) {
            if (weapEquipped != 6) {
                btn7Text = getString(R.string.equip);
            } else {
                btn7Text = getString(R.string.equipped);
            }
        } else {
            btn7Text = "Buy";
        }
        buyBtn7.setText(btn7Text);
    }

    //Loads all the above buttons
    public void loadButtons() {
        btnText1Value();
        btnText2Value();
        btnText3Value();
        btnText4Value();
        btnText5Value();
        btnText6Value();
        btnText7Value();
    }

    /* buyHandler() method grabs the button clicked through View v and performs an action based on
    the text displayed on the button.*/
    public void buyHandler(View v) throws JSONException {
        if (v.getId() == R.id.buyBtn1) {
            if (btn1Text.contains("Equip")) {
                weapEquipped = 0;
            }
        }
        if (v.getId() == R.id.buyBtn2) {
            c = wd.getJSONObject(1);
            price = c.getInt("price");
            if (btn2Text.contains("Buy")) {
                if (goldCount >= price) {
                    goldCount = goldCount - price;
                    item2Purchased = true;
                    editor.putBoolean("item2Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 1;
            }
            if (btn2Text.contains("Equip")) {
                weapEquipped = 1;
            }
        }
        if (v.getId() == R.id.buyBtn3) {
            c = wd.getJSONObject(2);
            price = c.getInt("price");
            if (btn3Text.contains("Buy")) {
                if (goldCount >= price) {
                    goldCount = goldCount - price;
                    item3Purchased = true;
                    editor.putBoolean("item3Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 2;
            }
            if (btn3Text.contains("Equip")) {
                weapEquipped = 2;
            }
        }
        if (v.getId() == R.id.buyBtn4) {
            c = wd.getJSONObject(3);
            price = c.getInt("price");
            if (btn4Text.contains("Buy")) {
                if (goldCount >= price) {
                    goldCount = goldCount - price;
                    item4Purchased = true;
                    editor.putBoolean("item4Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 3;
            }
            if (btn3Text.contains("Equip")) {
                weapEquipped = 3;
            }
        }
        if (v.getId() == R.id.buyBtn5) {
            c = wd.getJSONObject(4);
            price = c.getInt("price");
            if (btn5Text.contains("Buy")) {
                if (goldCount >= price) {
                    goldCount = goldCount - price;
                    item5Purchased = true;
                    editor.putBoolean("item5Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 4;
            }
            if (btn3Text.contains("Equip")) {
                weapEquipped = 4;
            }
        }
        if (v.getId() == R.id.buyBtn6) {
            c = wd.getJSONObject(5);
            price = c.getInt("price");
            if (btn6Text.contains("Buy")) {
                if (goldCount >= price) {
                    goldCount = goldCount - price;
                    item6Purchased = true;
                    editor.putBoolean("item6Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 5;
            }
            if (btn3Text.contains("Equip")) {
                weapEquipped = 5;
            }
        }
        if (v.getId() == R.id.buyBtn7) {
            c = wd.getJSONObject(6);
            price = c.getInt("price");
            if (btn7Text.contains("Buy")) {
                if (goldCount >= price) {
                    goldCount = goldCount - price;
                    item7Purchased = true;
                    editor.putBoolean("item7Purchased", true);
                } else {
                    showToast();
                }
                weapEquipped = 6;
            }
            if (btn3Text.contains("Equip")) {
                weapEquipped = 6;
            }
        }
        loadButtons();
        goldText.setText(String.valueOf(goldCount));
        editor.putInt("gold", goldCount);
        editor.putInt("weapEquipped", weapEquipped);
        editor.commit();
        save();
    }

    //Saves player data and updates attack values based on weapon, called by buyHandler().
    public void save() throws JSONException {
        JSONObject obj = new JSONObject(loadJSON());
        JSONObject jo = obj.getJSONObject("PlayerData");
        int baseAttMin1 = jo.getInt("baseAttMin");
        int baseAttMax1 = jo.getInt("baseAttMax");
        wd = obj.getJSONArray("Weapons");
        JSONObject weapon = wd.getJSONObject(weapEquipped);

        int weaponAttMin = weapon.getInt("attMin");
        int weaponAttMax = weapon.getInt("attMax");

        int totalAttMin = weaponAttMin + baseAttMin1;
        int totalAttMax = weaponAttMax + baseAttMax1;

        editor.putInt("attMin", totalAttMin);
        editor.putInt("attMax", totalAttMax);
        editor.commit();
    }

    //Displays error message when player has insufficient gold to purchase item
    public void showToast() {
        Context context = getApplicationContext();
        CharSequence text;
        final Toast toast;
        int duration = Toast.LENGTH_LONG;
        text = "Not enough gold!";

        toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onBack(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
