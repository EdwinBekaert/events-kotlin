package bl.country

import base.Dao
import data.Countries
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.select

class Dao(dbConn: Database): Dao(dbConn = dbConn){

    override fun getDetails(id: Long): Query {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDetails(code: String) =
        (Countries).select { Countries.country_code eq code }

}
