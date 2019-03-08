package bl

import base.Dao
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

// singletons
object App {

    private val dbConn by lazy { Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")}

    init {
        transaction(db = dbConn) {
            data.setup() // create database in memory
        }
    }

    // TODO 3 ways of factoring objects... which one?

    private val eventSrv by lazy { bl.event.Srv(mngr = eventMngr) }
    private val venueSrv by lazy { bl.venue.Srv(mngr = venueMngr) }
    private val countrySrv by lazy { bl.country.Srv(mngr = countryMngr) }

    @Suppress("UNCHECKED_CAST") // TODO suppress is maybe not the best way...
    @JvmStatic
    fun <S: base.Srv> getSrv(objPkg: String = "Base") =
            when (objPkg) {
                "bl.event.Srv" -> eventSrv
                "bl.venue.Srv" -> venueSrv
                "bl.country.Srv" -> countrySrv
                else -> throw IllegalArgumentException("object $objPkg is not yet defined in App.")
            } as S // TODO unchecked cast... what to do with them

    private val eventMngr by lazy { bl.event.Mngr() }
    private val venueMngr by lazy { bl.venue.Mngr() }
    private val countryMngr by lazy { bl.country.Mngr() }

    // TODO what if I have more then one object of the same type inside a package (like anotherDao)??
    // TODO How to fix with enum ???
    @JvmStatic
    fun getMngr(pkgName: Pkgs) =
        when (pkgName) {
            Pkgs.EVENT -> eventMngr
            Pkgs.VENUE -> venueMngr
            Pkgs.COUNTRY -> countryMngr
        }

    // TODO cannot be private here... because of inline fun not having access to private vals ...
    val eventDao by lazy { bl.event.Dao(dbConn) }
    val eventAnotherDao by lazy { bl.event.AnotherDao(dbConn) }
    val venueDao by lazy { bl.venue.Dao(dbConn) }
    val countryDao by lazy { bl.country.Dao(dbConn) }

    // TODO what are the caveats with inline/reified ???
    // TODO they exist in the class that calls them... So we probably loose access to vars from this class...
    // TODO well... dao objects cannot be private... no point in factoring then...

    @JvmStatic
    inline fun <reified D : Dao> getDao() =
            when (D::class) {
                bl.venue.Dao::class -> venueDao as D
                bl.event.Dao::class -> eventDao as D
                bl.event.AnotherDao::class -> eventAnotherDao as D
                bl.country.Dao::class -> countryDao as D
                else -> throw IllegalArgumentException("object ${D::class.java.canonicalName} is not yet defined in App.")
            }

}

