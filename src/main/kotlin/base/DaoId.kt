package base

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query

abstract class DaoId(private val dbConn: Database): Dao(dbConn = dbConn){
    internal abstract fun getDetails(id: Long): Query?

}
