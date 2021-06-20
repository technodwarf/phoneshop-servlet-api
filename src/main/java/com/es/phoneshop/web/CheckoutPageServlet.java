package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    protected static final String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";
    private ProductDao productDao;
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        orderService = DefaultOrderService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute("cart", cartService.getCart(request));
        if (!cart.getItems().isEmpty()) {
            request.setAttribute("order", orderService.getOrder(cart));
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        } else response.sendRedirect(request.getContextPath() + "/products");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        Map<String, String> errors = new HashMap<>();

        setRequiredParameter(request, "firstName", errors, order::setFirstName);
        setRequiredParameter(request, "lastName", errors, order::setLastName);
        setRequiredParameter(request, "phone", errors, order::setPhone);
        try {
            order.setDeliveryDate(LocalDate.parse(request.getParameter("deliveryDate")));
        } catch (DateTimeParseException e) {
            errors.put("deliveryDate", "Incorrect date");
        }
        setRequiredParameter(request, "deliveryAddress", errors, order::setDeliveryAddress);
        setPaymentMethod(request, errors, order);

        errorHandler(request, response, errors, order);
    }

    private void errorHandler(HttpServletRequest request, HttpServletResponse response,
                              Map<String, String> errors, Order order) throws ServletException, IOException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.getCart(request).setItems(new ArrayList<>());
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("cart", cartService.getCart(request));
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.getRequestDispatcher(CHECKOUT_JSP).forward(request, response);
        }
    }

    private void setRequiredParameter(HttpServletRequest request, String parameter,
                                      Map<String, String> errors, Consumer<String> consumer) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Field is required");
        } else {
            consumer.accept(value);
        }
    }

    private void setPaymentMethod(HttpServletRequest request, Map<String, String> errors, Order order) {
        String value = request.getParameter("paymentMethod");
        if (value.equals("Cash")) {
            order.setPaymentMethod(PaymentMethod.CASH);
        } else if (value.equals("Credit card")) {
            order.setPaymentMethod(PaymentMethod.CREDIT_CART);
        } else errors.put("paymentMethod", "Field is required");
    }
}
