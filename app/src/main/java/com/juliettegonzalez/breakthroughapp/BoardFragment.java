package com.juliettegonzalez.breakthroughapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class BoardFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;


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

        final LinearLayout piece = (LinearLayout) view.findViewById(R.id.piece);
        piece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piece.setBackgroundResource(R.drawable.square_shape_selected);
            }
        });


        return view;
    }

}
