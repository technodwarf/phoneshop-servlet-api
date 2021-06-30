package com.es.phoneshop.model.product;

import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private long maxId;
    private final List<Product> products;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    @Override
    public synchronized Product getProduct(Long id) throws NoSuchElementException {
        return products.stream()
                .filter(product -> id.equals(product.getId()))
                .findAny()
                .get();
    }

    @Override
    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        if (query != null) {
            ToIntFunction<Product> getNumberOfMatches = product -> (int) Arrays.stream(query.toLowerCase().split(" "))
                    .filter(product.getDescription().toLowerCase()::contains)
                    .count();
            if (sortField != null) {
                Comparator<Product> comparatorField = Comparator.comparing(product -> {
                    if (SortField.price == sortField) {
                        return (Comparable) product.getPrice();
                    } else {
                        return (Comparable) product.getDescription();
                    }
                });
                comparatorField = SortOrder.desc == sortOrder ? comparatorField.reversed() : comparatorField;
                return products.stream()
                        .sorted(Comparator.comparingInt(getNumberOfMatches).reversed()
                                .thenComparing(comparatorField))
                        .filter(product -> (getNumberOfMatches.applyAsInt(product) != 0))
                        .collect(Collectors.toList());
            } else {
                return products.stream()
                        .sorted(Comparator.comparingInt(getNumberOfMatches).reversed())
                        .filter(product -> (getNumberOfMatches.applyAsInt(product) != 0))
                        .collect(Collectors.toList());
            }
        } else return products;
    }

    @Override
    public List<Product> advancedFindProducts(String query, SortField sortField, SortOrder sortOrder, String searchType, int minprice, int maxprice) {
        List<Product> tempProducts = products;
        if (query != null) {
            if (searchType.equals("all words")) {
                return tempProducts.stream()
                        .filter(product -> product.getDescription().toLowerCase().contains(query))
                        .filter(product -> product.getPrice().intValue() <= maxprice)
                        .filter(product -> product.getPrice().intValue() >= minprice)
                        .collect(Collectors.toList());
            } else {
                ToIntFunction<Product> getNumberOfMatches = product -> (int) Arrays.stream(query.toLowerCase().split(" "))
                        .filter(product.getDescription().toLowerCase()::contains)
                        .count();
                if (sortField != null) {
                    Comparator<Product> comparatorField = Comparator.comparing(product -> {
                        if (SortField.price == sortField) {
                            return (Comparable) product.getPrice();
                        } else {
                            return (Comparable) product.getDescription();
                        }
                    });
                    comparatorField = SortOrder.desc == sortOrder ? comparatorField.reversed() : comparatorField;
                    tempProducts = products.stream()
                            .sorted(Comparator.comparingInt(getNumberOfMatches).reversed()
                                    .thenComparing(comparatorField))
                            .filter(product -> (getNumberOfMatches.applyAsInt(product) != 0))
                            .collect(Collectors.toList());
                } else {
                    tempProducts = products.stream()
                            .sorted(Comparator.comparingInt(getNumberOfMatches).reversed())
                            .filter(product -> (getNumberOfMatches.applyAsInt(product) != 0))
                            .collect(Collectors.toList());
                }
                return tempProducts.stream()
                        .filter(product -> product.getPrice().intValue() <= maxprice)
                        .filter(product -> product.getPrice().intValue() >= minprice)
                        .collect(Collectors.toList());
            }
        } else return tempProducts;
    }

    @Override
    public synchronized void save(Product product) {
        if (products.contains(product)) {
            products.set(product.getId().intValue(), product);
        } else {
            product.setId(maxId++);
            products.add(product);
        }
    }

    @Override
    public synchronized void delete(Long id) {
        products.removeIf(product -> products.contains(product));
    }
}
