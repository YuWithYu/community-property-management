package com.property.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("visitor")
public class Visitor {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long ownerId;
    private String visitorName;
    private String visitorPhone;
    private String idCard;
    private String visitReason;
    private LocalDateTime visitTime;
    private Integer parkingNeeded;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
