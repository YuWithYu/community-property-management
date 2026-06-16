package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.PageResult;
import com.property.common.Result;
import com.property.entity.Property;
import com.property.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/property")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping("/page")
    public Result<PageResult<Property>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String buildingNo,
            @RequestParam(required = false) String unitNo,
            @RequestParam(required = false) String roomNo,
            @RequestParam(required = false) Integer status) {
        Page<Property> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Property> wrapper = new LambdaQueryWrapper<>();
        if (buildingNo != null && !buildingNo.isEmpty()) {
            wrapper.like(Property::getBuildingNo, buildingNo);
        }
        if (unitNo != null && !unitNo.isEmpty()) {
            wrapper.like(Property::getUnitNo, unitNo);
        }
        if (roomNo != null && !roomNo.isEmpty()) {
            wrapper.like(Property::getRoomNo, roomNo);
        }
        if (status != null) {
            wrapper.eq(Property::getStatus, status);
        }
        wrapper.orderByDesc(Property::getCreateTime);
        Page<Property> result = propertyService.page(pageParam, wrapper);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/list")
    public Result<List<Property>> list() {
        List<Property> list = propertyService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Property> getById(@PathVariable Long id) {
        Property property = propertyService.getById(id);
        return Result.success(property);
    }

    @PostMapping
    public Result<String> save(@RequestBody Property property) {
        property.setCreateTime(LocalDateTime.now());
        property.setUpdateTime(LocalDateTime.now());
        propertyService.save(property);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody Property property) {
        property.setUpdateTime(LocalDateTime.now());
        propertyService.updateById(property);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        propertyService.removeById(id);
        return Result.success();
    }
}
