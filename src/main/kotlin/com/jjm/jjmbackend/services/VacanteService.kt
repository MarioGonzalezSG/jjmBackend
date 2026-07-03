package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.dto.VacanteRequest
import com.jjm.jjmbackend.dto.VacanteResponse
import com.jjm.jjmbackend.repositories.VacanteRepository
import com.jjm.jjmbackend.repositories.UserRepository

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class VacanteService(
    private val vacanteRepository: VacanteRepository,
    private val userRepository: UserRepository
) {
    fun create(companyId: Int, request: VacanteRequest): VacanteResponse? {
        val vacante = vacanteRepository.create(
            companyId = companyId,
            title = request.title,
            description = request.description,
            requirements = request.requirements,
            slots = request.slots,
            area = request.area,
            duration = request.duration,
            schedule = request.schedule,
            location = request.location,
            latitude = request.latitude,
            longitude = request.longitude,
            createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        ) ?: return null
        val user = userRepository.findById(companyId) ?: return null
        return VacanteResponse(
            id = vacante.id,
            companyId = vacante.companyId,
            companyName = user.name,
            title = vacante.title,
            description = vacante.description,
            requirements = vacante.requirements,
            slots = vacante.slots,
            area = vacante.area,
            duration = vacante.duration,
            schedule = vacante.schedule,
            location = vacante.location,
            status = vacante.status,
            createdAt = vacante.createdAt,
            latitude = vacante.latitude,
            longitude = vacante.longitude
        )
    }

    fun getAll(area: String? = null, location: String? = null): List<VacanteResponse> {
        return vacanteRepository.findAll(area, location)
    }

    fun getById(id: Int): VacanteResponse? {
        val vacante = vacanteRepository.findById(id) ?: return null
        val user = userRepository.findById(vacante.companyId) ?: return null
        return VacanteResponse(
            id = vacante.id,
            companyId = vacante.companyId,
            companyName = user.name,
            title = vacante.title,
            description = vacante.description,
            requirements = vacante.requirements,
            slots = vacante.slots,
            area = vacante.area,
            duration = vacante.duration,
            schedule = vacante.schedule,
            location = vacante.location,
            status = vacante.status,
            createdAt = vacante.createdAt,
            latitude = vacante.latitude,
            longitude = vacante.longitude
        )
    }

    fun getByCompany(companyId: Int): List<VacanteResponse> {
        return vacanteRepository.findByCompanyId(companyId).map { vacante ->
            val user = userRepository.findById(vacante.companyId)
            VacanteResponse(
                id = vacante.id,
                companyId = vacante.companyId,
                companyName = user?.name ?: "",
                title = vacante.title,
                description = vacante.description,
                requirements = vacante.requirements,
                slots = vacante.slots,
                area = vacante.area,
                duration = vacante.duration,
                schedule = vacante.schedule,
                location = vacante.location,
                status = vacante.status,
                createdAt = vacante.createdAt,
            latitude = vacante.latitude,
            longitude = vacante.longitude
        )
    }
    }

    fun update(id: Int, request: VacanteRequest): Boolean {
        return vacanteRepository.update(
            id = id,
            title = request.title,
            description = request.description,
            requirements = request.requirements,
            slots = request.slots,
            area = request.area,
            duration = request.duration,
            schedule = request.schedule,
            location = request.location,
            latitude = request.latitude,
            longitude = request.longitude
        )
    }

    fun delete(id: Int): Boolean {
        return vacanteRepository.delete(id)
    }
}
