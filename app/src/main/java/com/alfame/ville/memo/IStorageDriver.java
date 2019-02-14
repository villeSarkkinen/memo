package com.alfame.ville.memo;

import java.util.ArrayList;

public interface IStorageDriver {
    public abstract ArrayList loadItemsList();
    public abstract void addItem(String item);
    public abstract void removeItem(String item);
    public abstract void removeAll();
}
