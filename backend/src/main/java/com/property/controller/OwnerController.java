package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.PageResult;
import com.property.common.Result;
import com.property.entity.Owner;
import com.property.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/page")
    public Result<PageResult<Owner>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) String phone) {
        Page<Owner> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        if (realName != null && !realName.isEmpty()) {
            wrapper.like(Owner::getRealName, realName);
        }
        if (phone != null && !phone.isEmpty()) {
            wrapper.like(Owner::getPhone, phone);
        }
        wrapper.orderByDesc(Owner::getCreateTime);
        Page<Owner> result = ownerService.page(pageParam, wrapper);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/list")
    public Result<List<Owner>> list() {
        List<Owner> list = ownerService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Owner> getById(@PathVariable Long id) {
        Owner owner = ownerService.getById(id);
        return Result.success(owner);
    }

    @PostMapping
    public Result<String> save(@RequestBody Owner owner) {
        owner.setCreateTime(LocalDateTime.now());
        owner.setUpdateTime(LocalDateTime.now());
        ownerService.save(owner);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody Owner owner) {
        owner.setUpdateTime(LocalDateTime.now());
        ownerService.updateById(owner);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        ownerService.removeById(id);
        return Result.success();
    }
}
