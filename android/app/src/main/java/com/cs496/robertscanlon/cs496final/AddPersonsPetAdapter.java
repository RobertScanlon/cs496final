/****************************************************************************
 * filename:        AddPersonsPetAdapter.java
 *
 * author:          Robert Scanlon
 *
 * description:     CS496 Fall 2017 Final Project
 *
 * last edit:       24 November 2017
 ****************************************************************************/

package com.cs496.robertscanlon.cs496final;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robert on 11/24/17.
 */

public class AddPersonsPetAdapter extends SimpleAdapter {

    private Context mContext;
    private ArrayList<HashMap<String,String>> dataList;

    public AddPersonsPetAdapter(Context context, ArrayList<HashMap<String,String>> data, int resource, String[] from, int[] to) {
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
        Button addPetButton = (Button) v.findViewById(R.id.addPersonsPetButton);
        addPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddPersonsPetActivity.class);
                intent.putExtra("name", dataList.get(possition).get("name"));
                intent.putExtra("species", dataList.get(possition).get("species"));
                intent.putExtra("url", dataList.get(possition).get("pet_selfUrl"));
                intent.putExtra("person_id", dataList.get(possition).get("person_id"));
                mContext.startActivity(intent);
            }
        });
        return v;
    }
}
