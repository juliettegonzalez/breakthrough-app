package com.juliettegonzalez.breakthroughapp;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {


    public static final String REVEAL_X="REVEAL_X";
    public static final String REVEAL_Y="REVEAL_Y";
    public static final String MODE="TWO_PLAYERS";

    private boolean twoPlayerMode;
    private View revealView;
    private CoordinatorLayout activityGameRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Typeface caviarDreams_bold = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams_Bold.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ((TextView) toolbar.findViewById(R.id.actionbar_header_title)).setTypeface(caviarDreams_bold);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        activityGameRoot = (CoordinatorLayout) findViewById(R.id.activity_game_root);
        revealView = activityGameRoot;

        twoPlayerMode = getIntent().getBooleanExtra("TWO_PLAYERS", false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment gameSelectionFragment;

        if (twoPlayerMode){
            gameSelectionFragment = new TwoPawnsSelectionFragment();
        }else{
            gameSelectionFragment = new PawnSelectionFragment();
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, gameSelectionFragment)
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                destroyActivity(revealView, 400);
                MainActivity.cancelTransition();
                return true;

            case R.id.action_new_game:
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment gameFragment;

                if (twoPlayerMode){
                    gameFragment = MultiplayerFragment.newInstance(
                            TwoPawnsSelectionFragment.mWhiteSelectedPawn,
                            TwoPawnsSelectionFragment.mBlackSelectedPawn);
                }else{
                    gameFragment = BoardFragment.newInstance(PawnSelectionFragment.mSelectedPawn);
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, gameFragment)
                        .commit();
                return true;

            case R.id.action_quit_game:
                destroyActivity(revealView, 600);
                MainActivity.cancelTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void destroyActivity(View rootView, int duration) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            destroyCircularRevealActivity(rootView, duration);
        else
            finish();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void destroyCircularRevealActivity(final View rootView, int duration) {
        int cx = getIntent().getIntExtra(REVEAL_X, 0);
        int cy = getIntent().getIntExtra(REVEAL_Y, 0);

        float finalRadius = Math.max(rootView.getWidth(), rootView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, finalRadius, 0);
        circularReveal.setDuration(duration);

        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rootView.setVisibility(View.INVISIBLE);
                if (twoPlayerMode) {
                    TwoPawnsSelectionFragment.mWhiteSelectedPawn = null;
                    TwoPawnsSelectionFragment.mBlackSelectedPawn = null;
                }
                finishAfterTransition();
                overridePendingTransition(0,0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        // make the view visible and start the animation
        rootView.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    private void circularRevealActivity(View rootView) {

        int cx = getIntent().getIntExtra(REVEAL_X, 0);
        int cy = getIntent().getIntExtra(REVEAL_Y, 0);

        float finalRadius = Math.max(rootView.getWidth(), rootView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 0, finalRadius);
        circularReveal.setDuration(300);

        // make the view visible and start the animation
        rootView.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    /**
     * Implement circularReveal animation when launching activity
     * @param rootView
     */
    public void showRevealEffect(final View rootView) {

        revealView=rootView;
        rootView.setVisibility(View.INVISIBLE);

        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();

        if(viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    circularRevealActivity(rootView);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_game, menu);
        return true;
    }



}
