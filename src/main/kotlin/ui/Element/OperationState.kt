package ui.Element

// classe pour gérer les états d'opération
sealed class OperationState<out T> {
    object Loading : OperationState<Nothing>()
    data class Success<T>(val data: T) : OperationState<T>()
    data class Error(val message: String, val exception: Exception? = null) : OperationState<Nothing>()
    object Initial : OperationState<Nothing>()
}