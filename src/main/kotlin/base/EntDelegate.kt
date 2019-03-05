package base

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

class EntDelegate<V>(private val dirtyFields: DirtyFields) : ObservableProperty<V?>(initialValue = null) {

    private var initialYetDefined = false // did we set the initial value ? only mark dirty as of second time property is set

    // mark fields dirty when changed (-> triggers validations)
    override fun beforeChange(property: KProperty<*>, oldValue: V?, newValue: V?): Boolean {
        if (initialYetDefined && oldValue != newValue) {
            // map where key is property and value is the list of history of changes
            dirtyFields.getOrPut(property.name) { mutableListOf() }.add(oldValue)
        }
        initialYetDefined = true
        return oldValue != newValue
    }
}