package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.dto.LoginRequest
import com.jjm.jjmbackend.dto.RegisterRequest
import com.jjm.jjmbackend.dto.AuthResponse
import com.jjm.jjmbackend.dto.UserDto
import com.jjm.jjmbackend.models.User
import com.jjm.jjmbackend.repositories.UserRepository
import com.jjm.jjmbackend.repositories.CompanyRepository
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository,
    private val companyRepository: CompanyRepository
) {
    fun register(request: RegisterRequest): AuthResponse? {
        val existingUser = userRepository.findByEmailRaw(request.email)
        if (existingUser != null) return null

        val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt(12))
        val user = userRepository.create(
            email = request.email,
            password = hashedPassword,
            name = request.name,
            role = request.role
        ) ?: return null

        if (request.role == "EMPRESA") {
            companyRepository.create(user.id)
        }

        val token = JwtService.generateToken(user.id, user.email, user.role)
        return AuthResponse(
            token = token,
            user = UserDto(user.id, user.email, user.name, user.role)
        )
    }

    fun login(request: LoginRequest): AuthResponse? {
        val row = userRepository.findByEmailRaw(request.email) ?: return null
        val storedPassword = row[UsersTable.password]

        if (!BCrypt.checkpw(request.password, storedPassword)) return null

        val user = userRepository.findModelByEmail(request.email) ?: return null

        val token = JwtService.generateToken(user.id, user.email, user.role)
        return AuthResponse(
            token = token,
            user = UserDto(user.id, user.email, user.name, user.role)
        )
    }

    fun getUserFromToken(userId: Int): User? {
        return userRepository.findById(userId)
    }
}
