package com.alfame.ville.memo;


import java.util.ArrayList;

public interface IStorageOperations {
    public abstract ArrayList LoadItemsList();
    public abstract void addItem(String item);
    public abstract void removeItem(String item);
    public abstract void removeAll();
    public abstract void createDatabase();
}
