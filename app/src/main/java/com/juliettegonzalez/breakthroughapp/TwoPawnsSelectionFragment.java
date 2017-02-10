package com.juliettegonzalez.breakthroughapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Arrays;

public class TwoPawnsSelectionFragment extends Fragment {

    RelativeLayout content_game;
    TextView whitePawnSelectionTitle, blackPawnSelectionTitle;

    LinearLayout whiteDragonBtnSelection, whiteGrandpaBtnSelection, whiteKingBtnSelection, whiteWizardBtnSelection;
    LinearLayout blackDragonBtnSelection, blackGrandpaBtnSelection, blackKingBtnSelection, blackWizardBtnSelection;

    ImageButton mWhiteDragonPawnBtn, mWhiteGrandpaPawnBtn, mWhiteKingPawnBtn, mWhiteWizardPawnBtn;
    ImageButton mBlackDragonPawnBtn, mBlackGrandpaPawnBtn, mBlackKingPawnBtn, mBlackWizardPawnBtn;

    ArrayList<ImageButton> whitePawnsBtn;

    ArrayList<ImageButton> blackPawnsBtn;

    ImageButton mWhiteSelectedPawnBtn = null;
    ImageButton mBlackSelectedPawnBtn = null;

    static Player.PawnType mWhiteSelectedPawn = null;
    static Player.PawnType mBlackSelectedPawn = null;



    public TwoPawnsSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two_pawns_selection, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button);

        // Element initialization for animation
        whiteDragonBtnSelection = (LinearLayout) view.findViewById(R.id.dragon_white_btn_selection);
        whiteGrandpaBtnSelection = (LinearLayout) view.findViewById(R.id.grandpa_white_btn_selection);
        whiteKingBtnSelection = (LinearLayout) view.findViewById(R.id.king_white_btn_selection);
        whiteWizardBtnSelection = (LinearLayout) view.findViewById(R.id.wizard_white_btn_selection);
        whitePawnSelectionTitle = (TextView) view.findViewById(R.id.white_pawn_selection_title);

        blackDragonBtnSelection = (LinearLayout) view.findViewById(R.id.dragon_black_btn_selection);
        blackGrandpaBtnSelection = (LinearLayout) view.findViewById(R.id.grandpa_black_btn_selection);
        blackKingBtnSelection = (LinearLayout) view.findViewById(R.id.king_black_btn_selection);
        blackWizardBtnSelection = (LinearLayout) view.findViewById(R.id.wizard_black_btn_selection);
        blackPawnSelectionTitle = (TextView) view.findViewById(R.id.black_pawn_selection_title);
        content_game = (RelativeLayout) view.findViewById(R.id.content_game);

        content_game.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                content_game.getViewTreeObserver().removeOnPreDrawListener(this);

                blackPawnSelectionTitle.setTranslationY(-40);
                blackPawnSelectionTitle.setAlpha(0);

                whitePawnSelectionTitle.setTranslationY(-40);
                whitePawnSelectionTitle.setAlpha(0);

                /** BLACK **/
                blackDragonBtnSelection.setTranslationY(-60);
                blackDragonBtnSelection.setAlpha(0);

                blackGrandpaBtnSelection.setTranslationY(-60);
                blackGrandpaBtnSelection.setAlpha(0);

                blackKingBtnSelection.setTranslationY(-60);
                blackKingBtnSelection.setAlpha(0);

                blackWizardBtnSelection.setTranslationY(-60);
                blackWizardBtnSelection.setAlpha(0);

                /** WHITE **/
                whiteDragonBtnSelection.setTranslationY(-60);
                whiteDragonBtnSelection.setAlpha(0);

                whiteGrandpaBtnSelection.setTranslationY(-60);
                whiteGrandpaBtnSelection.setAlpha(0);

                whiteKingBtnSelection.setTranslationY(-60);
                whiteKingBtnSelection.setAlpha(0);

                whiteWizardBtnSelection.setTranslationY(-60);
                whiteWizardBtnSelection.setAlpha(0);

                startIntroAnimation();
                return true;
            }
        });

        mWhiteDragonPawnBtn = (ImageButton) view.findViewById(R.id.dragon_white_pawn_btn);
        mWhiteGrandpaPawnBtn = (ImageButton) view.findViewById(R.id.grandpa_white_pawn_btn);
        mWhiteKingPawnBtn = (ImageButton) view.findViewById(R.id.king_white_pawn_btn);
        mWhiteWizardPawnBtn = (ImageButton) view.findViewById(R.id.wizard_white_pawn_btn);

        mBlackDragonPawnBtn = (ImageButton) view.findViewById(R.id.dragon_black_pawn_btn);
        mBlackGrandpaPawnBtn = (ImageButton) view.findViewById(R.id.grandpa_black_pawn_btn);
        mBlackKingPawnBtn = (ImageButton) view.findViewById(R.id.king_black_pawn_btn);
        mBlackWizardPawnBtn = (ImageButton) view.findViewById(R.id.wizard_black_pawn_btn);

        whitePawnsBtn = new ArrayList<>(Arrays.asList(mWhiteDragonPawnBtn, mWhiteGrandpaPawnBtn,
                mWhiteKingPawnBtn, mWhiteWizardPawnBtn));
        blackPawnsBtn = new ArrayList<>(Arrays.asList(mBlackDragonPawnBtn, mBlackGrandpaPawnBtn,
                mBlackKingPawnBtn, mBlackWizardPawnBtn));

        // Button actions
        final Button startGameBtn = (Button) view.findViewById(R.id.start_multiplayer_game_btn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                MultiplayerFragment multiplayerFragment = MultiplayerFragment.newInstance(mWhiteSelectedPawn, mBlackSelectedPawn);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, multiplayerFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        for (final ImageButton whitePawnBtn : whitePawnsBtn) {
            whitePawnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWhiteSelectedPawnBtn != null) mWhiteSelectedPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
                    mWhiteSelectedPawnBtn = whitePawnBtn;

                    String type = getResources().getResourceEntryName(whitePawnBtn.getId());
                    if (type.contains("dragon")) {
                        mWhiteSelectedPawn = Player.PawnType.DRAGON;
                    }else if (type.contains("grandpa")){
                        mWhiteSelectedPawn = Player.PawnType.GRANDPA;
                    }else if(type.contains("king")){
                        mWhiteSelectedPawn = Player.PawnType.KING;
                    }else if (type.contains("wizard")){
                        mWhiteSelectedPawn = Player.PawnType.WIZARD;
                    }
                    whitePawnBtn.setBackgroundResource(R.drawable.white_rounded_button_selected);

                    if (mBlackSelectedPawn != null) startGameBtn.setEnabled(true);
                }
            });
        }

        for (final ImageButton blackPawnBtn : blackPawnsBtn) {
            blackPawnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBlackSelectedPawnBtn != null) mBlackSelectedPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
                    mBlackSelectedPawnBtn = blackPawnBtn;

                    String type = getResources().getResourceEntryName(blackPawnBtn.getId());
                    if (type.contains("dragon")) {
                        mBlackSelectedPawn = Player.PawnType.DRAGON;
                    }else if (type.contains("grandpa")){
                        mBlackSelectedPawn = Player.PawnType.GRANDPA;
                    }else if(type.contains("king")){
                        mBlackSelectedPawn = Player.PawnType.KING;
                    }else if (type.contains("wizard")){
                        mBlackSelectedPawn = Player.PawnType.WIZARD;
                    }
                    blackPawnBtn.setBackgroundResource(R.drawable.white_rounded_button_selected);

                    Log.d("DEBUG", "BlackSelected = " + mBlackSelectedPawn);
                    Log.d("DEBUG", "WhiteSelected = " + mWhiteSelectedPawn);


                    if (mWhiteSelectedPawn != null) startGameBtn.setEnabled(true);
                }
            });
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_new_game).setVisible(false);
        menu.findItem(R.id.action_quit_game).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void startIntroAnimation() {

        whitePawnSelectionTitle.animate()
                .translationY(0).alpha(1.f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300)
                .start();

        blackPawnSelectionTitle.animate()
                .translationY(0).alpha(1.f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300)
                .start();

        whiteDragonBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(100)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        whiteGrandpaBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(110)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        whiteKingBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(115)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        whiteWizardBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(105)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        /** BLACK **/
        blackDragonBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(100)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        blackGrandpaBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(110)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        blackKingBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(115)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

        blackWizardBtnSelection.animate()
                .translationY(0).alpha(1.f)
                .setStartDelay(105)
                .setInterpolator(new BounceInterpolator())
                .setDuration(850)
                .start();

    }
}
