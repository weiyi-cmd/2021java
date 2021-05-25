package com.fjp.controller;

import com.fjp.entity.Product;
import com.fjp.entity.Sort;
import com.fjp.service.ProductService;
import com.fjp.service.SortService;
import com.fjp.view.ProductFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.List;
import java.util.Map;

/**
 * 商品界面控制类
 */
public class ProductController extends ProductFrame {
    private ProductService productService;
    private SortService sortService;
    protected void initService() {
        productService = new ProductService();
        sortService = new SortService();
    }

    @Override
    protected void toSortFrame() {
        dispose();
        new SortController().setVisible(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected TableModel showProducts() {
        String[] thead = {"商品编号", "商品名称", "商品单价(/元)", "计价单位", "数量", "类别", "修改", "删除"};
        Map<String, Object> map = productService.findProducts(condition2, page, countPerPage);
        List<Product> products = (List<Product>) map.get("message");
        total = (int) map.get("total");
        pageCount = (int) map.get("pageCount");
        Object[][] tbody = new Object[products.size()][8];
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            tbody[i][0] = product.getId();
            tbody[i][1] = product.getName();
            tbody[i][2] = product.getPrice();
            tbody[i][3] = product.getUnit();
            tbody[i][4] = product.getCount();
            tbody[i][5] = product.getSort();
            tbody[i][6] = new JButton("修改");
            tbody[i][7] = new JButton("删除");
        }
        return new DefaultTableModel(tbody, thead) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 5;
            }
        };
    }

    @Override
    protected String updateProduct(String id, String name, String price, String unit, String count, String sort) {
        return productService.updateProduct(id, name, price, unit, count, sort);
    }

    @Override
    protected String deleteProduct(Integer id) {
        return productService.deleteProduct(id);
    }

    @Override
    protected String addProduct(String name, String price, String unit, String count, String sort) {
        return productService.addProduct(name, price, unit, count, sort);
    }

    @Override
    protected List<Sort> findAllSort() {
        return sortService.findAllSort();
    }

    @Override
    protected Sort findSortByName(String name) {
        return sortService.findSortByName(name);
    }


}
