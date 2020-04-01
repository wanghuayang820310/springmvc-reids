package com.why.action;

import com.why.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname RedisAction
 * @Description TODO
 * @Date 2020/3/29 10:29
 * @Created by why
 */
@Controller
@RequestMapping("/redistest")
public class RedisAction {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    @Qualifier("redisCacheManager")
    private CacheManager cacheManager;
    //http://localhost:8080/redistest/index
    @RequestMapping(value="index", method = RequestMethod.GET)
    public Object index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/success");
        System.out.println(cacheManager.getCache("user"));
        return mv;
    }
    //http://localhost:8080/redistest/json
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value="json", method = RequestMethod.GET)
    public Object json(HttpServletRequest request) {
        Map m = new HashMap();
        m.put("key", "我是map");
        redisUtil.hmset("m", m);
        return redisUtil.hmget("m");
    }

    //http://localhost:8080/redistest/readStr
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @ResponseBody
    @RequestMapping(value="readStr", method = RequestMethod.GET)
    public String readStr() {
        String userstr = (String) redisUtil.get("user1");
        System.out.println("user1的键值："+userstr);
        return userstr;
    }
}
