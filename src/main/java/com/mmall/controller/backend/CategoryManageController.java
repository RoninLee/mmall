package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponserCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-17 22:50
 * @description: 分类管理
 **/
public class CategoryManageController {

    @Autowired
    private IUserService iUserServer;

    public ServerResponse addCategovy(HttpSession session, String categovyName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponserCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if (iUserServer.checkAdminRole(user).isSussess()){
            //是管理员
            //todo 增加处理分类的逻辑
        }else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
        return  null;
    }

}
