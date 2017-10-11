package com.time.time;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guoweijie on 2017/10/11.
 */

public class NoteEdit extends AppCompatActivity {
    public static String last_content;
    private SQLiteDatabase dbread;
    public static int id;
    private NoteDB DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_edit);

        TextView tv_date =(TextView) findViewById(R.id.tv_date);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Date date = new Date();//获取时间
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置格式
        String dateString = time.format(date);//对时间进行格式转换
        tv_date.setText(dateString);//设置Text为时间
        //数据库实例
        final String name = "noteDatabase";
        NoteDB DB = new NoteDB(this, name, null, 2);
        dbread = DB.getReadableDatabase();

        //日记文本内容
        final EditText et_title   =(EditText) findViewById(R.id.et_title);
        final EditText et_content =(EditText) findViewById(R.id.et_content);


        //设置软键盘显示模式----总是可见
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //由上个活动的单击事件跳转的修改日记界面
        Intent intent1=getIntent();
        int id = intent1.getIntExtra("id",0);
        if (id!=0){
            Cursor cursor =dbread.query("note",null,null,null,null,null,null);
            while(cursor.moveToNext()){
                if( id == cursor.getInt((cursor.getColumnIndex("_id")))) {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    String _date = cursor.getString(cursor.getColumnIndex("date"));

                    et_title.setText(title);
                    et_content.setText(content);
                    tv_date.setText(_date);

                }

            }

        }
        //按钮
        ImageButton button_ok = (ImageButton)findViewById(R.id.btn_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql;
                //标题内容和正文内容
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                //获取时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNum = sdf.format(date);
                Intent intent1 = getIntent();
                int id = intent1.getIntExtra("id", 0);
                if (id == 0) {
                    int count = (int) (Math.random() * 10000) + 1;

                    Log.d("COUNT=", count + "");
                    if (!content.equals("")) {
                        sql = "insert into "
                                + NoteDB.TABLE_NAME_NOTES
                                + " values"
                                + "("
                                + count + ","
                                + "'" + title + "'" + ","
                                + "'" + content + "'" + ","
                                + "'" + dateNum + "')";
                        Log.d("LOG", sql);
                        dbread.execSQL(sql);
                    }
                } else {
                    if (!content.equals("")) {
                        sql = "update "
                                + NoteDB.TABLE_NAME_NOTES
                                + " set content ="
                                + "'" + content + "'" + ","
                                + "title = "
                                + "'" + title + "'" + ","
                                + "date = "
                                + "'" + dateNum + "'"
                                + "  where _id = "
                                + id;
                        Log.d("LOG", sql);
                        dbread.execSQL(sql);

                    }


                }
                finish();
            }
        });
        String content=et_content.getText().toString();
        //setValueAndSelection(et_content,content);

        ImageButton button_cancel =(ImageButton)findViewById(R.id.btn_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        /*String name = "note";
        NoteDB DB = new NoteDB(NoteEdit.this, name, null, 1);
        SQLiteDatabase dread = DB.getReadableDatabase();*/


        // Bundle bundle = this.getIntent().getExtras();//得到上一个活动的数据
        // last_content = bundle.getString("info");


    }
    public static void setValueAndSelection(EditText editText,String value){
        String tempValue = value == null ? "" : value;
        editText.setText(tempValue);
        editText.setSelection(tempValue.length());
    };
}
