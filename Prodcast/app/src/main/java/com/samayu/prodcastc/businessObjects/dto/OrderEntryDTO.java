package com.samayu.prodcastc.businessObjects.dto;

/**
 * Created by nandhini on 29/08/17.
 */

public class OrderEntryDTO {
    private String productId;
    private String quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
