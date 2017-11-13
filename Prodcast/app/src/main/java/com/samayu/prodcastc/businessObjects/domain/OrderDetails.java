package com.samayu.prodcastc.businessObjects.domain;

/**
 * Created by nandhini on 28/08/17.
 */

public class OrderDetails {
    private Product product;
    private  int quantity;
    private float subTotal;

  /*  public ProductOptions getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(ProductOptions productOptions) {
        this.productOptions = productOptions;
    }

    public ProductFlavors getProductFlavors() {
        return productFlavors;
    }

    public void setProductFlavors(ProductFlavors productFlavors) {
        this.productFlavors = productFlavors;
    }

    private ProductOptions productOptions;
    private ProductFlavors productFlavors;*/




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

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }
}
