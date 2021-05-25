package com.fjp.view;

import com.fjp.tools.JDBCUtils;

import javax.swing.*;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 登录界面
 */
public abstract class LoginFrame extends JFrame {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel identityLabel;
    private JTextField username;
    private JPasswordField password;
    private JButton login = new JButton("登录");
    private JButton register = new JButton("注册");
    private JPanel loginPanel = new JPanel();
	String[] identities = new String[] {"管理员","顾客"};
	protected final JComboBox<String> comboBox = new JComboBox<String>(identities);

    public LoginFrame() {
    	
    	usernameLabel = new JLabel("账号：");
    //	usernameLabel.setIcon(new ImageIcon(LoginFrame.class.getResource("用户.png")));
    //	usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
    	username = new JTextField(10);
    	passwordLabel = new JLabel("密码：");
    	password = new JPasswordField(10);
    	identityLabel = new JLabel("身份：");
    	comboBox.setSelectedIndex(1);
    	
    	
    	
        init();
        login.addActionListener(e -> {
            showAdminFrame(username.getText(), password.getText());
        });
        
        password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                	showAdminFrame(username.getText(), password.getText());
            }
        });
        
        register.addActionListener(e -> {
            showRegisterFrame();
        });
    }

    private void init() {
        setTitle("登录界面");
        Toolkit kit = Toolkit.getDefaultToolkit();
        setIconImage(kit.createImage("超市.png"));
        setBounds(100, 100, 555, 452);
        setSize(600, 400);
        setResizable(true);
        setLayout(null);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JDBCUtils.close(null, null, JDBCUtils.getConnection());
                System.exit(0);
            }
        });
        
        
        //将内容添加到面板
        loginPanel.setBounds(215, 40, 170, 280);
        loginPanel.add(usernameLabel);
        loginPanel.add(username);
        loginPanel.add(passwordLabel);
        loginPanel.add(password);
        loginPanel.add(identityLabel);
        loginPanel.add(comboBox);
        loginPanel.add(login);
        loginPanel.add(register);
        add(loginPanel);
    }

    /**
     * 验证登录信息
     * @param username 登录的账号
     * @param password 登录的密码
     */
    protected abstract void showAdminFrame(String username, String password);

    //跳转到注册界面
    protected abstract void showRegisterFrame();
}
