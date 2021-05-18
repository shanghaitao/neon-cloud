package com.neon.user.controller;

import com.neon.common.auth.UserToken;
import com.neon.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @Author shanghaitao
 * @Date 2021/4/23 17:05
 */
@FeignClient(value = "neon-user")
public interface UserController {
    User getUser(UserToken userToken, Integer userId);
}
