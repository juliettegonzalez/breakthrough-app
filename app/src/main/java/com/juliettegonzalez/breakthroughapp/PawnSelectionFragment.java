package com.juliettegonzalez.breakthroughapp;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juliettegonzalez.breakthroughapp.AI.Player;

/**
 * A placeholder fragment containing a simple view.
 */
public class PawnSelectionFragment extends Fragment {

    LinearLayout dragonBtnSelection, grandpaBtnSelection, kingBtnSelection, wizardBtnSelection;
    RelativeLayout content_game;
    TextView pawnSelectionTitle;

    ImageButton mDragonPawnBtn, mGrandpaPawnBtn, mKingPawnBtn, mWizardPawnBtn;
    ImageButton mSelectedPawnBtn = null;
    public static Player.PawnType mSelectedPawn = null;

    public PawnSelectionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_new_game).setVisible(false);
        menu.findItem(R.id.action_quit_game).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pawn_selection, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button);

        // Element initialization for animation
        dragonBtnSelection = (LinearLayout) view.findViewById(R.id.dragon_btn_selection);
        grandpaBtnSelection = (LinearLayout) view.findViewById(R.id.grandpa_btn_selection);
        kingBtnSelection = (LinearLayout) view.findViewById(R.id.king_btn_selection);
        wizardBtnSelection = (LinearLayout) view.findViewById(R.id.wizard_btn_selection);
        pawnSelectionTitle = (TextView) view.findViewById(R.id.pawn_selection_title);
        content_game = (RelativeLayout) view.findViewById(R.id.content_game);


        content_game.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                content_game.getViewTreeObserver().removeOnPreDrawListener(this);

                pawnSelectionTitle.setTranslationY(-40);
                pawnSelectionTitle.setAlpha(0);

                dragonBtnSelection.setTranslationY(-60);
                dragonBtnSelection.setAlpha(0);

                grandpaBtnSelection.setTranslationY(-60);
                grandpaBtnSelection.setAlpha(0);

                kingBtnSelection.setTranslationY(-60);
                kingBtnSelection.setAlpha(0);

                wizardBtnSelection.setTranslationY(-60);
                wizardBtnSelection.setAlpha(0);

                startIntroAnimation();
                return true;
            }
        });


        mDragonPawnBtn = (ImageButton) view.findViewById(R.id.dragon_pawn_btn);
        mGrandpaPawnBtn = (ImageButton) view.findViewById(R.id.grandpa_pawn_btn);
        mKingPawnBtn = (ImageButton) view.findViewById(R.id.king_pawn_btn);
        mWizardPawnBtn = (ImageButton) view.findViewById(R.id.wizard_pawn_btn);

        // Button actions
        final Button startGameBtn = (Button) view.findViewById(R.id.start_game_btn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                BoardFragment boardFragment = BoardFragment.newInstance(mSelectedPawn);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, boardFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack("pawnSelection")
                        .commit();
            }
        });

        mDragonPawnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedPawnBtn != null) mSelectedPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
                mSelectedPawnBtn = mDragonPawnBtn;
                mSelectedPawn = Player.PawnType.DRAGON;
                mDragonPawnBtn.setBackgroundResource(R.drawable.white_rounded_button_selected);
                startGameBtn.setEnabled(true);
            }
        });

        mGrandpaPawnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedPawnBtn != null) mSelectedPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
                mSelectedPawnBtn = mGrandpaPawnBtn;
                mSelectedPawn = Player.PawnType.GRANDPA;
                mGrandpaPawnBtn.setBackgroundResource(R.drawable.white_rounded_button_selected);
                startGameBtn.setEnabled(true);
            }
        });

        mKingPawnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedPawnBtn != null) mSelectedPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
                mSelectedPawnBtn = mKingPawnBtn;
                mSelectedPawn = Player.PawnType.KING;
                mKingPawnBtn.setBackgroundResource(R.drawable.white_rounded_button_selected);
                startGameBtn.setEnabled(true);
            }
        });

        mWizardPawnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedPawnBtn != null) mSelectedPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
                mSelectedPawnBtn = mWizardPawnBtn;
                mSelectedPawn = Player.PawnType.WIZARD;
                mWizardPawnBtn.setBackgroundResource(R.drawable.white_rounded_button_selected);
                startGameBtn.setEnabled(true);
            }
        });



        return view;
    }



    private void startIntroAnimation() {

        pawnSelectionTitle.animate()
                .translationY(0).alpha(1.f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300)
                .start();

        dragonBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(100)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        grandpaBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(110)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        kingBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(115)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        wizardBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(105)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

    }

}
