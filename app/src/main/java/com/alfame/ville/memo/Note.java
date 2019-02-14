package com.alfame.ville.memo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private int id;
    private String title;
    private String text;
    private String category;
    private int priority;
    private Date date;
    private boolean struck;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    Note(String title, String text, String category, int priority, boolean struck){
        this.title=title;
        this.text=text;
        this.category=category;
        this.priority=priority;
        this.struck=struck;

        Date date = new Date();
        System.out.println(" Datecheck: "+dateFormat.format(date));

    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public int getPriority() {
        return priority;
    }

    public Date getDate() {
        return date;
    }

    public boolean isStruck() {
        return struck;
    }


    //easier to set all at once thru edit
    public void setNote(String title, String text, String category, int priority, boolean struck){
        this.title=title;
        this.text=text;
        this.category=category;
        this.priority=priority;
        this.struck=struck;


        Date date = new Date();
        System.out.println(" Datecheck: "+dateFormat.format(date));
    }

}
