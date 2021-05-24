package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertTrue(product.getId() >= 0);
        Product result = productDao.getProduct((Long.valueOf(product.getId())));
        assertNotNull(result);
        assertEquals("test-product", result.getCode());
    }

    @Test
    public void testDeleteProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        List<Product> products = productDao.findProducts();
        assertTrue(products.contains(product));
        productDao.delete(product.getId());
        assertFalse(products.contains(product));
    }

    @Test
    public void testDeleteNonExistingProduct() {
        List<Product> products = productDao.findProducts();
        assertFalse(products.contains(1L));
        productDao.delete(1L);
        assertFalse(products.contains(1L));
    }

    @Test
    public void testAddExistingProduct() {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        List<Product> productsBefore = productDao.findProducts();
        productDao.save(product);
        List<Product> productsAfter = productDao.findProducts();
        assertTrue(productsAfter.equals(productsBefore));
    }
}
