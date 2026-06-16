package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.PageResult;
import com.property.common.Result;
import com.property.entity.Repair;
import com.property.service.OwnerService;
import com.property.service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/repair")
public class RepairController {

    @Autowired
    private RepairService repairService;
    
    @Autowired
    private OwnerService ownerService;

    @GetMapping("/page")
    public Result<PageResult<Repair>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status) {
        Page<Repair> pageParam = new Page<>(page, size);
        QueryWrapper<Repair> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) {
            wrapper.like("title", title);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        Page<Repair> result = repairService.page(pageParam, wrapper);
        
        // 填充业主信息和处理结果
        List<Repair> records = result.getRecords().stream().map(repair -> {
            if (repair.getOwnerId() != null) {
                com.property.entity.Owner owner = ownerService.getById(repair.getOwnerId());
                if (owner != null) {
                    repair.setOwnerName(owner.getRealName());
                    repair.setPhone(owner.getPhone());
                }
            }
            // 地址使用location字段
            repair.setAddress(repair.getLocation());
            // 处理结果使用reply字段
            repair.setResult(repair.getReply());
            // 处理人暂时为空，可以根据业务需要设置
            repair.setHandlerName("");
            return repair;
        }).collect(Collectors.toList());
        
        return Result.success(new PageResult<>(result.getTotal(), records));
    }

    @GetMapping("/list")
    public Result<List<Repair>> list() {
        List<Repair> list = repairService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Repair> getById(@PathVariable Long id) {
        Repair repair = repairService.getById(id);
        if (repair != null && repair.getOwnerId() != null) {
            com.property.entity.Owner owner = ownerService.getById(repair.getOwnerId());
            if (owner != null) {
                repair.setOwnerName(owner.getRealName());
                repair.setPhone(owner.getPhone());
            }
            repair.setAddress(repair.getLocation());
            repair.setResult(repair.getReply());
            repair.setHandlerName("");
        }
        return Result.success(repair);
    }

    @PostMapping
    public Result<String> save(@RequestBody Repair repair) {
        repair.setCreateTime(LocalDateTime.now());
        repair.setUpdateTime(LocalDateTime.now());
        repairService.save(repair);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody Repair repair) {
        repair.setUpdateTime(LocalDateTime.now());
        repairService.updateById(repair);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        repairService.removeById(id);
        return Result.success();
    }
}
