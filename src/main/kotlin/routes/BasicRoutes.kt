package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.io.File

fun Route.basicRoutes() {
    bodyTestRoute()
    downloadFile()
    openFile()
}

private fun Route.bodyTestRoute() {
    get("/body") {
        println("URI: ${call.request.uri}")
        println("Headers: ${call.request.headers.names()}")
        val body = call.receive<UserInfo>()
        println("Body: $body")

        call.respondText("Hello World!")
    }
}

private fun Route.downloadFile() {
    get("/download-file") {
        val file = File("files/crow.png")

        call.response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Attachment.withParameter(
                ContentDisposition.Parameters.FileName, "image.png"
            ).toString()
        )

        call.respondFile(file)
    }
}

private fun Route.openFile() {
    get("/open-file") {
        val file = File("files/crow.png")

        call.response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Inline.withParameter(
                ContentDisposition.Parameters.FileName, "image.png"
            ).toString()
        )

        call.respondFile(file)
    }
}

@Serializable
data class UserInfo (
    val user: String,
    val password: String
)
