package com.example.fearpally.glumcivezba.db.model;

        import com.j256.ormlite.field.DatabaseField;
        import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Movie.TABLE_NAME_USERS)
public class Movie {

    public static final String TABLE_NAME_USERS ="movies";
    public static final String FIELD_NAME_ID = "id";
    public static final String TABLE_MOVIE_NAME ="name";
    public static final String TABLE_MOVIE_GENR ="genre";
    public static final String FIELD_NAME_YEAR = "year";
    public static final String FIELD_NAME_USER = "user";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = TABLE_MOVIE_NAME)
    private String mName;

    @DatabaseField(columnName = TABLE_MOVIE_GENR)
    private String mGenre;

    @DatabaseField(columnName = FIELD_NAME_YEAR)
    private String mYear;

    @DatabaseField(columnName = FIELD_NAME_USER, foreign = true, foreignAutoRefresh = true)
    private Actor mUser;

    public Movie(){
        //prazan konstruktor za porebe ormlite
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmGenre() {
        return mGenre;
    }

    public void setmGenre(String mGenre) {
        this.mGenre = mGenre;
    }

    public String getmYear() {
        return mYear;
    }

    public void setmYear(String mYear) {
        this.mYear = mYear;
    }

    public Actor getmUser() {
        return mUser;
    }

    public void setmUser(Actor mUser) {
        this.mUser = mUser;
    }
    @Override
    public String toString(){
        return mName;
    }
}
