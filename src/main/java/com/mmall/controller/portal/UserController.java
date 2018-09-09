package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session){
        ServiceResponse<User> response = iUserService.login(username, password);
        if(response.isSussess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> register(User user){
        return iUserService.register(user);
    }

    @RequestMapping(value = "checkValid.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    @RequestMapping(value = "getUserInfo.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null){
            return ServiceResponse.createBySuccess(user);
        }
        return ServiceResponse.createByErrorMsg("用户未登录，无法获取当前用户信息");
    }

    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> forgetGetQuestion(String username){
        return iUserService.forgetGetQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.forgetCheckAnswer(username, question, answer);
    }



}