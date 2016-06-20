package model;

import DAO.DAO;

import java.util.*;

/**
 * Store implementation
 * Represents household store
 * @author A. Balitsky
 */
public class HouseHoldStore extends Store {
    private static volatile HouseHoldStore houseHoldStore;

    private Set<Category> categories = new HashSet<>();

    public static HouseHoldStore getHouseHoldStore(String name){
        HouseHoldStore localStore = houseHoldStore;
        if (localStore == null){
            synchronized (HouseHoldStore.class){
                localStore = houseHoldStore;
                if (localStore == null){
                    houseHoldStore = localStore = new HouseHoldStore(name);
                }
            }
        }
        return localStore;
    }

    private HouseHoldStore(String name) {
        super(name);
    }

    @Override
    public Set<Category> getLazyCategories(String storeName) {
        return categories = DAO.getLazyCategoriesByStoreName(storeName);
    }
}
