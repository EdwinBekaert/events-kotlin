package bl.event

import base.Dao
import data.Events
import data.Venues
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.select

class Dao(dbConn: Database): Dao(dbConn = dbConn){

    override fun getDetails(id: Long)
            = (Events leftJoin Venues).select { Events.event_id eq id }

    override fun getDetails(code: String): Query? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
