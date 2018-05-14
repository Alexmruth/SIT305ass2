package com.example.alexuni.sit305ass2;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

public class LevelsActivity extends AppCompatActivity {

    Button lvlBtn1, lvlBtn2, lvlBtn3, lvlBtn4, lvlBtn5, lvlBtn6;
    static  int currentLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        lvlBtn1 = findViewById(R.id.lvlBtn1);
        lvlBtn2 = findViewById(R.id.lvlBtn2);
        lvlBtn3 = findViewById(R.id.lvlBtn3);
        lvlBtn4 = findViewById(R.id.lvlBtn4);
        lvlBtn5 = findViewById(R.id.lvlBtn5);
        lvlBtn6 = findViewById(R.id.lvlBtn6);
    }

    public void lvlHandler(View v) {
        if(v.getId() == R.id.lvlBtn1) {
            currentLevel =  1;
        }
        if (v.getId() == R.id.lvlBtn2) {
            currentLevel = 2;
        }
        if (v.getId() == R.id.lvlBtn3) {
            currentLevel = 3;
        }
        if (v.getId() == R.id.lvlBtn4) {
            currentLevel = 4;
        }
        if (v.getId() == R.id.lvlBtn5) {
            currentLevel = 5;
        }
        if (v.getId() == R.id.lvlBtn6) {
            currentLevel = 6;
        }
        Intent intent = new Intent (this, LevelOne.class);
        startActivity(intent);
    }

    public void onBack(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
