package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;

public interface OrderService {
    Order getOrder(Cart cart);
    void placeOrder(Order order);
}
