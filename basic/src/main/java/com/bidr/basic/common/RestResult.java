package com.bidr.basic.common;

import com.bidr.basic.exception.CustomException;
import com.bidr.basic.exception.CustomExceptionType;
import lombok.Data;

@Data
public class RestResult {

    private boolean isok;
    private int code;
    private String message;
    private Object data;

    private RestResult() {

    }

    //请求出现异常时的响应数据封装
    public static RestResult error(CustomException e) {

        RestResult resultBean = new RestResult();
        resultBean.setIsok(false);
        resultBean.setCode(e.getCode());
        if(e.getCode() == CustomExceptionType.USER_INPUT_ERROR.getCode()){
            resultBean.setMessage(e.getMessage());
        }else if(e.getCode() == CustomExceptionType.SYSTEM_ERROR.getCode()){
            resultBean.setMessage(e.getMessage() + ",系统出现异常，请联系管理员电话：xxxxx进行处理!");
        }else{
            resultBean.setMessage("系统出现未知异常，请联系管理员电话：xxxxxx进行处理!");
        }
        return resultBean;
    }

    public static RestResult success() {
        RestResult resultBean = new RestResult();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage("success");
        return resultBean;
    }

    public static RestResult success(Object data) {
        RestResult resultBean = new RestResult();
        resultBean.setIsok(true);
        resultBean.setCode(200);
        resultBean.setMessage("success");
        resultBean.setData(data);
        return resultBean;
    }
}