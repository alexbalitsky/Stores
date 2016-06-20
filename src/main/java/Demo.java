import model.Product;
import model.Store;

/**
 * Demo
 * Run tasks for each store
 * @author A. Balitsky
 */
public class Demo {
    public static void main(String[] args) {
        //create stores
        Store groceryStore = Store.getNewStore("grocery", Store.Type.GROCERY);
        Store houseHoldStore = Store.getNewStore("houseHold", Store.Type.HOUSEHOLD);

        //run task for grocery store
        Thread grocery =  new Thread(new Demo().new Worker(groceryStore));
        grocery.start();

        //waiting 10 seconds
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //run task for household store
        Thread houseHold = new Thread(new Demo().new Worker(houseHoldStore));
        houseHold.start();

        //waiting for threads to finish
        try {
            grocery.join();
            houseHold.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

    private class Worker implements Runnable{
        private Store store;

        Worker(Store store){
            this.store = store;
        }

        @Override
        public void run() {

            //Create and save products

            Product product1 = createProduct("prod1" + "_" + store, 39.9, Product.Status.AVAILABLE);
            Product product2 = createProduct("prod2" + "_" + store, 33.3, Product.Status.AVAILABLE);
            Product product3 = createProduct("prod3" + "_" + store, 31.1, Product.Status.AVAILABLE);

            store.addProduct("categ1" + "_" + store, product1);
            store.addProduct("categ1" + "_" + store, product2);
            store.addProduct("categ1" + "_" + store, product3);

            Product product4 = createProduct("prod4" + "_" + store, 39.9, Product.Status.AVAILABLE);
            Product product5 = createProduct("prod5" + "_" + store, 33.3, Product.Status.AVAILABLE);
            Product product6 = createProduct("prod6" + "_" + store, 31.1, Product.Status.AVAILABLE);

            store.addProduct("categ2" + "_" + store, product4);
            store.addProduct("categ2" + "_" + store, product5);
            store.addProduct("categ2" + "_" + store, product6);

            Product product7 = createProduct("prod7" + "_" + store, 39.9, Product.Status.AVAILABLE);
            Product product8 = createProduct("prod8" + "_" + store, 33.3, Product.Status.AVAILABLE);
            Product product9 = createProduct("prod9" + "_" + store, 31.1, Product.Status.AVAILABLE);

            store.addProduct("categ3" + "_" + store, product7);
            store.addProduct("categ3" + "_" + store, product8);
            store.addProduct("categ3" + "_" + store, product9);

            //change status to ABSENT for all documents in categ1_<store> category
            store.changeStatus("category", "categ1" + "_" + store, Product.Status.ABSENT);

            //change status for half of other then categ1_<store> documents
            store.setStatusToOtherCategories("categ1" + "_" + store, Product.Status.EXPECTED);

            //increase price for available products by 20 percents
            store.increasePriceForSpecificStatus(20, Product.Status.AVAILABLE);
        }

        private Product createProduct(String title, double price, Product.Status status){
            Product product = new Product();
            product.setTitle(title);
            product.setPrice(price);
            product.setStatus(status);
            return product;
        }

    }


}
