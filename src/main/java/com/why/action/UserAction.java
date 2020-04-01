package com.why.action;

import com.why.bean.User;
import com.why.bean.util.PublicInfo;
import com.why.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther
 * @Date:2020/3/13
 * @Description:com.why.web
 * @version:1.0
 */

@Controller
//此处省略RequestMapping注解的话就会少一级路径
@RequestMapping("hello")
public class UserAction {

    public UserAction() {
        System.out.println("UserAction被创建");
    }

    @Resource
    UserService userService;
    @Autowired
    private PublicInfo publicInfo;
    Logger loger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    //http://localhost:8080//hello/test
    //http://localhost:8080//hello/test1
    //http://localhost:8080//hello/test2
//    @RequestMapping("/test")
//    value 可以指定多个请求地址，不过感觉没啥用
    @RequestMapping(value = {"/test1", "/test2"})
    public String toHello() {
        System.out.println("springmvc与spring整合成功！");
        loger.info("springmvc与spring整合成功！");
        loger.error("springmvc与spring整合成功！假的error");
        return "hello";
    }

    //http://localhost:8080//hello/testparam?username=why
    @RequestMapping("/testparam")
//  如果入参和变量名一样可以省略RequestParam
//  required 请求参数是否必须的
//    defaultValue 默认值，如果没有传则使用默认值
    public String toParam(@RequestParam("username") String user, @RequestParam(value = "age", required = false, defaultValue = "1") int age) {
        System.out.println("user:" + user);
        System.out.println("age:" + age);
        return "hello";
    }

    //http://localhost:8080/index.jsp
    @RequestMapping("/testbean")
// 参数直接传入用户对象
    public String tobean(User user) {
        System.out.println("user:" + user.getUserid());
        System.out.println("username:" + user.getUserName());
        System.out.println("age:" + user.getAge());
        return "hello";
    }

    //http://localhost:8080/hello/responseBody
//    直接把retrun结果输出
    @ResponseBody
    @RequestMapping("/responseBody")
// 返回Users对象
    public String responseBody() {
        ModelAndView mav = new ModelAndView();
        return "success";
    }

    //http://localhost:8080/hello/redirect
    @RequestMapping("/redirect")
// 返回Users对象
    public String redirect() {
        return "redirect:/redirect.jsp";
    }

    //http://localhost:8080/hello/forward
    @RequestMapping("/forward")
// 返回Users对象
    public String forward() {
        return "forward:/forward.jsp";
    }

    //http://localhost:8080/hello/modelandview
    @RequestMapping("/modelandview")
// 返回Users对象
    public ModelAndView modelandview() {
        ModelAndView mav = new ModelAndView();
        User u = new User(1, "username1", "pwd1", 10, 170d, new Date(System.currentTimeMillis()));

        mav.addObject("user", u);
        mav.setViewName("success");
        return mav;
    }

    //http://localhost:8080/hello/modelandviews
    @RequestMapping("/modelandviews")
// 返回Users对象
    public ModelAndView modelandviews() {
        ModelAndView mav = new ModelAndView();
        List<User> list = new ArrayList<User>();
        User u1 = new User(1, "username1", "pwd1", 10, 171d, new Date(System.currentTimeMillis()));
        User u2 = new User(2, "username2", "pwd2", 20, 172d, new Date(System.currentTimeMillis()));
        list.add(u1);
        list.add(u2);
        mav.addObject("user", list);
        mav.setViewName("success");
        return mav;
    }

    //http://localhost:8080//hello/testADD
    @RequestMapping("/testADD")
    public String AddUser() {
        User u = new User();
        u.setUserid(1);
        u.setUserName("user1");
        u.setAge(10);
        u.setUdate(new Date());
        u.setHeight((double) 100);
        System.out.println("add");
        try {
            userService.add(u);
            loger.info("add");
            return "helloADD";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "helloERROR";
        }


    }


    //	 http://localhost:8080//hello/proctest
    @RequestMapping("/proctest")
    public String ProcTest() {
        System.out.println(publicInfo.getShopid());
        System.out.println(publicInfo.getUserid());

        System.out.println("proctest");
        Map map = new HashMap();
        map.put("p1", "1001");
        map.put("p2", "0106");
        map.put("p3", "2019-01-01");
        String str;
        try {

            userService.callGetInvoice(map);
            str = "proctestsuccess";
            System.out.println("procedure return:p4" + map.get("p4") + ":p5" + map.get("p5") + ":p6" + map.get("p6") + ":p7" + map.get("p7") + ":p8" + map.get("p8"));
            //测试不存在的map
            System.out.println(":p9" + map.get("p9"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            str = e.getMessage();
        }
        System.out.println(str);
        return "hello";
    }

    //	 http://localhost:8080//hello/funtest
    @RequestMapping("/funtest")
    public String FunTest() {
        System.out.println("proctest");
        Map map = new HashMap();
        map.put("p1", "9998");
        map.put("p2", "1001");
        String str;
//	     过程调用成功返回结果，否则抛出异常
        try {
            userService.callGetGhid(map);
            str = "funtestsuccess";
            System.out.println("function return:" + map.get("p3") + ":正确的应该是:701000353");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            str = e.getMessage();
        }
        System.out.println(str);
        return "hello";
    }


}

