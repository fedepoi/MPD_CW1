package org.me.gcu.vivaldo_federico_s1828951;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class RoadWorkItem {
    private String title;
    private String description;
    private String link;
    private String point;
    private String author;
    private String comments;
    private String pubDate;

    private Date startDate;
    private Date endDate;
    private String works;
    private String management;

    private String type;


    public RoadWorkItem(String type) {
        this.title = "";
        this.description = "";
        this.link = "";
        this.point = "";
        this.author = "";
        this.comments = "";
        this.pubDate = "";
        this.type = type;
    }

    public RoadWorkItem(String title, String desc, String link, String point, String author, String comments, String pub) {
        this.title = title;
        this.description = desc;
        this.link = link;
        this.point = point;
        this.author = author;
        this.comments = comments;
        this.pubDate = pub;
    }


    private void parseDesc() {

        switch (this.type) {
            case "plannedRoadWork":
                System.out.println("prw");
                String noBR = this.description.replaceAll("(<br />)+", "+");
                String[] split = noBR.trim().split("\\+");

                if (split.length > 1 && split.length <= 3) {
                    try {
                        String sd = split[0].replace("Start Date: ", "");
                        String ed = split[1].replace("End Date: ", "");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMMM yyyy - HH:mm");
                        Date newSd = dateFormat.parse(sd);
                        Date newEd = dateFormat.parse(ed);
                        Log.d("->", String.valueOf(newSd));

                        setStartDate(newSd);
                        setEndDate(newEd);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    setDesc(split[2]);
                }
                break;
            case "roadWork":
                System.out.println("rw");
                Log.d("road work desc ->", this.description);


                String noBR2 = this.description.replaceAll("(<br />)+", "+");
                String[] split2 = noBR2.trim().split("\\+");
                Log.e("->", String.valueOf(split2.length));
                if (split2.length >= 1 && split2.length <= 3) {
                    try {
                        String sd = split2[0].replace("Start Date: ", "");
                        String ed = split2[1].replace("End Date: ", "");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMMM yyyy - HH:mm");
                        Date newSd = dateFormat.parse(sd);
                        Date newEd = dateFormat.parse(ed);
                        Log.d("->", String.valueOf(newSd));

                        setStartDate(newSd);
                        setEndDate(newEd);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Log.i("->", Arrays.toString(split2));

//                    try {
//                        if (split2.length >= 3) {
//                            setDesc(split2[2]);
//                        }
//                    } catch (Exception e) {
//                        Log.e("e", e.toString());
//                    }
                }


                break;
            case "incident":
                Log.d("incident ->", this.description);
                System.out.println("inc");
//                String noBR3 = this.description.replaceAll("(<br />)+", "+");
//                String[] split3 = noBR3.trim().split("\\+");
//                Log.e("->", String.valueOf(split3.length));
                break;
        }


    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.description = desc;
        parseDesc();
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setComments(String comm) {
        this.comments = comm;
    }

    public void setPubDate(String pub) {
        this.pubDate = pub;
    }

    public void setStartDate(Date s) {
        this.startDate = s;
    }

    public void setEndDate(Date s) {
        this.endDate = s;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    public String getPoint() {
        return this.point;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getComments() {
        return this.comments;
    }

    public String getPubDate() {
        return this.pubDate;
    }

    public Date getStartDate() {
        Log.e("date->", String.valueOf(this.startDate.getTime()));
        return this.startDate;

    }

    public Date getEndDate() {
        return this.endDate;
    }

    public String getLat() {
        String[] split = this.point.trim().split(" ");
        parseDesc();
        return split[0];
    }

    public String getLon() {
        String[] split = this.point.trim().split(" ");
        return split[split.length - 1];
    }

    public String toString() {
        return (this.title + " \n " + this.description + "\n" + this.link + "\n  point:" + this.point + "\n" + this.author + "\n" + this.comments + "\n" + this.startDate + "\n" + this.endDate + "\n" + this.pubDate + "\n" + getLat() + "\n" + getLon());
    }

    public String getType() {
        return this.type;
    }


}
