package com.example.cliff.fitnessapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import javax.microedition.khronos.egl.EGLDisplay;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    private EditText userNameText, ageText, genderText, heightText, weightText, neckText, chestText, waistText, hipsText;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        Button b = (Button) v.findViewById(R.id.submit);
        b.setOnClickListener(this);

        userNameText = (EditText) v.findViewById(R.id.userName_edit_text);
        ageText = (EditText) v.findViewById(R.id.age_edit_text);
        genderText = (EditText) v.findViewById(R.id.gender_edit_text);
        heightText = (EditText) v.findViewById(R.id.height_edit_text);
        weightText = (EditText) v.findViewById(R.id.weight_edit_text);
        neckText = (EditText) v.findViewById(R.id.neck_edit_text);
        chestText = (EditText) v.findViewById(R.id.chest_edit_text);
        waistText = (EditText) v.findViewById(R.id.waist_edit_text);
        hipsText = (EditText) v.findViewById(R.id.hips_edit_text);


        updateEditText();

        return v;
    }

    public void onClick(View v)
    {
        int id = v.getId();

        if (id == R.id.submit)
        {
            String userName = (String)(((EditText)getView().findViewById(R.id.userName_edit_text)).getText().toString());
            int age = Integer.valueOf((((EditText)getView().findViewById(R.id.age_edit_text)).getText().toString()));
            String gender = (String)(((EditText)getView().findViewById(R.id.gender_edit_text)).getText().toString());
            String height = (String)(((EditText)getView().findViewById(R.id.height_edit_text)).getText().toString());
            String weight = (String)(((EditText)getView().findViewById(R.id.weight_edit_text)).getText().toString());
            String neck = (String)(((EditText)getView().findViewById(R.id.neck_edit_text)).getText().toString());
            String chest = (String)(((EditText)getView().findViewById(R.id.chest_edit_text)).getText().toString());
            String waist = (String)(((EditText)getView().findViewById(R.id.waist_edit_text)).getText().toString());
            String hips = (String)(((EditText)getView().findViewById(R.id.hips_edit_text)).getText().toString());

            System.out.println("AGE IS " + age);

            //pasted here
            SQLiteOpenHelper helper = new FitnessAppHelper(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues value = new ContentValues();
            value.put("NAME", userName);
            value.put("AGE", age);
            value.put("GENDER", gender);
            value.put("HEIGHT", height);
            value.put("WEIGHT", weight);
            value.put("NECK", neck);
            value.put("CHEST", chest);
            value.put("WAIST", waist);
            value.put("HIPS", hips);


           // db.insert("PROFILE", null, value);
            db.update("PROFILE", value, null, null);

            Cursor cursor = db.rawQuery("SELECT * FROM PROFILE ORDER BY _id DESC LIMIT 1;", null);
            cursor.moveToFirst();

            String databaseUserName = cursor.getString(cursor.getColumnIndex("NAME"));
            int databaseAge = cursor.getInt(cursor.getColumnIndex("AGE"));
            String databaseGender = cursor.getString(cursor.getColumnIndex("GENDER"));
            String databaseHeight = cursor.getString(cursor.getColumnIndex("HEIGHT"));
            String databaseWeight = cursor.getString(cursor.getColumnIndex("WEIGHT"));
            String databaseNeck = cursor.getString(cursor.getColumnIndex("NECK"));
            String databaseChest = cursor.getString(cursor.getColumnIndex("CHEST"));
            String databaseWaist = cursor.getString(cursor.getColumnIndex("WAIST"));
            String databaseHips = cursor.getString(cursor.getColumnIndex("HIPS"));

        }
    }

    private void updateEditText()
    {
        SQLiteOpenHelper helper = new FitnessAppHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM PROFILE ORDER BY _id DESC LIMIT 1;", null);
        cursor.moveToFirst();

        String databaseUserName = cursor.getString(cursor.getColumnIndex("NAME"));
        int databaseAge = cursor.getInt(cursor.getColumnIndex("AGE"));
        String databaseGender = cursor.getString(cursor.getColumnIndex("GENDER"));
        String databaseHeight = cursor.getString(cursor.getColumnIndex("HEIGHT"));
        String databaseWeight = cursor.getString(cursor.getColumnIndex("WEIGHT"));
        String databaseNeck = cursor.getString(cursor.getColumnIndex("NECK"));
        String databaseChest = cursor.getString(cursor.getColumnIndex("CHEST"));
        String databaseWaist = cursor.getString(cursor.getColumnIndex("WAIST"));
        String databaseHips = cursor.getString(cursor.getColumnIndex("HIPS"));

        userNameText.setText((databaseUserName));
        ageText.setText(Integer.toString(databaseAge));
        genderText.setText(databaseGender);
        heightText.setText(databaseHeight);
        weightText.setText(databaseWeight);
        neckText.setText(databaseNeck);
        chestText.setText(databaseChest);
        waistText.setText(databaseWaist);
        hipsText.setText(databaseHips);

    }

}
