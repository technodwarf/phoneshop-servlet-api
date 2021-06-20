package com.es.phoneshop.model.order;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ArrayListOrderDao implements OrderDao {
    private static ArrayListOrderDao instance;

    public static synchronized ArrayListOrderDao getInstance() {
        if (instance == null) {
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    private Long maxId = 0L;
    private final List<Order> orderList;

    private ArrayListOrderDao() {
        this.orderList = new ArrayList<>();
    }

    @Override
    public synchronized void save(Order order) {
        if (orderList.contains(order)) {
            orderList.set(order.getId().intValue(), order);
        } else {
            order.setId(maxId++);
            orderList.add(order);
        }
    }

    @Override
    public Order getOrder(Long id) {
        return orderList.get(id.intValue());
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        return orderList.stream()
                                .filter(product -> product.getSecureId().equals(secureId))
                                .findAny()
                                .orElseThrow(NoSuchElementException::new);
    }
}
