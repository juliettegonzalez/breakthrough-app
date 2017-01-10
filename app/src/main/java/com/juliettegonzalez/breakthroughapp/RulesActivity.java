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
                "Breakthrough est un jeu de stratégie sur un plateau 8x8",
                R.drawable.rules_board,
                getResources().getColor(R.color.colorAccentMid)));
        addSlide(AppIntroFragment.newInstance("Déplacement",
                "Les pions peuvent se déplacer uniquement en avant, tout droit ou en diagonale",
                R.drawable.rules_move,
                getResources().getColor(R.color.colorAccentMid)));
        addSlide(AppIntroFragment.newInstance("Manger un adversaire",
                "Il est possible de manger un adversaire uniquement s'il est en diagonale, sinon il est impossible de bouger",
                R.drawable.rules_eat,
                getResources().getColor(R.color.colorAccentMid)));
        addSlide(AppIntroFragment.newInstance("Gagner",
                "Pour gagner, il faut atteindre la ligne adverse ou manger tous les adversaires",
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
