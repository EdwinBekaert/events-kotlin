package base

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow

// Execute a callback when variable is not null... avoid checking for nulls (.let trick)
fun <T:Any, R> T?.whenNotNull(callback: (T)->R): R? = this?.let(callback)

// runs callback for first row...
fun Query?.whenRow(callback: (ResultRow)->Unit) = this?.firstOrNull()?.let(callback)

// try get a column of first row or return null send TABLE.COLUM as expression
fun <R> Query?.tryGetFirst(col: Expression<R>) = this?.firstOrNull()?.tryGet(col)

