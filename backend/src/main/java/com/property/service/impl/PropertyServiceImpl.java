package com.property.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.entity.Property;
import com.property.mapper.PropertyMapper;
import com.property.service.PropertyService;
import org.springframework.stereotype.Service;

@Service
public class PropertyServiceImpl extends ServiceImpl<PropertyMapper, Property> implements PropertyService {
}
