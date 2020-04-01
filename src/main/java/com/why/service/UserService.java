package com.why.service;

import com.why.bean.User;
import com.why.dao.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * com.cht.integration.service.impl
 *
 * @author chenhaiting
 * @name UserServiceImpl
 * @description
 * @date 2018-05-01 17:24
 * <p>
 * <p>
 * Copyright (c) 2018  版权所有
 */
@Transactional
@Service("userService")
public class UserService {

    @Autowired
    private UserDao userDao;
 
    public int add(User user) {
//        int i = 1/0;//测试事务是否有效
        return userDao.add(user);
    }
 
    public int delete(Integer id) {
        return userDao.delete(id);
    }
 
    public int update(User user) {
        return userDao.update(user);
    }
 
    public List<User> get(User user) {
        return userDao.get(user);
    }
    public List<User> getall() {
        return userDao.getall();
    }
    public void callGetInvoice(Map map){
    	userDao.callGetInvoice(map);		
	}
    public String callGetGhid(Map map) {
    	return userDao.callGetGhid(map);
    }
    }
