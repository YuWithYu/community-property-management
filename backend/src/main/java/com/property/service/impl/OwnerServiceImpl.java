package com.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.property.entity.Owner;
import com.property.mapper.OwnerMapper;
import com.property.service.OwnerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner> implements OwnerService {

    @Override
    public Owner login(String username, String password) {
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Owner::getUsername, username)
               .eq(Owner::getPassword, password);
        return getOne(wrapper);
    }

    @Override
    public Owner register(Owner owner) {
        LambdaQueryWrapper<Owner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Owner::getUsername, owner.getUsername());
        if (count(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        owner.setStatus(1);
        owner.setCreateTime(LocalDateTime.now());
        owner.setUpdateTime(LocalDateTime.now());
        save(owner);
        return owner;
    }
}
