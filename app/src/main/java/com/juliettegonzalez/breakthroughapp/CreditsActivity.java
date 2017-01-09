package com.juliettegonzalez.breakthroughapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        Typeface caviarDreams_bold = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf");
        ((TextView)findViewById(R.id.credits_header)).setTypeface(caviarDreams_bold);
        ((TextView)findViewById(R.id.credits_subtitle_pictures)).setTypeface(caviarDreams_bold);
        ((TextView)findViewById(R.id.credits_subtitle_app)).setTypeface(caviarDreams_bold);

        getSupportActionBar().setTitle("Breakthrough App - Version 1");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
