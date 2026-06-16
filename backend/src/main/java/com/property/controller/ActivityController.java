package com.property.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.property.common.PageResult;
import com.property.common.Result;
import com.property.entity.Activity;
import com.property.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/page")
    public Result<PageResult<Activity>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status) {
        Page<Activity> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        if (title != null && !title.isEmpty()) {
            wrapper.like(Activity::getTitle, title);
        }
        if (status != null) {
            wrapper.eq(Activity::getStatus, status);
        }
        wrapper.orderByDesc(Activity::getCreateTime);
        Page<Activity> result = activityService.page(pageParam, wrapper);
        return Result.success(new PageResult<>(result.getTotal(), result.getRecords()));
    }

    @GetMapping("/list")
    public Result<List<Activity>> list() {
        List<Activity> list = activityService.list();
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Activity> getById(@PathVariable Long id) {
        Activity activity = activityService.getById(id);
        return Result.success(activity);
    }

    @PostMapping
    public Result<String> save(@RequestBody Activity activity) {
        activity.setCreateTime(LocalDateTime.now());
        activity.setUpdateTime(LocalDateTime.now());
        activityService.save(activity);
        return Result.success();
    }

    @PutMapping
    public Result<String> update(@RequestBody Activity activity) {
        activity.setUpdateTime(LocalDateTime.now());
        activityService.updateById(activity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        activityService.removeById(id);
        return Result.success();
    }
}
