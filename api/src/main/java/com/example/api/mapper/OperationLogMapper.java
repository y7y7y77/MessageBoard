package com.example.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.api.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
