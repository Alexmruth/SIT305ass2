package com.example.alexuni.sit305ass2;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class GameActivity extends AppCompatActivity {
    int intSwitch = 0;
    int dc = 0; // dialogue counter
    int counter = 0;
    int i = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
// https://www.javatpoint.com/android-json-parsing-tutorial
// http://www.vogella.com/tutorials/AndroidJSON/article.html
        TextView textJSON = findViewById(R.id.textJSON);

        String jsonString = loadJSON();

        //textJSON.setText(jsonString);
       /* try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        } */


    }

    public void main() {
        TextView textJSON = findViewById(R.id.textJSON);
        JSONParser parser = new JSONParser();
        textJSON.setText("test");
        try {
            Object obj = parser.parse(new FileReader("dialogue.json"));
            JSONObject jsonObject = (JSONObject) obj;
            String name = (String) jsonObject.get("Characters");
            textJSON.setText(name);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

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
    public void nextDialogue(View view) {
        intSwitch++;
        try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // This method is a working switch statement and JSON reader
    public void getText() throws JSONException {
        TextView textJSON = findViewById(R.id.textJSON);
        TextView nameJSON = findViewById(R.id.nameJSON);
        JSONObject obj = new JSONObject(loadJSON());
        JSONArray chars = obj.getJSONArray("Characters");
        JSONObject c = chars.getJSONObject(i); //THIS number changes which object in the array that is called
        String dialogue;
        String charName;


        /* if(obj.getString("Name").equals("Name"))
        {
            String name = obj.getString("Name");
            textJSON.setText(name);
        } else {
            textJSON.setText("banana");
        } */
        switch (intSwitch) {
            case 1:
                    charName = c.getString("character1");
                    dialogue = c.getString("introText1");
                    nameJSON.setText(charName);
                    textJSON.setText(dialogue);

                break;
            case 2:
                charName = c.getString("character2");
                dialogue = c.getString("introText2");
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;


        }

    }

}



