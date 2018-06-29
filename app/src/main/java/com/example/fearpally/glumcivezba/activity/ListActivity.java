package com.example.fearpally.glumcivezba.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;
import android.widget.Toolbar;


import com.example.fearpally.glumcivezba.R;
import com.example.fearpally.glumcivezba.db.ORMLightHelper;
import com.example.fearpally.glumcivezba.db.model.Actor;
import com.example.fearpally.glumcivezba.dialog.AboutDialog;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ORMLightHelper dataBaseHelper;
    private SharedPreferences sharedPref;

    public static String ACTOR_KEY ="ACTOR_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_status";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.actor_list);

        try {
            List<Actor> list = getDatabaseHelper().getmActorDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(ListActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Actor p = (Actor) listView.getItemAtPosition(position);

                    Intent intent = new Intent(ListActivity.this, Detail.class);
                    intent.putExtra(ACTOR_KEY, p.getMid());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }


//    ..84
    }
    //Metoda koja komunicira sa bazom podataka
    public ORMLightHelper getDatabaseHelper() {
        if (dataBaseHelper == null) {
            dataBaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return dataBaseHelper;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dataBaseHelper != null){
            OpenHelperManager.releaseHelper();
            dataBaseHelper = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }
    private void refresh(){

        ListView listView = (ListView) findViewById(R.id.actor_list);

        if(listView != null){
            ArrayAdapter<Actor> adapter = (ArrayAdapter<Actor>) listView.getAdapter();

            if(adapter != null){
                adapter.clear();
                List<Actor> list = null;
                try {
                    list = getDatabaseHelper().getmActorDao().queryForAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                adapter.addAll(list);
                adapter.notifyDataSetChanged();
            }

        }
    }

    private void showNotificationMesage(String message){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_name);
        builder.setContentTitle("Glumci vezba");
        builder.setContentText(message);

        Bitmap bitmapIcone =BitmapFactory.decodeResource(getResources(),R.drawable.ic_action_add);
        builder.setLargeIcon(bitmapIcone);
        notificationManager.notify(1,builder.build());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.add_actor:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_actor_layout);

                Button add = (Button)dialog.findViewById(R.id.add_actor);
                add.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        EditText name = (EditText) dialog.findViewById(R.id.actor_name);
                        EditText bio = (EditText) dialog.findViewById(R.id.actor_biography);
                        RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.actor_rating);
                        EditText birth = (EditText) dialog.findViewById(R.id.actor_birth);

                        Actor glumac = new Actor();
                        glumac.setmName(name.getText().toString());
                        glumac.setmBiography(bio.getText().toString());
                        glumac.setmBirth(birth.getText().toString());
                        glumac.setmScore(ratingBar.getRating());


                        try {
                            getDatabaseHelper().getmActorDao().create(glumac);

                            boolean toast = sharedPref.getBoolean(NOTIF_TOAST,false);
                            boolean status = sharedPref.getBoolean(NOTIF_STATUS,false);

                            if(toast){
                                Toast.makeText(ListActivity.this,"Dodat novi glumac",Toast.LENGTH_LONG).show();

                            }
                            if(status){
                                showNotificationMesage("Dodat novi glumac");
                            }
                            refresh();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });

                dialog.show();
                break;
            case R.id.about:

                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;
            case R.id.preferences:
                startActivity(new Intent(ListActivity.this, Preference.class));
                break;
        }
        return  super.onOptionsItemSelected(item);

    }
}
