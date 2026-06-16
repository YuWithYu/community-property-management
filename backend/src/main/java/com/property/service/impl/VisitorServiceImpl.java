package com.property.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.entity.Visitor;
import com.property.mapper.VisitorMapper;
import com.property.service.VisitorService;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements VisitorService {
}
