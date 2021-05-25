package com.fjp.view;

import com.fjp.entity.Product;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * 修改删除商品监听器
 */
public class UpdateAndDeleteProductListener extends AbstractCellEditor implements TableCellEditor {
    private static final long serialVersionUID = 1L;

    private int mode;
    private ProductFrame productFrame;

    UpdateAndDeleteProductListener(int mode, ProductFrame productFrame) {
        this.mode = mode;
        this.productFrame = productFrame;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (mode == 1) {
            if (JOptionPane.showConfirmDialog(productFrame, "是否删除此商品？") == JOptionPane.OK_OPTION) {
                Integer id = (Integer) table.getValueAt(row, 0);
                String message = productFrame.deleteProduct(id);
                productFrame.afterDelete();
                JOptionPane.showMessageDialog(productFrame, message);
            }
        } else {
            Integer id = (Integer) table.getValueAt(row, 0);
            String name = (String) table.getValueAt(row, 1);
            Double price = (Double) table.getValueAt(row, 2);
            String unit = (String) table.getValueAt(row, 3);
            Integer count = (Integer) table.getValueAt(row, 4);
            String sort = (String) table.getValueAt(row, 5);
            Integer sortId = null;
            if (productFrame.findSortByName(sort) != null) sortId = productFrame.findSortByName(sort).getId();
            Product product = new Product(id, name, price, unit, count, sortId);
            product.setSort(sort);
            new UpdateProductDialog(product, productFrame).setVisible(true);
        }
        return null;
    }
}