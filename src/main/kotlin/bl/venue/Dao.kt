package bl.venue

import base.DaoId
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query

class Dao(dbConn: Database): DaoId(dbConn = dbConn){

    override fun getDetails(id: Long): Query {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
