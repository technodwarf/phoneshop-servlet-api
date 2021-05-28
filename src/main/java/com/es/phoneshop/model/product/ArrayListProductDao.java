package com.es.phoneshop.model.product;

import java.util.*;
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
    private List<Product> products;

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
        HashMap<Product, Integer> productMap = new HashMap<>();
        if (query != null) {
            for (int i = 0; i < products.size(); i++) {
                for (int j = 0; j < query.split(" ").length; j++) {
                    if (products.get(i).getDescription().toUpperCase().contains((query.split(" ")[j]).toUpperCase())) {
                        if (products.get(i).getDescription().toUpperCase().equals(query.toUpperCase())) {
                            productMap.put(products.get(i), Integer.MAX_VALUE); // костыль, если запрос полностью совпадает названию продукта то он отобразиться первым
                        } else if (productMap.containsKey(products.get(i))) {
                            productMap.put(products.get(i), productMap.get(products.get(i)) + 1); //иначе добавить продукт в map либо увеличить число совпадений для продукта на 1
                        } else productMap.put(products.get(i), 0);
                    }
                }
            }
        }
        if (sortField != null) {
            Comparator<Product> comparator = Comparator.comparing(product -> {
                if (SortField.price == sortField) {
                    return (Comparable) product.getPrice();
                } else {
                    return (Comparable) product.getDescription();
                }
            });
            comparator = SortOrder.desc == sortOrder ? comparator.reversed() : comparator;
            return productMap
                    .entrySet()
                    .stream()
                    .sorted((e1, e2) -> Double.compare((e2.getValue()), (e1.getValue())))
                    .map(Map.Entry::getKey)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        return productMap
                .entrySet()
                .stream()
                .sorted((e1, e2) -> Double.compare((e2.getValue()), (e1.getValue())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
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
