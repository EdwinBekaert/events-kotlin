package base

// type alias the very long type...
// dirtyfields = <property Name, list of changes>
typealias DirtyFieldHistory = MutableList<Any?>
typealias DirtyFields = MutableMap<String, DirtyFieldHistory>

fun dirtyFieldsOf(vararg pairs: Pair<String, DirtyFieldHistory>): DirtyFields
        = mutableMapOf(pairs = *pairs) // * aka spread operator
