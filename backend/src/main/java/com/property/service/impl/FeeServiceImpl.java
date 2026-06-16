package com.property.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.entity.Fee;
import com.property.mapper.FeeMapper;
import com.property.service.FeeService;
import org.springframework.stereotype.Service;

@Service
public class FeeServiceImpl extends ServiceImpl<FeeMapper, Fee> implements FeeService {
}
