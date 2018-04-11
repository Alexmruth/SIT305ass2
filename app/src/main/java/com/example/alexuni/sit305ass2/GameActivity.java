package com.example.alexuni.sit305ass2;

import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import static com.example.alexuni.sit305ass2.R.drawable.stickfigure;

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

        TextView textJSON = findViewById(R.id.textJSON);
        Button upBtn = findViewById(R.id.upBtn);

       try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        upBtn.setOnClickListener(upPressed);

    }
    private View.OnClickListener upPressed = new View.OnClickListener() {
        public void onClick(View v) {
            ImageView test1 = findViewById(R.id.test1);
            ImageView test2 = findViewById(R.id.test2);
            if(test2.getBackground() == null) {
                test2.setBackgroundResource(R.drawable.stickfigure);
            } else {
                test1.setBackgroundResource(R.drawable.logo);
            }
        }
    };


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
        Button nextBtn = findViewById(R.id.nextBtn);
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
            case 0:
                charName = c.getString("character1");
                dialogue = c.getString("introText1");
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 1:
                charName = c.getString("character2");
                dialogue = c.getString("introText2");
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 2:

                break;


        }

    }

}



