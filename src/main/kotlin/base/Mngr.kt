package base

import bl.App
import bl.Pkgs
import bl.Req
import org.jetbrains.exposed.sql.Query

abstract class Mngr : Base(), MngrInterface {

    // cast in pkg-level mngr
    // pkg and name derived from typed
    internal inline fun <reified E: Ent> getEntCasted(objReq: Req, args: Args, entIdField: String, qryData: Query? = null)
            = objReq.getEnt(objName = E::class.java.canonicalName, args = args, entIdField = entIdField, qryData = qryData) as E

    // go get a manager from another package
    fun getMngr(pkgName: Pkgs) = App.getMngr(pkgName = pkgName)

}
