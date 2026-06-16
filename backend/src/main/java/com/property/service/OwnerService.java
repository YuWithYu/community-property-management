package com.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.property.entity.Owner;

public interface OwnerService extends IService<Owner> {
    Owner login(String username, String password);
    Owner register(Owner owner);
}
