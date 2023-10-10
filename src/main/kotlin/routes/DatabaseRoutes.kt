package routes

import DatabaseConnection
import responses.Response
import entities.TestTableEntry
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.databaseRoutes() {
    route("/database") {
        getEntries()
        addEntry()
        updateEntry()
        deleteEntry()
    }
}

private fun Route.getEntries() {
    get("/{id?}") {
        val database = DatabaseConnection.getInstance()

        val input: String? = call.parameters["id"]
        if (input == null) {
            val entries = database.getEntries()
            call.respond(HttpStatusCode.OK, Response.success(entries))
            return@get
        }

        val id = input.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInHeader())
            return@get
        }

        val entry = database.getEntryAt(id)
        if (entry == null) {
            call.respond(HttpStatusCode.NotFound, Response.errorEntryNotFound())
        }

        call.respond(HttpStatusCode.OK, Response.success(entry))
    }
}

private fun Route.addEntry() {
    post("/") {
        val database = DatabaseConnection.getInstance()

        val entry: TestTableEntry
        try {
            entry = call.receive<TestTableEntry>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInBody())
            return@post
        }

        val result = database.insertEntry(entry)
        if (result != 1) {
            call.respond(HttpStatusCode.BadRequest, Response.errorCouldNotFinishOperation())
            return@post
        }

        call.respond(HttpStatusCode.OK, Response.emptySuccess())
    }
}

private fun Route.updateEntry() {
    put("/{id?}") {
        val database = DatabaseConnection.getInstance()

        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInHeader())
            return@put
        }

        val entry: TestTableEntry
        try {
            entry = call.receive<TestTableEntry>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInBody())
            return@put
        }

        val result = database.updateEntry(entry)
        if (result != 1) {
            call.respond(HttpStatusCode.BadRequest, Response.errorCouldNotFinishOperation())
        }

        call.respond(HttpStatusCode.OK, Response.emptySuccess())
    }
}

private fun Route.deleteEntry() {
    delete("/{id?}") {
        val database = DatabaseConnection.getInstance()

        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, Response.errorInHeader())
            return@delete
        }

        val rowsEffected = database.deleteEntryAt(id)
        if (rowsEffected != 1) {
            call.respond(HttpStatusCode.BadRequest, Response.errorCouldNotFinishOperation())
            return@delete
        }

        call.respond(HttpStatusCode.OK, Response.emptySuccess())
    }
}
