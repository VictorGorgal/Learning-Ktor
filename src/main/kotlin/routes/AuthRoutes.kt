package routes

import entities.TestTableEntry
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import responses.Response

private val database = DatabaseConnection.getInstance()

fun Route.authRoutes() {
    route("/auth") {
        registerUser()
        loginUser()
    }
}

private fun Route.registerUser() {
    post("/register") {
        val database = DatabaseConnection.getInstance()

        val entry: TestTableEntry
        try {
            entry = call.receive<TestTableEntry>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInBody())
            return@post
        }

        entry.email = entry.email.lowercase()
        if (database.doesEmailExists(entry.email)) {
            call.respond(HttpStatusCode.BadRequest, Response.errorEmailAlreadyExists())
            return@post
        }

        entry.password = entry.hashedPassword()
        val result = database.insertEntry(entry)
        if (result != 1) {
            call.respond(HttpStatusCode.BadRequest, Response.errorCouldNotFinishOperation())
            return@post
        }

        call.respond(HttpStatusCode.OK, Response.emptySuccess())
    }
}

private fun Route.loginUser() {
    post("/login") {
        val database = DatabaseConnection.getInstance()

        val entry: TestTableEntry
        try {
            entry = call.receive<TestTableEntry>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInBody())
            return@post
        }

        entry.email = entry.email.lowercase()
        val user = database.getUserByEmail(entry.email)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInvalidEmailOrPassword())
            return@post
        }

        if (!BCrypt.checkpw(entry.password, user.password)) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInvalidEmailOrPassword())
            return@post
        }

        call.respond(HttpStatusCode.OK, Response.success("User successfully logged in"))
    }
}
