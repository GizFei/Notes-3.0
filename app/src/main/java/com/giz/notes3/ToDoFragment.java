package com.giz.notes3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import toDoDatebase.ToDoDatabase;
import toDoDatebase.ToDoItem;
import utils.ArcMenu;
import utils.InstructionDialog;
import utils.ToDoAdapter;

public class ToDoFragment extends Fragment {

    private ListView mListView;
    private ToDoAdapter mAdapter;
    private Context mContext;
    private ArcMenu mArcMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_todo, container, false);

        mListView = (ListView)view.findViewById(R.id.tab_todo_lv);
        mArcMenu = (ArcMenu)view.findViewById(R.id.tab_todo_arcMenu);

        updateUI();

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(mArcMenu.isOpen())
                    mArcMenu.toggleMenu(300);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

            }
        });

        mArcMenu.setMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                if(view.getTag().equals("New")){
                    View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_new_thing,
                            null);
                    ((TextView)dialogView.findViewById(R.id.dialog_title)).setText(getResources().
                            getString(R.string.newToDoTitle));
                    final EditText et = dialogView.findViewById(R.id.dialog_editText);
                    //final EditText et = new EditText(mContext);
                    new AlertDialog.Builder(mContext)
                            .setView(dialogView)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ToDoItem toDoItem = new ToDoItem();
                                    toDoItem.setThing(et.getText().toString());
                                    ToDoDatabase.get(mContext).addItem(toDoItem);
                                    mAdapter.setDeleteIconGone();
                                    mAdapter.addItem(toDoItem);
                                }
                            }).show();
                }else if(view.getTag().equals("Delete")){
                    if(mAdapter.isIconVisible())
                        mAdapter.setDeleteIconGone();
                    else 
                        mAdapter.setDeleteIconVisible();
                }
            }
        });

        return view;
    }

    public void foldArcMenu(){
        if(mArcMenu.isOpen())
            mArcMenu.fold();
    }

    private void updateUI() {
        if(mAdapter == null){
            mAdapter = new ToDoAdapter(getActivity(), ToDoDatabase.get(getActivity()));
            mListView.setAdapter(mAdapter);
        }else{
            mListView.setAdapter(mAdapter);
        }
    }
}
