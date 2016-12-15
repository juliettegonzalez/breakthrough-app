package com.juliettegonzalez.breakthroughapp;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface caviarDreams_bold = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf");
        ((TextView)findViewById(R.id.title_text_view)).setTypeface(caviarDreams_bold);

        getSupportActionBar().hide();

    }
}
