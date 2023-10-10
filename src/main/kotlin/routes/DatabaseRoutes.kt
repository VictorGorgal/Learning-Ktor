package routes

import DatabaseConnection
import io.ktor.server.routing.*

val database = DatabaseConnection.getInstance()

fun Route.databaseRoutes() {
    route("/database") {
        getEntryAt()
        addEntry()
        updateEntry()
        deleteEntry()
    }
}

private fun Route.getEntryAt() {
    get("/{id}") {
        val entry = database.getEntryAt(1)
    }
}

private fun Route.addEntry() {
    post("/{id}") {
    }
}

private fun Route.updateEntry() {
    put("/{id}") {
    }
}

private fun Route.deleteEntry() {
    delete("/{id}") {
    }
}
