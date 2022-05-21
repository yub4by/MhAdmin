package com.yppah.security;

import cn.hutool.core.util.StrUtil;
import com.yppah.entity.SysUser;
import com.yppah.service.SysUserService;
import com.yppah.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtil.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claims = jwtUtil.getClaimByToken(jwt);
        if (claims == null) {
            throw new JwtException("token异常");
        }
        if (jwtUtil.isTokenExpired(claims)) {
            throw new JwtException("token已过期");
        }

        String username = claims.getSubject();
        SysUser sysUser = sysUserService.getByUserName(username);
        // 自动登录，获取用户的权限信息
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username,
                null,
                userDetailService.getUserAuthority(sysUser.getId())
        );
        SecurityContextHolder.getContext().setAuthentication(token); // 上下文设置
        chain.doFilter(request, response); // 继续往下走
    }

}
