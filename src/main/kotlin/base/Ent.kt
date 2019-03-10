package base

import org.jetbrains.exposed.sql.Query

abstract class Ent(open val qryData: Query?, open val args: Args) : Base() {

    // TODO how to force children to have this object / primary Key ???
    companion object { const val primaryKey = "id"} // no need to init to see this prop

    protected val dirtyFields = dirtyFieldsOf() // see typeAlias DirtyFields

    // get value from args map and cast to receiver
    fun <V> getArgValue(key: String): V = args.getCasted(key = key)

    // execute code when a field is dirty / not dirty
    fun whenDirty(key: String, block: (history: DirtyFieldHistory) -> Unit) = if(dirtyFields.containsKey(key)) block(dirtyFields[key]!!) else Unit
    fun whenNotDirty(key: String, block: () -> Unit) = if( ! dirtyFields.containsKey(key)) block() else Unit
    // when dirty on entity
    fun whenDirty(block: (DirtyFields) -> Unit) = if(this.isDirty()) block(dirtyFields) else Unit
    fun isDirty() = dirtyFields.count() > 0

}


