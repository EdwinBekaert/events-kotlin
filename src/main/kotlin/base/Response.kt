package base

// system responds to API call in agreed format

data class Response (
        private var SUCCESS: Boolean = true,
        private var DATA: Map<String, Any?> = mapOf(),
        private var SPECS: Map<String, Any?> = mapOf(),
        private var FILENAME: String = "",
        private var ERRORS: Map<String, Any?> = mapOf()
) {

    fun addResponseData(data: Map<String, Any?>): Response = apply{ DATA = DATA.plus(data) }  // apply returns itself // why "DATA =" is needed here?
    fun addResponseSpecs(specs: Map<String, Any?>): Response = apply { SPECS = SPECS.plus(specs) }
    fun addResponseFileName(fileName: String): Response = apply { FILENAME = fileName }

    fun clearResponseData(): Response = apply { DATA = mapOf()}
    fun clearResponseSpecs(): Response = apply { SPECS = mapOf() }
    fun clearResponseFileName(): Response = apply { FILENAME = "" }

    fun addResponseError(errorMsg: String = "", details: Map<String, Any?> = emptyMap(), type: String = "fatal"): Response =
       apply {
            SUCCESS = false
            ERRORS = ERRORS.plus(mapOf("errorMsg" to errorMsg)).plus(details)
        }

    fun addValidationError(key: String, type: String, message: String): Response {
        // TODO
        return this
    }

    fun hasErrors(): Boolean = false // TODO
    fun hasFatalErrors(): Boolean = false // TODO
    fun hasValidationErrors(): Boolean = false // TODO

    fun isSuccess(): Boolean = SUCCESS

    fun isDataBinary(): Boolean = false // TODO

}