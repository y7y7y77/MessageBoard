package com.example.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.api.entity.UserMessage;
import com.example.api.mapper.UMMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin


public class UMController {
    @Autowired
    private UMMapper umMapper;
    @GetMapping("/UMs")
    public List<UserMessage> queryAll() {
        QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<>();
        return umMapper.selectList(queryWrapper);
    }
}
