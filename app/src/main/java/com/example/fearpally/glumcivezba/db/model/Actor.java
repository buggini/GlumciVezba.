package com.example.fearpally.glumcivezba.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Actor.TABLE_NAME_USERS)
public class Actor {

    public static final String TABLE_NAME_USERS = "actor";
    public static final String FIELD_NAME_ID = "id";
    public static final String TABLE_MOVIE_NAME = "name";
    public static final String TABLE_MOVIE_BIOGRAPHY = "biography";
    public static final String TABLE_MOVIE_SCORE = "score";
    public static final String TABLE_MOVIE_BIRTH = "birth";
    public static final String TABLE_MOVIE_MOVIES = "movies";

    @DatabaseField(columnName = FIELD_NAME_ID,generatedId = true)
    private int mid;

    @DatabaseField(columnName = TABLE_MOVIE_NAME)
    private String mName;

    @DatabaseField(columnName = TABLE_MOVIE_BIOGRAPHY)
    private String mBiography;

    @DatabaseField(columnName = TABLE_MOVIE_SCORE)
    private Float mScore;

    @DatabaseField(columnName = TABLE_MOVIE_BIRTH)
    private String mBirth;

    @ForeignCollectionField(columnName = Actor.TABLE_MOVIE_MOVIES, eager = true)
    private ForeignCollection<Movie> movies;

    public Actor(){

    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmBiography() {
        return mBiography;
    }

    public void setmBiography(String mBiography) {
        this.mBiography = mBiography;
    }

    public Float getmScore() {
        return mScore;
    }

    public void setmScore(Float mScore) {
        this.mScore = mScore;
    }

    public String getmBirth() {
        return mBirth;
    }

    public void setmBirth(String mBirth) {
        this.mBirth = mBirth;
    }

    public ForeignCollection<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ForeignCollection<Movie> movies) {
        this.movies = movies;
    }
    @Override
    public String toString(){
        return mName;
    }
}
