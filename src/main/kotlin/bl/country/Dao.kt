package bl.country

import base.DaoCode
import data.Countries
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select

class Dao(dbConn: Database): DaoCode(dbConn = dbConn){

    override fun getDetails(code: String) =
        (Countries).select { Countries.country_code eq code }

}
