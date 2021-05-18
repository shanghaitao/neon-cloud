package com.neon.user.resolver;

import com.neon.common.auth.UserToken;
import com.neon.common.util.Consts;
import com.neon.common.util.Jackson;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @Description
 * @Author shanghaitao
 * @Date 2021/4/25 14:37
 */
@Component
public class MyMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserToken.class);
    }

    @Override
    public UserToken resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(Consts.USER_INFO);
        if (StringUtils.hasText(token)){
            UserToken userToken = Jackson.JsonToBean(token, UserToken.class);
            return userToken;
        }
        return null;
    }
}
