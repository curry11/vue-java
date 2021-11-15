package com.markerhub.security;


import cn.hutool.core.util.StrUtil;
import com.markerhub.utils.JwtUtils;
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


//登录成功之后对访问链接的jwt进行校验的链路 确定用户之前是否登录成功
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    JwtUtils jwtUtils;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    //重写过滤的流程  获取jwt解析然后判断
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt  = request.getHeader(jwtUtils.getHeader());  //获取请求头中的jwt
        if (StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);  //如果jwt为空则直接按照链路走 之前配置的url白名单除外
            return;
        }

        Claims claims = jwtUtils.getClaimByToken(jwt);  //解析jwt
        if(claims == null) {  //如果jwt不合法就抛出异常
            throw new JwtException("token 异常");
        }
        if(jwtUtils.isTokenExpired(claims)){
            throw new JwtException("token已过期");
        }

        String username = claims.getSubject();  //根据之前配置的主体获取用户名
        //获取用户的权限等信息
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username,null,null);
        SecurityContextHolder.getContext().setAuthentication(token);  //设置认证的主体 帮助用户自动登录功能
        chain.doFilter(request,response);
    }
}
