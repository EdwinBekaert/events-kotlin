package data

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert

object Events : Table("EVENT") {
    val event_id = long("event_id").primaryKey()
    val event_name = varchar("event_name", 50)
    val venue_id = (long("venue_id") references Venues.venue_id).nullable()
    val seat_price = double("seat_price").default(0.00)
    val seat_price_euro = double("seat_price_euro").default(0.00)
}

object Venues : Table("VENUE") {
    val venue_id = long("venue_id").primaryKey()
    val venue_name = varchar("venue_name", 50)
    val country_code = (varchar("country_code", 2) references Countries.country_code).nullable()
}

object Countries : Table("COUNTRY") {
    val country_code = varchar("country_code", 2).primaryKey()
    val country_currencyCode =  varchar("currency_code", 3)
}

object MeAnother : Table("ME_ANOTHER") {
    val another_id = long("another_id").primaryKey()
    val event_id = long("event_id") references Events.event_id
    val another_name = varchar("event_name", 50)
}

fun setup(): Boolean {

    SchemaUtils.create (Countries, Venues, Events, MeAnother)

    var countryCode = Countries.insert {
        it[country_code] = "BE"
        it[country_currencyCode] = "EUR"
    } get Countries.country_code

    Venues.insert {
        it[venue_id] = 1111L
        it[venue_name] = "the 1111 venue"
        it[country_code] = countryCode
    }

    countryCode = Countries.insert {
        it[country_code] = "UK"
        it[country_currencyCode] = "GBP"
    } get Countries.country_code

    Venues.insert {
        it[venue_id] = 2222L
        it[venue_name] = "the 1111 venue"
        it[country_code] = countryCode
    }

    Events.insert {
        it[event_id] = 3333L
        it[event_name] = "event 3333 @ venue 1111"
        it[venue_id] = 1111L
        it[seat_price] = 55.95
        it[seat_price_euro] = 55.95
    }

    val eventList = listOf(1111L to "event 1111", 2222L to "event 2222", 4444L to "4444")
    Events.batchInsert(eventList) { (id, name) ->
        this[Events.event_id] = id
        this[Events.event_name] = name
        this[Events.venue_id] = 1111L
        this[Events.seat_price] = 10.01
        this[Events.seat_price_euro] = 10.01
    }

    MeAnother.insert {
        it[another_id] = 1234L
        it[event_id] = 3333L
        it[another_name] = "Another 1234 for event 333"
    }

    return true
}