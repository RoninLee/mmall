package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.utils.MD5Util;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-06 19:25
 * @description: 用户登陆API实现类
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0 ){
            return ServiceResponse.createByErrorMsg("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user==null){
            return ServiceResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登录成功",user);
    }

    public ServiceResponse<String> register(User user){
        ServiceResponse<String> checkUsername = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!checkUsername.isSussess()){
            return checkUsername;
        }

        ServiceResponse<String> checkEmail = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!checkEmail.isSussess()){
            return checkEmail;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密password
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0 ){
            return ServiceResponse.createByErrorMsg("注册失败");
        }
        return ServiceResponse.createBySuccess("注册成功");
    }

    public ServiceResponse<String> checkValid(String str,String type){
        if(StringUtils.isNoneBlank(type)){
            if(Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServiceResponse.createByErrorMsg("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int checkEmail = userMapper.checkEmail(str);
                if(checkEmail > 0){
                    return ServiceResponse.createByErrorMsg("邮箱已存在");
                }
            }
        }else {
            return ServiceResponse.createByErrorMsg("参数错误");
        }
        return ServiceResponse.createBySuccess("校验成功");
    }

    public ServiceResponse<String> forgetGetQuestion(String username){
        ServiceResponse<String> checkValid = this.checkValid(username, Const.CURRENT_USER);
        if(checkValid.isSussess()){
            return ServiceResponse.createByErrorMsg("用户不存在");
        }
        String forgetGetQuestion = userMapper.forgetGetQuestion(username);
        if(StringUtils.isBlank(forgetGetQuestion)){
            return ServiceResponse.createByErrorMsg("找回密码的问题为空");
        }
        return ServiceResponse.createBySuccess(forgetGetQuestion);
    }

    public ServiceResponse<String> forgetCheckAnswer(String username,String question,String answer){
        int forgetCheckAnswer = userMapper.forgetCheckAnswer(username, question, answer);
        if(forgetCheckAnswer>0){
            //说明问题及问题的答案是这个用户的并且是正确的
            String forgetToekn = UUID.randomUUID().toString();
            TokenCache.setKey("token_"+username,forgetToekn);
            return ServiceResponse.createBySuccess(forgetToekn);
        }
        return ServiceResponse.createByErrorMsg("答案错误");
    }

}
