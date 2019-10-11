package cn.huyingjie.grabticket.service.Impl;

import cn.huyingjie.grabticket.entity.City;
import cn.huyingjie.grabticket.entity.User;
import cn.huyingjie.grabticket.mapper.slave.CityMapper;
import cn.huyingjie.grabticket.mapper.master.UserMapper;
import cn.huyingjie.grabticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    // 主数据源
    private UserMapper userMapper;

    @Autowired
    // 从数据源
    private CityMapper cityMapper;



    @Override
    public User findByName(String userName) {

        City city = cityMapper.getCityByName("上海");
        User user = userMapper.getUserByName(userName);
        user.setCity(city);
        return user;
    }


}
