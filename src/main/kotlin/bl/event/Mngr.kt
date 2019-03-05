package bl.event

import base.Args
import base.Mngr
import bl.App
import bl.Pkgs
import bl.Req
import org.jetbrains.exposed.sql.Query

class Mngr : Mngr() {

    override val pkgName = Pkgs.EVENT

    // TODO both getDao & getAnotherDao calls are typed; which one is preferential? left vs right ...
    override fun getSrv()  = App.getSrv<Srv>(objPkg = Srv::class.java.canonicalName)
    override fun getDao() = App.getDao<Dao>() // right side typing
    override fun getEnt(objReq: Req, args: Args, qryData: Query?)
            = getEntCasted<Ent>(objReq = objReq, qryData = qryData, args = args, entIdField = Ent.primaryKey)

    fun getAnotherDao(): AnotherDao = App.getDao() // if ever we have another dao in the package
    fun getAnotherEnt(objReq: Req, args: Args, qryData: Query?)
            = getEntCasted<AnotherEnt>(objReq = objReq, qryData = qryData, args = args, entIdField = AnotherEnt.primaryKey)

}