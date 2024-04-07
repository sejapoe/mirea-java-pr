package ru.sejapoe.javapr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sejapoe.javapr.domain.AccountEntity;
import ru.sejapoe.javapr.exception.ConflictException;
import ru.sejapoe.javapr.exception.UnauthorizedException;
import ru.sejapoe.javapr.repo.AccountRepository;
import ru.sejapoe.javapr.security.JwtService;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Wrong username or password");
        }
        AccountEntity account = accountRepository.findByUsername(username).orElseThrow();
        return jwtService.generateToken(Map.of(
                "ROLE", account.getRole().toString()
        ), username, Duration.ofDays(10));
    }

    @Transactional
    public String register(String username, String password) {
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new ConflictException("User with that name already exists");
        }
        var account = accountRepository.save(
                AccountEntity.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .role(AccountEntity.Role.USER)
                        .build()
        );
        return jwtService.generateToken(Map.of(
                "ROLE", account.getRole().toString()
        ), username, Duration.ofDays(10));
    }
}
