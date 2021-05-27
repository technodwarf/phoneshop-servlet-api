package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private long maxId;
    private List<Product> products;

    public ArrayListProductDao() {
        this.products = new ArrayList<>();
        initializeSampleProducts();
    }

    @Override
    public synchronized Product getProduct(Long id) throws NoSuchElementException {
        return products.stream()
                .filter(product -> id.equals(product.getId()))
                .findAny()
                .get();
    }

    @Override
    public synchronized List<Product> findProducts(String query) {
        HashMap<Product, Integer> productMap = new HashMap<>();
        for (int i = 0; i < products.size(); i++) {
            for (int j = 0; j < query.split(" ").length; j++) {
                if (products.get(i).getDescription().toUpperCase().contains((query.split(" ")[j]).toUpperCase())) {
                    if (products.get(i).getDescription().toUpperCase().equals(query.toUpperCase())) {
                        productMap.put(products.get(i),Integer.MAX_VALUE); // костыль, если запрос полностью совпадает названию продукта то он отобразиться первым
                    }
                    else if (productMap.containsKey(products.get(i))) {
                        productMap.put(products.get(i), productMap.get(products.get(i)) + 1); //иначе добавить продукт в map либо увеличить число совпадений для продукта на 1
                    }
                    else productMap.put(products.get(i), 0);
                }
            }
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

    private List<Product> initializeSampleProducts() {
        Currency usd = Currency.getInstance("USD");
        save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product("palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));

        return products;
    }
}
