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

//Home of database code, if methods need to be called from MainActivity add them to the interfaces like these methods
public class SQLiteDriver implements IStorageDriver {

    private String dbName,tableName,idName,titleName,textName,categoryName,deadlineName,struckName;
    private File file;
    private int idCount;

    private SQLiteDatabase database;


    SQLiteDriver(File file){
        this.file=file.getAbsoluteFile();

        dbName="notes";
        idCount=0;
        tableName="notes";
        idName="ID";
        titleName="Title";
        textName="Text";
        categoryName="Category";
        deadlineName="Deadline";
        struckName="Struck";

        System.out.println("Constructor "+ file.getName()+" " +file.getAbsolutePath());
        createDatabase();

    }

    @Override
    public ArrayList loadItemsList() {
        _createDatabase();
        ArrayList<Note> noteList = new ArrayList<>();
        String sql;
        System.out.println("loadItemsList()");

        //UH OH HARDCODED
        sql = "SELECT ID, TITLE, TEXT, CATEGORY, STRUCK FROM notes ORDER BY ID";
        Cursor cl = database.rawQuery(sql,null);
        cl.moveToFirst();
        while(!cl.isAfterLast()){
            //checks if struck
            Boolean struckB=false;
            if(!cl.getString(4).toUpperCase().equals("FALSE")){
                struckB=true;
            }

            //testing
            //System.out.println("loadItemsList :");
            //System.out.println("note ID: "+cl.getInt(0)+ " Title: "+cl.getString(1)+" Text: "+cl.getString(2));

            //id,title,text,category,struck
            Note newNote = new Note(cl.getInt(0),
                    cl.getString(1),
                    cl.getString(2),
                    cl.getString(3));
            newNote.setStruck(struckB);
            noteList.add(newNote);

            cl.moveToNext();
        }
        cl.close();

        idCount=noteList.size()+1;
        //prints out all, for testing
        for(int i=0;i<noteList.size();i++){
            noteList.get(i).printNote();
        }
        database.close();
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
                    // "'"+deadlineName+"', " +
                    "'"+ struckName+"') " +

                    "VALUES ("
                    +note.getID()+","+
                    "'"+note.getTitle()+"'," +
                    "'"+note.getText()+"'," +
                    "'"+note.getCategory()+"'," +
                    // "'"+note.getDeadlineString()+"'," +
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
        _createDatabase();

        /**
         * UPDATE notes
         * SET
         * Title='note.getTitle()',
         * Text='note.getText()',
         * Category='note.getCategory()',
         * Deadline='note.getDeadlineString()',
         * Struck='String.valueOf(note.isStruck()).toUppercase()',
         * WHERE id=note.getID();
         */
        try{
            String sql ="UPDATE "+tableName+" SET "+
                    titleName+"='"+note.getTitle()+"', "+
                    textName+"='"+note.getText()+"', "+
                    categoryName+"='"+note.getCategory()+"', "+
                    struckName+"='"+String.valueOf(note.isStruck()).toUpperCase()+"', "+
                    //deadlineName+"='"+note.getDeadlineString()+"', "+
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
        _createDatabase();

        //DELETE FROM or DELETE * FROM, dunno
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
                databaseTesty(1); //add
                databaseTesty(2); //read
                _setidCounter();



            }
            else{
                System.out.println("createDatabase file exists");
                _createDatabase();
                _setidCounter();
                //databaseTesty(1);
                //databaseTesty(2);

                //handle it already

            }

        }
        catch (Exception e){
            System.out.println("createDatabase: "+e);
        }


    }

    private void _setidCounter() {
        _createDatabase();

        /*

         */


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
        //ArrayList<Note> arrayList = loadItemsList();

        database.close();


    }


    private void databaseTesty(int selection) {//for testing
        _createDatabase();
        /** Testy
         1 for testing write
         2 for reading
         :D
         **/
        String sql;

        Cursor cl;
        switch (selection){
            case 1:
                //WRITING
                Note newNote = new Note(idCount,"NJOICE","I FEEL IT and THINGS","Anttis kaka");
                addItem(newNote);
                Note newNote1 = new Note(idCount,"Wwenks","wih wuah wauau ankan kanka","Anttis kaka3");
                addItem(newNote1);
                Note newNote2 = new Note(idCount,"Hi","How are yeeet","Anttis kaka1");
                addItem(newNote2);
                break;

            case 2:

                //RETRIEVE
                sql = "SELECT ID, TITLE, TEXT, CATEGORY FROM notes ORDER BY ID;";
                cl = database.rawQuery(sql,null);
                cl.moveToFirst();
                while(!cl.isAfterLast()){
                    System.out.println("note ID: "+cl.getInt(0)+ " Title: "+cl.getString(1)+" Text: "+cl.getString(2)+" Category: "+cl.getString(3));
                    cl.moveToNext();
                }
                cl.close();
                break;

            default:
                break;

        }
        database.close();
    }

    private void createTables() {

        //dropping table for safety/testing
        /*
         */
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("DROP TABLE notes;");

            database.execSQL(sql.toString());
            System.out.println("createTables: dropped table");
        }
        catch (SQLiteException e){
            System.out.println("createTable dropping e: "+e);
        }


        try{
            StringBuilder sql = new StringBuilder();

            sql.append("CREATE TABLE "+tableName+" (");
            sql.append(idName +" INTEGER AUTO_INCREMENT PRIMARY KEY,");
            sql.append(titleName    +" VARCHAR(25),");
            sql.append(textName  +" VARCHAR(255),");
            sql.append(categoryName     +" VARCHAR(255),");
            sql.append(deadlineName+" VARCHAR(15),");
            sql.append(struckName   +" BOOLEAN );");
            database.execSQL(sql.toString());
            System.out.println("createTable SQL CODE: "+sql);
        }
        catch (SQLiteException e){
            System.out.println("createTables e: "+e);

        }
        database.close();

    }

    private void _createDatabase() {

        if(database==null){
            //System.out.println("DATABASE CREATION");
        }
        database= openOrCreateDatabase(file.getAbsolutePath(), null,null);
    }


    public int getidCount(){
        _createDatabase();
        _setidCounter();
        database.close();
        return  idCount;
    }
}
