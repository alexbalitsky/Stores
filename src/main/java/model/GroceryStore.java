package model;

import DAO.DAO;

import java.util.*;

/**
 * Store implementation
 * Represents grocery store
 * @author A. Balitsky
 */
public class GroceryStore extends Store{
    private static volatile GroceryStore groceryStore;

    private Set<Category> categories = new HashSet<>();

    public static GroceryStore getGroceryStore(String name){
        GroceryStore localStore = groceryStore;
        if (localStore == null){
            synchronized (GroceryStore.class){
                localStore = groceryStore;
                if (localStore == null){
                    groceryStore = localStore = new GroceryStore(name);
                }
            }
        }
        return localStore;
    }

    private GroceryStore(String name) {
        super(name);
    }

    @Override
    public Set<Category> getLazyCategories(String storeName) {
        return categories = DAO.getLazyCategoriesByStoreName(storeName);
    }
}
