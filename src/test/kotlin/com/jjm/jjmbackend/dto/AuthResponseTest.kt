package com.jjm.jjmbackend.dto

import kotlin.test.Test
import kotlin.test.assertEquals

class AuthResponseTest {

    @Test
    fun `should create AuthResponse with token and user`() {
        val user = UserDto(id = 1, email = "test@example.com", name = "Test User", role = "ESTUDIANTE")
        val token = "test-token"
        val authResponse = AuthResponse(token = token, user = user)

        assertEquals(token, authResponse.token)
        assertEquals(user, authResponse.user)
    }

    @Test
    fun `should create AuthResponse with different roles`() {
        val user = UserDto(id = 2, email = "empresa@test.com", name = "Empresa Test", role = "EMPRESA")
        val authResponse = AuthResponse(token = "token-empresa", user = user)

        assertEquals("EMPRESA", authResponse.user.role)
        assertEquals(user, authResponse.user)
    }
}
