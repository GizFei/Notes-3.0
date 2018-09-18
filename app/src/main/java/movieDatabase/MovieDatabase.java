package movieDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieDatabase {

    private static MovieDatabase sMovieDatabase;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static MovieDatabase get(Context context) {
        if(sMovieDatabase == null){
            sMovieDatabase = new MovieDatabase(context);
        }
        return sMovieDatabase;
    }

    private MovieDatabase(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MovieDatabaseHelper(mContext).getWritableDatabase();
    }

    public List<MovieItem> getMovieItems(){
        List<MovieItem> items = new ArrayList<>();

        Cursor cursor = mDatabase.query(
                MovieDbSchema.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            items.add(getItem(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    public void addMovieItem(MovieItem movieItem){
        mDatabase.insert(MovieDbSchema.TABLE_NAME, null,
                getContentValues(movieItem));
    }

    public void updateMovieItem(MovieItem movieItem){
        mDatabase.update(MovieDbSchema.TABLE_NAME, getContentValues(movieItem),
                MovieDbSchema.Cols.UUID + "=?",
                new String[]{movieItem.getUUID().toString()});
    }

    public void deleteMovieItem(MovieItem movieItem){
        mDatabase.delete(MovieDbSchema.TABLE_NAME, MovieDbSchema.Cols.UUID + "=?",
                new String[]{movieItem.getUUID().toString()});
    }

    public MovieItem queryMovieItem(String uuid){
        Cursor cursor = mDatabase.query(MovieDbSchema.TABLE_NAME, null,
                MovieDbSchema.Cols.UUID + "=?",
                new String[]{uuid},
                null,
                null,
                null);
        cursor.moveToFirst();
        MovieItem item = getItem(cursor);
        cursor.close();

        return item;
    }

    private ContentValues getContentValues(MovieItem movieItem){
        ContentValues values = new ContentValues();

        values.put(MovieDbSchema.Cols.UUID, movieItem.getUUID().toString());
        values.put(MovieDbSchema.Cols.NAME, movieItem.getName());
        values.put(MovieDbSchema.Cols.SEEN, movieItem.isHasSeen());
        values.put(MovieDbSchema.Cols.COMMENT, movieItem.getComment());

        return values;

    }

    private MovieItem getItem(Cursor cursor){
        String uuid = cursor.getString(cursor.getColumnIndex(MovieDbSchema.Cols.UUID));
        String name = cursor.getString(cursor.getColumnIndex(MovieDbSchema.Cols.NAME));
        int hasSeen = cursor.getInt(cursor.getColumnIndex(MovieDbSchema.Cols.SEEN));
        String comment = cursor.getString(cursor.getColumnIndex(MovieDbSchema.Cols.COMMENT));

        MovieItem movieItem = new MovieItem(UUID.fromString(uuid));
        movieItem.setName(name);
        movieItem.setHasSeen(hasSeen == 1 ? true : false);
        movieItem.setComment(comment);

        return movieItem;
    }
}
