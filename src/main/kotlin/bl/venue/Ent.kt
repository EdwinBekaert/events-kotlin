package bl.venue

import base.Args
import base.EntDelegate
import base.getOrDefaultCasted
import base.whenRow
import data.Venues
import org.jetbrains.exposed.sql.Query

class Ent (override val qryData: Query? = null, override val args: Args) : base.Ent(qryData = qryData, args = args){

    companion object { val primaryKey = Ent::venueId.name}

    var venueId: Long? by EntDelegate(this.dirtyFields)
    var venueName: String? by EntDelegate(this.dirtyFields)

    lateinit var country: bl.country.Ent

    init {

        qryData.whenRow {
            venueId = it[Venues.venue_id]
            venueName = it[Venues.venue_name]
        }

        // set args to override TODO this:: or Ent:: ???
        venueId   = args.getOrDefaultCasted(key = Ent::venueId.name,   defaultValue = venueId) // send this::eventId.name so when prop name is refactored, it will still work
        venueName = args.getOrDefaultCasted(key = this::venueName.name, defaultValue = venueName)

    }

}