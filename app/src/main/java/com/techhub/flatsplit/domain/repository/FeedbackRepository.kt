package com.techhub.flatsplit.domain.repository

import com.techhub.flatsplit.domain.model.Feedback

interface FeedbackRepository {
    suspend fun submitFeedback(feedback: Feedback)
}