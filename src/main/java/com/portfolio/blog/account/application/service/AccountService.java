package com.portfolio.blog.account.application.service;

import com.portfolio.blog.account.domain.Account;
import com.portfolio.blog.account.infra.AccountRepository;
import com.portfolio.blog.config.security.model.SecurityAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;

    // Account 정보를 조회하고 SecurityUser 를 생성하여 리턴합니다
    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        Optional<List<Account>> findAdmin = Optional.ofNullable(accountRepository.findAccountAndRolesByAccountId(accountId));
        Account result = findAdmin.orElseThrow(() -> new InternalAuthenticationServiceException("계정정보 없음")).get(0);

        return new SecurityAccount(result.getAccountId(), result.getPassword(), result.getRole().getName());
    }

}
