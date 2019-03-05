package bl

import base.Args
import base.Ent
import base.Response
import base.whenNotNull
import org.jetbrains.exposed.sql.Query

class Req {
    val entities = mutableMapOf<String, Ent>()
    val response by lazy { Response() }

    fun getEnt(objName: String, args: Args, qryData: Query?, entIdField: String)
            = entities.getOrPut(key = "$objName.${args.getOrDefault(entIdField, defaultValue = "init")}") {
                createEnt(objName = objName, args = args, qryData = qryData)
            }

    // TODO Streamline object factoring as in App
    private fun createEnt(objName: String, args: Args, qryData: Query?) =
        when (objName) {
            // init with qryData if available
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
            else -> throw IllegalArgumentException("object $objName is not (yet) defined in Req.")
        }

}