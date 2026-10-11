package com.techhub.flatsplit.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.data.repository.FeedbackRepositoryImpl
import com.techhub.flatsplit.domain.repository.FeedbackRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeedbackModule {

    @Provides
    @Singleton
    fun provideFeedbackRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): FeedbackRepository {
        return FeedbackRepositoryImpl(
            firestore = firestore,
            firebaseAuth = firebaseAuth
        )
    }
}