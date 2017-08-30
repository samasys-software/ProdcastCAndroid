package businessObjects.domain;

/**
 * Created by nandhini on 28/08/17.
 */

public class OrderDetails {
    private Product product;
    private  int quantity;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
