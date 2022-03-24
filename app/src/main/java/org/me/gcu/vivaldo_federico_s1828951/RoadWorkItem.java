package org.me.gcu.vivaldo_federico_s1828951;

import android.util.Log;

import java.util.Arrays;

public class RoadWorkItem {
    private String title;
    private String description;
    private String link;
    private String point;
    private String author;
    private String comments;
    private String pubDate;

    private String startDate;
    private String endDate;
    private String works;
    private String management;



    public RoadWorkItem(){
        this.title="";
        this.description="";
        this.link="";
        this.point="";
        this.author="";
        this.comments="";
        this.pubDate="";
    }

    public RoadWorkItem(String title,String desc, String link,String point, String author,String comments, String pub){
        this.title=title;
        this.description=desc;
        this.link=link;
        this.point=point;
        this.author=author;
        this.comments=comments;
        this.pubDate=pub;
    }


    private void parseDesc(){
        String noBR = this.description.replaceAll("(<br />)+", "+");
        String[] split = noBR.trim().split("\\+");
      // Log.e("nobr->",Integer.toString(split.length));
        if(split.length>1&&split.length <= 3){

             setStartDate(split[0]);
             setEndDate(split[1]);


        }


    }





    public void setTitle(String title){
        this.title=title;
    }
    public void setDesc(String desc){
        this.description=desc;
        parseDesc();
    }
    public void setLink(String link){
        this.link=link;
    }
    public void setPoint(String point){
        this.point=point;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public void setComments(String comm){
        this.comments=comm;
    }
    public void setPubDate(String pub){
        this.pubDate=pub;
    }
    public void setStartDate(String s){
        this.startDate=s;
    }
    public void setEndDate(String s){
        this.endDate=s;
    }

    public String getTitle(){
        return this.title;
    }
    public String getDesc(){
        return this.description;
    }
    public String getLink(){
        return this.link;
    }
    public String getPoint(){
        return this.point;
    }
    public String getAuthor(){
        return this.author;
    }
    public String getComments(){
        return this.comments;
    }
    public String getPubDate(){
        return this.pubDate;
    }
public String getStartDate(){return this.startDate;}
public String getEndDate(){return this.endDate;}
    public String getLat(){
            String[] split = this.point.trim().split(" ");
            parseDesc();
return split[0];
    }

    public String getLon(){
            String[] split = this.point.trim().split(" ");
        return split[split.length-1];
    }

    public String toString (){
        return(this.title+" - "+this.description);
    }


}
