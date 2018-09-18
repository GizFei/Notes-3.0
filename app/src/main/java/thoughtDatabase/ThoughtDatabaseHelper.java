package thoughtDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ThoughtDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "thoughtDb";
    public static final int VERSION = 1;

    public ThoughtDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s(%s, %s, %s)", ThoughtDbSchema.TABLE_NAME,
                ThoughtDbSchema.Cols.UUID, ThoughtDbSchema.Cols.TITLE, ThoughtDbSchema.Cols.CONTENT);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
