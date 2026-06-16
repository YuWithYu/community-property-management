package com.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.entity.Admin;

public interface AdminService extends IService<Admin> {
    Admin login(String username, String password);
}
