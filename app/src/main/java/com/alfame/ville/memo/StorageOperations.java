package com.alfame.ville.memo;


import java.util.ArrayList;

public class StorageOperations implements IStorageOperations,IStorageDriver {

    public IStorageDriver driver;

    public StorageOperations(IStorageDriver driver) {
        this.driver = driver;
    }

    @Override
    public ArrayList loadItemsList() {


        return driver.loadItemsList();
    }

    @Override
    public void addItem(Note note) {
        driver.addItem(note);

    }

    @Override
    public void editItem(Note note) {
        driver.editItem(note);

    }

    @Override
    public void removeItem(Note note) {
        driver.removeItem(note);

    }

    @Override
    public void removeAll() {
        driver.removeAll();

    }

    @Override
    public void createDatabase() {


    }

    @Override
    public int getidCount() {
        return driver.getidCount();
    }
}
