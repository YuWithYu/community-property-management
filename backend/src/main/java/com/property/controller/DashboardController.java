package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.property.common.Result;
import com.property.entity.Complaint;
import com.property.entity.Repair;
import com.property.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private RepairService repairService;
    
    @Autowired
    private FeeService feeService;
    
    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 业主总数
        long ownerCount = ownerService.count();
        statistics.put("ownerCount", ownerCount);
        
        // 房产总数
        long propertyCount = propertyService.count();
        statistics.put("propertyCount", propertyCount);
        
        // 待处理报修
        QueryWrapper<Repair> repairWrapper = new QueryWrapper<>();
        repairWrapper.eq("status", 0);
        long pendingRepair = repairService.count(repairWrapper);
        statistics.put("pendingRepair", pendingRepair);
        
        // 本月应收费用
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.withDayOfMonth(1);
        QueryWrapper<com.property.entity.Fee> feeWrapper = new QueryWrapper<>();
        feeWrapper.ge("create_time", firstDay.atStartOfDay());
        BigDecimal monthFee = feeService.list(feeWrapper)
            .stream()
            .map(com.property.entity.Fee::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("monthFee", monthFee);
        
        return Result.success(statistics);
    }

    @GetMapping("/feeChart")
    public Result<Map<String, Object>> getFeeChart() {
        Map<String, Object> result = new HashMap<>();
        List<String> months = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        // 获取最近6个月的数据
        for (int i = 5; i >= 0; i--) {
            LocalDate month = LocalDate.now().minusMonths(i);
            String monthStr = month.format(formatter);
            months.add(monthStr);
            
            LocalDate firstDay = month.withDayOfMonth(1);
            LocalDate lastDay = month.withDayOfMonth(month.lengthOfMonth());
            
            QueryWrapper<com.property.entity.Fee> wrapper = new QueryWrapper<>();
            wrapper.between("create_time", firstDay.atStartOfDay(), lastDay.atTime(23, 59, 59));
            BigDecimal total = feeService.list(wrapper)
                .stream()
                .map(com.property.entity.Fee::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            values.add(total);
        }
        
        result.put("months", months);
        result.put("values", values);
        return Result.success(result);
    }

    @GetMapping("/repairChart")
    public Result<List<Map<String, Object>>> getRepairChart() {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 待处理
        QueryWrapper<Repair> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("status", 0);
        long pending = repairService.count(pendingWrapper);
        Map<String, Object> pendingMap = new HashMap<>();
        pendingMap.put("name", "待处理");
        pendingMap.put("value", pending);
        result.add(pendingMap);
        
        // 处理中
        QueryWrapper<Repair> processingWrapper = new QueryWrapper<>();
        processingWrapper.eq("status", 1);
        long processing = repairService.count(processingWrapper);
        Map<String, Object> processingMap = new HashMap<>();
        processingMap.put("name", "处理中");
        processingMap.put("value", processing);
        result.add(processingMap);
        
        // 已完成
        QueryWrapper<Repair> completedWrapper = new QueryWrapper<>();
        completedWrapper.eq("status", 2);
        long completed = repairService.count(completedWrapper);
        Map<String, Object> completedMap = new HashMap<>();
        completedMap.put("name", "已完成");
        completedMap.put("value", completed);
        result.add(completedMap);
        
        return Result.success(result);
    }

    @GetMapping("/latestRepairs")
    public Result<List<Map<String, Object>>> getLatestRepairs() {
        QueryWrapper<Repair> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.last("limit 5");
        List<Repair> repairs = repairService.list(wrapper);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Repair repair : repairs) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", repair.getId());
            map.put("content", repair.getContent());
            map.put("status", repair.getStatus());
            map.put("createTime", repair.getCreateTime());
            
            // 获取业主姓名
            com.property.entity.Owner owner = ownerService.getById(repair.getOwnerId());
            map.put("ownerName", owner != null ? owner.getRealName() : "未知");
            
            result.add(map);
        }
        
        return Result.success(result);
    }

    @GetMapping("/latestComplaints")
    public Result<List<Map<String, Object>>> getLatestComplaints() {
        QueryWrapper<Complaint> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.last("limit 5");
        List<Complaint> complaints = complaintService.list(wrapper);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Complaint complaint : complaints) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", complaint.getId());
            map.put("content", complaint.getContent());
            map.put("status", complaint.getStatus());
            map.put("createTime", complaint.getCreateTime());
            
            // 获取业主姓名
            com.property.entity.Owner owner = ownerService.getById(complaint.getOwnerId());
            map.put("ownerName", owner != null ? owner.getRealName() : "未知");
            
            result.add(map);
        }
        
        return Result.success(result);
    }
}
