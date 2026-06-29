package com.jjm.jjmbackend.dto

import com.jjm.jjmbackend.models.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthResponseTest {

    @Test
    fun `should create AuthResponse with token and user`() {
        val user = User(id = 1, email = "test@example.com", name = "Test User")
        val token = "test-token"
        val authResponse = AuthResponse(token = token, user = user)

        assertEquals(token, authResponse.token)
        assertEquals(user, authResponse.user)
    }

    @Test
    fun `should create AuthResponse with null token`() {
        val user = User(id = 2, email = "other@example.com", name = "Other User")
        val authResponse = AuthResponse(user = user)

        assertNull(authResponse.token)
        assertEquals(user, authResponse.user)
    }

    @Test
    fun `should allow copying AuthResponse`() {
        val user1 = User(id = 1, email = "test@example.com", name = "Test User")
        val user2 = User(id = 2, email = "other@example.com", name = "Other User")
        val original = AuthResponse(token = "token1", user = user1)
        
        val copied = original.copy(token = "token2", user = user2)

        assertEquals("token2", copied.token)
        assertEquals(user2, copied.user)
        assertEquals("token1", original.token)
        assertEquals(user1, original.user)
    }
}
