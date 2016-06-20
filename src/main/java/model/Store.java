package model;

import DAO.DAO;

import java.util.*;


/**
 * Abstract store which contains:
 *  factory method for creating stores
 *  CRUD operations with products
 * @author A. Balitsky
 */
public abstract class Store {
    private String name;

    Store(String name) {
        this.name = name;
    }

    /**
     * Factory method for creating stores
     * @param name - store name to create
     * @param type - store type
     * @return Specific store
     */
    public static Store getNewStore(String name, Type type) {
        switch (type) {
            case GROCERY:
                return GroceryStore.getGroceryStore(name);
            case HOUSEHOLD:
                return HouseHoldStore.getHouseHoldStore(name);
            default:
                return null;
        }
    }

    /**
     * Save product to DB
     * @param categoryName - name of product category
     * @param product - certain product to save
     */
    public void addProduct(String categoryName, Product product) {
        DAO.addProduct(categoryName, getName(), product);
    }

    /**
     * Retreive all products from DB that match to pattern 'field -> value'
     * @param field - field that specifies find pattern;
     * @param value - value of the {@param field} that specifies find pattern
     * @return {@code List} of products that match to pattern 'field -> value'
     */
    public List<Product> getProducts(String field, String value) {
        return DAO.getProducts(field, value);
    }

    /**
     * Change status for documents that match to pattern 'field -> value'
     * @param field - field that specifies find pattern;
     * @param value - value of the {@param field} that specifies find pattern
     * @param status - status to change in specific documents
     */
    public void changeStatus(String field, String value, Product.Status status) {
        DAO.changeStatus(field, value, status.toString());
    }

    /**
     * Change price for documents that match to pattern 'field -> value'
     * @param field - field that specifies find pattern;
     * @param value - value of the {@param field} that specifies find pattern
     * @param price - price to change in specific documents
     */
    public void changePrice(String field, String value, double price){
        DAO.changePrice(field, value, price);
    }

    /**
     * This change status of half of products that do not belong to specific {@param category}
     * @param category - category whose products will NOT be changed
     * @param status - status to change in specific documents
     */
    public void setStatusToOtherCategories(String category, Product.Status status) {
        DAO.setStatusToOtherCategories(category, status.toString());
    }

    /**
     * Increases price for documents with specific status by {@param value} percents
     * @param value - number in percents to increase in specific documents
     * @param status - status that specifies documents to change
     */
    public void increasePriceForSpecificStatus(double value, Product.Status status) {
        DAO.increasePriceForSpecificStatus(value, status.toString());
    }

    /**
     * Retreive all categories that belong to certain store without products from DB
     * @param storeName - store name
     * @return {@code Set} of categories without products
     */
    public Set<Category> getLazyCategories(String storeName) {
        return DAO.getLazyCategoriesByStoreName(storeName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public enum Type {
        GROCERY,
        HOUSEHOLD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store store = (Store) o;

        return name != null ? name.equals(store.name) : store.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
