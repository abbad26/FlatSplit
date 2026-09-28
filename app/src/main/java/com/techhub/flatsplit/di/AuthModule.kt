package com.techhub.flatsplit.di

import com.google.firebase.auth.FirebaseAuth
import com.techhub.flatsplit.data.repository.AuthRepositoryImpl
import com.techhub.flatsplit.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository{
        return AuthRepositoryImpl(firebaseAuth)
    }
}