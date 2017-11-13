package com.samayu.prodcastc.businessObjects.dto;

import com.samayu.prodcastc.businessObjects.domain.Product;
import com.samayu.prodcastc.businessObjects.domain.ProductFlavors;
import com.samayu.prodcastc.businessObjects.domain.ProductOptions;

import java.util.List;

/**
 * Created by nandhini on 13/11/17.
 */

public class ProductListDTO extends ProdcastDTO {
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    private List<Product> productList;
    private  List<ProductOptions> productOptionsList;
    private  List<ProductFlavors> productFlavorsList;

    public List<ProductOptions> getProductOptionsList() {
        return productOptionsList;
    }

    public void setProductOptionsList(List<ProductOptions> productOptionsList) {
        this.productOptionsList = productOptionsList;
    }

    public List<ProductFlavors> getProductFlavorsList() {
        return productFlavorsList;
    }

    public void setProductFlavorsList(List<ProductFlavors> productFlavorsList) {
        this.productFlavorsList = productFlavorsList;
    }
}
