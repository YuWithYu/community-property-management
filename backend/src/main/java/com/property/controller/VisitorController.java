package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.PageResult;
import com.property.common.Result;
import com.property.entity.Visitor;
import com.property.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @GetMapping("/page")
    public Result<PageResult<Visitor>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String visitorName,
            @RequestParam(required = false) Integer status) {
        Page<Visitor> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Visitor> wrapper = new LambdaQueryWrapper<>();
        if (visitorName != null && !visitorName.isEmpty()) {
            wrapper.like(Visitor::getVisitorName, visitorName);
        }
        if (status != null) {
            wrapper.eq(Visitor::getStatus, status);
        }
        wrapper.orderByDesc(Visitor::getCreateTime);
        Page<Visitor> result = visitorService.page(pageParam, wrapper);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/list")
    public Result<List<Visitor>> list() {
        List<Visitor> list = visitorService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Visitor> getById(@PathVariable Long id) {
        Visitor visitor = visitorService.getById(id);
        return Result.success(visitor);
    }

    @PostMapping
    public Result<String> save(@RequestBody Visitor visitor) {
        visitor.setCreateTime(LocalDateTime.now());
        visitor.setUpdateTime(LocalDateTime.now());
        visitorService.save(visitor);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody Visitor visitor) {
        visitor.setUpdateTime(LocalDateTime.now());
        visitorService.updateById(visitor);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        visitorService.removeById(id);
        return Result.success();
    }

    @PutMapping("/approve/{id}")
    public Result<String> approve(@PathVariable Long id) {
        Visitor visitor = visitorService.getById(id);
        if (visitor != null) {
            visitor.setStatus(1);
            visitor.setUpdateTime(LocalDateTime.now());
            visitorService.updateById(visitor);
        }
        return Result.success();
    }
}
