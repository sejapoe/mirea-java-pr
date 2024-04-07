package ru.sejapoe.javapr.unit.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import ru.sejapoe.javapr.domain.AccountEntity
import ru.sejapoe.javapr.exception.ConflictException
import ru.sejapoe.javapr.exception.UnauthorizedException
import ru.sejapoe.javapr.repo.AccountRepository
import ru.sejapoe.javapr.security.JwtService
import ru.sejapoe.javapr.service.AuthService
import java.util.*

internal class AuthServiceTest {
    @Test
    @DisplayName("Test login success")
    fun login() {
        val authenticationManager = mock<AuthenticationManager> {
            on { authenticate(any()) } doReturn null
        }

        val jwtService = mock<JwtService> {
            on { generateToken(any(), any(), any()) } doReturn "mock-token"
        }

        val accountRepository = mock<AccountRepository> {
            on { findByUsername(any()) } doReturn Optional.of(AccountEntity(1L, "username", "password", AccountEntity.Role.USER))
        }

        val authService = AuthService(accountRepository, jwtService, authenticationManager, mock())
        val token = authService.login("username", "password")

        verify(authenticationManager, times(1)).authenticate(any())
        verify(jwtService, times(1))
            .generateToken(any(), any(), any())
        assertThat(token).isEqualTo("mock-token")
    }

    @Test
    @DisplayName("Test login failed")
    fun loginFailed() {
        val authenticationManager = mock<AuthenticationManager> {
            on { authenticate(any()) } doThrow UsernameNotFoundException::class
        }

        val jwtService = mock<JwtService> {
            on { generateToken(any(), any(), any()) } doReturn "mock-token"
        }

        val authService = AuthService(mock(), jwtService, authenticationManager, mock())
        assertThatThrownBy {
            authService.login("username", "password")
        }.isInstanceOf(UnauthorizedException::class.java)
            .message().isEqualTo("Wrong username or password")

        verify(authenticationManager, times(1)).authenticate(any())
        verify(jwtService, times(0))
            .generateToken(any(), any(), any())
    }

    @Test
    @DisplayName("Test register success")
    fun register() {
        val passwordEncoder = NoOpPasswordEncoder.getInstance()

        val userRepository = mock<AccountRepository> {
            on { findByUsername(any()) } doReturn Optional.empty()
            on { save(any()) } doReturn AccountEntity(1L, "username", "password", AccountEntity.Role.USER)
        }

        val jwtService = mock<JwtService> {
            on { generateToken(any(), any(), any()) }.thenReturn("mock-token")
        }

        val authService = AuthService(userRepository, jwtService, mock(), passwordEncoder)
        authService.register("username", "password")

        verify(userRepository, times(1)).findByUsername("username")
        verify(userRepository, times(1)).save(any())
        verify(jwtService, times(1))
            .generateToken(any(), any(), any())
    }

    @Test
    @DisplayName("Test register failed")
    fun registerFailed() {
        val passwordEncoder = NoOpPasswordEncoder.getInstance()

        val userRepository = mock<AccountRepository> {
            on { findByUsername(any()) } doReturn Optional.of(AccountEntity(1L, "username", "password", AccountEntity.Role.USER))
            on { save(any()) } doReturn AccountEntity(1L, "username", "password", AccountEntity.Role.USER)
        }

        val jwtService = mock<JwtService> {
            on { generateToken(any(), any(), any()) } doReturn "mock-token"
        }

        val authService = AuthService(userRepository, jwtService, mock(), passwordEncoder)
        assertThatThrownBy {
            authService.register("username", "password")
        }.isInstanceOf(ConflictException::class.java).message().isEqualTo("User with that name already exists")

        verify(userRepository, times(1)).findByUsername("username")
        verify(userRepository, times(0)).save(any())
        verify(jwtService, times(0))
            .generateToken(any(), any(), any())
    }
}