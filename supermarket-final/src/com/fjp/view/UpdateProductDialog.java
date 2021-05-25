package com.fjp.view;

import com.fjp.entity.Product;
import com.fjp.entity.Sort;

import javax.swing.*;
import java.util.List;

/**
 * 修改商品界面
 */
public class UpdateProductDialog extends JDialog {
    private Product product;
    private JPanel updateProductPanel = new JPanel();
    private JLabel nameLabel = new JLabel("名称：");
    private JLabel priceLabel = new JLabel("价格：");
    private JLabel unitLabel = new JLabel("单位：");
    private JLabel sortIdLabel = new JLabel("类别：");
    private JLabel countLabel = new JLabel("数量：");
    private JTextField name = new JTextField(10);
    private JTextField price = new JTextField(10);
    private JTextField unit = new JTextField(10);
    private JSpinner sort = new JSpinner();
    private JTextField count = new JTextField(10);
    private JButton submit = new JButton("修改");

    UpdateProductDialog(Product product, ProductFrame productFrame) {
        this.product = product;
        init();
        List<Sort> sorts = productFrame.findAllSort();
        String[] sortNames = new String[sorts.size()];
        for (int i = 0; i < sorts.size(); i++) {
            sortNames[i] = sorts.get(i).getName();
        }
        sort.setModel(new SpinnerListModel(sortNames));
        submit.addActionListener(e -> {
            String id = product.getId().toString();
            String name = this.name.getText();
            String price = this.price.getText();
            String unit = this.unit.getText();
            String sort = (String) this.sort.getValue();
            String count = this.count.getText();
            String message = productFrame.updateProduct(id, name, price, unit, count, sort);
            JOptionPane.showMessageDialog(this, message);
            productFrame.updateProductFrame();
        });
    }

    private void init() {
        setTitle("修改商品信息");
        setModal(true);
        setLocationRelativeTo(null);
        setSize(500, 300);
        setLayout(null);
        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        unit.setText(product.getUnit());
        count.setText(String.valueOf(product.getCount()));
        updateProductPanel.setBounds(160, 30, 180, 200);
        updateProductPanel.add(nameLabel);
        updateProductPanel.add(name);
        updateProductPanel.add(priceLabel);
        updateProductPanel.add(price);
        updateProductPanel.add(unitLabel);
        updateProductPanel.add(unit);
        updateProductPanel.add(countLabel);
        updateProductPanel.add(count);
        updateProductPanel.add(sortIdLabel);
        updateProductPanel.add(sort);
        updateProductPanel.add(submit);
        add(updateProductPanel);
    }
}
