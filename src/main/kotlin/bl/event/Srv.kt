package bl.event

import base.Args
import base.Srv
import base.argsOf
import base.getOrErrorCasted
import bl.Pkgs
import bl.Req
import org.jetbrains.exposed.sql.Query

class Srv(private val mngr: Mngr) : Srv(mngr = mngr) {

    override val primaryKey = Ent.primaryKey // gives base service access to id

    // hook when Ent is created
    override fun whenEnt(objEnt: base.Ent, objReq: Req) {
        // TODO what about this cast?
        with(objEnt as Ent){
            // get another ent from the same package
            anotherEnt = getAnotherEntById(args = argsOf(Ent.primaryKey to objEnt.eventId), objReq = objReq)

            // get another ent from the another package
            // both event & venue are queried together, so pass qry & args to getEntByData() and create venue ent
            val venueSrv = mngr.getMngr(Pkgs.VENUE).getSrv()
            venue = venueSrv.getEntByData(objReq = objReq, args = objEnt.args, qryData = qryData)
            venue.whenDirty(venue::venueId.name){
                // when Id is changed reload data
                // also check currency
                println(it)
            }

            // TODO this:: or Ent:: or objEnt:: or ::
            whenDirty(objEnt::eventId.name){
                println("id is dirty $eventId")
            }

            // :: == this::eventCode.name
            whenDirty(::eventCode.name){
                println("event code is dirty. Current value: ${objEnt.eventCode}")
                it.forEach { println(it) } // print all history
            }

        }
    }

    private fun getAnotherEntById(objReq: Req, args: Args): AnotherEnt {
        val entId: Long = args.getOrErrorCasted(Ent::eventId.name)
        val objDao = mngr.getAnotherDao()
        return objDao.dbTrans {
            val qryData = objDao.getDetailsByEvent(id = entId)
            mngr.getAnotherEnt(objReq = objReq, qryData = qryData, args = args)
        }
    }

}

