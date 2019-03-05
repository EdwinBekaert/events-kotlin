package bl.country

import base.Args
import base.Mngr
import bl.App
import bl.Pkgs
import bl.Req
import org.jetbrains.exposed.sql.Query

class Mngr : Mngr() {
    override val pkgName = Pkgs.VENUE

    override fun getSrv() = App.getSrv<Srv>(objPkg = Srv::class.java.canonicalName)
    override fun getDao() = App.getDao<Dao>()
    override fun getEnt(objReq: Req, args: Args, qryData: Query?)
            = getEntCasted<Ent>(objReq = objReq, qryData = qryData, args = args, entIdField = Ent.primaryKey)
}