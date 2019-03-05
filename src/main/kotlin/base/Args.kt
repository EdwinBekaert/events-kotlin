package base

// args = key/values from front end
// make an alias as "args: Args" is more readable then "args: Map<String, Any?>" + we can change easier the type
typealias Args = Map<String, Any?>

fun argsOf(vararg pairs: Pair<String, Any?>): Args = mapOf(pairs = *pairs) // * aka spread operator

// make getValue auto-cast to receiver variable
fun <T> Args.getCasted(key: String): T = this.get(key = key) as T

// getValue casted, but if not present, return defaultValue
fun <T> Args.getOrDefaultCasted(key: String, defaultValue: Any?): T
        = this.getOrDefault(key = key, defaultValue = defaultValue) as T

fun <T> Args.getOrErrorCasted(key: String): T
        = this.getOrElse(key = key){
            throw IllegalArgumentException("$key not present in args")
        } as T
