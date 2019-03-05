
import base.argsOf
import bl.Req
import java.time.LocalDateTime

fun main() {
    println(LocalDateTime.now())
    val args = argsOf(
            "eventId" to 3333L,
            "venueId" to 2222L, // change venue
            "eventCode" to "event_code_2546861" // we wanna change eventCode to event_code_2546861
    )

    val request = Req()
    val resp = EventDemo.getEventsSrv().getDetailsAndSpecs(objReq = request, args = args)
    println("start")
    println(resp)
    println("----------------------------")

    val resp2 = EventDemo.getEventsSrv().getDetailsAndSpecs(objReq = request, args = args)
    println(resp2)
    println("----------------------------")


    request.entities.forEach {
        val (key, objEnt) = it.toPair()
        println("$key = $objEnt")
    }

    println("----------------------------")
    println(resp)
    println("----------------------------")

}

