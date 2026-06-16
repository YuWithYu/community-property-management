package com.property.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.entity.ParkingSpace;
import com.property.mapper.ParkingSpaceMapper;
import com.property.service.ParkingSpaceService;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpaceServiceImpl extends ServiceImpl<ParkingSpaceMapper, ParkingSpace> implements ParkingSpaceService {
}
