package com.alfame.ville.memo;

import java.util.ArrayList;

//Home of database code, if methods need to be called from MainActivity add them to the interfaces like these methods
public class SQLiteDriver implements IStorageDriver {
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
}
