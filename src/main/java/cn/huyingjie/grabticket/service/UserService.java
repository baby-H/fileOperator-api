package cn.huyingjie.grabticket.service;

import cn.huyingjie.grabticket.entity.User;

public interface UserService {
    User findByName(String userName);
}
