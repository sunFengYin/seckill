package com.feng.seckill.service;

import com.feng.seckill.entity.User;

public interface UserService {

    void register(User user);

    User login(String phone, String password);

    User findUserById(int id);

    User findUserFromCache(int id);

}
