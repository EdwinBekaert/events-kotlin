package base

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.transactions.transaction

abstract class Dao(private val dbConn: Database) : Base() {

    // can we make one of the 2 abstract ???
    internal abstract fun getDetails(id: Long): Query?
    internal abstract fun getDetails(code: String): Query?

    // inject database connection in transaction
    internal fun <T> dbTrans (block: () -> T): T = transaction(db = dbConn) { block() }

}