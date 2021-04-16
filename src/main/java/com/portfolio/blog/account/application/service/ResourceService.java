package com.portfolio.blog.account.application.service;

import com.portfolio.blog.account.application.dto.RoleResourceDto;
import com.portfolio.blog.account.infra.ResourceRepository;
import com.portfolio.blog.account.infra.AccessIpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final AccessIpRepository accessIpRepository;

    @Cacheable(value = "resourceList")
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<RoleResourceDto> resourcesList = resourceRepository.findAllResourceByType("url");

        resourcesList.forEach(re -> {
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            configAttributes.add(new SecurityConfig(re.getRoleName()));
            result.put(new AntPathRequestMatcher(re.getResourceName()), configAttributes);
        });

        return result;
    }

    @Cacheable(value = "accessIpList")
    public List<String> getAccessIpList() {

        List<String> accessIpList = accessIpRepository.findAll().stream()
                .map(accessIp -> accessIp.getAddress()).collect(Collectors.toList());

        return accessIpList;
    }

}
