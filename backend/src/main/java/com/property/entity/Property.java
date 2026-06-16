package com.property.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("property")
public class Property {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String buildingNo;
    private String unitNo;
    private String roomNo;
    private BigDecimal area;
    private String type;
    private Long ownerId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
