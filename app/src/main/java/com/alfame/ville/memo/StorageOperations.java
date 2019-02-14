package com.alfame.ville.memo;


import java.util.ArrayList;

public class StorageOperations implements IStorageOperations {

    public IStorageDriver driver = null;

    public StorageOperations(IStorageDriver driver) {
        this.driver = driver;
    }

    @Override
    public ArrayList LoadItemsList() {
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
}
