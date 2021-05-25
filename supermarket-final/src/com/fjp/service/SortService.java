package com.fjp.service;

import com.fjp.dao.ProductDao;
import com.fjp.dao.SortDao;
import com.fjp.dao.impl.ProductDaoImpl;
import com.fjp.dao.impl.SortDaoImpl;
import com.fjp.entity.Product;
import com.fjp.entity.Sort;

import java.util.List;

public class SortService {
    private SortDao sortDao = new SortDaoImpl();
    private ProductDao productDao = new ProductDaoImpl();

    public List<Sort> findAllSort() {
        return sortDao.findAllSort();
    }

    public Sort findSortByName(String name) {
        return sortDao.findSortByName(name);
    }

    public String addSort(String name, String description) {
        String message = checkSort(name, description);
        if (message != null) return message;
        if (sortDao.findSortByName(name) != null) return "商品种类名称已存在！";
        sortDao.addSort(name, description);
        return "添加成功";
    }

    public String updateSort(Integer id, String name, String description) {
        String message = checkSort(name, description);
        if (message != null) return message;
        List<Sort> sorts = sortDao.findAllSort();
        for (Sort sort : sorts) {
            if (sort.getName().equals(name) && !sort.getId().equals(id)) return "商品种类名称已存在！";
        }
        sortDao.updateSort(id, name, description);
        return "修改成功！";
    }

    private String checkSort(String name, String description) {
        if (name.equals("")) return "商品种类名称不能为空！";
        if (description.equals("")) return "商品种类描述不能为空！";
        if (name.length() > 10) return "商品种类名称长度不能超过10！";
        if (description.length() > 20) return "商品种类描述长度不能超过20！";
        return null;
    }

    public String deleteSort(Integer id) {
        for (Product product : productDao.findProducts("%%")) {
            if (product.getSortId().equals(id)) return "存在商品为此类型！";
        }
        if (!sortDao.deleteSort(id)) return "商品种类id不存在";
        return "删除成功！";
    }
}
