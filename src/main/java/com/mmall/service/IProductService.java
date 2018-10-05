package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-10-05 20:54
 * @description: 商品管理接口
 **/
public interface IProductService {

    ServerResponse saveOrUpdateProduct(Product product);

}
