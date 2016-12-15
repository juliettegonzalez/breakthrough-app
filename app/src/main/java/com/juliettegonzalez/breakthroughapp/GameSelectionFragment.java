package com.juliettegonzalez.breakthroughapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameSelectionFragment extends Fragment {

    ImageButton mDragonPawnBtn, mGrandpaPawnBtn, mKingPawnBtn, mWizardPawnBtn;

    public GameSelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_selection, container, false);

        mDragonPawnBtn = (ImageButton) view.findViewById(R.id.dragon_pawn_btn);
        mGrandpaPawnBtn = (ImageButton) view.findViewById(R.id.grandpa_pawn_btn);
        mKingPawnBtn = (ImageButton) view.findViewById(R.id.king_pawn_btn);
        mWizardPawnBtn = (ImageButton) view.findViewById(R.id.wizard_pawn_btn);

        mDragonPawnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDragonPawnBtn.setBackgroundResource(R.drawable.white_rounded_button_selected);
                mGrandpaPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
                mKingPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
                mWizardPawnBtn.setBackgroundResource(R.drawable.white_rounded_button);
            }
        });


        return view;
    }
}
