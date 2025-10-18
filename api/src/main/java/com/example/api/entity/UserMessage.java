package com.example.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private LocalDateTime createtime;

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
