package com.bidr.jwt.accesslog;

import lombok.Data;

import java.util.Date;
/**
 * @Author liuxiaobin
 * @Description 访问系统日志实体类，用户记录日志
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@Data
public class AccessLog {
    //访问者用户名
    private String username;
    //请求路径
    private String url;
    //请求消耗时长
    private Integer duration;
    //http 方法：GET、POST等
    private String httpMethod;
    //http 请求响应状态码
    private Integer httpStatus;
    //访问者ip
    private String ip;
    //此条记录的创建时间
    private Date createTime;
}