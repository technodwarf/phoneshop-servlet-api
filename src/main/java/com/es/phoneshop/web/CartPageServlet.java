package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    protected static final String CART_JSP = "/WEB-INF/pages/cart.jsp";
    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher(CART_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();
        if (productIds != null) {
            for (int i = 0; i < productIds.length; i++) {
                Long productId = Long.valueOf(productIds[i]);

                int quantity;
                try {
                    quantity = getQuantity(quantities[i], request);
                    if (quantity < 0) throw new NumberFormatException();
                    cartService.update(cartService.getCart(request), productId, quantity);
                } catch (OutOfStockException | NumberFormatException | ParseException e) {
                    errorHandler(errors, productId, e);
                }
            }
            if (errors.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully!");
            } else {
                request.setAttribute("errors", errors);
                doGet(request, response);
            }
        }
        else response.sendRedirect(request.getContextPath() + "/cart?message=Go buy yourself something nice first!");
    }

    private void errorHandler(Map<Long, String> errors, Long productId, Exception e) throws ServletException, IOException {
        if (e.getClass().equals(ParseException.class)) {
            errors.put(productId, "Not a number");
        } else if (e.getClass().equals(OutOfStockException.class)) {
            errors.put(productId, "Not enough in stock, available " + ((OutOfStockException) e).getStockAvailable() + " more");
        } else errors.put(productId, "Only positive numbers");
    }

    private int getQuantity(String quantityString, HttpServletRequest request) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
        return numberFormat.parse(quantityString).intValue();
    }
}
