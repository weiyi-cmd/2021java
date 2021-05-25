package com.fjp.dao;

import com.fjp.entity.Product;

import java.util.List;

public interface ProductDao {
    @SuppressWarnings("unchecked")
    List<Product> findProducts(String condition);

    Product findProductById(Integer id);

    Product findProductByName(String name);


    boolean deleteProduct(Integer id);

    boolean updateProduct(Product product);
}
