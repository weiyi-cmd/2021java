package com.fjp.view;

import com.fjp.entity.Sort;
import com.fjp.tools.JDBCUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * 商品列表界面
 */
public abstract class ProductFrame extends JFrame {
    protected String condition2 = "";
    protected int page = 1;
    protected int countPerPage = 15;
    protected int total;
    protected int pageCount;
    private JTable productTable = new JTable();
    private JScrollPane productPanel = new JScrollPane();
    private JPanel splitPanel = new JPanel();
    private JLabel pageLabel = new JLabel("第n页/共n页 共X条记录 | ");
    private JButton firstButton = new JButton("首页");
    private JButton previousButton = new JButton("上一页");
    private JButton nextButton = new JButton("下一页");
    private JButton lastButton = new JButton("末页");
    private JTextField condition = new JTextField(10);
    private JButton queryButton = new JButton("查询");
    private JPanel queryPanel = new JPanel();
    private JButton addButton = new JButton("添加商品");
    private JButton resetPassword = new JButton("修改密码");
    private JButton productSort = new JButton("商品种类");

    public ProductFrame() {
        init();
        initService();
        updateProductFrame();
        addListener();
        pageLabel.setFont(new Font("微软雅黑",Font.PLAIN,17));
    }

    private void addListener() {
        queryButton.addActionListener(e -> {
            page = 1;
            condition2 = condition.getText();
            updateProductFrame();
        });
        condition.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    page = 1;
                    condition2 = condition.getText();
                    updateProductFrame();
                }
            }
        });
        firstButton.addActionListener(e -> {
            page = 1;
            condition.setText(condition2);
            updateProductFrame();
        });
        previousButton.addActionListener(e -> {
            page--;
            condition.setText(condition2);
            updateProductFrame();
        });
        nextButton.addActionListener(e -> {
            page++;
            condition.setText(condition2);
            updateProductFrame();
        });
        lastButton.addActionListener(e -> {
            page = pageCount;
            condition.setText(condition2);
            updateProductFrame();
        });
        addButton.addActionListener(e -> {
            new AddProductDialog(ProductFrame.this).setVisible(true);
        });
        resetPassword.addActionListener(e -> {
            new ResetPasswordDialog().setVisible(true);
        });
        productSort.addActionListener(e -> {
            toSortFrame();
        });
    }

    private void init() {
    	productTable.setRowHeight(25);
    	productTable.setFont(new Font("微软雅黑",Font.PLAIN,17));
        setTitle("商品列表");
        Toolkit kit = Toolkit.getDefaultToolkit();
        setIconImage(kit.createImage("超市.png"));
        setResizable(false);
        setSize(1000, 700);
        setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JDBCUtils.close(null, null, JDBCUtils.getConnection());
                System.exit(0);
            }
        });
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        productTable.setDefaultRenderer(Object.class, defaultTableCellRenderer);
        setLocationRelativeTo(null);
        queryPanel.add(condition);
        queryPanel.add(queryButton);
        queryPanel.add(resetPassword);
        queryPanel.add(productSort);
        splitPanel.add(pageLabel);
        splitPanel.add(firstButton);
        splitPanel.add(previousButton);
        splitPanel.add(nextButton);
        splitPanel.add(lastButton);
        splitPanel.add(addButton);
        add(queryPanel, BorderLayout.SOUTH);
        add(splitPanel, BorderLayout.CENTER);
    }

    //更新商品列表界面
    public void updateProductFrame() {
        TableModel tableModel = showProducts();
        remove(productPanel);
        productTable.setModel(tableModel);
        productTable.getColumnModel().getColumn(6).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> new JButton("修改"));
        productTable.getColumnModel().getColumn(7).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> new JButton("删除"));
        productTable.getColumnModel().getColumn(6).setCellEditor(new UpdateAndDeleteProductListener(2, ProductFrame.this));
        productTable.getColumnModel().getColumn(7).setCellEditor(new UpdateAndDeleteProductListener(1, ProductFrame.this));
        productPanel = new JScrollPane(productTable);
        productPanel.setPreferredSize(new Dimension(800, 550));
        add(productPanel, BorderLayout.NORTH);
        validate();
        pageLabel.setText(String.format("第%d页/共%d页 共%d条记录 | ", page, pageCount, total));
        firstButton.setEnabled(page != 1);
        previousButton.setEnabled(page != 1);
        lastButton.setEnabled(page != pageCount);
        nextButton.setEnabled(page != pageCount);
    }

    //删除后当前页的变化
    void afterDelete() {
        total--;
        if (total % countPerPage == 0 && total != 0) page--;
        updateProductFrame();
    }

    //添加后当前页的变化
    void afterAdd() {
        page = 1;
        condition.setText("");
        condition2 = "";
        updateProductFrame();
        page = pageCount;
        updateProductFrame();
    }

    //查询满足条件的商品
    protected abstract TableModel showProducts();

    /**
     * 修改指定的商品信息
     * @param id 要修改的商品id
     * @param name 修改后的商品名称
     * @param price 修改后的商品价格
     * @param unit 修改后的商品计价单位
     * @param count 修改后的商品数量
     * @param sort 修改后的商品种类名称
     * @return 系统提示信息
     */
    protected abstract String updateProduct(String id, String name, String price, String unit, String count, String sort);

    /**
     * 删除指定的商品信息
     * @param id 要删除的商品id
     * @return 系统提示信息
     */
    protected abstract String deleteProduct(Integer id);

    /**
     * 添加商品信息
     * @param name 添加的商品名称
     * @param price 添加的商品价格
     * @param unit 添加的商品计价单位
     * @param count 添加的商品数量
     * @param sort 添加的商品种类名称
     * @return 系统提示信息
     */
    protected abstract String addProduct(String name, String price, String unit, String count, String sort);

    /**
     * 查找商品种类信息
     * @return 所有商品种类信息
     */
    protected abstract List<Sort> findAllSort();

    /**
     * 根据商品种类名称查找商品种类
     * @param name 查找的商品种类名称
     * @return 查找的商品种类信息
     */
    protected abstract Sort findSortByName(String name);

    //初始化controller层的service
    protected abstract void initService();

    //跳转到商品种类列表界面
    protected abstract void toSortFrame();

}
