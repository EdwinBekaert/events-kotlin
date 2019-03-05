package bl.country

import base.Args
import base.EntDelegate
import base.getOrDefaultCasted
import base.whenRow
import data.Countries
import org.jetbrains.exposed.sql.Query

class Ent (qryData: Query? = null, args: Args) : base.Ent(args = args){

    companion object { val primaryKey = Ent::countryCode.name}

    var countryCode: String? by EntDelegate(this.dirtyFields)
    var currencyCode: String? by EntDelegate(this.dirtyFields)

    init {

        qryData.whenRow {
            countryCode = it[Countries.country_code]
            currencyCode = it[Countries.country_currencyCode]
        }

        // set args to override TODO this:: or Ent:: ???
        countryCode   = args.getOrDefaultCasted(key = Ent::countryCode.name,   defaultValue = countryCode)
        currencyCode = args.getOrDefaultCasted(key = this::currencyCode.name, defaultValue = currencyCode)

    }

}