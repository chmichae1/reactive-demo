package com.example.reactive.utils;

import com.example.reactive.entity.Users;
import com.example.reactive.model.User;
import org.springframework.beans.BeanUtils;

public class AppUtils {
    public static User entityToModel(Users users) {
        User user = new User();
        BeanUtils.copyProperties(users, user);
        return user;
    }

    public static Users modelToEntity(User user) {
        Users users = new Users();
        BeanUtils.copyProperties(user, users);
        return users;
    }
}
