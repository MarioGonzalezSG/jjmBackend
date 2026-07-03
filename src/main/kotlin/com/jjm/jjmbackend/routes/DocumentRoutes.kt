package com.jjm.jjmbackend.routes

import com.jjm.jjmbackend.dto.DocumentRequest
import com.jjm.jjmbackend.middleware.requireAuth
import com.jjm.jjmbackend.middleware.requireRole
import com.jjm.jjmbackend.repositories.DocumentRepository
import com.jjm.jjmbackend.services.DocumentService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.documentRoutes() {

    val documentService = DocumentService(DocumentRepository())

    route("/documents") {

        post {
            val user = requireAuth(call)
            val request = try {
                call.receive<DocumentRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }
            val doc = documentService.upload(request, user.userId)
            if (doc == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al subir documento"))
                return@post
            }
            call.respond(HttpStatusCode.Created, doc)
        }

        get("/mis-documentos") {
            val user = requireRole(call, "ESTUDIANTE")
            val docs = documentService.getMyDocuments(user.userId)
            call.respond(HttpStatusCode.OK, docs)
        }

        get("/vacante/{vacanteId}") {
            requireRole(call, "EMPRESA")
            val vacanteId = call.parameters["vacanteId"]?.toIntOrNull()
            if (vacanteId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID invalido"))
                return@get
            }
            val docs = documentService.getByVacante(vacanteId)
            call.respond(HttpStatusCode.OK, docs)
        }

        get("/oficiales") {
            val docs = documentService.getOfficial()
            call.respond(HttpStatusCode.OK, docs)
        }

        post("/oficiales") {
            val user = requireRole(call, "ADMINISTRADOR")
            val request = try {
                call.receive<DocumentRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Cuerpo de solicitud invalido"))
                return@post
            }
            val doc = documentService.upload(
                DocumentRequest(name = request.name, type = "OFICIAL", fileUrl = request.fileUrl),
                user.userId
            )
            if (doc == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al subir documento"))
                return@post
            }
            call.respond(HttpStatusCode.Created, doc)
        }
    }
}
