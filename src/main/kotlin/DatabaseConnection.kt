import entities.TestTableEntities
import entities.TestTableEntry
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.lang.Exception

class DatabaseConnection private constructor() {
    companion object {
        @Volatile
        private var instance: DatabaseConnection? = null
        @Volatile
        private lateinit var database: Database

        fun getInstance(): DatabaseConnection =
            instance?: synchronized(this) {
                instance?: DatabaseConnection().also { instance = it }
            }

        fun initDatabase() {
            database = Database.connect(
                url = "jdbc:mariadb://localhost:3306/testdb",
                driver = "org.mariadb.jdbc.Driver",
                user = "root",
                password = "Morenamorenas1!"
            )
        }
    }

    fun getEntryAt(index: Int): TestTableEntry? {
        val entries = database.from(TestTableEntities)
            .select()
            .where { TestTableEntities.id eq index }
            .map { TestTableEntities.createEntity(it) }

        if (entries.isEmpty()) {
            return null
        }

        if (entries.size > 1) {
            throw Exception("Check database, multiple items with id $index where found")
        }

        return entries.first()
    }

    fun getEntries(): List<TestTableEntry> {
        return database.from(TestTableEntities)
            .select()
            .map { TestTableEntities.createEntity(it) }
    }

    fun insertEntry(name: String, email: String, password: String) {
        database.insert(TestTableEntities) {
            set(it.name, name)
            set(it.email, email)
            set(it.password, password)
        }
    }

    fun insertEntry(entry: TestTableEntry): Int {
        return database.insert(TestTableEntities) {
            set(it.name, entry.name)
            set(it.email, entry.email)
            set(it.password, entry.password)
        }
    }

    fun updateEntry(entry: TestTableEntry): Int {
        return database.update(TestTableEntities) {
            set(it.name, entry.name)
            set(it.email, entry.email)
            set(it.password, entry.password)
            where { it.id eq entry.id }
        }
    }

    fun deleteEntry(entry: TestTableEntry) {
        database.delete(TestTableEntities) {
            it.id eq entry.id
        }
    }
}
