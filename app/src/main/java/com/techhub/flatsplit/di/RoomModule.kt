package com.techhub.flatsplit.di

import com.google.firebase.firestore.FirebaseFirestore
import com.techhub.flatsplit.data.repository.FlatRoomRepositoryImpl
import com.techhub.flatsplit.domain.repository.FlatRoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFlatRoomRepository(
        firestore: FirebaseFirestore
    ): FlatRoomRepository {
        return FlatRoomRepositoryImpl(firestore)
    }

}