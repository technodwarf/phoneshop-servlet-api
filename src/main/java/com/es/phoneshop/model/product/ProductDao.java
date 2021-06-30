package com.es.phoneshop.model.product;

import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    List<Product> advancedFindProducts(String query, SortField sortField, SortOrder sortOrder, String searchType, int minprice, int maxprice);

    void save(Product product);

    void delete(Long id);
}
