package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.OrderDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

public class OrderOverviewPageServlet extends HttpServlet {
    protected static final String ORDER_OVERVIEW_JSP = "/WEB-INF/pages/orderOverview.jsp";
    private OrderDao orderDao;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("cart", cartService.getCart(request));
            String secureOrderId = request.getPathInfo().substring(1);
            request.setAttribute("order", orderDao.getOrderBySecureId(secureOrderId));
            request.getRequestDispatcher(ORDER_OVERVIEW_JSP).forward(request, response);
        } catch (NumberFormatException | IndexOutOfBoundsException | NoSuchElementException e) {
            request.getRequestDispatcher("/WEB-INF/pages/errorOrderNotFound.jsp").forward(request, response);
        }
    }
}
