package base

import bl.Req

// These are public methods accessible outside bl (ex: main or api)
interface SrvInterface {

    fun getDetailsAndSpecs(objReq: Req, args: Args): Response
}