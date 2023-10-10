package plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.io.File

fun Application.configureRouting() {
    routing {
        get("/body") {
            println("URI: ${call.request.uri}")
            println("Headers: ${call.request.headers.names()}")
            val body = call.receive<UserInfo>()
            println("Body: $body")

            call.respondText("Hello World!")
        }

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
}

@Serializable
data class UserInfo (
    val user: String,
    val password: String
)
