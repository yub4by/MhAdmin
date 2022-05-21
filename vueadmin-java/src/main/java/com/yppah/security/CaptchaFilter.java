package com.yppah.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yppah.common.exception.CaptchaException;
import com.yppah.common.lang.Const;
import com.yppah.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if ("/login".equals(url) && request.getMethod().equals("POST")){
            try {
                // 校验验证码
                validate(request);
            } catch (CaptchaException e) {
                // 验证码不正确，校验失败，则跳转到认证失败处理器LoginFailureHandler
                loginFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
        //验证码正确，则放行，令程序继续往下执行
        filterChain.doFilter(request, response);
    }

    /**
     * 校验验证码的逻辑
     * @param request
     */
    private void validate(HttpServletRequest request) {
        String code = request.getParameter("code");
        String key = request.getParameter("token");

        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }
        if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
            throw new CaptchaException("验证码错误");
        }

        // 验证码只能一次性使用
        redisUtil.hdel(Const.CAPTCHA_KEY, key);
    }

}
