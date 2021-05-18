package com.neon.user.controller;

import com.neon.common.auth.UserToken;
import com.neon.user.entity.User;
import com.neon.user.rocketmq.MqProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author shanghaitao
 * @Date 2021/4/23 14:28
 */
@RestController
public class UserControllerImpl implements UserController{
    @Autowired
    MqProducerService mqProducerService;

    @GetMapping("/getUser")
    public User getUser(UserToken userToken,Integer userId){
        System.out.println(userToken);
        return new User(userId,"阿童木",20,(byte)0);
    }

    @GetMapping("/sendMq")
    public String sendMq(){
        return mqProducerService.send();
    }
}
