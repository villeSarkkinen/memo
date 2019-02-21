package com.alfame.ville.memo;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

//Home of database code, if methods need to be called from MainActivity add them to the interfaces like these methods
public class SQLiteDriver implements IStorageDriver {

    //    private String dbName,tableName,idName,titleName,textName,categoryName,struckName;
    private Resources res;

    private SQLiteDatabase database;

    /*
    *         tableName="notes";
        idName="ID";
        titleName="Title";
        textName="Text";
        categoryName="Category";
        struckName="Struck";
        Try getting resources again
    * */

    @Override
    public ArrayList loadItemsList() {
        return null;
    }

    @Override
    public void addItem(String item) {

    }

    @Override
    public void removeItem(String item) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void createDatabase() {
        /*
        getLists();//gets categories from Strings resources
        try{
            File file = this.getDatabasePath(res.getString(R.string.dbName));
            //if file exists, only opens database
            if(file.length()<1){
                _createDatabase();
                createTables();
            }
            else{
                System.out.println("IT EXISTS");
                createDatabase();
            }
            databaseTesty();
        }
        catch (Exception e){
            System.out.println("Database exception: "+e);
        }
         */
    }

    private void databaseTesty() {//for testing

        //WRITING
        String sql = "INSERT INTO notes (Title,Text) VALUES ('Get Milk!','DISGUSTING')";
        database.execSQL(sql);
        sql = "INSERT INTO notes (Title,Text) VALUES ('Hey nice','WOW')";
        database.execSQL(sql);


        //RETRIEVE
        sql = "SELECT ID, TITLE, TEXT FROM notes ORDER BY ID";
        Cursor cl = database.rawQuery(sql,null);
        cl.moveToFirst();
        while(!cl.isAfterLast()){
            System.out.println("note ID: "+cl.getInt(0)+ " Title: "+cl.getString(1)+" Text: "+cl.getString(2));
            cl.moveToNext();
        }
        cl.close();


    }

    private void createTables() {
        StringBuilder sql = new StringBuilder();

        sql.append("CREATE TABLE "                      +res.getString(R.string.dbName)+"(");
        sql.append(res.getString(R.string.noteID)       +" INTEGER AUTO_INCREMENT PRIMARY KEY,");
        sql.append(res.getString(R.string.noteTitle)    +" VARCHAR(25),");
        sql.append(res.getString(R.string.noteText)     +" VARCHAR(255),");
        sql.append(res.getString(R.string.noteDate)     +" VARCHAR(15),");
        sql.append(res.getString(R.string.notePriority) +" INTEGER,");
        sql.append(res.getString(R.string.noteStruck)   +" BOOLEAN );");
        database.execSQL(sql.toString());
    }

    private void _createDatabase() {

        String name = res.getString(R.string.dbName);
        database= openOrCreateDatabase(name,
                SQLiteDatabase.CREATE_IF_NECESSARY,null);
        System.out.println("DATABASE WAS CREATED");



    }
}
