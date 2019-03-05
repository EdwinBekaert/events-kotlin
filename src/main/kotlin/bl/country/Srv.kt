package bl.country

import base.Srv
import bl.Req
import org.jetbrains.exposed.sql.Query

class Srv(private val mngr: Mngr) : Srv(mngr = mngr){

    override val primaryKey = Ent.primaryKey // gives base service access to id

    override fun whenEnt(objEnt: base.Ent, qryData: Query?, objReq: Req) {
        with(objEnt as Ent){

        }
    }

}
