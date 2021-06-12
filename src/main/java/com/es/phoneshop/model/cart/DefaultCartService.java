package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE =
            DefaultCartService.class.getName() + ".cart";

    private final ProductDao productDao;

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
        recalculateCart(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId,
                                    int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst().get();

        int oldQuantity = cartItem.getQuantity();
        int stock = cartItem.getProduct().getStock();

        if (oldQuantity < quantity) {
            if (stock - (quantity - oldQuantity) < 0) throw new
                    OutOfStockException(product, product.getStock());
            cartItem.getProduct().setStock(stock - (quantity - oldQuantity));
            cartItem.setQuantity(quantity);
            //not enough in stock? throw exception
            //else add diff between new and old quantities
        } else if (oldQuantity > quantity) {
            cartItem.getProduct().setStock(stock + (oldQuantity - quantity));
            cartItem.setQuantity(quantity);
            //if user removed some quantity of product then
            //return it to stock,update quantity in cart
        } else if (quantity <= 0) {
            delete(cart, productId);
        }
        recalculateCart(cart);
    }

    @Override
    public void delete(Cart cart, Long productId) {
        productDao.getProduct(productId).setStock(
                productDao.getProduct(productId).getStock() +
                        cart.getItems().stream().filter(cartItem -> cartItem.getProduct().getId()
                                .equals(productId)).findFirst().get().getProduct().getStock());

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        recalculateCart(cart);
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(q -> q).sum());

        cart.setTotalCost(cart.getItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(new BigDecimal(0), (a, b) -> a.add(b)));
    }
}
