package com.example.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttrs) {
            HttpServletRequest request = servletAttrs.getRequest();
            String auth = request.getHeader("Authorization");
            if (auth != null && !auth.isEmpty()) {
                template.header("Authorization", auth);
            }

            String reqId = request.getHeader("X-Request-Id");
            if (reqId == null || reqId.isEmpty()) {
                reqId = UUID.randomUUID().toString();
            }
            template.header("X-Request-Id", reqId);
        }
    }
}
