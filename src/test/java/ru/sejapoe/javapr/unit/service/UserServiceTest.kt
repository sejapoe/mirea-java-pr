package ru.sejapoe.javapr.unit.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.sejapoe.javapr.domain.UserEntity
import ru.sejapoe.javapr.exception.NotFoundException
import ru.sejapoe.javapr.repo.UserRepository
import ru.sejapoe.javapr.service.EmailService
import ru.sejapoe.javapr.service.UserService
import java.util.*

class UserServiceTest {
    @Test
    fun getById() {
        val userRepository = mock<UserRepository> {
            on { findById(any()) } doReturn Optional.of(UserEntity())
        }

        val userService = UserService(mock(), userRepository, mock())

        assertDoesNotThrow {
            userService.getById(1)
        }

        verify(userRepository, times(1)).findById(1)
    }

    @Test
    fun getByIdNotFound() {
        val userRepository = mock<UserRepository> {
            on { findById(any()) } doReturn Optional.empty()
        }

        val userService = UserService(mock(), userRepository, mock())

        val ex = assertThrows<NotFoundException> {
            userService.getById(1)
        }

        assertEquals("User with ID [1] is not found", ex.message)
        verify(userRepository, times(1)).findById(1)
    }

    @Test
    fun getAll() {
        val userRepository = mock<UserRepository> {
            on { findAll() } doReturn listOf()
        }

        val userService = UserService(mock(), userRepository, mock())

        assertEquals(listOf<UserEntity>(), userService.all)

        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun create() {
        val entityToSave = UserEntity.builder().id(1).build()

        val userRepository = mock<UserRepository> {
            on { save(any()) } doReturn entityToSave
        }

        val emailService = mock<EmailService>() {
            on { sendAdmin(any(), any()) } doAnswer {}
        }

        val userService = UserService(mock(), userRepository, emailService)

        userService.create(entityToSave)

        verify(userRepository, times(1)).save(any())
        verify(emailService, times(1)).sendAdmin("New User", "Saved user[1]")
    }

    @Test
    fun delete() {
        val userRepository = mock<UserRepository> {
            on { findById(any()) } doReturn Optional.of(UserEntity())
            on { delete(any()) } doAnswer {}
        }

        val userService = UserService(mock(), userRepository, mock())

        assertDoesNotThrow {
            userService.delete(1)
        }

        verify(userRepository, times(1)).findById(1)
        verify(userRepository, times(1)).delete(any())
    }
}