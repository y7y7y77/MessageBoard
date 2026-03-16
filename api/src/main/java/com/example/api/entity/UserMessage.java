package com.example.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName("usermessage")
public class UserMessage {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createtime;
    public void setCreatetime(){
        this.createtime = LocalDateTime.now();
    }
    @Override

    public String toString() {
        return "UserMessage{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", createtime=" + createtime +
                '}';
    }
}
