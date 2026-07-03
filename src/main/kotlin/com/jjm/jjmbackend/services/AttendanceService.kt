package com.jjm.jjmbackend.services

import com.jjm.jjmbackend.dto.AttendanceRequest
import com.jjm.jjmbackend.dto.AttendanceResponse
import com.jjm.jjmbackend.repositories.AttendanceRepository

class AttendanceService(
    private val attendanceRepository: AttendanceRepository
) {
    fun register(companyId: Int, request: AttendanceRequest): Boolean {
        attendanceRepository.create(
            studentId = request.studentId,
            companyId = companyId,
            date = request.date,
            status = request.status,
            notes = request.notes
        )
        return true
    }

    fun getByStudent(studentId: Int, companyId: Int): List<AttendanceResponse> {
        return attendanceRepository.findByStudent(studentId, companyId)
    }

    fun update(id: Int, status: String?, notes: String?): Boolean {
        return attendanceRepository.update(id, status, notes)
    }
}
