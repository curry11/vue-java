package com.markerhub.config;


import com.markerhub.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//securityConfig 配置
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	LoginFailureHandler loginFailureHandler;

	@Autowired
	CaptchaFilter captchaFilter;

	@Autowired
	LoginSuccessHandler loginSuccessHandler;

	@Autowired
	JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean  //因为重写了构造器
	JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
		return jwtAuthenticationFilter;
	}

	private static final String[] URL_WHITELIST = {

			"/login",
			"/logout",
			"/captcha",
			"/favicon.ico",

	};


	//配置http安全 允许跨域
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable() //关闭csrf 预防攻击

				// 登录配置
				.formLogin()
				.successHandler(loginSuccessHandler)  //登录成功处理器
				.failureHandler(loginFailureHandler)  //登录失败处理器
//
//				.and()
//				.logout()
//				.logoutSuccessHandler(jwtLogoutSuccessHandler)

				// 禁用session
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// 配置url拦截规则
				.and()
				.authorizeRequests()
				.antMatchers(URL_WHITELIST).permitAll()  //过滤我们配置的白名单连接
				.anyRequest().authenticated()

				// 异常处理器
				.and()
				.exceptionHandling()
 				.authenticationEntryPoint(jwtAuthenticationEntryPoint)  //jwt认证失败的处理器
				.accessDeniedHandler(jwtAccessDeniedHandler)  //权限不足的处理器

				// 配置自定义的过滤器  addFilterBefore 定义在哪个链路之前
				.and()
				.addFilter(jwtAuthenticationFilter())
				.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);



	}

}
