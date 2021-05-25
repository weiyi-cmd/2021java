package com.fjp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fjp.dao.ProductDao;
import com.fjp.dao.SortDao;
import com.fjp.dao.impl.ProductDaoImpl;
import com.fjp.dao.impl.SortDaoImpl;
import com.fjp.entity.Product;
import com.fjp.entity.Sort;
import com.fjp.dao.*;

public class ProductService {
    private ProductDao productDao = new ProductDaoImpl();
    private SortDao sortDao = new SortDaoImpl();
    private AddProduct addProducts = new ProductDaoImpl();
    private AddProduct addProducts2 = new AddProducttoTXT();

    public Map<String, Object> findProducts(String condition, int page, int countPerPage) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Product> products = productDao.findProducts("%" + condition + "%");
        int begin = (page - 1) * countPerPage;
        int end = begin + countPerPage;
        int total = products.size();
        int pageCount = (total - 1) / countPerPage + 1;
        if (end > products.size()) end = products.size();
        result.put("message", products.subList(begin, end));
        result.put("total", total);
        result.put("pageCount", pageCount);
        for (Product product : products) {
            Sort sort = sortDao.findSortById(product.getSortId());
            if (sort != null) product.setSort(sort.getName());
        }
        return result;
    }

    public String addProduct(String name, String price, String unit, String count, String sort) {
        String message = checkProduct(name, price, unit, count, sort);
        if (message != null) return message;
        if (productDao.findProductByName(name) != null) return "商品名称已存在！";
        Integer count2 = Integer.parseInt(count);
        Double price2 = Double.parseDouble(price);
        Sort sort2 = sortDao.findSortByName(sort);
        Integer sortId = sort2.getId();
        Product product = new Product(null, name, price2, unit, count2, sortId);
        addProducts.addProduct(product);
        addProducts2.addProduct(product);
        return "添加成功！";
    }

    public String updateProduct(String id, String name, String price, String unit, String count, String sort) {
        String message = checkProduct(name, price, unit, count, sort);
        if (message != null) return message;
        Integer id2 = Integer.parseInt(id);
        Integer count2 = Integer.parseInt(count);
        Double price2 = Double.parseDouble(price);
        Sort sort2 = sortDao.findSortByName(sort);
        if (sort2 == null) return "商品种类不存在！";
        List<Product> products = productDao.findProducts("%%");
        for (Product product : products) {
            if (product.getName().equals(name) && !product.getId().equals(id2)) return "商品名称已存在！";
        }
        Integer sortId = sort2.getId();
        Product product = new Product(id2, name, price2, unit, count2, sortId);
        productDao.updateProduct(product);
        return "修改成功！";
    }

    public String deleteProduct(Integer id) {
        if (productDao.deleteProduct(id)) return "删除成功！";
        return "商品编号不存在！";
    }

    private String checkProduct(String name, String price, String unit, String count, String type) {
        if (name.length() > 10) return "商品名称长度不能超过10！";
        if (unit.length() > 10) return "商品计价单位长度不能超过10！";
        if (name.equals("")) return "商品名称不能为空！";
        if (unit.equals("")) return "商品计价单位不能为空！";
        try {
            int count2 = Integer.parseInt(count);
            if (count2 <= 0) return "商品数量必须为正整数！";
        } catch (NumberFormatException e) {
            return "商品数量必须为正整数！";
        }
        try {
            double price2 = Double.parseDouble(price);
            if (price2 <= 0) return "商品价格必须为正实数！";
        } catch (NumberFormatException e) {
            return "商品价格必须为正实数";
        }
        Sort sort = sortDao.findSortByName(type);
        if (sort == null) return "没有找到此类别！";
        return null;
    }
}
