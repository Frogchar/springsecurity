package com.bidr.jwt.exception;
/**
 * @Author liuxiaobin
 * @Description 枚举异常类型
 * @Date  2020/6/18
 * @Param
 * @return
 **/
public enum CustomExceptionType {
    USER_INPUT_ERROR(400,"用户输入异常"),
    SYSTEM_ERROR (500,"系统服务异常"),
    OTHER_ERROR(9999,"其他未知异常"),
    SESSION_INVALID(8888,"session过期");

    CustomExceptionType(int code, String typeDesc) {
        this.code = code;
        this.typeDesc = typeDesc;
    }

    private String typeDesc;//异常类型中文描述

    private int code; //code

    public String getTypeDesc() {
        return typeDesc;
    }

    public int getCode() {
        return code;
    }
}