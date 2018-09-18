package toDoDatebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ToDoDatabase {

    private static ToDoDatabase sToDoDatabase;
    private Context mContext;

    private SQLiteDatabase mDatabase;

    public static ToDoDatabase get(Context context) {
        if(sToDoDatabase == null){
            sToDoDatabase = new ToDoDatabase(context);
        }
        return sToDoDatabase;
    }

    private ToDoDatabase(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ToDoDatabaseHelper(mContext).getWritableDatabase();
    }

    public List<ToDoItem> getToDoItems(){
        Cursor cursor = mDatabase.query(ToDoDbSchema.ToDoTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        List<ToDoItem> items = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ToDoItem item = new ToDoItem(cursor.getString(cursor.getColumnIndex(ToDoDbSchema.ToDoTable.Cols.UUID)));
            item.setThing(cursor.getString(cursor.getColumnIndex(ToDoDbSchema.ToDoTable.Cols.THING)));
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    public void addItem(ToDoItem item){
        ContentValues values = getContentValues(item);

        mDatabase.insert(ToDoDbSchema.ToDoTable.TABLE_NAME, null, values);
    }

    public void deleteItem(ToDoItem item){
        String uuid = item.getUUID().toString();
        mDatabase.delete(ToDoDbSchema.ToDoTable.TABLE_NAME,
                ToDoDbSchema.ToDoTable.Cols.UUID + "=?", new String[]{uuid});
    }

    @NonNull
    private ContentValues getContentValues(ToDoItem item) {
        ContentValues values = new ContentValues();

        values.put(ToDoDbSchema.ToDoTable.Cols.UUID, item.getUUID().toString());
        values.put(ToDoDbSchema.ToDoTable.Cols.THING, item.getThing());
        return values;
    }

    public void updateItem(ToDoItem item){
        String uuid = item.getUUID().toString();

        ContentValues values = getContentValues(item);
        mDatabase.update(ToDoDbSchema.ToDoTable.TABLE_NAME, values, ToDoDbSchema.ToDoTable.Cols.UUID +
                "=?", new String[]{uuid});
    }

}
