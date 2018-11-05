package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-06 19:25
 * @description: 用户登陆的实现类
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0 ){
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user==null){
            return ServerResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    public ServerResponse<String> register(User user){
        ServerResponse<String> checkUsername = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!checkUsername.isSuccess()){
            return checkUsername;
        }

        ServerResponse<String> checkEmail = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!checkEmail.isSuccess()){
            return checkEmail;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密password
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMsg("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }

    public ServerResponse<String> checkValid(String str,String type){
        if(StringUtils.isNoneBlank(type)){
            if(Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int checkEmail = userMapper.checkEmail(str);
                if(checkEmail > 0){
                    return ServerResponse.createByErrorMsg("邮箱已存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        return ServerResponse.createBySuccess("校验成功");
    }

    public ServerResponse<String> forgetGetQuestion(String username){
        ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
        if(checkValid.isSuccess()){
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        String forgetGetQuestion = userMapper.forgetGetQuestion(username);
        if(StringUtils.isBlank(forgetGetQuestion)){
            return ServerResponse.createByErrorMsg("找回密码的问题为空");
        }
        return ServerResponse.createBySuccess(forgetGetQuestion);
    }

    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        int forgetCheckAnswer = userMapper.forgetCheckAnswer(username, question, answer);
        if(forgetCheckAnswer>0){
            //说明问题及问题的答案是这个用户的并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMsg("答案错误");
    }

    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMsg("参数错误，token需要传递");
        }
        ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()){
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMsg("token无效或者过期");
        }
        if(StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.forgetResetPassword(username, md5Password);
            if (rowCount>0){
                return ServerResponse.createBySuccess("修改密码成功");
            }
        }else {
            return ServerResponse.createByErrorMsg("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMsg("修改密码失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        int checkUserPassword = userMapper.checkUserPassword(user.getId(), MD5Util.MD5EncodeUtf8(passwordOld));
        if(checkUserPassword == 0 ){
            return ServerResponse.createByErrorMsg("旧密码输入错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int resetPassword = userMapper.updateByPrimaryKeySelective(user);
        if (resetPassword > 0){
            return ServerResponse.createBySuccess("修改密码成功");
        }
        return ServerResponse.createByErrorMsg("修改密码失败");
    }

    public ServerResponse<User> updateInfo(User user){
        //username不能被更新
        //email也要进行校验，不能是已存在的Email
        int checkEmailByUserId = userMapper.checkEmailByUserId(user.getId(), user.getEmail());
        if (checkEmailByUserId > 0){
            return ServerResponse.createByErrorMsg("邮箱已被注册");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setPhone(user.getPhone());
        int updateInfo = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateInfo > 0){
            return ServerResponse.createBySuccess("更新信息成功",updateUser);
        }
        return ServerResponse.createByErrorMsg("修改信息失败");
    }

    public ServerResponse<User> getUserInfo(int userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMsg("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}



