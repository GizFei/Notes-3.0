package com.giz.notes3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import utils.ColorPopupWindow;
import utils.InstructionDialog;
import utils.Theme;

public class MainActivity extends AppCompatActivity {

    private static final String THEME_COLOR_KEY = "theme_color_key";
    private static final String TO_DO_FRAGMENT = "toDoFragment";
    private static final String THOUGHT_FRAGMENT = "thoughtFragment";
    private static final String MOVIE_FRAGMENT = "movieFragment";
    private static final String PREFERENCE_KEY = "key_name";
    private static final String CURRENT_FRAGMENT = "currentFragment";

    private ToDoFragment mToDoFragment;
    private ThoughtFragment mThoughtFragment;
    private MovieFragment mMovieFragment;
    private ConstraintLayout mContainerLayout;
    private View mMaskView;

    private int currentFragment = 1;
    private int resumeFragment;  // 从onSaveInstanceState中恢复时的保存变量
    private int mThemeColor = Theme.THEME_COLOR_DEFAULT;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todo:
                    setCurrentFragment(1);
                    return true;
                case R.id.navigation_thought:
                    setCurrentFragment(2);
                    if(mToDoFragment != null)
                        mToDoFragment.foldArcMenu();
                    return true;
                case R.id.navigation_movie:
                    setCurrentFragment(3);
                    if(mToDoFragment != null)
                        mToDoFragment.foldArcMenu();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_instruction:
                InstructionDialog id = new InstructionDialog(this, R.layout.app_instruction);
                ((TextView)id.findViewById(R.id.instruction_tv)).
                        setMovementMethod(ScrollingMovementMethod.getInstance());
                id.show();
                return true;
            case R.id.menu_change_color:
                final ColorPopupWindow cpw = new ColorPopupWindow(this, mThemeColor);
                PopupWindow pw = cpw.getPopupWindow();
                mMaskView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.mask_in));
                mMaskView.setVisibility(View.VISIBLE);
                pw.showAtLocation(mContainerLayout, Gravity.BOTTOM, 0, 0);
                pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        int selectedColor = cpw.getSelectedColorId();
                        mMaskView.setAnimation(AnimationUtils.loadAnimation(MainActivity.this,
                                R.anim.mask_out));
                        mMaskView.setVisibility(View.GONE);
                        if(mThemeColor != selectedColor){
                            mThemeColor = selectedColor;
                            SharedPreferences preferences = MainActivity.this
                                    .getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(PREFERENCE_KEY, mThemeColor);
                            editor.apply();
                            recreate();
                        }
                    }
                });
                return true;
            case R.id.menu_default_color:
                mThemeColor = Theme.THEME_COLOR_DEFAULT;
                SharedPreferences preferences1 = this.getSharedPreferences(PREFERENCE_KEY,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putInt(PREFERENCE_KEY, mThemeColor);
                editor1.apply();
                recreate();
                return true;
            case R.id.menu_update_log:
                InstructionDialog id1 = new InstructionDialog(this, R.layout.update_log);
                ((TextView)id1.findViewById(R.id.update_log_tv)).
                        setMovementMethod(ScrollingMovementMethod.getInstance());
                id1.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGMENT, currentFragment);
        // 保存Fragment
        if(mToDoFragment != null){
            getSupportFragmentManager().putFragment(outState, TO_DO_FRAGMENT, mToDoFragment);
        }
        if(mThoughtFragment != null){
            getSupportFragmentManager().putFragment(outState, THOUGHT_FRAGMENT, mThoughtFragment);
        }
        if(mMovieFragment != null){
            getSupportFragmentManager().putFragment(outState, MOVIE_FRAGMENT, mMovieFragment);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = this.getSharedPreferences(PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        mThemeColor = preferences.getInt(PREFERENCE_KEY, Theme.THEME_COLOR_DEFAULT);
        setNotesTheme(mThemeColor);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContainerLayout = findViewById(R.id.container);
        mMaskView = findViewById(R.id.mask);

        // !要在super.OnCreate(savedInstanceState)之后调用。
        if(savedInstanceState != null){
            FragmentManager fm = getSupportFragmentManager();
            mToDoFragment = (ToDoFragment)fm.getFragment(savedInstanceState, TO_DO_FRAGMENT);
            mThoughtFragment = (ThoughtFragment)fm.getFragment(savedInstanceState, THOUGHT_FRAGMENT);
            mMovieFragment = (MovieFragment)fm.getFragment(savedInstanceState, MOVIE_FRAGMENT);

            resumeFragment = savedInstanceState.getInt(CURRENT_FRAGMENT);
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(getIntent().hasExtra("MOVETOTWO")){
            navigation.setSelectedItemId(R.id.navigation_thought);
        }else if(getIntent().hasExtra("MOVETOTHREE")){
            navigation.setSelectedItemId(R.id.navigation_movie);
        }else{
            navigation.setSelectedItemId(R.id.navigation_todo);
        }
        setCurrentFragment(resumeFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrentFragment(currentFragment);
    }

    private void setNotesTheme(int themeId){
        switch (themeId){
            case Theme.THEME_COLOR_DEFAULT:
                setTheme(R.style.AppTheme);
                break;
            case Theme.THEME_COLOR_BLUE:
                setTheme(R.style.AppBlueTheme);
                break;
            case Theme.THEME_COLOR_PINK:
                setTheme(R.style.AppPinkTheme);
                break;
            case Theme.THEME_COLOR_RED:
                setTheme(R.style.AppRedTheme);
                break;
            case Theme.THEME_COLOR_GRAY:
                setTheme(R.style.AppGrayTheme);
                break;
            case Theme.THEME_COLOR_YELLOW:
                setTheme(R.style.AppYellowTheme);
                break;
            case Theme.THEME_COLOR_ORANGE:
                setTheme(R.style.AppOrangeTheme);
                break;
            case Theme.THEME_COLOR_GREEN:
                setTheme(R.style.AppGreenTheme);
                break;
            case Theme.THEME_COLOR_PURPLE:
                setTheme(R.style.AppPurpleTheme);
                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }
    }

    private void setCurrentFragment(int index){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case 1:
                currentFragment = 1;
               if(mToDoFragment == null){
                    mToDoFragment = new ToDoFragment();
                    transaction.add(R.id.fragment_container, mToDoFragment);
                }else{
                    transaction.show(mToDoFragment);
                }
                break;
            case 2:
                currentFragment = 2;
                if(mThoughtFragment == null){
                    mThoughtFragment = new ThoughtFragment();
                    transaction.add(R.id.fragment_container, mThoughtFragment);
                }else{
                    transaction.show(mThoughtFragment);
                }
                break;
            case 3:
                currentFragment = 3;
                if(mMovieFragment == null){
                    mMovieFragment = new MovieFragment();
                    transaction.add(R.id.fragment_container, mMovieFragment);
                }else{
                    transaction.show(mMovieFragment);
                }
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if(mToDoFragment != null){
            transaction.hide(mToDoFragment);
        }
        if(mThoughtFragment != null){
            transaction.hide(mThoughtFragment);
        }
        if(mMovieFragment != null){
            transaction.hide(mMovieFragment);
        }
    }

}
