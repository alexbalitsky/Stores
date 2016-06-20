package model;

/**
 * Model of product
 * @author A. Balitsky
 */
public class Product {
    private String title;
    private double price;
    private Status status;

    public enum Status{
        AVAILABLE("available"),
        ABSENT("absent"),
        EXPECTED("expected");

        private String status;

        Status(String status){
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (Double.compare(product.price, price) != 0) return false;
        if (title != null ? !title.equals(product.title) : product.title != null) return false;
        return status == product.status;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = title != null ? title.hashCode() : 0;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
