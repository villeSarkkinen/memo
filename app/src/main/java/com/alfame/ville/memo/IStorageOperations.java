package com.alfame.ville.memo;


import java.util.ArrayList;

public interface IStorageOperations {
    public abstract ArrayList loadItemsList();
    public abstract void addItem(Note note);
    public abstract void editItem(Note note);
    public abstract void removeItem(Note note);
    public abstract void removeAll();
    public abstract void createDatabase();
    public abstract int getidCount();
}
