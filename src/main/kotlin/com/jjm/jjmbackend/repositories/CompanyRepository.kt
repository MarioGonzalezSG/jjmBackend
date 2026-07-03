package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.CompaniesTable
import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.models.Company
import com.jjm.jjmbackend.dto.CompanyResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CompanyRepository {

    fun create(userId: Int): Company? {
        return transaction {
            CompaniesTable.insert {
                it[CompaniesTable.userId] = userId
            }.resultedValues?.firstOrNull()?.let { mapRow(it) }
        }
    }

    fun findByUserId(userId: Int): Company? {
        return transaction {
            CompaniesTable.select { CompaniesTable.userId eq userId }
                .mapNotNull { mapRow(it) }
                .singleOrNull()
        }
    }

    fun update(userId: Int, description: String?, address: String?, phone: String?, website: String?, latitude: Double?, longitude: Double?): Company? {
        transaction {
            exec("""
                UPDATE companies SET
                    description = '$description',
                    address = '$address',
                    phone = '$phone',
                    website = '$website',
                    latitude = $latitude,
                    longitude = $longitude
                WHERE user_id = $userId
            """.trimIndent())
        }
        return findByUserId(userId)
    }

    fun getAllWithLocation(): List<CompanyResponse> {
        return transaction {
            (CompaniesTable innerJoin UsersTable)
                .select { UsersTable.role eq "EMPRESA" }
                .map { row ->
                    CompanyResponse(
                        id = row[CompaniesTable.id],
                        userId = row[CompaniesTable.userId],
                        companyName = row[UsersTable.name],
                        email = row[UsersTable.email],
                        description = row[CompaniesTable.description],
                        address = row[CompaniesTable.address],
                        phone = row[CompaniesTable.phone],
                        website = row[CompaniesTable.website],
                        latitude = row[CompaniesTable.latitude],
                        longitude = row[CompaniesTable.longitude]
                    )
                }
        }
    }

    private fun mapRow(row: ResultRow): Company {
        return Company(
            id = row[CompaniesTable.id],
            userId = row[CompaniesTable.userId],
            description = row[CompaniesTable.description],
            address = row[CompaniesTable.address],
            phone = row[CompaniesTable.phone],
            website = row[CompaniesTable.website],
            latitude = row[CompaniesTable.latitude],
            longitude = row[CompaniesTable.longitude]
        )
    }
}
