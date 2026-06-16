package com.property.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.entity.Activity;
import com.property.mapper.ActivityMapper;
import com.property.service.ActivityService;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
}
