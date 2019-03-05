import bl.App

object EventDemo {

    fun getEventsSrv(): bl.event.Srv = App.getSrv(bl.event.Srv::class.java.canonicalName)

}
