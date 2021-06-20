package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private final OrderDao orderDao = ArrayListOrderDao.getInstance();

    private static class SingletonHelper {
        private static final DefaultOrderService INSTANCE =
                new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return (CartItem)item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        try {
            order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        } catch (NullPointerException e) {
            order.setTotalCost(new BigDecimal(0));
        }
        order.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity).mapToInt(Integer::intValue).sum());
        return order;
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }
}
