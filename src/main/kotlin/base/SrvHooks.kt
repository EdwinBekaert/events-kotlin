package base

import bl.Req

// hooks are "protected" so no interface but abstract class
abstract class SrvHooks: Base () {
    protected abstract fun whenEnt(objEnt: Ent, objReq: Req)
    //fun whenSpecs() : Unit
    //fun whenData() : Unit
}