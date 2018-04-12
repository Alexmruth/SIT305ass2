package com.example.alexuni.sit305ass2;

import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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
import java.util.Timer;
import java.util.TimerTask;

import static com.example.alexuni.sit305ass2.R.drawable.stickfigure;

public class GameActivity extends AppCompatActivity {
    int intSwitch = 0;
    int dc = 0; // dialogue counter
    int counter = 0;
    int i = 0;

    // screen size
    int viewWidth;
    int viewHeight;

    //images
    private ImageView image1;
    private Button upBtn;

    //position
    private float arrowUp;
    private float arrowUpY;
    private float arrowDownX;
    private float arrowDownY;
    private float arrowLeftX;
    private float arrowLeftY;
    private float arrowRightX;
    private float arrowRightY;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView textJSON = findViewById(R.id.textJSON);
        image1 = findViewById(R.id.image1);
        upBtn = findViewById(R.id.upBtn);
        int height;


        // Gets screen size
        final FrameLayout view = findViewById(R.id.fl);
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    //defines width and height of framelayout
                    viewWidth = view.getWidth();
                    viewHeight = view.getHeight();
                }
            });
        }


//Opens the getText method
        try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        upBtn.setOnClickListener(upPressed);

    }


    private View.OnClickListener upPressed = new View.OnClickListener() {
        public void onClick(View v) {

            /*if(test2.getBackground() == null) {
                test2.setBackgroundResource(R.drawable.stickfigure);
            } else {
                test1.setBackgroundResource(R.drawable.logo);
            } */
            moveUp();


        }
    };
    public void moveUp() {
        // up
            arrowUp = (float) Math.ceil(image1.getY() - (viewHeight / 10));
            image1.setY(arrowUp);
        
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



