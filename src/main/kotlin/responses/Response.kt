package responses

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val success: Boolean,
    val data: T,
    val errorCode: ResponseCodes? = null
) {
    companion object {
        fun <T> success(data: T) = Response(true, data)
        fun emptySuccess() = Response(true, "")

//        fun <T> error(data: T) = Response(false, data)
        fun errorInHeader() = Response(false, "Header data does not match input", ResponseCodes.ERROR_IN_HEADER)
        fun errorInBody() = Response(false, "Body data does not match input", ResponseCodes.ERROR_IN_BODY)
        fun errorEntryNotFound() = Response(false, "No entries found", ResponseCodes.ENTRY_NOT_FOUND)
        fun errorCouldNotFinishOperation() = Response(false, "Could not finish operation", ResponseCodes.COULD_NOT_FINISH_OPERATION)
    }
}
