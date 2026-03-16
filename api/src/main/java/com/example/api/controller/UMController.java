package com.example.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.api.entity.OperationLog;
import com.example.api.entity.UserMessage;
import com.example.api.mapper.OperationLogMapper;
import com.example.api.mapper.UMMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin


public class UMController {
    @Autowired
    private UMMapper umMapper;

    @Autowired
    private OperationLogMapper logMapper;

    @Value("${admin.username:admin}")
    private String adminUsername;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    private static final DateTimeFormatter LOG_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 取第一个 IP（多级代理时可能有多个）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private void addLog(HttpServletRequest request, String username, String action, String detail) {
        OperationLog log = new OperationLog();
        log.setTime(LocalDateTime.now().format(LOG_FMT));
        log.setIp(getClientIp(request));
        log.setUsername(username);
        log.setAction(action);
        log.setDetail(detail);
        logMapper.insert(log);
    }

    @GetMapping("/UMs")
    public List<UserMessage> queryAll() {
        QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<>();
        return umMapper.selectList(queryWrapper);
    }

    @PostMapping("/CM")
    public UserMessage add(@RequestBody UserMessage userMessage, HttpServletRequest request) {
        userMessage.setCreatetime();
        umMapper.insert(userMessage);
        String detail = userMessage.getMessage();
        if (detail != null && detail.length() > 50) detail = detail.substring(0, 50) + "...";
        addLog(request, userMessage.getUsername(), "留言", "发表留言: " + detail);
        return userMessage;
    }

    @DeleteMapping("/DM/{id}")
    public void delete(@PathVariable Integer id, HttpServletRequest request) {
        umMapper.deleteById(id);
        addLog(request, "管理员", "删除", "删除留言 ID: " + id);
    }

    @DeleteMapping("/DM/batch")
    public void deleteBatch(@RequestBody List<Integer> ids, HttpServletRequest request) {
        if (ids != null && !ids.isEmpty()) {
            umMapper.deleteBatchIds(ids);
            addLog(request, "管理员", "批量删除", "删除留言 IDs: " + ids);
        }
    }

    @PostMapping("/admin/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        Map<String, Object> result = new HashMap<>();
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            result.put("success", true);
            result.put("token", "admin-" + System.currentTimeMillis());
            addLog(request, username, "登录", "管理员登录成功");
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
            addLog(request, username != null ? username : "未知", "登录失败", "账号或密码错误");
        }
        return result;
    }

    @GetMapping("/admin/logs")
    public List<OperationLog> getLogs() {
        return logMapper.selectList(
            new QueryWrapper<OperationLog>().orderByDesc("id")
        );
    }
}
