package com.giz.notes3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import movieDatabase.MovieDatabase;
import movieDatabase.MovieItem;
import utils.MovieAdapter;

public class MovieFragment extends Fragment {

    private ListView mMovieLv;
    private ImageButton mButton;
    private MovieAdapter mAdapter;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_movie, container, false);

        mMovieLv = (ListView)view.findViewById(R.id.tab_movie_lv);
        mButton = (ImageButton) view.findViewById(R.id.tab_movie_btn);

        updateUI();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_new_thing,
                        null);
                ((TextView)dialogView.findViewById(R.id.dialog_title)).setText("输入电影名");
                final EditText et = dialogView.findViewById(R.id.dialog_editText);
                new AlertDialog.Builder(mContext)
                        .setView(dialogView)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MovieItem movieItem = new MovieItem();
                                movieItem.setName(et.getText().toString());
                                mAdapter.addItem(movieItem);
                                MovieDatabase.get(mContext).addMovieItem(movieItem);
                            }
                        }).show();
            }
        });

        return view;
    }

    private void updateUI() {
        if(mAdapter == null){
            mAdapter = new MovieAdapter(mContext, MovieDatabase.get(mContext).getMovieItems());
            mMovieLv.setAdapter(mAdapter);
        }else{
            mMovieLv.setAdapter(mAdapter);
        }
    }
}
