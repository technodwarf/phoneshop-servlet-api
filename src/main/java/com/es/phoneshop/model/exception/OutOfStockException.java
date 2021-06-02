package com.es.phoneshop.model.exception;

import com.es.phoneshop.model.product.Product;

public class OutOfStockException extends Exception {
    private Product product;
    private int stockAvailable;

    public OutOfStockException(Product product, int stockAvailable){
        this.product = product;
        this.stockAvailable = stockAvailable;
    }

    public Product getProduct() {
        return product;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }
}
