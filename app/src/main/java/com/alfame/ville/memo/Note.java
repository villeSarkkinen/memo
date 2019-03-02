package com.alfame.ville.memo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private int id;//id used for database operations
    private String title;//
    private String text;
    private String category;

    private boolean struck;//Note marked as complete by user
    private Date deadline;//used to display date of creation instead of deadline

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    //constructor for adding Note
    Note(int id,String title,String text,String category){
        this.id=id;
        this.title=title;
        this.text=text;
        this.category=category;
        this.struck = false;

        deadline = new Date();

        System.out.println(" Datecheck: "+dateFormat.format(deadline));



    }


    //used when loading Notes from database
    Note(int id, String title, String text, String category, String deadline){
        this.id=id;
        this.title=title;
        this.text=text;
        this.category=category;
        struck=false;//struck set from loadItemsList
        try {
            this.deadline= dateFormat.parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    //easier to set all at once thru edit
    public void setNote(String title, String text, String category){
        this.title=title;
        this.text=text;
        this.category=category;

    }


    public void printNote(){
        System.out.println(
                "printNote: "+id
                        +" Title: "+title
                        +" Text: "+text
                        +" Category: "+category
                        +" Date: "+getDeadlineString()
                        +" Struck: "+struck
        );
    }
}
