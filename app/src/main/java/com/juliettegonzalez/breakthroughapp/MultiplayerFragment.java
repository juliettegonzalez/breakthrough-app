package com.juliettegonzalez.breakthroughapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.hanks.htextview.HTextView;
import com.juliettegonzalez.breakthroughapp.AI.MainGame;
import com.juliettegonzalez.breakthroughapp.AI.Player;
import com.juliettegonzalez.breakthroughapp.AI.SquareBoard;

import java.util.ArrayList;
import java.util.List;


public class MultiplayerFragment extends Fragment {

    private static final String ARG_PAWN_PLAYER1 = "player1_pawn";
    private static final String ARG_PAWN_PLAYER2  = "player2_pawn";

    private Player.PawnType mPawnPlayer1;
    private Player.PawnType mPawnPlayer2;

    private MainGame mGame;
    private GridView mBoardGrid;
    private List<SquareBoard> mSquareBoardList = new ArrayList<>();

    private HTextView gameStateTextView;
    private View selectedView = null;
    private ArrayList<View> possibleMoveView = new ArrayList<>();

    private BoardFragment.OnGameFragmentListener mListener;

    public MultiplayerFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pawnPlayer1
     * @param pawnPlayer2
     * @return A new instance of fragment MultiplayerFragment.
     */
    public static MultiplayerFragment newInstance(Player.PawnType pawnPlayer1, Player.PawnType pawnPlayer2) {
        MultiplayerFragment fragment = new MultiplayerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAWN_PLAYER1, pawnPlayer1);
        args.putSerializable(ARG_PAWN_PLAYER2, pawnPlayer2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPawnPlayer1 = (Player.PawnType) getArguments().getSerializable(ARG_PAWN_PLAYER1);
            mPawnPlayer2 = (Player.PawnType) getArguments().getSerializable(ARG_PAWN_PLAYER2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final Player player1 = new Player(false, Player.Color.WHITE, mPawnPlayer1);
        final Player player2 = new Player(false, Player.Color.BLACK, mPawnPlayer2);

        mGame = new MainGame(player1, player2);
        mSquareBoardList = mGame.getmBoard().matrixToList();
        SquareBoardAdapter squareBoardAdapter = new SquareBoardAdapter(getActivity(), R.layout.square, mSquareBoardList);

        mBoardGrid = (GridView) view.findViewById(R.id.multiplayer_board_grid);
        mBoardGrid.setAdapter(squareBoardAdapter);

        // Update message of the game's state
        gameStateTextView = (HTextView) view.findViewById(R.id.multiplayer_game_state_text);
        gameStateTextView.animateText(getString(R.string.player1_message));

        mBoardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SquareBoard selectedSquare = mSquareBoardList.get(position);

                // Player 1's turn
                if (mGame.isFirstPlayerTurn()){
                    // Getting suggestion
                    if (selectedSquare.getOwner() != null &&
                            selectedSquare.getOwner().equals(mGame.getmPlayer1())){
                        if (view != selectedView && selectedView != null) selectedView.setBackgroundResource(R.drawable.square_shape);

                        view.setBackgroundResource(R.drawable.square_shape_selected);
                        selectedView = view;

                        mGame.setmSelectedSquare(selectedSquare);
                        ArrayList<SquareBoard> possibleMoves = mGame.getPossibleMoves(mGame.getmPlayer1());
                        clearSuggestions();

                        if (!possibleMoves.isEmpty()){
                            for (SquareBoard square : possibleMoves) {
                                possibleMoveView.add(mBoardGrid.getChildAt(mSquareBoardList.indexOf(square)));
                                mBoardGrid.getChildAt(mSquareBoardList.indexOf(square)).setBackgroundResource(R.drawable.square_suggested_shape);
                            }
                        }
                    }

                    // Moving a pawn
                    if (mGame.getmSelectedSquare() != null &&
                            (selectedSquare.isFree() || (selectedSquare.getOwner() == player2))){
                        if (mGame.movePawn(selectedSquare)){
                            clearSuggestions();
                            selectedView.setBackgroundResource(R.drawable.square_shape);
                            selectedView = null;
                            ((BaseAdapter) mBoardGrid.getAdapter()).notifyDataSetChanged();

                            if (!mGame.isGameWon()){
                                mGame.endTurn();
                                gameStateTextView.animateText(getString(R.string.player2_message));
                            }
                        }
                    }
                }else{
                    // Getting suggestion
                    if (selectedSquare.getOwner() != null &&
                            selectedSquare.getOwner().equals(mGame.getmPlayer2())){
                        if (view != selectedView && selectedView != null) selectedView.setBackgroundResource(R.drawable.square_shape);

                        view.setBackgroundResource(R.drawable.square_shape_selected);
                        selectedView = view;

                        mGame.setmSelectedSquare(selectedSquare);
                        ArrayList<SquareBoard> possibleMoves = mGame.getPossibleMoves(mGame.getmPlayer2());
                        clearSuggestions();

                        if (!possibleMoves.isEmpty()){
                            for (SquareBoard square : possibleMoves) {
                                possibleMoveView.add(mBoardGrid.getChildAt(mSquareBoardList.indexOf(square)));
                                mBoardGrid.getChildAt(mSquareBoardList.indexOf(square)).setBackgroundResource(R.drawable.square_suggested_shape);
                            }
                        }
                    }

                    // Moving a pawn
                    if (mGame.getmSelectedSquare() != null &&
                            (selectedSquare.isFree() || (selectedSquare.getOwner() == player1))){
                        if (mGame.movePawn(selectedSquare)){
                            clearSuggestions();
                            selectedView.setBackgroundResource(R.drawable.square_shape);
                            selectedView = null;
                            ((BaseAdapter) mBoardGrid.getAdapter()).notifyDataSetChanged();

                            if (!mGame.isGameWon()){
                                mGame.endTurn();
                                gameStateTextView.animateText(getString(R.string.player1_message));
                            }
                        }
                    }
                }
            }
        });


        // Implement the listener
        mGame.setGameStateListener(new MainGame.GameStateListener() {
            @Override
            public void onGameEnd(Player winner) {

                // Send alertdialog
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                LayoutInflater inflater =  LayoutInflater.from(getActivity());
                builder.setView(inflater.inflate(R.layout.end_game_alertdialog, null));
                final android.app.AlertDialog alertDialog = builder.create();

                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.show();

                if (winner == mGame.getmPlayer1() ) {
                    ((TextView) alertDialog.findViewById(R.id.end_game_message)).setText(R.string.winning1_message);
                }else{
                    ((TextView) alertDialog.findViewById(R.id.end_game_message)).setText(R.string.losing1_message);
                }


                Button backBtn = (Button) alertDialog.findViewById(R.id.back_alertdialog_btn);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        mListener.onActivityEnd();
                    }
                });
            }
        });



        return view;
    }


    public void clearSuggestions(){
        if (!possibleMoveView.isEmpty()){
            for (View suggestedView : possibleMoveView) {
                suggestedView.setBackgroundResource(R.drawable.square_shape);
            }
            possibleMoveView.clear();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BoardFragment.OnGameFragmentListener) {
            mListener = (BoardFragment.OnGameFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BoardFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
