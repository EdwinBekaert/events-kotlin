package bl.event

import base.Args
import base.EntDelegate
import base.getOrDefaultCasted
import base.whenRow
import data.Events
import org.jetbrains.exposed.sql.Query

class Ent (qryData: Query? = null, args: Args) : base.Ent(args = args) {

    companion object { val primaryKey = Ent::eventId.name}

    var eventId: Long? by EntDelegate(this.dirtyFields)
    var eventCode: String? by EntDelegate(this.dirtyFields)
    var seatPrice: Double? by EntDelegate(this.dirtyFields)
    var seatPriceEuro: Double? by EntDelegate(this.dirtyFields)

    lateinit var venue: bl.venue.Ent
    lateinit var anotherEnt: AnotherEnt // do this later... whenEnt() of srv

    // init after prop definitions otherwise not accessible
    init {

        qryData.whenRow {
            eventId = it[Events.event_id]
            eventCode = it[Events.event_name]
            seatPrice = it[Events.seat_price]
            seatPriceEuro = it[Events.seat_price_euro]
        }

        // set args to override TODO this:: or Ent:: ???
        eventId   = args.getOrDefaultCasted(key = Ent::eventId.name,    defaultValue = eventId) // send this::eventId.name so when prop name is refactored, it will still work
        eventCode = args.getOrDefaultCasted(key = this::eventCode.name, defaultValue = eventCode)
        seatPrice = args.getOrDefaultCasted(key = this::seatPrice.name, defaultValue = seatPrice)
        seatPriceEuro = args.getOrDefaultCasted(key = this::seatPriceEuro.name, defaultValue = seatPriceEuro)

    }

}

