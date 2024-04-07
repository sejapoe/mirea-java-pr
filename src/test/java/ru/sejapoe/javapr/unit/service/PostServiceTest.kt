package ru.sejapoe.javapr.unit.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import ru.sejapoe.javapr.domain.PostEntity
import ru.sejapoe.javapr.domain.UserEntity
import ru.sejapoe.javapr.exception.NotFoundException
import ru.sejapoe.javapr.repo.PostRepository
import ru.sejapoe.javapr.service.EmailService
import ru.sejapoe.javapr.service.PostService
import ru.sejapoe.javapr.service.UserService
import java.util.*

class PostServiceTest {
    @Test
    fun getById() {
        val postRepository = mock<PostRepository> {
            on { findById(any()) } doReturn Optional.of(PostEntity())
        }

        val postService = PostService(mock(), postRepository, mock(), mock())

        assertDoesNotThrow {
            postService.getById(1)
        }

        verify(postRepository, times(1)).findById(1)
    }

    @Test
    fun getByIdNotFound() {
        val postRepository = mock<PostRepository> {
            on { findById(any()) } doReturn Optional.empty()
        }

        val postService = PostService(mock(), postRepository, mock(), mock())

        val ex = assertThrows<NotFoundException> {
            postService.getById(1)
        }

        assertEquals("Post with ID [1] is not found", ex.message)
        verify(postRepository, times(1)).findById(1)
    }

    @Test
    fun getAll() {
        val postRepository = mock<PostRepository> {
            on { findAll() } doReturn listOf()
        }

        val postService = PostService(mock(), postRepository, mock(), mock())

        assertEquals(listOf<PostEntity>(), postService.all)

        verify(postRepository, times(1)).findAll()
    }

    @Test
    fun create() {
        val userService = mock<UserService> {
            on { getById(any()) } doReturn UserEntity()
        }

        val entityToSave = PostEntity(1L, "xyt", null, UserEntity.builder().id(1).build())

        val postRepository = mock<PostRepository> {
            on { save(any()) } doReturn entityToSave
        }

        val emailService = mock<EmailService>() {
            on { sendAdmin(any(), any()) } doAnswer {}
        }

        val postService = PostService(mock(), postRepository, userService, emailService)

        postService.create(entityToSave)

        verify(userService, times(1)).getById(1)
        verify(postRepository, times(1)).save(any())
        verify(emailService, times(1)).sendAdmin("New Post", "Created post[1]")
    }

    @Test
    fun delete() {
        val postRepository = mock<PostRepository> {
            on { findById(any()) } doReturn Optional.of(PostEntity())
            on { delete(any()) } doAnswer {}
        }

        val postService = PostService(mock(), postRepository, mock(), mock())

        assertDoesNotThrow {
            postService.delete(1)
        }

        verify(postRepository, times(1)).findById(1)
        verify(postRepository, times(1)).delete(any())
    }
}