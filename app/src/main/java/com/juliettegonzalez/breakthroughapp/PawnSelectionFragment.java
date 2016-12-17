package com.juliettegonzalez.breakthroughapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class PawnSelectionFragment extends Fragment {

    ImageButton mDragonPawnBtn, mGrandpaPawnBtn, mKingPawnBtn, mWizardPawnBtn;

    public PawnSelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pawn_selection, container, false);

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


        Button startGameBtn = (Button) view.findViewById(R.id.start_game_btn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                BoardFragment boardFragment = BoardFragment.newInstance("coucou");
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment, boardFragment)
                        .addToBackStack("pawnSelection")
                        .commit();
            }
        });


        return view;
    }
}
