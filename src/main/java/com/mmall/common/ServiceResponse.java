package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-06 19:44
 * @description: 响应对象
 **/
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)/*序列化的json不要null,序列化后只显示非null字段*/
public class ServiceResponse<T> implements Serializable{
    private int status;
    private String msg;
    private T data;

    private ServiceResponse(int status) {
        this.status = status;
    }

    private ServiceResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServiceResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServiceResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore/*此注释会在序列化之后隐藏注释的字段，不在序列化结果之中*/
    public boolean isSussess(){
        return this.status == ResponserCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServiceResponse createBySuccess(){
        return new ServiceResponse(ResponserCode.SUCCESS.getCode());
    }

    public static <T> ServiceResponse createBySuccessMsg(String msg){
        return new ServiceResponse(ResponserCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServiceResponse createBySuccess(T data){
        return new ServiceResponse(ResponserCode.SUCCESS.getCode(),data);
    }

    public static <T> ServiceResponse createBySuccess(String msg,T data){
        return new ServiceResponse(ResponserCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServiceResponse<T> createByError(){
        return new ServiceResponse<T>(ResponserCode.ERROR.getCode(),ResponserCode.ERROR.getDesc());
    }

    public static <T> ServiceResponse<T> createByErrorMsg(String msg){
        return new ServiceResponse<T>(ResponserCode.ERROR.getCode(),msg);
    }

    public static <T> ServiceResponse<T> createByErrorCodeMsg(int errorCode,String errorMsg){
        return new ServiceResponse<T>(errorCode,errorMsg);
    }

}
