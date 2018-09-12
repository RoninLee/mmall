package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-06 19:22
 * @description: 用户登陆API
 **/
public interface IUserService {

    ServerResponse<User> login (String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse<String> forgetGetQuestion(String username);

    ServerResponse<String> forgetCheckAnswer(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

    ServerResponse<User> updateInfo(User user);

    ServerResponse<User> getUserInfo(int userId);
}
