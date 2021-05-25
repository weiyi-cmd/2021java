package com.fjp.view;

import com.fjp.dao.AddProduct;
import com.fjp.dao.AddProducttoTXT;
import com.fjp.dao.ProductDao;
import com.fjp.dao.SortDao;
import com.fjp.dao.impl.ProductDaoImpl;
import com.fjp.dao.impl.SortDaoImpl;
import com.fjp.entity.Product;
import com.fjp.entity.Sort;
import com.fjp.tools.JDBCUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品列表界面
 */
public class CustomerProductFrame extends JFrame {
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
    private JButton resetPassword = new JButton("修改密码");

    public CustomerProductFrame() {
        init();
    //    initService();
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
        resetPassword.addActionListener(e -> {
            new ResetPasswordDialog().setVisible(true);
        });
    }

    private void init() {
    	productTable.setRowHeight(25);
    	productTable.setFont(new Font("微软雅黑",Font.PLAIN,17));
        setTitle("商品列表");
        Toolkit kit = Toolkit.getDefaultToolkit();
        setIconImage(kit.createImage("超市.png"));
        setResizable(true);
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
        splitPanel.add(pageLabel);
        splitPanel.add(firstButton);
        splitPanel.add(previousButton);
        splitPanel.add(nextButton);
        splitPanel.add(lastButton);
        add(queryPanel, BorderLayout.SOUTH);
        add(splitPanel, BorderLayout.CENTER);
    }

    //更新商品列表界面
    public void updateProductFrame() {
        TableModel tableModel = showProducts();
        remove(productPanel);
        productTable.setModel(tableModel);
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


    //查询满足条件的商品
    //protected abstract TableModel showProducts();
    private ProductDao productDao = new ProductDaoImpl();
    private SortDao sortDao = new SortDaoImpl();
    private AddProduct addProducts = new ProductDaoImpl();
    private AddProduct addProducts2 = new AddProducttoTXT();
    protected TableModel showProducts() {
        String[] thead = {"商品编号", "商品名称", "商品单价(/元)", "计价单位", "数量", "类别"};
        Map<String, Object> map = findProducts(condition2, page, countPerPage);
        List<Product> products = (List<Product>) map.get("message");
        total = (int) map.get("total");
        pageCount = (int) map.get("pageCount");
        Object[][] tbody = new Object[products.size()][6];
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            tbody[i][0] = product.getId();
            tbody[i][1] = product.getName();
            tbody[i][2] = product.getPrice();
            tbody[i][3] = product.getUnit();
            tbody[i][4] = product.getCount();
            tbody[i][5] = product.getSort();
        }
        return new DefaultTableModel(tbody, thead) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 10;
            }
        };
    }
    
    /**
     * 查找商品种类信息
     * @return 所有商品种类信息
     */
    protected List<Sort> findAllSort() {
		return null;
	}

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
    
    /**
     * 根据商品种类名称查找商品种类
     * @param name 查找的商品种类名称
     * @return 查找的商品种类信息
     */
    protected Sort findSortByName(String name) {
		return null;
	}
    
    //初始化controller层的service
    protected void initService() {
	}

}