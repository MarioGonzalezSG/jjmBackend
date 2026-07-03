package com.jjm.jjmbackend.repositories

import com.jjm.jjmbackend.database.tables.AttendanceTable
import com.jjm.jjmbackend.database.tables.UsersTable
import com.jjm.jjmbackend.models.Attendance
import com.jjm.jjmbackend.dto.AttendanceResponse
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AttendanceRepository {

    fun create(studentId: Int, companyId: Int, date: String, status: String, notes: String?): Attendance? {
        return transaction {
            AttendanceTable.insert {
                it[AttendanceTable.studentId] = studentId
                it[AttendanceTable.companyId] = companyId
                it[AttendanceTable.date] = date
                it[AttendanceTable.status] = status
                it[AttendanceTable.notes] = notes
            }.resultedValues?.firstOrNull()?.let { mapRow(it) }
        }
    }

    fun findByStudent(studentId: Int, companyId: Int): List<AttendanceResponse> {
        return transaction {
            (AttendanceTable innerJoin UsersTable)
                .select { (AttendanceTable.studentId eq studentId) and (AttendanceTable.companyId eq companyId) }
                .map { row ->
                    AttendanceResponse(
                        id = row[AttendanceTable.id],
                        studentId = row[AttendanceTable.studentId],
                        studentName = row[UsersTable.name],
                        date = row[AttendanceTable.date],
                        status = row[AttendanceTable.status],
                        notes = row[AttendanceTable.notes]
                    )
                }
        }
    }

    fun update(id: Int, status: String?, notes: String?): Boolean {
        transaction {
            exec("UPDATE attendance SET status = '$status', notes = '$notes' WHERE id = $id")
        }
        return true
    }

    private fun mapRow(row: ResultRow): Attendance {
        return Attendance(
            id = row[AttendanceTable.id],
            studentId = row[AttendanceTable.studentId],
            companyId = row[AttendanceTable.companyId],
            date = row[AttendanceTable.date],
            status = row[AttendanceTable.status],
            notes = row[AttendanceTable.notes]
        )
    }
}
