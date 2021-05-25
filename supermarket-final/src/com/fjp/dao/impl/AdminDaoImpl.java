package com.fjp.dao.impl;

import com.fjp.view.CustomerProductFrame;
import com.fjp.controller.LoginController;
import com.fjp.controller.ProductController;
import com.fjp.dao.AdminDao;
import com.fjp.entity.Admin;
import com.fjp.tools.JDBCUtils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fjp.view.LoginFrame;
import com.fjp.view.RegisterDialog;

import javax.swing.*;




public class AdminDaoImpl extends BaseDaoImpl implements AdminDao {

	

	public class Type1 extends LoginFrame{
		protected void showAdminFrame(String username, String password) {
		}

	    protected void showRegisterFrame() {
		}
	    public int type() {
	    	
	    	int y = -1; 
	    	if(comboBox.getSelectedItem() == "顾客") {
	      	  y = 1;
	    	}
	    	if(comboBox.getSelectedItem() == "管理员") {
	      	  y = 2;
	    	}
	    	return y;
	    }
	}	
	
	
    @SuppressWarnings("unchecked")
    @Override
    public Admin login(String username, String password) {
    	Type1 t1 = new Type1(); 
    	String sql = "select * from admin where username = ? and password = ?";
    	if(t1.type() == 1) {
    		sql = "select * from customer where username = ? and password = ?";
          }
    	if(t1.type() == 2) {
    		sql = "select * from admin where username = ? and password = ?";
    	}
		Connection conn = JDBCUtils.getConnection();
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(1, username);
		params.put(2, password);
		List<Admin> admins = query(sql, conn, params, Admin.class);
		if (admins.size() > 0) return admins.get(0);
		return null;

    }

    @Override
    public boolean resetPassword(String username, String newPassword, String password) {
    	Type1 t1 = new Type1(); 
        String sql = "update customer set password = ? where username = ? and password = ?";
    	if(t1.type() == 1) {
             sql = "update customer set password = ? where username = ? and password = ?";
    	}
    	if(t1.type() == 2) {
            sql = "update admin set password = ? where username = ? and password = ?";
    	}
        Connection conn = JDBCUtils.getConnection();
        Map<Integer, Object> params = new HashMap<Integer, Object>();
        params.put(1, newPassword);
        params.put(2, username);
        params.put(3, password);
        return doCUD(sql, conn, params) != 0;
    }

    public boolean register(String username, String password) {
    	String sql = "insert into customer values(?, ?)";
    	Connection conn = JDBCUtils.getConnection();
    	Map<Integer, Object> params = new HashMap<Integer, Object>();
    	params.put(1, username);
    	params.put(2, password);
    	return doCUD(sql, conn, params) != 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Admin findAdminByUsername(String username) {
        String sql = "select * from admin where username = ?";
        Connection conn = JDBCUtils.getConnection();
        Map<Integer, Object> params = new HashMap<Integer, Object>();
        params.put(1, username);
        List<Admin> admins = query(sql, conn, params, Admin.class);
        if (admins.size() > 0) return admins.get(0);
        return null;
    }


}
