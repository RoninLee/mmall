package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    User selectLogin(@Param("username") String username,@Param("password") String password);

    String forgetGetQuestion (String username);

    int forgetCheckAnswer (@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int forgetResetPassword(@Param("username") String username,@Param("passwordNew") String passwordNew);

    int checkUserPassword(@Param("userId") int userId,@Param("passwordOld") String passwordOld);

    int checkEmailByUserId(@Param("userId") int userId,@Param("email") String email);

}