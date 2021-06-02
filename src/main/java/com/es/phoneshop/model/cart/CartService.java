package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.exception.OutOfStockException;

public interface CartService {
    Cart getCart();
    void add(Long productId, int quantity) throws OutOfStockException;
}
