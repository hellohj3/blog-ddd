package com.portfolio.blog.config.security.metaDataSource;

import com.portfolio.blog.account.application.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class UrlSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();
    private ResourceService resourceService;

    public UrlSecurityMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap, ResourceService resourceService) {
        this.requestMap = requestMap;
        this.resourceService = resourceService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        // 요청 url 에 대한 권한정보 추출 작업
        if (requestMap != null) {
            for (Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
                RequestMatcher matcher = entry.getKey();
                // 요청과 map 의 key 값을 매칭
                if (matcher.matches(request)) {
                    // 매칭된 요청에 대한 권한정보를 리턴
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
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
