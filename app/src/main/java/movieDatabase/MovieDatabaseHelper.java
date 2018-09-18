package movieDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import movieDatabase.MovieDbSchema.Cols;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieInfo";
    private static final int VERSION = 1;

    public MovieDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s(%s, %s, %s, %s)", MovieDbSchema.TABLE_NAME,
                Cols.UUID, Cols.NAME, Cols.SEEN, Cols.COMMENT);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
