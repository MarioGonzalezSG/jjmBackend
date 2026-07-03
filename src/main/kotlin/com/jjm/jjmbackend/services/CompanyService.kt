package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.dto.CompanyRequest
import com.jjm.jjmbackend.dto.CompanyResponse
import com.jjm.jjmbackend.repositories.CompanyRepository
import com.jjm.jjmbackend.repositories.UserRepository

class CompanyService(
    private val companyRepository: CompanyRepository,
    private val userRepository: UserRepository
) {
    fun getProfile(userId: Int): CompanyResponse? {
        val company = companyRepository.findByUserId(userId) ?: return null
        val user = userRepository.findById(userId) ?: return null
        return CompanyResponse(
            id = company.id,
            userId = company.userId,
            companyName = user.name,
            email = user.email,
            description = company.description,
            address = company.address,
            phone = company.phone,
            website = company.website,
            latitude = company.latitude,
            longitude = company.longitude
        )
    }

    fun updateProfile(userId: Int, request: CompanyRequest): CompanyResponse? {
        companyRepository.update(
            userId = userId,
            description = request.description,
            address = request.address,
            phone = request.phone,
            website = request.website,
            latitude = request.latitude,
            longitude = request.longitude
        )
        return getProfile(userId)
    }

    fun getAllWithLocation(): List<CompanyResponse> {
        return companyRepository.getAllWithLocation()
    }
}
