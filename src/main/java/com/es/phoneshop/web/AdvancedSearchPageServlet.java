package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.RecentlyViewed;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewed recentlyViewed;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewed = RecentlyViewed.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        String searchType = request.getParameter("searchType");
        int minprice = 0;
        int maxprice = Integer.MAX_VALUE;
        try {
            maxprice = Integer.parseInt(request.getParameter("maxprice"));
            minprice = Integer.parseInt(request.getParameter("minprice"));
        } catch (NumberFormatException e) {}
        if (minprice >= maxprice) {
            minprice = 0;
            request.setAttribute("maxprice", maxprice);
        }
        Cart cart = cartService.getCart(request);
        request.setAttribute("cart", cart);
        request.setAttribute("recentlyViewed", recentlyViewed.getQueue(request));
        request.setAttribute("products", productDao.advancedFindProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null),
                searchType, minprice, maxprice));
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quantity = request.getParameter("quantity");
        String productId = request.getParameter("productId");
        try {
            if (Integer.parseInt(quantity) <= 0) throw new NumberFormatException();
            cartService.add(cartService.getCart(request), Long.valueOf(productId), Integer.parseInt(quantity));
            response.sendRedirect(request.getContextPath() + "/products/advancedSearch?message=Added " + quantity + " " +
                    productDao.getProduct(Long.valueOf(productId)).getDescription());
        } catch (OutOfStockException e) {
            response.sendRedirect(request.getContextPath() + "/products/advancedSearch?error=Not enough in stock.");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/products/advancedSearch?error=Error : numbers greater than 0 only.");
        }
    }
}
