package com.alfame.ville.memo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private int id;
    private String title;
    private String text;
    private String category;

    private boolean struck;
    private Date deadline;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    //dev phase constructor
    Note(int id,String title,String text,String category){
        this.id=id;
        this.title=title;
        this.text=text;
        this.category=category;
        this.struck = false;

        deadline = new Date();

        System.out.println(" Datecheck: "+dateFormat.format(deadline));



    }


    Note(int id, String title, String text, String category, Date deadline){
        this.title=title;
        this.text=text;
        this.category=category;
        struck=false;
        this.deadline= deadline;

    }


    public int getID(){
        return id;
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

    public String getDeadlineString(){
        return dateFormat.format(deadline);

    }

    public boolean isStruck() {
        return struck;
    }

    public void setStruck(Boolean struck){
        this.struck=struck;

    }

    public Date getDate(){
        return deadline;
    }


    //easier to set all at once thru edit
    public void setNote(String title, String text, String category, boolean struck){
        this.title=title;
        this.text=text;
        this.category=category;
        this.struck=struck;

        Date date = new Date();
        System.out.println(" Datecheck: "+dateFormat.format(date));
    }

    public void printNote(){
        System.out.println(

                "ID: "+id+
                        "Title: "+title+
                        "Text: "+text
                        +"Category: "+category
                        +"Struck: "+struck
        );
    }
}
