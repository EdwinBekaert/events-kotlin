package base

abstract class Ent(val args: Args) : Base() {

    // TODO how to force children to have this object / primary Key ???
    companion object { open val primaryKey = "id"} // no need to init to see this prop

    protected val dirtyFields = dirtyFieldsOf() // see typeAlias DirtyFields

    // get value from args map and cast to receiver
    fun <V> getArgValue(key: String): V = args.getCasted(key = key)

    // execute code when a field is dirty / not dirty
    fun whenDirty(key: String, block: (history: DirtyFieldHistory) -> Unit) = if(dirtyFields.containsKey(key)) block(dirtyFields.get(key)!!) else Unit
    fun whenNotDirty(key: String, block: () -> Unit) = if( ! dirtyFields.containsKey(key)) block() else Unit

    fun isDirty() = dirtyFields.count()

}


