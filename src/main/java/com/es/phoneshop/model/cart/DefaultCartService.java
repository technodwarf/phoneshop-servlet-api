package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE =
            DefaultCartService.class.getName() + ".cart";

    private ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE =
                new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId,
                                 int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, product.getStock());
        }

        Optional<CartItem> optionalCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findAny();

        optionalCartItem.ifPresentOrElse(cartItem -> {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }, () -> {
            cart.getItems().add(new CartItem(product, quantity));
        });

        product.setStock(product.getStock() - quantity);
    }
}
