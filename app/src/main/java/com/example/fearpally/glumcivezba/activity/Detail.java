package com.example.fearpally.glumcivezba.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fearpally.glumcivezba.R;
import com.example.fearpally.glumcivezba.db.ORMLightHelper;
import com.example.fearpally.glumcivezba.db.model.Actor;
import com.example.fearpally.glumcivezba.db.model.Movie;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import static com.example.fearpally.glumcivezba.activity.ListActivity.NOTIF_STATUS;
import static com.example.fearpally.glumcivezba.activity.ListActivity.NOTIF_TOAST;

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

//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
//
//        if(toolbar != null) {
//            setSupportActionBar(toolbar);
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int kljuc = getIntent().getExtras().getInt(ListActivity.ACTOR_KEY);

        try {
            glumac = getDatabaseHelper().getmActorDao().queryForId(kljuc);

            name = (EditText) findViewById(R.id.actor_name);
            biography = (EditText) findViewById(R.id.actor_biography);
            birthday = (EditText) findViewById(R.id.actor_birth);
            ratingBar = (RatingBar) findViewById(R.id.actor_rating);

            name.setText(glumac.getmName());
            biography.setText(glumac.getmBiography());
            birthday.setText(glumac.getmBirth());
            ratingBar.setRating(glumac.getmScore());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ListView listView =(ListView) findViewById(R.id.actor_movies);

        try {
            List<Movie> list = getDatabaseHelper().getmMovieDao().queryBuilder()
                    .where()
                    .eq(Movie.FIELD_NAME_USER, glumac.getMid())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = (Movie) listView.getItemAtPosition(position);
                    Toast.makeText(Detail.this,movie.getmName()+" "+movie.getmGenre()+ " "+ movie.getmYear() ,Toast.LENGTH_LONG).show();
                }
            });
        } catch (SQLException e) {
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

    private void refresh() {
        ListView listView = (ListView) findViewById(R.id.actor_movies);

        if (listView != null) {
            ArrayAdapter<Movie> adapter = (ArrayAdapter<Movie>) listView.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();
                    List<Movie> list = getDatabaseHelper().getmMovieDao().queryBuilder()
                            .where()
                            .eq(Movie.FIELD_NAME_USER, glumac.getMid())
                            .query();
                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showNotificationMesage(String message) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentTitle("Glumci Vezba");
        builder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        builder.setLargeIcon(bm);

        mNotificationManager.notify(1, builder.build());
    }

    private void showMessage(String message) {

        boolean toast = sharedPreferences.getBoolean(NOTIF_TOAST, false);
        boolean status = sharedPreferences.getBoolean(NOTIF_STATUS, false);

        if (toast) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        if (status) {
            showNotificationMesage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_movie:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_movie_layout);

                Button add = (Button) dialog.findViewById(R.id.add_movie_btn);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText name = (EditText) dialog.findViewById(R.id.movie_name);
                        EditText genre = (EditText) dialog.findViewById(R.id.movie_genre);
                        EditText year = (EditText) dialog.findViewById(R.id.movie_year);

                        Movie m = new Movie();
                        m.setmName(name.getText().toString().trim());
                        m.setmGenre(genre.getText().toString().trim());
                        m.setmYear(year.getText().toString().trim());
                        m.setmUser(glumac);

                        try {
                            getDatabaseHelper().getmMovieDao().create(m);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        refresh();

                        showMessage("New Movie added to actor!");

                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
            case R.id.edit:
                final Dialog dialogEdit = new Dialog(this);
                dialogEdit.setContentView(R.layout.edit_actor_layout);

                Button edit = (Button) dialogEdit.findViewById(R.id.add_actor_btn_edit);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        name = (EditText) findViewById(R.id.actor_name_edit);
                        biography = (EditText) findViewById(R.id.actor_biography_edit);
                        birthday = (EditText) findViewById(R.id.actor_birth_edit);
                        ratingBar = (RatingBar) findViewById(R.id.actor_rating_edit);

                        glumac.setmName(name.getText().toString().trim());
                        glumac.setmBiography(biography.getText().toString().trim());
                        glumac.setmBirth(birthday.getText().toString().trim());
                        glumac.setmScore(ratingBar.getRating());

                        try {
                            getDatabaseHelper().getmActorDao().update(glumac);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refresh();
                        showMessage("Actor detail updated");

                    }
                });

                Button cancel = (Button) dialogEdit.findViewById(R.id.cancel_actor_btn_edit);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogEdit.dismiss();
                    }
                });
                dialogEdit.show();

                break;
            case R.id.remove:
                    //OVTVARAMO DIALOG ZA UNOS INFORMACIJA
                final Dialog dialogRemove = new Dialog(this);
                dialogRemove.setContentView(R.layout.remove_actor_layout);

                TextView textView =(TextView) findViewById(R.id.text_dialog);

                Button deleteDialog = (Button) dialogRemove.findViewById(R.id.delete_actor_btn_dialog);
                deleteDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            getDatabaseHelper().getmActorDao().delete(glumac);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        showMessage("Actor Deleted");

                        finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                    }
                });
                Button cancelDialog = (Button) dialogRemove.findViewById(R.id.cancel_actor_btn_dialog);
                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogRemove.dismiss();

                        finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                    }
                });
                dialogRemove.show();
                break;
    }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //nakon rada sa bazom podataka potrebno je obavezno
        //oslobodidi resurse!

        if(dataBaseHelper != null){
            OpenHelperManager.releaseHelper();
            dataBaseHelper = null;
        }
    }
}

