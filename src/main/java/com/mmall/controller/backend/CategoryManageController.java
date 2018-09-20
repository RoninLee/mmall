package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponserCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-17 22:50
 * @description: 分类管理
 **/
@Controller
@RequestMapping("/Manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserServer;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("/add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponserCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验是否是管理员
        if (iUserServer.checkAdminRole(user).isSussess()){
            //是管理员
            //增加处理分类的逻辑
            return iCategoryService.addCategory(categoryName,parentId);
        }else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping("/update_categoryName.do")
    @ResponseBody
    public ServerResponse updateCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponserCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }

        if (iUserServer.checkAdminRole(user).isSussess()){
            //更新categoryName
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping("/get_category.do")
    @ResponseBody
    public ServerResponse getCategoryChildrenByParentId(HttpSession session,Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponserCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }

        if (iUserServer.checkAdminRole(user).isSussess()){
            //查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.selectCategoryChildrenByParentId(categoryId);
        }else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping("/get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMsg(ResponserCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if (iUserServer.checkAdminRole(user).isSussess()){
            //查询当前节点的id和递归子节点的id
            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
        }else {
            return ServerResponse.createByErrorMsg("无权限操作，需要管理员权限");
        }
    }

}
