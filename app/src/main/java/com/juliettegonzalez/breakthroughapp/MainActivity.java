package com.juliettegonzalez.breakthroughapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static LinearLayout expand_start_layout;
    private static LinearLayout expand_hidden_content;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface caviarDreams_bold = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf");
        ((TextView)findViewById(R.id.title_text_view)).setTypeface(caviarDreams_bold);

        getSupportActionBar().hide();

        expand_start_layout = (LinearLayout) findViewById(R.id.expand_start_layout);
        expand_hidden_content = (LinearLayout) findViewById(R.id.expand_hidden_content);


        final Button newGameBtn = (Button) findViewById(R.id.new_game_btn);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2];
                location[0] = getResources().getDisplayMetrics().widthPixels / 2;
                location[1] = getResources().getDisplayMetrics().heightPixels / 2;
                startTransition(location);
            }
        });

        Button rulesBtn = (Button) findViewById(R.id.rules_btn);
        rulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });


        TextView creditLink = (TextView) findViewById(R.id.credits_link);
        creditLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new BottomSheetDialog(MainActivity.this);
                dialog.setContentView(R.layout.activity_credits);
                dialog.show();
            }
        });

    }


    private void startTransition(final int[] location){
        expand_hidden_content.animate()
                .alpha(0)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        expand_start_layout.animate()
                                .scaleY(5)
                                .scaleX((float)1.5)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .setDuration(400)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        Intent intent = new Intent(MainActivity.this, GameActivity.class);

                                        intent.putExtra(GameActivity.REVEAL_X, location[0]);
                                        intent.putExtra(GameActivity.REVEAL_Y, location[1]);

                                        startActivity(intent);
                                        overridePendingTransition(0,0);
                                    }
                                })
                                .start();
                    }
                })
                .start();
    }


    public static void cancelTransition(){
        expand_hidden_content.setAlpha(1);
        expand_start_layout.setScaleY(1);
        expand_start_layout.setScaleX(1);
    }
}
