package com.time.time;

/**
 * Created by guoweijie on 2017/10/11.
 */

public class Note {
    private String title;
    private int imageId;
    private String date;
    private String content;
    private  int id;

    public Note (int imageId,int id,String title,String content, String date){
        this.title=title;
        this.imageId=imageId;
        this.date=date;
        this.content=content;
        this.id=id;
    }

    public String getTitle(){
        return  title;
    }
    public int getImageId(){
        return imageId;
    }
    public String getDate(){
        return  date;
    }
    public  String getContent(){
        return  content;
    }
    public  int getId()
    {return  id;}
}