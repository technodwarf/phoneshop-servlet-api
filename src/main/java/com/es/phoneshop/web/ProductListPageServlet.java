package com.es.phoneshop.web;

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

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private RecentlyViewed recentlyViewed;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        recentlyViewed = RecentlyViewed.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");
        request.setAttribute("recentlyViewed", recentlyViewed.getQueue(request));
        request.setAttribute("products", productDao.findProducts(query,
                Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
