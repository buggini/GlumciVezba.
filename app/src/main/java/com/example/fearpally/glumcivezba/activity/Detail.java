package com.example.fearpally.glumcivezba.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.fearpally.glumcivezba.R;
import com.example.fearpally.glumcivezba.db.ORMLightHelper;
import com.example.fearpally.glumcivezba.db.model.Actor;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

public class Detail extends AppCompatActivity {


    private ORMLightHelper dataBaseHelper;
    private SharedPreferences sharedPreferences;
    private Actor glumac;

    private EditText name;
    private EditText biography;
    private EditText birthday;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar !=null){
            setSupportActionBar(toolbar);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int kljuc = getIntent().getExtras().getInt(ListActivity.ACTOR_KEY);

        try {
            glumac = getDatabaseHelper().getmActorDao().queryForId(kljuc);

            name = (EditText) findViewById(R.id.actor_name);
            biography = (EditText) findViewById(R.id.actor_biography);
            birthday = (EditText) findViewById(R.id.actor_birth);
            ratingBar = (RatingBar)findViewById(R.id.actor_rating);

            name.setText(glumac.getmName());
            biography.setText(glumac.getmBiography());
            birthday.setText(glumac.getmBirth());
            ratingBar.setRating(glumac.getmScore());
        }catch (SQLException e){
            e.printStackTrace();
        }



    }

    //Metoda koja komunicira sa bazom podataka
    public ORMLightHelper getDatabaseHelper() {
        if (dataBaseHelper == null) {
            dataBaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return dataBaseHelper;
    }
}
