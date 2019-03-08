package base

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

abstract class Dao(private val dbConn: Database) : Base() {

    // inject database connection in transaction
    internal fun <T> dbTrans (block: () -> T): T = transaction(db = dbConn) { block() }

}