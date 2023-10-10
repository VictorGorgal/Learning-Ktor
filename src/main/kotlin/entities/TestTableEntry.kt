package entities

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int
import org.ktorm.schema.text

data class TestTableEntry(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)

object TestTableEntities: BaseTable<TestTableEntry>("test_table") {
    val id = int("id").primaryKey()
    val name = text("name")
    val email = text("email")
    val password = text("password")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = TestTableEntry(
        id = row[id]!!,
        name = row[name]!!,
        email = row[email]!!,
        password = row[password]!!,
    )
}