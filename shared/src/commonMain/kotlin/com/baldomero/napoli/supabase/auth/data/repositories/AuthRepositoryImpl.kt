package com.baldomero.napoli.supabase.auth.data.repositories

import com.baldomero.napoli.eventplanner.core.data.utils.NetworkResult
import com.baldomero.napoli.eventplanner.core.domain.models.User
import com.baldomero.napoli.eventplanner.core.utils.toMyError
import com.baldomero.napoli.supabase.auth.domain.repository.AuthRepository
import com.baldomero.napoli.supabase.auth.utils.toInstance
import com.baldomero.napoli.supabase.network.NetworkModule
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AuthRepositoryImpl(private val networkModule: NetworkModule) : AuthRepository {
    override suspend fun createUseWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<NetworkResult<User?>> = flow {
        emit(NetworkResult.Loading(true))
        try {
            networkModule.client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val user = networkModule.client.auth.currentUserOrNull()

            emit(NetworkResult.Success(user.toInstance()))
        } catch (e: Throwable) {
            emit(NetworkResult.Error(error = e.toMyError()))
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<NetworkResult<User?>> = flow {
        emit(NetworkResult.Loading(true))
        try {
            networkModule.client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val user = networkModule.client.auth.currentUserOrNull()

            emit(NetworkResult.Success(user.toInstance()))
        } catch (e: Throwable) {
            emit(NetworkResult.Error(error = e.toMyError()))
        }
    }

    override suspend fun checkIsLoggedUserUseCase(): Flow<NetworkResult<User?>> = flow {
        emit(NetworkResult.Loading(true))
        try {
            networkModule.client.auth.awaitInitialization()
            val user = networkModule.client.auth.currentUserOrNull()
            emit(NetworkResult.Success(user.toInstance()))
        } catch (e: Throwable) {
            emit(NetworkResult.Error(error = e.toMyError()))
        }
    }

    @NativeCoroutines
    override suspend fun loginWithGoogle(
        token: String,
        rawNonce: String
    ): Flow<NetworkResult<User?>> = flow {
        emit(NetworkResult.Loading(true))
        try {
            networkModule.client.auth.signInWith(IDToken) {
                idToken = token
                provider = Google
                nonce = rawNonce
            }
            val user = networkModule.client.auth.currentUserOrNull()

            emit(NetworkResult.Success(user.toInstance()))

        } catch (e: Throwable) {
            emit(NetworkResult.Error(error = e.toMyError()))

        }
    }
}