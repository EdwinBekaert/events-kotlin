# Learning Kotlin

I am building some sort of entity framework in order to learn the ins and outs of the Kotlin language.
I will implement an event application. 
I appreciate your constructive feedback.

Uses:
- Kotlin
- Gradle
- Exposed with H2 database
- more to come...

#### What I discovered so far:
1. **Singleton** = object. 
    
    add ```@JvmStatic``` for methods.
    
    All objects other then Entity should not hold state and as such they can be singletons.
    An object called APP was created that holds all objects in its private properties.
    
    ```
    object App {
    
        private val eventMngr by lazy { bl.event.Mngr() }
        private val venueMngr by lazy { bl.venue.Mngr() }

        private val eventSrv by lazy { bl.event.Srv(mngr = eventMngr) }
        private val venueSrv by lazy { bl.venue.Srv(mngr = venueMngr) }
        
        private val dbConn by lazy { Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")}
        
        private val eventDao by lazy { bl.event.Dao(dbConn) }
        private val eventAnotherDao by lazy { bl.event.AnotherDao(dbConn) }
        private val venueDao by lazy { bl.venue.Dao(dbConn) }
    
    }
    ```  
    
2. **Inline Reified**
    
    inline reified allows you to do ```D::class.java.canonicalName```: 
    ``` 
    inline fun <reified D: base.Dao> getDao() =
          when (D::class.java.canonicalName) {
            "bl.venue.Dao" -> venueDao as D
            "bl.event.Dao" -> eventDao as D
            "bl.event.AnotherDao" -> eventAnotherDao as D
            else -> throw IllegalArgumentException("object ${D::class.java.canonicalName} is not yet defined in App.")
    } 
    ```
    
    however venueDao & eventDao need to be public properties as the whole method will be inlined in the caller object.
    
3. **Lazy loading** properties is pretty sweet:

    ```private val eventSrv by lazy { bl.event.Srv(mngr = eventMngr) }```
    
4. Useful **extension functions**
   
   getOrPut() & getOrDefault() on a map
   
    ```
    fun getEnt(objName: String, args: Args, qryData: Query?, entIdField: String)
        = entities.getOrPut(key = "$objName.${args.getOrDefault(entIdField, defaultValue = "init")}") {
            createEnt(objName = objName, args = args, qryData = qryData)
        }
    ```
5. Build my own extension functions.
    + **whenNotNull**

        too often I use ```object?.let { do something when object is not null }```
        
        I thought it would be nicer to see ```object.whenNotNull { do something }```
        
        So I created this extension:
        ```
        fun <T:Any, R> T?.whenNotNull(callback: (T)->R): R? = this?.let(callback)
        ```
    + **whenRow**
        
        The query object of exposed was used like this in order to retrieve data:
        ```
        qryData?.forEach{
            venueId = it[Venues.venue_id]
            venueName = it[Venues.venue_name]
            countryCode = it[Venues.country_code]
        }
        ```
        I created an extension function called ```whenRow``` that does exact the same:
        ```
        qryData.whenRow {
            venueId = it[Venues.venue_id]
            venueName = it[Venues.venue_name]
            countryCode = it[Venues.country_code]
        }
        ```
        here's the extension method:
        ```
        fun Query?.whenRow(callback: (ResultRow)->Unit) = this?.firstOrNull()?.let(callback)
        ```
        
        We could use it as argument: ```private fun getEntById(objReq: Req, args: Args): Ent```
        
        ```
        val args = argsOf(
            "eventId" to 3333L,
            "eventCode" to "event_code_2546861"
        )
        
        val seatPrice: Double? = args.getOrDefaultCasted(key = this::seatPrice.name, defaultValue = seatPrice)
        val entId: Long = args.getOrErrorCasted(primaryKey) // we need an id
        ```
        
6. Build my own **type alias** and discover the **spread operator** 

    I pass around the ```args``` argument quite often which is a key/value map. I also repeated the same code on the values like casting, null checking and throwing errors when key is not in the map. This is how I typed this behaviour:
    ```
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
    ```
7. **Companion Object**
    
    Each entity needed a way to communicate its primary key without instantiating the object.
    A companion object gathers static methods and properties accessible without instantiation.
    Companion object is a singleton inside a class and cannot access the class object's properties.
    ```
    companion object { open val primaryKey = "id"}
    ```
    
8. **Blocks** as means of surrounding code

    When a property of an entity exists in the "dirty fields" map, we need to execute some code.
    Instead of copy/pasting that code in each service we use **blocks** to make the service more readable:
    ```
    fun whenDirty(key: String, block: (history: DirtyFieldHistory) -> Unit) = if(dirtyFields.containsKey(key)) block(dirtyFields.get(key)!!) else Unit
    ```
    History argument of the block becomes ```it``` in the body and you can iterated over the history. 
    ```
    whenDirty(objEnt::eventCode.name){
        println("event code is dirty. Current value: ${objEnt.eventCode}")
        it.forEach { println(it) } // print all history
    }
    ```
    also instead of using ```eventId``` as key, I use ```objEnt::eventId.name``` that would make refactoring a breeze.
    
    Same idea for database transactions from the DAO:
    ```
    internal fun <T> dbTrans (block: () -> T): T = transaction(db = dbConn) { block() }
    ```
    
    ```
    return objDao.dbTrans {
        val qryData = objDao.getDetailsByEvent(id = entId)
        mngr.getAnotherEnt(objReq = objReq, qryData = qryData, args = args)
    }
    ```
    
9. **Delegated Properties** 
    
    I want to track the ```dirty fields``` in a map with their historical values.
    So I created a class for property delegation that implements ```ObservableProperty```. 
    The ```dirtyFields``` map from the entity is passed to the constructor of the delegate. 
    
    ```
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
    ```
    
    We can use the delegate in our entity
    ```
    var eventId: Long? by EntDelegate(this.dirtyFields)
    var eventCode: String? by EntDelegate(this.dirtyFields)
    var seatPrice: Double? by EntDelegate(this.dirtyFields)
    ```
    
