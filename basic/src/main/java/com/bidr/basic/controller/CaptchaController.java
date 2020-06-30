package com.bidr.basic.controller;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.bidr.basic.common.CaptchaCode;
import com.bidr.basic.common.MyContants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
/**
 * @Author liuxiaobin
 * @Description
 * 生成验证码
 * 1、生成验证码图片
 * 2、将生成的验证码存储在session中
 * 3、使用输出流返回客户端
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@RestController
public class CaptchaController {

    @RequestMapping(value="/kaptcha",method = RequestMethod.GET)
    public void kaptcha(HttpSession session, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg;images/png");
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        //将验证码存到session
        session.setAttribute(MyContants.CAPTCHA_SESSION_KEY, new CaptchaCode(captcha.getCode(),2 * 60));
        ServletOutputStream outputStream=response.getOutputStream();
        captcha.write(outputStream);
        outputStream.flush();

    }
}
