package base

// Execute a callback when variable is not null... avoid checking for nulls (.let trick)
fun <T:Any, R> T?.whenNotNull(callback: (T)->R): R? = this?.let(callback)

// change qryData?.mapNotNull to qryData.whenRow... + fix nulls
fun <T, R: Any> Iterable<T>?.whenRow(callback: (T) -> R): List<R>?
        = this.let { this?.mapNotNullTo(ArrayList(), callback) }




