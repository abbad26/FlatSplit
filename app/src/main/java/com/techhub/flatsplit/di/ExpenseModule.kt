package com.techhub.flatsplit.di

import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.data.repository.ExpenseRepositoryImpl
import com.techhub.flatsplit.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpenseModule {

    @Provides
    @Singleton
    fun provideExpenseRepository(
        firestore: FirebaseFirestore
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(firestore)
    }
}