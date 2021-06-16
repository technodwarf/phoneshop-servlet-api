package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
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
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        Cart cart = cartService.getCart(request);
        request.setAttribute("cart", cart);
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("quantity");
        String productId = request.getParameter("productId");
        try {
            if (Integer.parseInt(quantity) <= 0) throw new NumberFormatException();
            cartService.add(cartService.getCart(request), Long.valueOf(productId), Integer.parseInt(quantity));
            response.sendRedirect(request.getContextPath() + "/products?message=Added " + quantity + " " +
                    productDao.getProduct(Long.valueOf(productId)).getDescription());
        } catch (OutOfStockException e) {
            response.sendRedirect(request.getContextPath() + "/products?error=Not enough in stock.");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products?error=Error : numbers greater than 0 only.");
        }
    }
}
