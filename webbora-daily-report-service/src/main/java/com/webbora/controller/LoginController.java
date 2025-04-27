package com.webbora.controller;

/**
 * @desc: TODO
 * @author: Jupiter.Lin
 * @date: 2025/4/27
 */

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(@RequestParam(required = false) String username,@RequestParam(required = false) String password,
                        HttpServletRequest request) {
        // Redirect to login page if the request is not POST
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return "redirect:/";
        }
        // Validate username and password
        if (authenticateUser(username, password)) {
            StpUtil.login(username);
            return "redirect:/"; // Redirect to homepage on successful login
        } else {
            return "login"; // Return to login page on failure
        }
    }

    @GetMapping("/logout")
    public String logout() {
        StpUtil.logout();
        return "redirect:/login"; // 注销后重定向到登录页面
    }

    @GetMapping({"/","/index"})
    public String index() {
        if (!StpUtil.isLogin()) {
            return "login"; // 未登录时重定向到login.html
        }
        return "index";
    }



    // TODO: 验证用户名和密码的逻辑
    private boolean authenticateUser(String username, String password) {
        return "admin".equals(username) && "123".equals(password);
    }
}
