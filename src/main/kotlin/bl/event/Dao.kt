package bl.event

import base.DaoId
import data.Events
import data.Venues
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select

class Dao(dbConn: Database): DaoId(dbConn = dbConn) {

    override fun getDetails(id: Long)
            = (Events leftJoin Venues).select { Events.event_id eq id }

}
