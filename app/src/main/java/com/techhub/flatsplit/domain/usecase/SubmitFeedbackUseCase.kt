package com.techhub.flatsplit.domain.usecase

import com.techhub.flatsplit.domain.model.Feedback
import com.techhub.flatsplit.domain.repository.FeedbackRepository
import javax.inject.Inject

class SubmitFeedbackUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {
    suspend operator fun invoke(feedback: Feedback) {
        feedbackRepository.submitFeedback(feedback)
    }
}