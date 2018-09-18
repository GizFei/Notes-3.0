package com.giz.notes3;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import movieDatabase.MovieItem;

/**
 * Created by Giz on 2018/8/17.
 */
public class MovieDialog extends Dialog {

    private int width;

    private ImageView mImageView;  // 观看状态图标
    private TextView mNameTextView; // 影片名文本
    private TextView mCommentTextView;    // 简要影评文本
    private Button mButton;        // 编辑记录按钮

    private MovieItem mMovieItem;

    public MovieDialog(Context context, MovieItem item){
        super(context);
        setContentView(R.layout.movie_details);
        mMovieItem = item;
        initViews();
    }

    private void initViews() {
        mImageView = (ImageView)findViewById(R.id.movie_details_iv);
        mNameTextView = (TextView)findViewById(R.id.movie_details_nameTv);
        mCommentTextView = (TextView)findViewById(R.id.movie_details_commentTv);
        mButton = (Button)findViewById(R.id.movie_details_btn);

        if(mMovieItem.isHasSeen()){
            mImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.seen));
        }else{
            mImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.unseen));
        }

        mNameTextView.setText("《"+mMovieItem.getName()+"》");
        mCommentTextView.setText(mMovieItem.getComment());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MovieEditActivity.class);
                intent.putExtra("MOVIE_UUID", mMovieItem.getUUID().toString());
                getContext().startActivity(intent);
            }
        });

        this.getWindow().setAttributes(getParams());
    }

    private void setSize(){
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm =  new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        if(getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE){
            width = dm.heightPixels;
        }else{
            width = dm.widthPixels;
        }
    }

    public WindowManager.LayoutParams getParams(){
        setSize();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        return params;
    }


}
