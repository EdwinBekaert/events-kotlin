package base

import bl.Req
import org.jetbrains.exposed.sql.Query

abstract class Srv(private val mngr: Mngr) : SrvInterface, SrvHooks() {

    protected abstract val primaryKey: String // primary key of the basic ent.

    // public methods return response object ... called by the API
    override fun getDetailsAndSpecs(objReq: Req, args: Args): Response {
       //try {
            val objEnt = getEntById(objReq = objReq, args = args)
            getEntAndSpecs(objEnt = objEnt, objReq = objReq)
            objReq.response
                .addResponseData(mapOf(objEnt::class.java.canonicalName to objEnt))
                .addResponseData(mapOf("edwin" to 50*2))
       /*} catch (e: Exception) {
            objReq.response.addResponseError(errorMsg = e.message?:"Error in ${this::getDetailsAndSpecs.name}"
                                        , details = mapOf("stack" to e.stackTrace)
                                        , type = "fatal")
        } finally {*/
            return objReq.response
       //}
    }

    // internal methods
    fun <E: Ent> getEntByData(objReq: Req, args: Args, qryData: Query? = null): E {
        val objEnt = mngr.getEnt(objReq = objReq, args = args, qryData = qryData)
        whenEnt(objEnt = objEnt, objReq = objReq) // make the hook when entity is filled... for package specific handling
        return objEnt as E
    }

    fun getEntById(objReq: Req, args: Args): Ent {
        val entId: Long = args.getOrErrorCasted(primaryKey)
        val objDao = mngr.getDao() as DaoId // cast to Dao that uses ID
        return objDao.dbTrans {
            val qryData = objDao.getDetails(id = entId)
            getEntByData(objReq = objReq, args = args, qryData = qryData)
        }
    }

    fun <E: Ent> getEntByCode(objReq: Req, args: Args): E {
        val entCode: String = args.getOrErrorCasted(primaryKey)
        val objDao = mngr.getDao() as DaoCode
        return objDao.dbTrans {
            val qryData = objDao.getDetails(code = entCode)
            getEntByData(objReq = objReq, args = args, qryData = qryData) as E
        }
    }

    // private methods
    private fun getEntAndSpecs(objEnt: Ent, objReq: Req): Ent {
        return objEnt
    }

/*

	private ent function getEntAndSpecs() {
		var startTick=getTickCount();

		// step 2 :: get & apply the specs
		var mySpecs = getAndSetSpecs(objEnt=objEnt,params=arguments);

		// some defaults and spec reloading
		var dataChanged = false;
		if (objEnt.getEntMode() eq 'CREATE'){

			dataChanged = objEnt.setSpecDefaults(defaultData=mySpecs.defaults); // set defaults based on specs
			if (dataChanged) { // need to reload specs?
				getAndSetSpecs(objEnt=objEnt,params=arguments);
				dataChanged = false;
			}
		}

		// non-active defaults
		objEnt.setNonActiveDefaults();

		// when specs + reload
		dataChanged = whenSpecs(objEnt=objEnt, params=arguments, isLocalLoad=isLocalLoad(arguments));

		if (dataChanged) { // need to reload specs?
			getAndSetSpecs(objEnt=objEnt,params=arguments);
			objEnt.setNonActiveDefaults(); // do it again...
			dataChanged = false;
		}
		return objEnt;
	} // getEntAndSpecs

 */

}
