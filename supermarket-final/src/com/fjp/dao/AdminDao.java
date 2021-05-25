package com.fjp.dao;

import com.fjp.entity.Admin;

public interface AdminDao {
    Admin login(String username, String password);

    boolean resetPassword(String username, String newPassword, String password);

    Admin findAdminByUsername(String username);

	boolean register(String username, String password);
}
