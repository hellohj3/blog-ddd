package com.portfolio.blog.account.application.service;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.domain.Role;
import com.portfolio.blog.account.domain.RoleEnum;
import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.account.infra.RoleRepository;
import com.portfolio.blog.account.ui.dto.AccountDto;
import com.portfolio.blog.config.security.model.SecurityAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService implements UserDetailsService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    // Account 정보를 조회하고 SecurityUser 를 생성하여 리턴합니다
    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        Optional<Account> findAccount = Optional.ofNullable(accountRepository.findAccountAndRolesByAccountId(accountId));
        Account result = findAccount.orElseThrow(() -> new InternalAuthenticationServiceException("계정정보 없음"));

        return new SecurityAccount(result.getAccountId(), result.getPassword(), result.getRole().getName());
    }

    // Account 생성
    @Transactional
    public Account saveAccount(AccountDto accountDto) {
        return accountRepository.save(accountDto.createAccount(
                passwordEncoder.encode(accountDto.getPassword()),
                roleRepository.findByName(RoleEnum.MEMBER.getKey())
                        .orElseThrow(() -> new IllegalArgumentException("권한정보 없음"))));
    }

    public Boolean findAccount(String accountId) {
        Optional<Account> findByOptional = accountRepository.findById(accountId);

        return findByOptional.isPresent();
    }
}
