package base

import bl.Pkgs
import bl.Req
import org.jetbrains.exposed.sql.Query

interface MngrInterface {

    val pkgName: Pkgs

    fun getSrv(): Srv // cast in pkg-level mngr
    fun getDao(): Dao // cast in pkg-level mngr
    fun getEnt(objReq: Req, args: Args, qryData: Query? = null): Ent // cast in pkg-level mngr

}