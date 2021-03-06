package com.example.cliff.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateWorkoutAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list;
    private Context context;
    //Reference an instance of fragment, passed in through the constructor
    private CreateWorkoutFragment fragment;

    public CreateWorkoutAdapter(ArrayList<String> list, Context context, CreateWorkoutFragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.create_workout_listview, null);
        }

        //Handle TextView and display string from the list
        TextView listViewText = (TextView) view.findViewById(R.id.list_view_add_exercise);
        listViewText.setText(list.get(position));

        //Handle remove button and onClickListener
        Button removeButton = (Button) view.findViewById(R.id.remove_button);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // notify the fragment of the position of the exercise to delete
                fragment.removeExercise(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

}
