package com.spider.modules.sys.controller;

import com.spider.modules.sys.entity.SysUserEntity;
import com.spider.modules.sys.form.SysLoginForm;
import com.spider.modules.sys.service.SysUserService;
import com.spider.modules.sys.service.SysUserTokenService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 直接登录
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-11-14
 */
@RestController
@RequestMapping
public class FeignPassController {

    private final SysUserTokenService sysUserTokenService;
    private final SysUserService sysUserService;

    @Autowired
    public FeignPassController(SysUserTokenService sysUserTokenService, SysUserService sysUserService) {
        this.sysUserTokenService = sysUserTokenService;
        this.sysUserService = sysUserService;
    }

    /**
     * 假登录
     *
     * @param form 用户信息
     * @return token
     */
    @PostMapping("/sys/feignPass")
    public Map<String, Object> pass(@RequestBody SysLoginForm form) {

        // 用户信息
        SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

        // 生成token，并保存到数据库
        return sysUserTokenService.createToken(user.getUserId());
    }

}
