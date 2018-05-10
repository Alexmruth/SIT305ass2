package com.example.alexuni.sit305ass2;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

public class LevelsActivity extends AppCompatActivity {

    Button lvlBtn1, lvlBtn2;
    static  int currentLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        lvlBtn1 = findViewById(R.id.lvlBtn1);
        lvlBtn2 = findViewById(R.id.lvlBtn2);
    }

    public void lvlHandler(View v) {
        if(v.getId() == R.id.lvlBtn1) {
            currentLevel =  1;
        }
        if (v.getId() == R.id.lvlBtn2) {
            currentLevel = 2;
        }
        Intent intent = new Intent (this, LevelOne.class);
        startActivity(intent);
    }
}
