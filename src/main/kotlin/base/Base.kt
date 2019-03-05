package base

import java.time.LocalDateTime

abstract class Base {

    val createdOn: LocalDateTime = LocalDateTime.now()

}
