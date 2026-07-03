package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.VacantesTable
import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.database.tables.CompaniesTable
import com.jjm.jjmbackend.models.Vacante
import com.jjm.jjmbackend.dto.VacanteResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class VacanteRepository {

    fun create(
        companyId: Int, title: String, description: String, requirements: String?,
        slots: Int, area: String?, duration: String?, schedule: String?, location: String?,
        latitude: Double?, longitude: Double?, createdAt: String
    ): Vacante? {
        return transaction {
            VacantesTable.insert {
                it[VacantesTable.companyId] = companyId
                it[VacantesTable.title] = title
                it[VacantesTable.description] = description
                it[VacantesTable.requirements] = requirements
                it[VacantesTable.slots] = slots
                it[VacantesTable.area] = area
                it[VacantesTable.duration] = duration
                it[VacantesTable.schedule] = schedule
                it[VacantesTable.location] = location
                it[VacantesTable.latitude] = latitude
                it[VacantesTable.longitude] = longitude
                it[VacantesTable.createdAt] = createdAt
            }.resultedValues?.firstOrNull()?.let { mapRow(it) }
        }
    }

    fun findById(id: Int): Vacante? {
        return transaction {
            VacantesTable.select { VacantesTable.id eq id }
                .mapNotNull { mapRow(it) }
                .singleOrNull()
        }
    }

    fun findAll(area: String? = null, location: String? = null): List<VacanteResponse> {
        return transaction {
            val baseQuery = VacantesTable
                .innerJoin(UsersTable, { VacantesTable.companyId }, { UsersTable.id })

            var query = baseQuery.selectAll()

            if (area != null) {
                query = query.andWhere { VacantesTable.area.lowerCase() eq area.lowercase() }
            }
            if (location != null) {
                query = query.andWhere { VacantesTable.location.lowerCase() like "%${location.lowercase()}%" }
            }

            query.map { row ->
                VacanteResponse(
                    id = row[VacantesTable.id],
                    companyId = row[VacantesTable.companyId],
                    companyName = row[UsersTable.name],
                    title = row[VacantesTable.title],
                    description = row[VacantesTable.description],
                    requirements = row[VacantesTable.requirements],
                    slots = row[VacantesTable.slots],
                    area = row[VacantesTable.area],
                    duration = row[VacantesTable.duration],
                    schedule = row[VacantesTable.schedule],
                    location = row[VacantesTable.location],
                    status = row[VacantesTable.status],
                    createdAt = row[VacantesTable.createdAt],
                    latitude = row[VacantesTable.latitude],
                    longitude = row[VacantesTable.longitude]
                )
            }
        }
    }

    fun findByCompanyId(companyId: Int): List<Vacante> {
        return transaction {
            VacantesTable.select { VacantesTable.companyId eq companyId }
                .mapNotNull { mapRow(it) }
        }
    }

    fun update(id: Int, title: String?, description: String?, requirements: String?, slots: Int?,
               area: String?, duration: String?, schedule: String?, location: String?,
               latitude: Double?, longitude: Double?): Boolean {
        return transaction {
            VacantesTable.update({ VacantesTable.id eq id }) { upd ->
                title?.let { upd[VacantesTable.title] = it }
                description?.let { upd[VacantesTable.description] = it }
                requirements?.let { upd[VacantesTable.requirements] = it }
                slots?.let { upd[VacantesTable.slots] = it }
                area?.let { upd[VacantesTable.area] = it }
                duration?.let { upd[VacantesTable.duration] = it }
                schedule?.let { upd[VacantesTable.schedule] = it }
                location?.let { upd[VacantesTable.location] = it }
                latitude?.let { upd[VacantesTable.latitude] = it }
                longitude?.let { upd[VacantesTable.longitude] = it }
            } > 0
        }
    }

    fun findAllActive(): List<Vacante> {
        return transaction {
            VacantesTable.select { VacantesTable.status eq "ACTIVA" }
                .mapNotNull { mapRow(it) }
        }
    }

    fun updateStatus(id: Int, status: String): Boolean {
        return transaction {
            VacantesTable.update({ VacantesTable.id eq id }) {
                it[VacantesTable.status] = status
            } > 0
        }
    }

    fun countActive(): Int {
        return transaction {
            VacantesTable.select { VacantesTable.status eq "ACTIVA" }.count().toInt()
        }
    }

    fun delete(id: Int): Boolean {
        transaction {
            exec("DELETE FROM vacantes WHERE id = $id")
        }
        return true
    }

    private fun mapRow(row: ResultRow): Vacante {
        return Vacante(
            id = row[VacantesTable.id],
            companyId = row[VacantesTable.companyId],
            title = row[VacantesTable.title],
            description = row[VacantesTable.description],
            requirements = row[VacantesTable.requirements],
            slots = row[VacantesTable.slots],
            area = row[VacantesTable.area],
            duration = row[VacantesTable.duration],
            schedule = row[VacantesTable.schedule],
            location = row[VacantesTable.location],
            latitude = row[VacantesTable.latitude],
            longitude = row[VacantesTable.longitude],
            status = row[VacantesTable.status],
            createdAt = row[VacantesTable.createdAt]
        )
    }
}
