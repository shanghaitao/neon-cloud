package com.neon.common.delay;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.neon.common.auth.UserToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author shanghaitao
 * @Date 2021/5/6 11:34
 */
public class DelayTest {

    public static void main(String[] args) throws InterruptedException {
        Optional<UserToken> a = Optional.ofNullable(new UserToken());

        a.ifPresent(b->{
            b.setTime(123456);
            b.setUserId(123);
        });

        System.out.println(a.orElse(null));

    }
}
