package com.markerhub.security;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.markerhub.common.exception.CaptchaException;
import com.markerhub.common.lang.Const;
import com.markerhub.common.lang.Result;
import com.markerhub.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//验证码过滤器,在登录之前先进行验证码的验证  因为是一次性校验所以继承OncePerRequestFilter
@Component
public class CaptchaFilter extends OncePerRequestFilter {

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	LoginFailureHandler loginFailureHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

		String url = httpServletRequest.getRequestURI();  //只需要拦截loginurl就行

		if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {

			try{
				// 校验验证码
				validate(httpServletRequest,httpServletResponse);
			} catch (CaptchaException e) {
				// 如果失败就交给认证失败处理器
				loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
			}
		}
		//正确的就继续沿着链路走
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	// 校验验证码逻辑
	private void validate(HttpServletRequest httpServletRequest,HttpServletResponse response) throws IOException {

		String code = httpServletRequest.getParameter("code");
		String key = httpServletRequest.getParameter("token");

		if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
			throw new CaptchaException("验证码错误");
//			response.setContentType("application/json;charset=UTF-8");
//			ServletOutputStream outputStream = response.getOutputStream();
//
//			Result result = Result.fail("yan");
//
//			//把result 序列号成json格式
//			outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
//
//			outputStream.flush();
//			outputStream.close();
		}

		//在redis中保存的数据记录比较
		if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
			throw new CaptchaException("验证码错误");
//			response.setContentType("application/json;charset=UTF-8");
//			ServletOutputStream outputStream = response.getOutputStream();
//
//			Result result = Result.fail("yan");
//
//			//把result 序列号成json格式
//			outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
//
//			outputStream.flush();
//			outputStream.close();
		}

		// 一次性使用
		redisUtil.hdel(Const.CAPTCHA_KEY, key);
	}
}
