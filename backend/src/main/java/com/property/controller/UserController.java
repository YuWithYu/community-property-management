package com.property.controller;

import com.property.common.Result;
import com.property.entity.Admin;
import com.property.entity.Owner;
import com.property.service.AdminService;
import com.property.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params, HttpSession session) {
        String username = params.get("username");
        String password = params.get("password");
        String type = params.get("type");
        
        Map<String, Object> result = new HashMap<>();
        
        // type: "1"=业主, "2"=管理员
        if ("2".equals(type) || "admin".equals(type)) {
            Admin admin = adminService.login(username, password);
            if (admin != null) {
                session.setAttribute("user", admin);
                session.setAttribute("userType", "admin");
                result.put("user", admin);
                result.put("type", 2);
                return Result.success(result);
            }
        } else {
            Owner owner = ownerService.login(username, password);
            if (owner != null) {
                session.setAttribute("user", owner);
                session.setAttribute("userType", "owner");
                result.put("user", owner);
                result.put("type", 1);
                return Result.success(result);
            }
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result<Owner> register(@RequestBody Owner owner) {
        Owner registered = ownerService.register(owner);
        return Result.success(registered);
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> info(HttpSession session) {
        Object user = session.getAttribute("user");
        String userType = (String) session.getAttribute("userType");
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("type", userType);
        return Result.success(result);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.invalidate();
        return Result.success();
    }

    @PutMapping("/update")
    public Result<String> update(@RequestBody Owner owner, HttpSession session) {
        ownerService.updateById(owner);
        session.setAttribute("user", owner);
        return Result.success();
    }

    @PutMapping("/password")
    public Result<String> updatePassword(@RequestBody Map<String, String> params, HttpSession session) {
        String newPassword = params.get("newPassword");
        Owner owner = (Owner) session.getAttribute("user");
        if (owner != null) {
            // 更新密码
            owner.setPassword(newPassword);
            ownerService.updateById(owner);
            return Result.success();
        }
        return Result.error("用户未登录");
    }
}
