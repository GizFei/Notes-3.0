package com.giz.notes3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import movieDatabase.MovieDatabase;
import movieDatabase.MovieItem;

public class MovieEditActivity extends AppCompatActivity {

    private MovieItem mMovieItem;
    private EditText mMovieNameEt;
    private Switch mSeenSwitch;
    private EditText mCommentText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_edit);

        String uuid = getIntent().getStringExtra("MOVIE_UUID");
        mMovieItem = MovieDatabase.get(this).queryMovieItem(uuid);

        initViews();
    }

    private void initViews() {
        mMovieNameEt = (EditText) findViewById(R.id.movie_edit_name);
        mSeenSwitch = (Switch)findViewById(R.id.movie_edit_seen);
        mCommentText = (EditText)findViewById(R.id.movie_edit_comment);
        mButton = (Button)findViewById(R.id.movie_edit_btn);

        mMovieNameEt.setText(mMovieItem.getName());

        mSeenSwitch.setChecked(mMovieItem.isHasSeen());
        mSeenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMovieItem.setHasSeen(isChecked);
            }
        });

        mCommentText.setText(mMovieItem.getComment());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovieItem.setComment(mCommentText.getText().toString());
                mMovieItem.setName(mMovieNameEt.getText().toString());
                // update the database
                MovieDatabase.get(MovieEditActivity.this).updateMovieItem(mMovieItem);
                Intent intent = new Intent(MovieEditActivity.this,
                        MainActivity.class);
                intent.putExtra("MOVETOTHREE", "YES");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MovieEditActivity.this.finish();
            }
        });
    }
}
