package bl.venue

import base.Args
import base.Srv
import base.tryGetFirst
import bl.Pkgs
import bl.Req
import data.Venues
import org.jetbrains.exposed.sql.Query

class Srv(private val mngr: Mngr) : Srv(mngr = mngr) {

    override val primaryKey = Ent.primaryKey // gives base service access to id

    override fun whenEnt(objEnt: base.Ent, qryData: Query?, objReq: Req) {
        with(objEnt as Ent) {
            whenDirty(objEnt::venueId.name) {
                println("id is dirty $venueId")
            }
            // get country data which is NOT in qryData, so fetch from countrySrv...
            // when not null -> get country by id
            // when no country code -> get country by data
            country = qryData.tryGetFirst(Venues.country_code)?.let {
                val newArgs: Args = args.plus(bl.country.Ent.primaryKey to it)
                mngr.getMngr(Pkgs.COUNTRY).getSrv().getEntByCode(objReq = objReq, args = newArgs) as bl.country.Ent
            } ?: mngr.getMngr(Pkgs.COUNTRY).getSrv().getEntByData(objReq = objReq, args = args) as bl.country.Ent
        }
    }
}


