package com.techhub.flatsplit.di

import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.data.repository.UserRepositoryImpl
import com.techhub.flatsplit.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firestore)
    }
}