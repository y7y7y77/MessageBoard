package com.example.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 将 Vue Router history 模式的前端路由转发到 index.html
 * 仅匹配精确路径，不影响 /admin/login、/admin/logs 等 REST 接口
 */
@Controller
public class SpaForwardController {

    @GetMapping("/admin")
    public String forwardAdmin() {
        return "forward:/index.html";
    }
}
