package DAO;

import com.mongodb.*;
import model.Category;
import model.Product;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.lang.Math.toIntExact;


/**
 * Data access object
 * Class that can access data from MongoDB
 * @author A. Balitsky
 */
public class DAO {
    /**
     * Collection which represents products
     */
    private static DBCollection products;

    /**
     * Connect to MongoDB
     * Getting the products collection
     */
    static {
        try {
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB("products");
            products = db.getCollection("products");

        } catch (UnknownHostException | MongoException e) {
            e.printStackTrace();
        }
    }

    public static void addProduct(String category, String store, Product product) {
        try {
            BasicDBObject document = new BasicDBObject();
            document.put("store", store);
            document.put("category", category);
            document.put("title", product.getTitle());
            document.put("price", product.getPrice());
            document.put("status", product.getStatus().toString());
            products.insert(document);
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public static List<Product> getProducts(String field, String value) {
        List<Product> result = new LinkedList<>();
        try {
            BasicDBObject searchQuery = new BasicDBObject(field, value);
            DBCursor dbCursor = products.find(searchQuery);
            while (dbCursor.hasNext()) {
                Product product = new Product();
                product.setTitle((String) dbCursor.next().get("title"));
                product.setPrice((Double) dbCursor.next().get("price"));
                Product.Status status = null;
                String strStatus = (String) dbCursor.next().get("status");
                if (strStatus.equals("available")) {
                    status = Product.Status.AVAILABLE;
                } else if (strStatus.equals("expected")) {
                    status = Product.Status.EXPECTED;
                } else if (strStatus.equals("absent")) {
                    status = Product.Status.ABSENT;
                }
                product.setStatus(status);
                result.add(product);
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void changeStatus(String field, String value, String status){
        try {
            BasicDBObject query = new BasicDBObject(field, value);
            BasicDBObject newDocument = new BasicDBObject("status", status);
            BasicDBObject updateObj = new BasicDBObject("$set", newDocument);
            products.updateMulti(query, updateObj);
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    //was not able to find better solution
    public static void setStatusToOtherCategories(String category, String status) {
        try {
            BasicDBObject query = new BasicDBObject();
            query.put("category", new BasicDBObject("$ne", category));
            DBCursor dbCursor = products.find(query).limit(toIntExact(products.count(query) / 2));

            while (dbCursor.hasNext()) {
                DBObject old = dbCursor.next();
                BasicDBObject newDocument = new BasicDBObject("status", status);
                BasicDBObject updateObj = new BasicDBObject("$set", newDocument);
                products.update(old, updateObj);
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public static void increasePriceForSpecificStatus(double percentValue, String status) {
        Double newValue = (100 + percentValue)/100;
        try {
            BasicDBObject query = new BasicDBObject("status", status);
            BasicDBObject newDocument = new BasicDBObject("price", newValue);
            BasicDBObject updateObj = new BasicDBObject("$mul", newDocument);
            products.updateMulti(query, updateObj);
        }catch (MongoException e){
            e.printStackTrace();
        }
    }

    public static void changePrice(String field, String value, double price){
        try {
            BasicDBObject query = new BasicDBObject(field, value);
            BasicDBObject newDocument = new BasicDBObject("price", price);
            BasicDBObject updateObj = new BasicDBObject("$set", newDocument);
            products.updateMulti(query, updateObj);
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public static Set<Category> getLazyCategoriesByStoreName(String storeName){
        Set<Category> categories = new HashSet<>();
        try {
            BasicDBObject query = new BasicDBObject("store", storeName);
            DBCursor dbCursor = products.find(query);
            while (dbCursor.hasNext()){
                String categoryName = dbCursor.next().get("category").toString();
                Category category = new Category(categoryName);
                categories.add(category);
            }
        }catch (MongoException e){
            e.printStackTrace();
        }
        return categories;
    }

    public static void dropAll() {
        products.drop();
    }

    public static long count() {
        return products.count();
    }

    public static void showCollection() {
        DBCursor dbCursor = products.find();
        while (dbCursor.hasNext()) {
            System.out.println(dbCursor.next());
        }
    }

}
