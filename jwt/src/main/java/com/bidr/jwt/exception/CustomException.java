package com.bidr.jwt.exception;
/**
 * @Author liuxiaobin
 * @Description 自定义运行时异常类
 * @Date  2020/6/18
 * @Param
 * @return
 **/
public class CustomException extends RuntimeException{
    //异常错误编码
    private int code ;
    //异常信息
    private String message;

    private CustomException(){}

    public CustomException(CustomExceptionType exceptionTypeEnum, String message) {
        this.code = exceptionTypeEnum.getCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
