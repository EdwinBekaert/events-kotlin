package bl.event

import base.Args
import base.EntDelegate
import base.getOrDefaultCasted
import base.whenRow
import data.MeAnother
import org.jetbrains.exposed.sql.Query

class AnotherEnt (override val qryData: Query? = null, args: Args): base.Ent(qryData = qryData, args = args) {

    companion object { val primaryKey = AnotherEnt::anotherId.name}

    var anotherId: Long? by EntDelegate(dirtyFields = this.dirtyFields)
    var anotherCode: String? by EntDelegate(dirtyFields = this.dirtyFields)

    init {
        qryData.whenRow {
            anotherId = it[MeAnother.another_id]
            anotherCode = it[MeAnother.another_name]
        }
        // set value from args to override db value
        anotherId = args.getOrDefaultCasted(key = this::anotherId.name, defaultValue = anotherId) // send this::eventId.name so when prop name is refactored, it will still work
        anotherCode = args.getOrDefaultCasted(key = this::anotherCode.name, defaultValue = anotherCode)
    }

}