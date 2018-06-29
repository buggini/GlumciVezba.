package com.example.fearpally.glumcivezba.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.fearpally.glumcivezba.db.model.Actor;
import com.example.fearpally.glumcivezba.db.model.Movie;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ORMLightHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "glumci.dp";
    private static final int DATABASE_VERSION = 1;

    private Dao<Movie, Integer> mMovieDao = null;
    private Dao<Actor, Integer> mActorDao = null;

    public ORMLightHelper(Context context){
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try{
            TableUtils.createTable(connectionSource, Movie.class);
            TableUtils.createTable(connectionSource, Actor.class);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try{
            TableUtils.dropTable(connectionSource, Movie.class, true);
            TableUtils.dropTable(connectionSource, Actor.class, true);
        }catch (java.sql.SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Dao<Movie, Integer> getmMovieDao() {
        if(mMovieDao == null){
            try {
                mMovieDao = getDao(Movie.class);
            } catch (java.sql.SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return mMovieDao;
    }

    public Dao<Actor, Integer> getmActorDao() throws java.sql.SQLException {
        if (mActorDao == null){
            mActorDao = getDao(Actor.class);
        }
        return mActorDao;
    }
    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        mMovieDao = null;
        mActorDao = null;

        super.close();
    }
}
