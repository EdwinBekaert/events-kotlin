package bl.venue

import base.Srv
import base.whenRow
import bl.Pkgs
import bl.Req
import data.Venues
import org.jetbrains.exposed.sql.Query

class Srv(private val mngr: Mngr) : Srv(mngr = mngr){

    override val primaryKey = Ent.primaryKey // gives base service access to id

    override fun whenEnt(objEnt: base.Ent, qryData: Query?, objReq: Req) {
        with(objEnt as Ent){
            whenDirty(objEnt::venueId.name){
                println("id is dirty $venueId")
            }
            // get country data which is NOT in qry, so fetch from countrySrv...
            var countryCode: String = ""
            qryData.whenRow { countryCode = it[Venues.country_code]!!}
            val newArgs = args.plus(bl.country.Ent.primaryKey to countryCode)
            country = mngr.getMngr(Pkgs.COUNTRY).getSrv().getEntByCode(objReq = objReq, args = newArgs) as bl.country.Ent
        }
    }

}
