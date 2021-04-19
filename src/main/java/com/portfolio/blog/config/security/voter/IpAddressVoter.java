package com.portfolio.blog.config.security.voter;

import com.portfolio.blog.account.application.service.ResourceService;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.List;

import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;

/**
 * 접근 가능 Ip를 판단하는 Voter
 *
 * @author 박상재
 * @version 1.0
 */
public class IpAddressVoter implements AccessDecisionVoter<Object> {

    private ResourceService resourceService;

    public IpAddressVoter(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return (attribute.getAttribute() != null);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> configList) {

        if (!(authentication.getDetails() instanceof WebAuthenticationDetails)) {
            return ACCESS_DENIED;
        }

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String address = details.getRemoteAddress();
        List<String> accessIpList = resourceService.getAccessIpList();

        int result = ACCESS_DENIED;

        for (String ipAddress : accessIpList) {

            if (address.equals(ipAddress)) {
                return ACCESS_ABSTAIN;
            }
        }

        if(result == ACCESS_DENIED){
            throw new AccessDeniedException("Invalid ipAddress can not accessed");
        }

        return result;
    }

}
