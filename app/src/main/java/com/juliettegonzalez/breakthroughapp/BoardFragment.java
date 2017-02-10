package com.juliettegonzalez.breakthroughapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


public class BoardFragment extends Fragment {

    private static final String ARG_PAWN = "player_pawn";
    private Player.PawnType mPlayerPawn;

    private MainGame mGame;
    private GridView mBoardGrid;
    private List<SquareBoard> mSquareBoardList = new ArrayList<>();

    private HTextView gameStateTextView;
    private View selectedView = null;
    private ArrayList<View> possibleMoveView = new ArrayList<>();


    public BoardFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param playerPawn Parameter 1.
     * @return A new instance of fragment BoardFragment.
     */
    public static BoardFragment newInstance(Player.PawnType playerPawn) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAWN, playerPawn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayerPawn = (Player.PawnType) getArguments().getSerializable(ARG_PAWN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Player player1 = new Player(false, Player.Color.WHITE, mPlayerPawn);
        final Player computer = new Player(true, Player.Color.BLACK, Player.getRandomPawn(mPlayerPawn));

        mGame = new MainGame(player1, computer);
        mSquareBoardList = mGame.getmBoard().matrixToList();
        SquareBoardAdapter squareBoardAdapter = new SquareBoardAdapter(getActivity(), R.layout.square, mSquareBoardList);

        mBoardGrid = (GridView) view.findViewById(R.id.board_grid);
        mBoardGrid.setAdapter(squareBoardAdapter);

        // Update message of the game's state
        gameStateTextView = (HTextView) view.findViewById(R.id.game_state_text);
        gameStateTextView.animateText(getString(R.string.player_message));

        mBoardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SquareBoard selectedSquare = mSquareBoardList.get(position);

                // Forbid click if not player's turn
                if (mGame.isHumanTurn()){
                    // Getting suggestion
                    if (selectedSquare.getOwner() != null &&
                            selectedSquare.getOwner().equals(mGame.getmPlayer1())){
                        if (view != selectedView && selectedView != null) selectedView.setBackgroundResource(R.drawable.square_shape);

                        view.setBackgroundResource(R.drawable.square_shape_selected);
                        selectedView = view;

                        mGame.setmSelectedSquare(selectedSquare);
                        ArrayList<SquareBoard> possibleMoves = mGame.getPossibleMoves();
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
                            (selectedSquare.isFree() || (selectedSquare.getOwner() == computer))){
                        if (mGame.movePawn(selectedSquare)){
                            clearSuggestions();
                            selectedView.setBackgroundResource(R.drawable.square_shape);
                            selectedView = null;
                            ((BaseAdapter) mBoardGrid.getAdapter()).notifyDataSetChanged();

                            if (!mGame.isGameWon()){
                                mGame.endTurn();
                                new AIPlayTask().execute();
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
                android.app.AlertDialog alertDialog = builder.create();

                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                if (winner.isComputer()) {
                    ((TextView) alertDialog.findViewById(R.id.end_game_message)).setText(R.string.losing_message);
                }else{
                    ((TextView) alertDialog.findViewById(R.id.end_game_message)).setText(R.string.winning_message);
                }


                Button backBtn = (Button) alertDialog.findViewById(R.id.back_alertdialog_btn);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
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


    private class AIPlayTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            gameStateTextView.animateText(getString(R.string.ai_message));
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            long startTime = System.currentTimeMillis();
            mGame.aiPlaying();
            long difference = System.currentTimeMillis() - startTime;
            Log.d("TIME MEASURE" , "Time : " + difference/1000 + "s");
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Void result) {
            if (!mGame.isGameWon()){
                mGame.endTurn();
                gameStateTextView.animateText(getString(R.string.player_message));
            }

            ((BaseAdapter) mBoardGrid.getAdapter()).notifyDataSetChanged();
        }
    }

}
