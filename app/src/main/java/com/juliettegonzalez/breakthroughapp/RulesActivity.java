package com.juliettegonzalez.breakthroughapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class RulesActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Breakthrough",
                getResources().getString(R.string.rules_presentation),
                R.drawable.rules_board,
                getResources().getColor(R.color.colorAccentMid)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.rules_title_movement),
                getResources().getString(R.string.rules_movement),
                R.drawable.rules_move,
                getResources().getColor(R.color.colorAccentMid)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.rules_title_attack),
                getResources().getString(R.string.rules_attack),
                R.drawable.rules_eat,
                getResources().getColor(R.color.colorAccentMid)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.rules_title_win),
                getResources().getString(R.string.rules_win),
                R.drawable.rules_win,
                getResources().getColor(R.color.colorAccentMid)));


        // SHOW or HIDE the statusbar
        showStatusBar(false);

        // Hide Skip/Done button.
        setProgressButtonEnabled(true);
        setButtonState(skipButton, true);
        skipButtonEnabled = true;
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
