package com.fjp.view;

import javax.swing.*;

/**
 * 添加商品种类界面
 */
public class AddSortDialog extends JDialog {
    private JPanel updateProductPanel = new JPanel();
    private JLabel nameLabel = new JLabel("名称：");
    private JLabel descriptionLabel = new JLabel("描述：");
    private JTextField name = new JTextField(10);
    private JTextField description = new JTextField(10);
    private JButton submit = new JButton("添加");

    AddSortDialog(SortFrame sortFrame) {
        init();
        submit.addActionListener(e -> {
            String name = this.name.getText();
            String description = this.description.getText();
            JOptionPane.showMessageDialog(this, sortFrame.addSort(name, description));
            sortFrame.updateSortFrame();
        });
    }

    private void init() {
        setTitle("添加商品种类信息");
        setModal(true);
        setLocationRelativeTo(null);
        setSize(500, 300);
        setLayout(null);
        updateProductPanel.setBounds(160, 0, 170, 280);
        updateProductPanel.add(nameLabel);
        updateProductPanel.add(name);
        updateProductPanel.add(descriptionLabel);
        updateProductPanel.add(description);
        updateProductPanel.add(submit);
        add(updateProductPanel);
    }
}
