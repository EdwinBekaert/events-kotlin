package bl.country

import base.Srv
import bl.Req

class Srv(private val mngr: Mngr) : Srv(mngr = mngr) {

    override val primaryKey = Ent.primaryKey // gives base service access to id

    override fun whenEnt(objEnt: base.Ent, objReq: Req) {
        /*with(objEnt as Ent){

        }*/
    }

}
