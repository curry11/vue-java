package com.markerhub.controller;

import com.markerhub.common.lang.Result;
import com.markerhub.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	SysUserService sysUserService;

	@GetMapping("/test")
	public Result test() {
		return Result.succ(sysUserService.list());
	}



}
