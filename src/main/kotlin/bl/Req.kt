package bl

import base.Args
import base.Ent
import base.Response
import base.whenNotNull
import org.jetbrains.exposed.sql.Query

class Req {
    val entities = mutableMapOf<String, Ent>()
    val response by lazy { Response() }

    inline fun <reified E : Ent> getEnt(args: Args, qryData: Query?, entIdField: String) = entities.getOrPut(key = "${E::class.java.canonicalName}.${args.getOrDefault(entIdField, defaultValue = "init")}") {
        when (E::class.java.canonicalName) { // init with qryData if available else only with args

            bl.event.Ent::class.java.canonicalName
                -> qryData.whenNotNull { bl.event.Ent(qryData = it, args = args) }
                    ?: bl.event.Ent(args = args)

            bl.event.AnotherEnt::class.java.canonicalName
                -> qryData.whenNotNull { bl.event.AnotherEnt(qryData = it, args = args) }
                    ?: bl.event.AnotherEnt(args = args)

            bl.venue.Ent::class.java.canonicalName
                -> qryData.whenNotNull { bl.venue.Ent(qryData = it, args = args) }
                    ?: bl.venue.Ent(args = args)

            bl.country.Ent::class.java.canonicalName
                -> qryData.whenNotNull { bl.country.Ent(qryData = it, args = args) }
                    ?: bl.country.Ent(args = args)

            else -> throw IllegalArgumentException("object ${E::class.java.canonicalName} is not (yet) defined in Req.")
        }
    }
}