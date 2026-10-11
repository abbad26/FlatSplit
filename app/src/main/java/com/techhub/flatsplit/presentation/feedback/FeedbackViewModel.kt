package com.techhub.flatsplit.presentation.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techhub.flatsplit.domain.model.Feedback
import com.techhub.flatsplit.domain.usecase.SubmitFeedbackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val submitFeedbackUseCase: SubmitFeedbackUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    fun onCategorySelected(category: String) {
        if (_uiState.value.isSubmitting || _uiState.value.isSubmitted)
            return

        if (category !in FEEDBACK_CATEGORIES) return
        _uiState.update {
            it.copy(
                category = category,
                errorMessage = null
            )
        }
    }

    fun onRatingSelected(rating: Int) {
        if (_uiState.value.isSubmitting ||
            _uiState.value.isSubmitted
        ) return

        if (rating !in 1..5) return

        _uiState.update {
            it.copy(
                rating = rating,
                errorMessage = null
            )
        }
    }

    fun onMessageChanged(message: String) {
        if (_uiState.value.isSubmitting ||
            _uiState.value.isSubmitted
        ) return

        _uiState.update {
            it.copy(
                message = message.take(MAX_MESSAGE_LENGTH),
                errorMessage = null
            )
        }
    }

    fun submitFeedback() {
        val current = _uiState.value

        if (current.isSubmitting || current.isSubmitted) return

        val cleanMessage = current.message.trim()

        if (cleanMessage.length < MIN_MESSAGE_LENGTH) {
            _uiState.update {
                it.copy(
                    errorMessage =
                        "Please enter at least $MIN_MESSAGE_LENGTH characters."
                )
            }
            return
        }

        if (current.category !in FEEDBACK_CATEGORIES) {
            _uiState.update {
                it.copy(errorMessage = "Please select a feedback category.")
            }
            return
        }

        // Update loading state immediately to prevent duplicate taps.
        _uiState.update {
            it.copy(
                isSubmitting = true,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            try {
                submitFeedbackUseCase(
                    Feedback(
                        category = current.category,
                        message = cleanMessage,
                        rating = current.rating.takeIf { it in 1..5 }
                    )
                )

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        isSubmitted = true,
                        errorMessage = null
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (exception: Exception) {
                val error = if (exception is IllegalStateException) {
                    exception.message
                        ?: SUBMISSION_ERROR
                } else {
                    SUBMISSION_ERROR
                }

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error
                    )
                }
            }
        }
    }

    fun sendAnotherFeedback() {
        if (!_uiState.value.isSubmitting) {
            _uiState.value = FeedbackUiState()
        }
    }

    private companion object {
        const val MIN_MESSAGE_LENGTH = 5
        const val MAX_MESSAGE_LENGTH = 1000

        const val SUBMISSION_ERROR =
            "We couldn't submit your feedback. Check your connection and try again."

        val FEEDBACK_CATEGORIES = setOf(
            "Bug report",
            "Feature request",
            "General feedback",
            "Other"
        )
    }
}


data class FeedbackUiState(
    val category: String = "General feedback",
    val rating: Int = 0,
    val message: String = "",
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    val errorMessage: String? = null
)