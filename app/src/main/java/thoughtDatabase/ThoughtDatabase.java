package thoughtDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ThoughtDatabase {

    private static ThoughtDatabase sThoughtDatabase;

    private SQLiteDatabase mDatabase;

    public static ThoughtDatabase get(Context context){
        if(sThoughtDatabase == null){
            sThoughtDatabase = new ThoughtDatabase(context);
        }
        return  sThoughtDatabase;
    }

    private ThoughtDatabase(Context context){
        Context mContext = context.getApplicationContext();
        mDatabase = new ThoughtDatabaseHelper(mContext).getWritableDatabase();
    }

    public List<Thought> getThoughts(){
        List<Thought> thoughts = new ArrayList<>();
        Cursor cursor = mDatabase.query(ThoughtDbSchema.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            thoughts.add(getThought(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return thoughts;
    }

    public void addThought(Thought thought){
        ContentValues values = getContentValues(thought);
        mDatabase.insert(ThoughtDbSchema.TABLE_NAME, null, values);
    }

    public void updateThought(Thought thought){
        mDatabase.update(ThoughtDbSchema.TABLE_NAME, getContentValues(thought),
                ThoughtDbSchema.Cols.UUID + "=?",
                new String[]{thought.getUUID().toString()});
    }

    public void deleteThought(Thought thought){
        mDatabase.delete(ThoughtDbSchema.TABLE_NAME, ThoughtDbSchema.Cols.UUID + "=?",
                new String[]{thought.getUUID().toString()});
    }

    public Thought queryThought(String uuid){
        Cursor cursor = mDatabase.query(ThoughtDbSchema.TABLE_NAME,
                null,
                ThoughtDbSchema.Cols.UUID + "=?",
                new String[]{uuid},
                null,
                null,
                null);
        cursor.moveToFirst();
        return getThought(cursor);
    }

    private Thought getThought(Cursor cursor){
        String uuid = cursor.getString(cursor.getColumnIndex(ThoughtDbSchema.Cols.UUID));
        String title = cursor.getString(cursor.getColumnIndex(ThoughtDbSchema.Cols.TITLE));
        String content = cursor.getString(cursor.getColumnIndex(ThoughtDbSchema.Cols.CONTENT));

        Thought thought = new Thought(uuid);
        thought.setTitle(title);
        thought.setContent(content);

        return thought;
    }

    private ContentValues getContentValues(Thought thought){
        ContentValues values = new ContentValues();

        values.put(ThoughtDbSchema.Cols.UUID, thought.getUUID().toString());
        values.put(ThoughtDbSchema.Cols.TITLE, thought.getTitle());
        values.put(ThoughtDbSchema.Cols.CONTENT, thought.getContent());

        return values;
    }

}
