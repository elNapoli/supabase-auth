package com.baldomero.napoli.supabase.auth.domain.repository

import com.baldomero.napoli.eventplanner.core.data.utils.NetworkResult
import com.baldomero.napoli.eventplanner.core.domain.models.User
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    @NativeCoroutines
    suspend fun createUseWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<NetworkResult<User?>>

    @NativeCoroutines
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<NetworkResult<User?>>

    @NativeCoroutines
    suspend fun checkIsLoggedUserUseCase(): Flow<NetworkResult<User?>>

    @NativeCoroutines
    suspend fun loginWithGoogle(token: String, rawNonce: String): Flow<NetworkResult<User?>>

}