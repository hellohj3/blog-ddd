package com.portfolio.blog.config.security.factory;

import com.portfolio.blog.account.application.service.ResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 데이터베이스의 정보를토대로 url 맵핑정보를 생성하는 클래스
 *
 * @author 박상재
 * @version 1.0
 */
public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private ResourceService resourceService;

    public void setResourcesService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourcesMap;

    public void init() {
        resourcesMap = resourceService.getResourceList();
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() {
        if (resourcesMap == null) {
            init();
        }
        return resourcesMap;
    }

    public Class<LinkedHashMap> getObjectType() {
        return LinkedHashMap.class;
    }

    public boolean isSingleton() {
        return true;
    }

}
