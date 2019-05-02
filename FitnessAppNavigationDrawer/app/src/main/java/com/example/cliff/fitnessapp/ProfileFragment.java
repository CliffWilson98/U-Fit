package com.example.cliff.fitnessapp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


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
            Database db = Database.getDatabase();

            DatabaseValues value = db.newValues();
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

            QueryResult queryResult = db.query("SELECT * FROM PROFILE ORDER BY _id DESC LIMIT 1;");
            queryResult.moveToFirst();

            String databaseUserName = queryResult.getString("NAME");
            int databaseAge = queryResult.getInt("AGE");
            String databaseGender = queryResult.getString("GENDER");
            String databaseHeight = queryResult.getString("HEIGHT");
            String databaseWeight = queryResult.getString("WEIGHT");
            String databaseNeck = queryResult.getString("NECK");
            String databaseChest = queryResult.getString("CHEST");
            String databaseWaist = queryResult.getString("WAIST");
            String databaseHips = queryResult.getString("HIPS");

        }
    }

    private void updateEditText()
    {
        Database db = Database.getDatabase();

        QueryResult queryResult = db.query("SELECT * FROM PROFILE ORDER BY _id DESC LIMIT 1;");
        queryResult.moveToFirst();

        String databaseUserName = queryResult.getString("NAME");
        int databaseAge = queryResult.getInt("AGE");
        String databaseGender = queryResult.getString("GENDER");
        String databaseHeight = queryResult.getString("HEIGHT");
        String databaseWeight = queryResult.getString("WEIGHT");
        String databaseNeck = queryResult.getString("NECK");
        String databaseChest = queryResult.getString("CHEST");
        String databaseWaist = queryResult.getString("WAIST");
        String databaseHips = queryResult.getString("HIPS");

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
