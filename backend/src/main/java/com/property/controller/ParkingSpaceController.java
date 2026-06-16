package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.PageResult;
import com.property.common.Result;
import com.property.entity.ParkingSpace;
import com.property.service.OwnerService;
import com.property.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parking")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;
    
    @Autowired
    private OwnerService ownerService;

    @GetMapping("/page")
    public Result<PageResult<ParkingSpace>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String spaceNo,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) Integer status) {
        Page<ParkingSpace> pageParam = new Page<>(page, size);
        QueryWrapper<ParkingSpace> wrapper = new QueryWrapper<>();
        if (spaceNo != null && !spaceNo.isEmpty()) {
            wrapper.like("space_no", spaceNo);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        Page<ParkingSpace> result = parkingSpaceService.page(pageParam, wrapper);
        
        // 填充业主姓名
        List<ParkingSpace> records = result.getRecords().stream().map(parking -> {
            if (parking.getOwnerId() != null) {
                com.property.entity.Owner owner = ownerService.getById(parking.getOwnerId());
                if (owner != null) {
                    parking.setOwnerName(owner.getRealName());
                }
            }
            return parking;
        }).collect(Collectors.toList());
        
        return Result.success(new PageResult<>(result.getTotal(), records));
    }

    @GetMapping("/list")
    public Result<List<ParkingSpace>> list() {
        List<ParkingSpace> list = parkingSpaceService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<ParkingSpace> getById(@PathVariable Long id) {
        ParkingSpace parkingSpace = parkingSpaceService.getById(id);
        if (parkingSpace != null && parkingSpace.getOwnerId() != null) {
            com.property.entity.Owner owner = ownerService.getById(parkingSpace.getOwnerId());
            if (owner != null) {
                parkingSpace.setOwnerName(owner.getRealName());
            }
        }
        return Result.success(parkingSpace);
    }

    @PostMapping
    public Result<String> save(@RequestBody ParkingSpace parkingSpace) {
        parkingSpace.setCreateTime(LocalDateTime.now());
        parkingSpace.setUpdateTime(LocalDateTime.now());
        parkingSpaceService.save(parkingSpace);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody ParkingSpace parkingSpace) {
        parkingSpace.setUpdateTime(LocalDateTime.now());
        parkingSpaceService.updateById(parkingSpace);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        parkingSpaceService.removeById(id);
        return Result.success();
    }
}
