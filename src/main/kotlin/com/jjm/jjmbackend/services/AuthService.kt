package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.dto.LoginRequest
import com.jjm.jjmbackend.dto.RegisterRequest
import com.jjm.jjmbackend.models.User
import com.jjm.jjmbackend.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository
) {

    // 🔹 REGISTRO
    fun register(request: RegisterRequest): User? {

        // 1. Verificar si el usuario ya existe
        val existingUser = userRepository.findByEmailRaw(request.email)

        if (existingUser != null) {
            return null // email ya registrado
        }

        // 2. Hash seguro con BCrypt
        val hashedPassword = hashPassword(request.password)

        // 3. Crear usuario
        return userRepository.create(
            email = request.email,
            password = hashedPassword,
            name = request.name
        )
    }

    // 🔹 LOGIN
    fun login(request: LoginRequest): User? {

        // 1. Buscar usuario en DB
        val row = userRepository.findByEmailRaw(request.email)
            ?: return null

        val storedPassword = row[UsersTable.password]

        // 2. Validar password
        if (!verifyPassword(request.password, storedPassword)) {
            return null
        }

        // 3. Retornar modelo limpio
        return userRepository.findModelByEmail(request.email)
    }

    // 🔹 HASH con BCrypt
    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(12))
    }

    // 🔹 VERIFICACIÓN con BCrypt
    private fun verifyPassword(input: String, stored: String): Boolean {
        return BCrypt.checkpw(input, stored)
    }
}