package com.example.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("operation_log")
public class OperationLog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String time;
    private String ip;
    private String username;
    private String action;
    private String detail;
}
