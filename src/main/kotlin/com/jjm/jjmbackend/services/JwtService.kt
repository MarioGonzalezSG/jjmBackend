package com.jjm.jjmbackend.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import io.github.cdimascio.dotenv.dotenv
import java.util.*

object JwtService {

    private val dotenv = dotenv { ignoreIfMissing = true }

    private val secret = System.getenv("JWT_SECRET") ?: dotenv["JWT_SECRET"] ?: "default_secret"
    private val issuer = System.getenv("JWT_ISSUER") ?: dotenv["JWT_ISSUER"] ?: "jjm-academic"
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(userId: Int, email: String, role: String): String {
        return JWT.create()
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + 86400000 * 7)) // 7 days
            .sign(algorithm)
    }

    fun verifyToken(token: String): DecodedJWT? {
        return try {
            JWT.require(algorithm)
                .withIssuer(issuer)
                .build()
                .verify(token)
        } catch (e: Exception) {
            null
        }
    }
}
