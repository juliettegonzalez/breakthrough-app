package com.juliettegonzalez.breakthroughapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.juliettegonzalez.breakthroughapp.AI.MainGame;
import com.juliettegonzalez.breakthroughapp.AI.Player;
import com.juliettegonzalez.breakthroughapp.AI.SquareBoard;

import java.util.ArrayList;
import java.util.List;


public class BoardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private MainGame mGame;
    private GridView mBoardGrid;
    private List<SquareBoard> mSquareBoardList = new ArrayList<>();


    public BoardFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BoardFragment.
     */
    public static BoardFragment newInstance(String param1) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        Player player1 = new Player(false, Player.Color.BLACK, Player.PawnType.DRAGON);
        Player computer = new Player(true, Player.Color.WHITE, Player.PawnType.GRANDPA);

        mGame = new MainGame(player1, computer);
        mSquareBoardList = mGame.getBoard().matrixToList();
        SquareBoardAdapter squareBoardAdapter = new SquareBoardAdapter(getActivity(), R.layout.square, mSquareBoardList);

        mBoardGrid = (GridView) view.findViewById(R.id.board_grid);
        mBoardGrid.setAdapter(squareBoardAdapter);

        /*final LinearLayout piece = (LinearLayout) view.findViewById(R.id.piece);
        piece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piece.setBackgroundResource(R.drawable.square_shape_selected);
            }
        });*/

        return view;
    }

}
