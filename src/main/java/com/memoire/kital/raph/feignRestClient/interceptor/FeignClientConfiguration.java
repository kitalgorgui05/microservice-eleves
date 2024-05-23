package com.memoire.kital.raph.feignRestClient.interceptor;

import com.memoire.kital.raph.security.SecurityUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientConfiguration implements RequestInterceptor{
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = SecurityUtils.getJwtToken();
        if(null != token && !token.trim().isEmpty()){
            requestTemplate.header("Authorization", "Bearer " + token);
        }
    }
}
