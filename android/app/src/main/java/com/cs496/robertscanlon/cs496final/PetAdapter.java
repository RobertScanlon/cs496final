package com.cs496.robertscanlon.cs496final;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by robert on 11/22/17.
 */


public class PetAdapter extends SimpleAdapter {

    private Context mContext;
    private ArrayList<HashMap<String,String>> dataList;

    public PetAdapter(Context context, ArrayList<HashMap<String,String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        dataList = data;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int pos, View convView, ViewGroup par) {
        final int possition = pos;
        View v = super.getView(pos, convView, par);
        Button deleteButton;
        deleteButton = (Button) v.findViewById(R.id.deletePetButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeletePetActivity.class);
                intent.putExtra("name", dataList.get(possition).get("name"));
                intent.putExtra("species", dataList.get(possition).get("species"));
                intent.putExtra("url", dataList.get(possition).get("pet_selfUrl"));
                mContext.startActivity(intent);
            }
        });

        return v;
    }
}
