package com.property.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.entity.ActivityParticipant;
import com.property.mapper.ActivityParticipantMapper;
import com.property.service.ActivityParticipantService;
import org.springframework.stereotype.Service;

@Service
public class ActivityParticipantServiceImpl extends ServiceImpl<ActivityParticipantMapper, ActivityParticipant> implements ActivityParticipantService {
}
