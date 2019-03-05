package bl.event

import base.Dao
import data.MeAnother
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.select

class AnotherDao(dbConn: Database): Dao(dbConn = dbConn){

    fun getDetailsByEvent(id: Long)
            = MeAnother.select { MeAnother.event_id eq id }

    override fun getDetails(id: Long) : Query {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDetails(code: String): Query? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
