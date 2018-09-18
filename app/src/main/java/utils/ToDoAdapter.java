package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.giz.notes3.R;

import java.util.List;

import toDoDatebase.ToDoDatabase;
import toDoDatebase.ToDoItem;

public class ToDoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ToDoItem> mList;
    private ToDoDatabase mToDoDatabase;

    private boolean iconVisible = false;

    public ToDoAdapter(Context context, ToDoDatabase toDoDatabase){
        mToDoDatabase = toDoDatabase;
        mList = mToDoDatabase.getToDoItems();
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public boolean isIconVisible(){
        return iconVisible;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.tab_todo_item, null);
            viewHolder.mTextView = view.findViewById(R.id.tab_todo_item_tv);
            viewHolder.mDeleteImageView = view.findViewById(R.id.tab_todo_item_iv);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.mTextView.setText(mList.get(pos).getThing());

        viewHolder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final ToDoItem item = mList.get(pos);
                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.tab_todo_edit,
                        null);
                final EditText et = dialogView.findViewById(R.id.tab_todo_editText);
                et.setText(item.getThing());
                //final EditText et = new EditText(mContext);
                new AlertDialog.Builder(mContext)
                        .setView(dialogView)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                item.setThing(et.getText().toString());
                                ToDoDatabase.get(mContext).updateItem(item);
                                notifyDataSetChanged();
                            }
                        }).show();
                return true;
            }
        });
        //viewHolder.mDeleteImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.remove));
        if(mList.get(pos).isIconVisible()){
            viewHolder.mDeleteImageView.setVisibility(View.VISIBLE);
            viewHolder.mDeleteImageView.setClickable(true);
            viewHolder.mDeleteImageView.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.delete_icon_in));
        }else{
            viewHolder.mDeleteImageView.setClickable(false);
            viewHolder.mDeleteImageView.setVisibility(View.GONE);
        }
        viewHolder.mDeleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("做完了吗？")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteItem(pos);
                                setDeleteIconGone();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        return view;
    }

    public void setDeleteIconVisible(){
        iconVisible = true;
        for(ToDoItem item : mList){
            item.setIconVisible(true);
        }
        notifyDataSetChanged();
    }

    public void setDeleteIconGone(){
        iconVisible = false;
        for(ToDoItem item : mList){
            item.setIconVisible(false);
        }
        notifyDataSetChanged();
    }

    public void deleteItem(int i){
        ToDoItem item = mList.remove(i);
        mToDoDatabase.deleteItem(item);
        notifyDataSetChanged();
    }

    public void addItem(ToDoItem toDoItem){
        mList.add(toDoItem);
        notifyDataSetChanged();
    }

    private class ViewHolder{
        TextView mTextView;
        ImageView mDeleteImageView;
    }
}
