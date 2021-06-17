package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.RecentlyViewed;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private RecentlyViewed recentlyViewed;
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewed = RecentlyViewed.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getPathInfo();
        Optional<Product> product = Optional.ofNullable(productDao.getProduct(parseProductId(request)));
        ArrayList<Product> queue = recentlyViewed.getQueue(request);
        recentlyViewed.add(queue, product.get());
        request.setAttribute("recentlyViewed", recentlyViewed.getQueue(request));
        request.setAttribute("product", productDao.getProduct(Long.valueOf(productId.substring(1))));
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        String quantity = request.getParameter("quantity");
            try {
                if (Integer.parseInt(quantity) <= 0 ) throw new NumberFormatException();
                cartService.add(cartService.getCart(request), productId, Integer.parseInt(quantity));
            } catch (OutOfStockException e) {
                response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=Not enough in stock.");
                return;
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/products/" + productId + "?error=Error : numbers greater than 0 only.");
                return;
            }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added!");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productInfo = request.getPathInfo().substring(1);
        return Long.valueOf(productInfo);
    }
}
