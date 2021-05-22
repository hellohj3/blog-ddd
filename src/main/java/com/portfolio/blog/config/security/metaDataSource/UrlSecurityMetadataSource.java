package com.portfolio.blog.config.security.metaDataSource;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class UrlSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        if (requestMap != null) {
            for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
                RequestMatcher metcher = entry.getKey();
                if (metcher.matches(request)) {
                    return entry.getValue();
                }
            }
        }

        return null;
    }

    // DefaultFilterInvocationSecurityMetadataSource 사용
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        this.requestMap.values().forEach(allAttributes::addAll);
        return allAttributes;
    }

    // DefaultFilterInvocationSecurityMetadataSource 사용
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

}
