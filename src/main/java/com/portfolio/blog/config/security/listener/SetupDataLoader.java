/*
package com.portfolio.blog.config.security.listener;

import com.portfolio.blog.account.domain.*;
import com.portfolio.blog.account.infra.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

*/
/**
 * 테스트 데이터 생성 클래스
 * 기본적으로 데이터가 있어야 정상 작동하는
 * Spring Security 관련 데이터들을 insert 한다.
 * (이 클래스는 형상관리하지 않는다.)
 *
 * @author 박상재
 * @version 1.0
 *//*

@RequiredArgsConstructor
@Transactional
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private static AtomicInteger count = new AtomicInteger(0);

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccessIpRepository accessIpRepository;
    private final ResourceRepository resourceRepository;
    private final RoleResourceRepository roleResourceRepository;
    private final RoleHierarchyRepository roleHierarchyRepository;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup)
            return;;

        setupSecurityResources();
        setupAccessIpData();
        alreadySetup = true;
    }

    */
/**
     * Security 관련 데이터 셋업
     *//*

    private void setupSecurityResources() {
        // 최고관리자
        Set<Role> superRoles = new HashSet<>();
        Role superRole = createRoleIfNotFound("ROLE_SUPER", "최고관리자");
        superRoles.add(superRole);                                                                                            // role 생성
        createResourceIfNotFound("/admin/**", superRoles, "url");                                   // 접근가능 resource 및 roleResource 생성
        createAccountIfNotFound("super","1234","최고관리자",30,"hellohj_@naver.com", superRole);      // account 생성

        // 관리자
        Set<Role> adminRoles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        adminRoles.add(adminRole);
        createResourceIfNotFound("/admin/**", adminRoles, "url");
        createAccountIfNotFound("admin","1234","관리자",20,"hellohj@gmail.com", adminRole);

        // 권한 계층 생성
        createRoleHierarchyIfNotFound(adminRole, superRole);

        // 사용자
        Set<Role> memberRoles = new HashSet<>();
        Role memberRole = createRoleIfNotFound("ROLE_MEMBER", "사용자");
        memberRoles.add(adminRole);
        createResourceIfNotFound("/member/**", memberRoles, "url");
        createAccountIfNotFound("member","1234","사용자",10,"", memberRole);

        // 권한 계층 생성
        createRoleHierarchyIfNotFound(memberRole, adminRole);
    }

    */
/**
     * Ip 접근제한 관련 데이터 셋업
     *//*

    private void setupAccessIpData() {
        accessIpRepository.findByAddress("127.0.0.1").orElseGet(() ->
                accessIpRepository.save(
                        AccessIp.builder()
                                .address("127.0.0.1")
                                .build()
                )
        );
    }

    */
/**
     * Role 이 존재하지 않으면 생성하는 메소드
     * @param roleName
     * @param roleDesc
     * @return Role
     *//*

    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        return roleRepository.findByName(roleName).orElseGet(() ->
                roleRepository.save(
                        Role.builder()
                                .name(roleName)
                                .roleDesc(roleDesc)
                                .build()
                ));
    }

    */
/**
     * Resource / RoleResource 가 존재하지 않으면 생성하는 메소드
     * @param resourceName
     * @param roleSet
     * @param resourceType
     *//*

    public void createResourceIfNotFound(String resourceName, Set<Role> roleSet, String resourceType) {
        for (Role r : roleSet) {
            Resource resource = resourceRepository.findByName(resourceName).orElseGet(() ->
                    resourceRepository.save(
                            Resource.builder()
                                    .name(resourceName)
                                    .type(resourceType)
                                    .orderNumber(count.incrementAndGet())
                                    .build()));

            roleResourceRepository.findByRoleAndResource(r, resource).orElseGet(() ->
                    roleResourceRepository.save(
                            RoleResource.createRoleResource(r, resource)));
        }
    }

    */
/**
     * Account 가 존재하지 않으면 생성하는 메소드
     * @param accountId
     * @param password
     * @param name
     * @param age
     * @param email
     *//*

    public void createAccountIfNotFound(String accountId, String password, String name, Integer age, String email, Role role) {
        accountRepository.save(
                Account.builder()
                        .accountId(accountId)
                        .password(passwordEncoder.encode(password))
                        .name(name)
                        .age(age)
                        .email(email)
                        .role(role)
                        .build()
        );
    }

    */
/**
     * 권한 계층 생성하는 메소드
     * @param childRole
     * @param parentRole
     *//*

    public void createRoleHierarchyIfNotFound(Role childRole, Role parentRole) {
        Optional<RoleHierarchy> roleHierarchy = roleHierarchyRepository.findByChildName(parentRole.getName());
        RoleHierarchy childRoleHierarchy;
        RoleHierarchy parentRoleHierarchy;

        parentRoleHierarchy = roleHierarchy.orElseGet(() ->
                roleHierarchyRepository.save(
                        RoleHierarchy.builder()
                                .childName(parentRole.getName())
                                .build()
                )
        );

        roleHierarchy = roleHierarchyRepository.findByChildName(childRole.getName());
        childRoleHierarchy = roleHierarchy.orElseGet(() ->
                roleHierarchyRepository.save(
                        RoleHierarchy.builder()
                                .childName(childRole.getName())
                                .build()
                )
        );

        childRoleHierarchy.changeParentRoleName(parentRoleHierarchy);
    }
}
*/
