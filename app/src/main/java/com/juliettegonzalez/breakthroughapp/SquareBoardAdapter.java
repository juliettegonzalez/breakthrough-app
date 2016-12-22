package com.juliettegonzalez.breakthroughapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.juliettegonzalez.breakthroughapp.AI.Player;
import com.juliettegonzalez.breakthroughapp.AI.SquareBoard;

import java.util.List;

/**
 * Created by juliettegonzalez on 18/12/2016.
 */

public class SquareBoardAdapter extends ArrayAdapter<SquareBoard> {

    Context context;
    List<SquareBoard> squareItemList;
    int layoutResID;

    public SquareBoardAdapter(Context context, int layoutResourceID, List<SquareBoard> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.squareItemList = listItems;
        this.layoutResID = layoutResourceID;
    }

    public int getCount() {
        return squareItemList.size();
    }

    public SquareBoard getItem(int position) {
        return squareItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SquareHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new SquareHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.pawnIcon = (ImageView) view.findViewById(R.id.square_pawn);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (SquareHolder) view.getTag();
        }

        SquareBoard dItem = this.squareItemList.get(position);

        if (!dItem.isFree()){
            Player.PawnType type = dItem.getOwner().getPawnType();
            switch (type) {
                case DRAGON:
                    if (dItem.getOwner().getColor() == Player.Color.WHITE){
                        drawerHolder.pawnIcon.setBackgroundResource(R.drawable.ic_dragon_pawn_white);
                    }else{
                        drawerHolder.pawnIcon.setBackgroundResource(R.drawable.ic_dragon_pawn_black);
                    }
                    break;
                case GRANDPA:
                    if (dItem.getOwner().getColor() == Player.Color.WHITE){
                        drawerHolder.pawnIcon.setBackgroundResource(R.drawable.ic_grandpa_pawn_white);
                    }else{
                        drawerHolder.pawnIcon.setBackgroundResource(R.drawable.ic_grandpa_pawn_black);
                    }
                    break;
                case KING:
                    if (dItem.getOwner().getColor() == Player.Color.WHITE){
                        drawerHolder.pawnIcon.setBackgroundResource(R.drawable.ic_king_pawn_white);
                    }else{
                        drawerHolder.pawnIcon.setBackgroundResource(R.drawable.ic_king_pawn_black);
                    }
                    break;
                case WIZARD:
                    if (dItem.getOwner().getColor() == Player.Color.WHITE){
                        drawerHolder.pawnIcon.setBackgroundResource(R.drawable.ic_wizard_pawn_white);
                    }else{
                        drawerHolder.pawnIcon.setBackgroundResource(R.drawable.ic_wizard_pawn_black);
                    }
                    break;
            }
        }else{
            drawerHolder.pawnIcon.setBackgroundResource(0);
        }

        return view;
    }


    /**
     * Holder
     */
    private static class SquareHolder {
        ImageView pawnIcon;
    }
}
