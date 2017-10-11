package com.time.time;

/**
 * Created by guoweijie on 2017/10/11.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
public class NoteAdapter extends ArrayAdapter<Note> {
    private int resourceId;
    public NoteAdapter(Context context, int textViewResourceId, List<Note> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position , View convertView, ViewGroup parent){
        Note note = getItem(position); //获取实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        //实例xml中的控件
        ImageView noteImage= (ImageView) view.findViewById(R.id.note_photo);
        TextView noteTitle = (TextView) view.findViewById(R.id.note_summary);
        TextView noteDate = (TextView) view.findViewById(R.id.note_date);
        TextView noteContent =(TextView) view.findViewById(R.id.note_content);

        //设置Image 和 title ,content,date
        noteImage.setImageResource(note.getImageId());
        noteTitle.setText(note.getTitle());
        noteDate.setText(note.getDate());
        noteContent.setText(note.getContent());
        return  view;

    }
}
