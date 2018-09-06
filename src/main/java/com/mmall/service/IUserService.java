package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-06 19:22
 * @description: 用户登陆API
 **/
public interface IUserService {
    ServiceResponse<User> login (String username, String password);
}
