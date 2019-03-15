package com.example.cliff.fitnessapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WhiteTextAdapter extends ArrayAdapter<String> {

    private Context context;
    private int id;
    private List<String> list;

    public WhiteTextAdapter(Context context, int textVewResourceID, List<String> list) {

        super(context, textVewResourceID, list);
        this.context = context;
        this.id = textVewResourceID;
        this.list = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        TextView text = (TextView) v.findViewById(R.id.white_listview);

        if (list.get(position) != null) {
            text.setText(list.get(position));
        }

        return v;
    }
}
