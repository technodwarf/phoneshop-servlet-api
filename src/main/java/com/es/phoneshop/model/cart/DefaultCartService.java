package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

public class DefaultCartService implements CartService {
    private Cart cart = new Cart();

    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, product.getStock());
        }
        boolean itemNotExists = true;
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getProduct().getId() == productId) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                itemNotExists = false;
                break;
            }
        }
        if (itemNotExists) cart.getItems().add(new CartItem(product, quantity));
        product.setStock(product.getStock() - 1);
    }
}
