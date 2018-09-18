package toDoDatebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import toDoDatebase.ToDoDbSchema.ToDoTable;
import toDoDatebase.ToDoDbSchema.ToDoTable.Cols;

public class ToDoDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tododatabase";
    private static final int VERSION = 1;

    public ToDoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = String.format("create table %s(%s, %s)", ToDoTable.TABLE_NAME, Cols.UUID, Cols.THING);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
