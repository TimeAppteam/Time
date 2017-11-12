package com.time.time;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guoweijie on 2017/10/11.
 */

public class NoteEdit extends AppCompatActivity {
    private SQLiteDatabase dbread;
    public static int id;
    private NoteDB DB;

    MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    SimpleDateFormat sDateFormat;
    String    date,dateString;
    EditText et_content;

    String content;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_edit);

        TextView tv_date =(TextView) findViewById(R.id.tv_date);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Date date = new Date();//获取时间
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置格式
        dateString = time.format(date);//对时间进行格式转换
        tv_date.setText(dateString);//设置Text为时间
        //数据库实例
        final String name = "noteDatabase";
        NoteDB DB = new NoteDB(this, name, null, 2);
        dbread = DB.getReadableDatabase();

        //日记文本内容
        final EditText et_title   =(EditText) findViewById(R.id.et_title);
        et_content =(EditText) findViewById(R.id.et_content);


        //设置软键盘显示模式----总是可见
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        //保存按钮
        ImageButton button_ok = (ImageButton)findViewById(R.id.btn_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql;
                //标题内容和正文内容
                String title = et_title.getText().toString();
                content = et_content.getText().toString();
                //获取时间
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateNum = sdf.format(date);
                Intent intent1 = getIntent();
                int id = intent1.getIntExtra("id", 0);
                //新建日记
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
                    //已存在日记，进行修改
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
                Intent mIntent = new Intent();
                mIntent.putExtra("saveState", "1");
                setResult(1, mIntent);
                finish();

            }
        });

        //取消保存的按钮
        ImageButton button_cancel =(ImageButton)findViewById(R.id.btn_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.putExtra("saveState", "0");
                setResult(1, mIntent);
                finish();



            }
        });



        FloatingActionButton addInf = (FloatingActionButton)findViewById(R.id.floatButton);
        addInf.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                 String inform = writeTime();


            }
        });

        dbHelper=new MyDatabaseHelper(this,"appHistory.db",null,1);
        db=dbHelper.getWritableDatabase();


    }
    public String writeTime(){
        long time=0;
        String stime="0";
        sDateFormat= new SimpleDateFormat("yyyyMMdd");
        date = sDateFormat.format(new    java.util.Date());

        String appInf ="";
        Cursor cursorTableTime=db.rawQuery("select * from time"+date,null);
        if(cursorTableTime.moveToFirst()){
            do{
                String appname = "";
                String packagename=cursorTableTime.getString(cursorTableTime.getColumnIndex("appName"));
                String usetime=cursorTableTime.getString(cursorTableTime.getColumnIndex("useTime"));
                // String usefrequency=cursor.getString(cursor.getColumnIndex("useFrequency"));
                String usefrequency=null;

                PackageManager pm = getApplicationContext().getPackageManager();
                try{
                    appname= pm.getApplicationLabel(pm.getApplicationInfo(packagename, PackageManager.GET_META_DATA)).toString();


                    Log.d("apprealname=",appInf);

                }
                catch (PackageManager.NameNotFoundException e) {
                }
                Log.d("time=",usetime);
                int usetimeInt = Integer.parseInt(usetime);

                int usetimeIntMin =usetimeInt/60;
                /*Date useTimeDate = new Date(usetimeInt);
                SimpleDateFormat format = new SimpleDateFormat("MM:ss ");
                String useTime = format.format(useTimeDate);*/
                if(!appname.contains("Android") & !appname.contains("System") & !appname.contains("Launcher") & !appname.contains("Package") ) {
                    appInf = appInf + appname + ":";

                    if (usetimeIntMin > 0) {
                        appInf = appInf + "   "+usetimeIntMin + " min" + "\n";
                    } else {
                        appInf = appInf + "   "+usetimeInt + " s" + "\n";
                    }
                    Log.d("appname", packagename);
                    Log.d("appUsetime", usetimeInt + "");
                }

            }while(cursorTableTime.moveToNext());
        }


        //手机总使用时间和频次
        Cursor cursorTbaleFre=db.rawQuery("select * from fre"+date,null);
        String fre="0";
        String totalTime="0";
        if(cursorTbaleFre.moveToFirst()){
            fre=cursorTbaleFre.getString(cursorTbaleFre.getColumnIndex("wakeFrequency"));
            totalTime = String.valueOf(Long.valueOf(cursorTbaleFre.getString(cursorTbaleFre.getColumnIndex("totalUseTime")))/60);
        }
        else{}

        Date date = new Date();//获取时间
        SimpleDateFormat timeAdd = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置格式
        String dateStringAdd = timeAdd.format(date);//对时间进行格式转换

        String addContent="截止至"+dateStringAdd+"\n"+"一共使用手机时间为"+totalTime+"分钟"+"\n"
                +"\n"+"今日手机应用使用情况为:"+"\n"
                + appInf
                + "                 By：一键记录";


        content=addContent+et_content.getText().toString();
        et_content.setText(content);
        // Log.d("time=",stime);
        Log.d("fre=",fre);
        Log.d("Totaltime1=",totalTime);
        return stime;
    };

}
