package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.PageResult;
import com.property.common.Result;
import com.property.entity.Complaint;
import com.property.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/complaint")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/page")
    public Result<PageResult<Complaint>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status) {
        Page<Complaint> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Complaint> wrapper = new LambdaQueryWrapper<>();
        if (title != null && !title.isEmpty()) {
            wrapper.like(Complaint::getTitle, title);
        }
        if (status != null) {
            wrapper.eq(Complaint::getStatus, status);
        }
        wrapper.orderByDesc(Complaint::getCreateTime);
        Page<Complaint> result = complaintService.page(pageParam, wrapper);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/list")
    public Result<List<Complaint>> list() {
        List<Complaint> list = complaintService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Complaint> getById(@PathVariable Long id) {
        Complaint complaint = complaintService.getById(id);
        return Result.success(complaint);
    }

    @PostMapping
    public Result<String> save(@RequestBody Complaint complaint) {
        complaint.setCreateTime(LocalDateTime.now());
        complaint.setUpdateTime(LocalDateTime.now());
        complaintService.save(complaint);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody Complaint complaint) {
        complaint.setUpdateTime(LocalDateTime.now());
        complaintService.updateById(complaint);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        complaintService.removeById(id);
        return Result.success();
    }
}
