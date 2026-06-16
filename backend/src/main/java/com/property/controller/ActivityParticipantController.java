package com.property.controller;

import com.property.common.Result;
import com.property.entity.ActivityParticipant;
import com.property.service.ActivityParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/activity-participant")
public class ActivityParticipantController {

    @Autowired
    private ActivityParticipantService activityParticipantService;

    @GetMapping("/list")
    public Result<List<ActivityParticipant>> list() {
        List<ActivityParticipant> list = activityParticipantService.list();
        return Result.success(list);
    }

    @PostMapping
    public Result<String> save(@RequestBody ActivityParticipant participant) {
        participant.setCreateTime(LocalDateTime.now());
        activityParticipantService.save(participant);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        activityParticipantService.removeById(id);
        return Result.success();
    }
}
