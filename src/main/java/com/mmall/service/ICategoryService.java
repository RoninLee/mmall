package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-18 19:18
 * @description: 分类管理接口
 **/
public interface ICategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    ServerResponse selectCategoryChildrenByParentId(Integer categoryId);

    ServerResponse getCategoryAndDeepChildrenCategory(Integer categoryId);

}
