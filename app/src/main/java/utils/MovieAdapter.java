package utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.giz.notes3.MovieDialog;
import com.giz.notes3.R;

import java.util.List;

import movieDatabase.MovieDatabase;
import movieDatabase.MovieItem;

/**
 * Created by Giz on 2018/8/17.
 */
public class MovieAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MovieItem> mList;
    private Context mContext;

    public MovieAdapter(Context context, List<MovieItem> list){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.tab_movie_item, null);
            viewHolder.mTextView = (TextView)convertView.findViewById(R.id.item_movie_tv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.mTextView.setText(mList.get(position).getName());
        viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDialog movieDialog = new MovieDialog(mContext, mList.get(position));
                movieDialog.show();
            }
        });
        viewHolder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("确定删除该条电影记录吗？")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MovieDatabase.get(mContext).deleteMovieItem(mList.get(position));
                                mList.remove(position);
                                MovieAdapter.this.notifyDataSetChanged();
                            }
                        }).show();
                return true;
            }
        });

        return convertView;
    }

    private class ViewHolder{
        TextView mTextView;
    }

    public void addItem(MovieItem item){
        mList.add(item);
        notifyDataSetChanged();
    }
}
