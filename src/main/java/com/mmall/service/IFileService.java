package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-10-17 19:01
 * @description: 文件处理的服务
 **/
public interface IFileService {

    String upload(MultipartFile file, String path);

}
