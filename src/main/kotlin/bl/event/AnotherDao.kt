package bl.event

import base.Dao
import data.MeAnother
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select

class AnotherDao(dbConn: Database): Dao(dbConn = dbConn){

    fun getDetailsByEvent(id: Long)
            = MeAnother.select { MeAnother.event_id eq id }

}
