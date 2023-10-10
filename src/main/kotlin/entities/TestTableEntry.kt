package entities

import kotlinx.serialization.Serializable
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class TestTableEntry(
    var id: Int = 0,
    var name: String = "",
    var email: String,
    var password: String
) {
    fun hashedPassword(): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}

object TestTableEntities: BaseTable<TestTableEntry>("test_table") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val email = varchar("email")
    val password = varchar("password")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = TestTableEntry(
        id = row[id]!!,
        name = row[name]!!,
        email = row[email]!!,
        password = row[password]!!,
    )
}
