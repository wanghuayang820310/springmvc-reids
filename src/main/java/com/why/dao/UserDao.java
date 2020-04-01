package com.why.dao;


import com.why.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    int add(User user);

    int delete(Integer id);

    int update(User user);

    List<User> get(User user);
    List<User> getall();
    String callGetGhid(Map map);
    void callGetInvoice(Map map);

}
