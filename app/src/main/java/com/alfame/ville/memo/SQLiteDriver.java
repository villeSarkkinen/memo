package com.alfame.ville.memo;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.File;
import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class SQLiteDriver implements IStorageDriver {

    private String tableName,idName,titleName,textName,categoryName,deadlineName,struckName;
    private File file;
    private int idCount;

    private SQLiteDatabase database;


    SQLiteDriver(File file){
        this.file=file.getAbsoluteFile();
        idCount=0;
        tableName="notes";
        idName="ID";
        titleName="Title";
        textName="Text";
        categoryName="Category";
        deadlineName="Deadline";
        struckName="Struck";
        createDatabase();

    }

    @Override
    public ArrayList loadItemsList() {
        //loads Notes from database
        _createDatabase();
        ArrayList<Note> noteList = new ArrayList<>();
        String sql;
        System.out.println("loadItemsList()");


        sql = "SELECT "+idName+", "+titleName+", "+textName+", "+categoryName+", "+deadlineName+", "
                +struckName+" FROM "+tableName+" ORDER BY ID;";
        Cursor cl = database.rawQuery(sql,null);
        cl.moveToFirst();
        while(!cl.isAfterLast()){
            //checks if struck
            Boolean struckB=true;
            if(!cl.getString(5).toUpperCase().contains("TRUE")){
                struckB=false;
            }

            //id,title,text,category,deadline,struck
            Note newNote = new Note(
                    cl.getInt(0),//id
                    cl.getString(1),//title
                    cl.getString(2),//text
                    cl.getString(3),//category
                    cl.getString(4));//deadline
            newNote.setStruck(struckB);
            noteList.add(newNote);
            cl.moveToNext();
        }
        cl.close();
        database.close();

        //prints out all, for testing
        for(int i=0;i<noteList.size();i++){
            noteList.get(i).printNote();
        }
        return noteList;
    }

    @Override
    public void addItem(Note note) {
        _createDatabase();

        /**
         * use storageOperations.getidCount() for id when creating a Note so it don't get mixed up
         *
         *
         * INSERT INTO notes ('id','Title','Text','Category','Struck')
         * VALUES(int,'String','String','String','String');
         *
         * ' and " etc in strings are going to mess with SQL!
         */

        try{
            String sql = "INSERT INTO "+tableName+" " +
                    "('"+ idName + "', " +
                    "'"+ titleName+"', " +
                    "'"+ textName+"', " +
                    "'"+ categoryName+"', " +
                    "'"+deadlineName+"', " +
                    "'"+ struckName+"') " +

                    "VALUES ("
                    +note.getID()+","+
                    "'"+note.getTitle()+"'," +
                    "'"+note.getText()+"'," +
                    "'"+note.getCategory()+"'," +
                    "'"+note.getDeadlineString()+"'," +
                    "'"+String.valueOf(note.isStruck()).toUpperCase()+"'"+
                    ");";
            System.out.println("IDCOUNT: "+idCount+" SQL CODE: "+sql);
            database.execSQL(sql);
            idCount++;
        }
        catch (SQLiteException e){
            System.out.println("AddItem e: "+e);

        }
        database.close();


    }

    @Override
    public void editItem(Note note) {
        //updated edited Note in database
        _createDatabase();

        /**
         * UPDATE notes
         * SET
         * Title='note.getTitle()',
         * Text='note.getText()',
         * Category='note.getCategory()',
         * Deadline='note.getDeadlineString()',
         * Struck='String.valueOf(note.isStruck()).toUppercase()'
         * WHERE id=note.getID();
         */

        try{
            String sql ="UPDATE "+tableName+" SET "+
                    titleName+"='"+note.getTitle()+"', "+
                    textName+"='"+note.getText()+"', "+
                    categoryName+"='"+note.getCategory()+"', "+
                    struckName+"='"+String.valueOf(note.isStruck()).toUpperCase()+"', "+
                    deadlineName+"='"+note.getDeadlineString()+"', "+
                    categoryName+"='"+note.getCategory()+"' "+

                    " WHERE "+idName+"="+note.getID()+";";

            System.out.println("editItem sql: "+sql);
            database.execSQL(sql);
        }
        catch (SQLiteException e){
            System.out.println("editItem e: "+e);
        }
        database.close();


    }

    @Override
    public void removeItem(Note note) {
        //deletes Note from database
        _createDatabase();
        try{
            String sql = "DELETE FROM "+ tableName +" WHERE "+idName+"= "+note.getID()+";";
            database.execSQL(sql);
            System.out.println("removeItem SQL CODE: "+sql);

        }
        catch (SQLException e){
            System.out.println("removeItem e: "+e);
        }
        database.close();
    }

    @Override
    public void removeAll() {
        //deletes all Notes from database
        _createDatabase();
        try{
            String sql = "DELETE FROM "+tableName+ ";";
            database.execSQL(sql);
            System.out.println("SQL CODE: "+sql);

        }
        catch (SQLiteException e){
            System.out.println("removeAll e: "+e);
        }
        database.close();


    }

    @Override
    public void createDatabase() {
        System.out.println("createDatabase!");
        try{
            //if file exists, only connects to database
            if(file.length()<1){
                System.out.println("createDatabase says file does not exist");
                _createDatabase();
                createTables();
            }
            else{
                System.out.println("createDatabase says file exists");
                _createDatabase();
            }
            database.close();

        }
        catch (Exception e){
            System.out.println("createDatabase: "+e);
        }



    }

    private void _setidCounter() {
        //fetches ids from database and sets idCount ahead of highest
        //should always be the last ID of the table
        _createDatabase();
        idCount=0;
        String sql = "SELECT ID FROM notes ORDER BY ID;";
        Cursor cl = database.rawQuery(sql,null);
        cl.moveToFirst();
        while(!cl.isAfterLast()){
            if(idCount<=cl.getInt(0)){
                idCount=cl.getInt(0);
                idCount+=1;
            }
            cl.moveToNext();
        }
        cl.close();
        database.close();
    }

    private void createTables() {
        //creates the notes table
        try{
            StringBuilder sql = new StringBuilder();

            sql.append("CREATE TABLE "+tableName+" (");
            sql.append(idName +" INTEGER AUTO_INCREMENT PRIMARY KEY,");
            sql.append(titleName    +" VARCHAR(25),");
            sql.append(textName  +" VARCHAR(255),");
            sql.append(categoryName     +" VARCHAR(255),");
            sql.append(deadlineName+" VARCHAR(15),");
            sql.append(struckName   +" VARCHAR(10));");
            database.execSQL(sql.toString());
            System.out.println("createTable SQL CODE: "+sql);
        }
        catch (SQLiteException e){
            System.out.println("createTables e: "+e);

        }
        database.close();

    }

    private void _createDatabase() {
        //opens or creates database for app, closed in methods after SQL
        database= openOrCreateDatabase(file.getAbsolutePath(), null,null);
    }


    public int getidCount(){
        //gets a clear id for a new note
        _setidCounter();//closes database as well
        return  idCount;
    }
}
