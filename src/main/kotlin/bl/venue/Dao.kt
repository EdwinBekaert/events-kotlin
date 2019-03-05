package bl.venue

import base.Dao
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query

class Dao(dbConn: Database): Dao(dbConn = dbConn){

    override fun getDetails(id: Long): Query {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDetails(code: String): Query? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
