package com.markerhub.controller;

import com.markerhub.common.lang.Result;
import com.markerhub.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends BaseController {

	@Autowired
	SysUserService sysUserService;

	@GetMapping("/test")
	public Result test() {
		redisUtil.set("1",100);
		return Result.succ(redisUtil.get("1"));
	}

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	// 普通用户、超级管理员
//	@PreAuthorize("hasAuthority('sys:user:list')")
	@GetMapping("/test/pass")
	public Result pass() {

		// 加密后密码
		String password = bCryptPasswordEncoder.encode("111111");

		boolean matches = bCryptPasswordEncoder.matches("111111", password);

		System.out.println("匹配结果：" + matches);

		return Result.succ(password);
	}


}
