package com.time.time;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2015/8/28 0028.
 */

public class myFragment extends Fragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private List<Note> noteList = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private ListView listView;
    private SQLiteDatabase dbread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfragment, container, false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        noteAdapter = new NoteAdapter(getContext(), R.layout.note_item, noteList);
        //NoteAdapter(Context context, int textViewResourceId, List<Note> objects)

        String name = "noteDatabase";
        NoteDB DB = new NoteDB(getContext(), name, null, 2);
        dbread = DB.getReadableDatabase();

        getData();
        //装载ListView
        ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
        listView.setAdapter(noteAdapter);

        //RefreshNoteList();


        //设置增加日记按钮
        ImageButton addNote = (ImageButton) getActivity().findViewById(R.id.btn_editnote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NoteEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("info", "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
                //finish();

            }
        });


        RefreshNoteList();

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setOnScrollListener(this);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            RefreshNoteList();
        }
    }

    public void RefreshNoteList() {
        int size = noteList.size();

        if (size > 0) {

            noteAdapter.notifyDataSetChanged();
        }
//     else listView.setAdapter(noteAdapter);

    }
    //如果正确接收上一个页面返回的数据，刷新界面

    /*public void initNote() {
        for (int i = 0; i < 20; i++) {
            String numb= i+"";
            Note note1 = new Note( R.drawable.ali,numb,"wewewewewe", "2221212asasasasas");
            noteList.add(note1);
        }
    }*/

    public List<Note> getData() {
        //新建数据库
        noteList.clear();
        Cursor cursor = dbread.query("Note", null, "content!=\"\"", null, null, null, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            Log.d("date=", date);
            Note note1 = new Note(R.drawable.ali, id, title, content, date);
            noteList.add(note1);
        }
        cursor.close();
        return noteList;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        switch (i1) {
            case SCROLL_STATE_FLING:
                Log.i("main", "用户在手指离开屏幕之前，由于用力的滑了一下，视图能依靠惯性继续滑动");
            case SCROLL_STATE_IDLE:
                Log.i("main", "视图已经停止滑动");
            case SCROLL_STATE_TOUCH_SCROLL:
                Log.i("main", "手指没有离开屏幕，试图正在滑动");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        final int n = arg2;
        Note note = noteList.get(arg2);
        Intent intent = new Intent(getContext(), NoteEdit.class);
        intent.putExtra("id", note.getId());
        startActivity(intent);
        //Toast.makeText(MainActivity.this,Note.getTitle(),Toast.LENGTH_LONG).show();
        /*String content = listView.getItemAtPosition(n) + "";
        Log.i("main","position="+content);
        String content1 = content.substring(content.indexOf("=") + 1,
                content.indexOf(","));
        Log.d("CONTENT", content1);*/
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int n = i;  //adapter中的位置
        final Note note = noteList.get(n);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("删除日记");
        builder.setMessage("确认删除？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int id = note.getId();
                String sql_del = "delete from Note  where _id=" + id;
                dbread.execSQL(sql_del);
                RefreshNoteList();

            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create();
        builder.show();

        return false;
    }

}





