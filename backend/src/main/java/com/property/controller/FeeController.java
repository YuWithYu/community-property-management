package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.PageResult;
import com.property.common.Result;
import com.property.entity.Fee;
import com.property.service.FeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/fee")
public class FeeController {

    @Autowired
    private FeeService feeService;

    @GetMapping("/page")
    public Result<PageResult<Fee>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer status) {
        Page<Fee> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Fee> wrapper = new LambdaQueryWrapper<>();
        if (ownerId != null) {
            wrapper.eq(Fee::getOwnerId, ownerId);
        }
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Fee::getType, type);
        }
        if (status != null) {
            wrapper.eq(Fee::getStatus, status);
        }
        wrapper.orderByDesc(Fee::getCreateTime);
        Page<Fee> result = feeService.page(pageParam, wrapper);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/list")
    public Result<List<Fee>> list() {
        List<Fee> list = feeService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Fee> getById(@PathVariable Long id) {
        Fee fee = feeService.getById(id);
        return Result.success(fee);
    }

    @PostMapping
    public Result<String> save(@RequestBody Fee fee) {
        fee.setCreateTime(LocalDateTime.now());
        fee.setUpdateTime(LocalDateTime.now());
        feeService.save(fee);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody Fee fee) {
        fee.setUpdateTime(LocalDateTime.now());
        feeService.updateById(fee);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        feeService.removeById(id);
        return Result.success();
    }

    @PutMapping("/pay/{id}")
    public Result<String> pay(@PathVariable Long id) {
        Fee fee = feeService.getById(id);
        if (fee != null) {
            fee.setStatus(1);
            fee.setPayTime(LocalDateTime.now());
            fee.setUpdateTime(LocalDateTime.now());
            feeService.updateById(fee);
        }
        return Result.success();
    }
}
