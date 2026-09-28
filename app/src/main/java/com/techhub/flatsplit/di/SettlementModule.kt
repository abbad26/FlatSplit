package com.techhub.flatsplit.di

import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.data.repository.SettlementRepositoryImpl
import com.techhub.flatsplit.domain.repository.SettlementRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettlementModule {

    @Provides
    @Singleton
    fun provideSettlementRepository(
        firestore: FirebaseFirestore
    ): SettlementRepository {
        return SettlementRepositoryImpl(firestore)
    }
}