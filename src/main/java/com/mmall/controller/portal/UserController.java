package com.mmall.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Author: RoninLee
 * Email:  zlli233@qq.com
 * Date:   2018-09-04 23:53
 * Desc:
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    /**
     * 用户登录
     * @param name
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public Object login(String name, String password, HttpSession session){
        return null;
    }
}
