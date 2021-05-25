package com.fjp.dao.impl;

import com.fjp.dao.ProductDao;
import com.fjp.entity.Product;
import com.fjp.tools.JDBCUtils;
import com.fjp.dao.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDaoImpl extends BaseDaoImpl implements ProductDao,AddProduct {
    @SuppressWarnings("unchecked")
    @Override
    public List<Product> findProducts(String condition) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select * from product where name like ? or id like ? or sortId in (select id from sort where name like ?)";
        Map<Integer, Object> params = new HashMap<Integer, Object>();
        params.put(1, condition);
        params.put(2, condition);
        params.put(3, condition);
        return (List<Product>) query(sql, conn, params, Product.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Product findProductById(Integer id) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select * from product where id = ?";
        Map<Integer, Object> params = new HashMap<Integer, Object>();
        params.put(1, id);
        List<Product> products = query(sql, conn, params, Product.class);
        if (products.size() == 0) return null;
        return products.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Product findProductByName(String name) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "select * from product where name = ?";
        Map<Integer, Object> params = new HashMap<Integer, Object>();
        params.put(1, name);
        List<Product> products = query(sql, conn, params, Product.class);
        if (products.size() == 0) return null;
        return products.get(0);
    }



    @Override
    public boolean deleteProduct(Integer id) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "delete from product where id = ?";
        Map<Integer, Object> params = new HashMap<Integer, Object>();
        params.put(1, id);
        return doCUD(sql, conn, params) != 0;
    }

    @Override
    public boolean updateProduct(Product product) {
        Connection conn = JDBCUtils.getConnection();
        String sql = "update product set name = ?, price = ?, unit = ?, count = ?, sortId = ? where id = ?";
        Map<Integer, Object> params = new HashMap<Integer, Object>();
        params.put(1, product.getName());
        params.put(2, product.getPrice());
        params.put(3, product.getUnit());
        params.put(4, product.getCount());
        params.put(5, product.getSortId());
        params.put(6, product.getId());
        return doCUD(sql, conn, params) != 0;
        
   
    }

	@Override
	public boolean addProduct(Product product) {
		 Connection conn = JDBCUtils.getConnection();
	        String sql = "insert into product(name, price, unit, sortId, count) values(?, ?, ?, ?, ?)";
	        Map<Integer, Object> params = new HashMap<Integer, Object>();
	        params.put(1, product.getName());
	        params.put(2, product.getPrice());
	        params.put(3, product.getUnit());
	        params.put(4, product.getSortId());
	        params.put(5, product.getCount());
	        return doCUD(sql, conn, params) != 0;
		// TODO Auto-generated method stub

	}
}
