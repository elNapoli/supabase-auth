package com.baldomero.napoli.supabase.auth.domain.usecases

import com.baldomero.napoli.eventplanner.core.data.utils.NetworkResult
import com.baldomero.napoli.supabase.auth.domain.models.User
import com.baldomero.napoli.supabase.auth.domain.repository.AuthRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

class SignInWithEmailAndPasswordUseCase(
    private val repository: AuthRepository,
) {
    @NativeCoroutines
    suspend operator fun invoke(params: Params): Flow<NetworkResult<User?>> =
        repository.signInWithEmailAndPassword(params.email, params.password)

    data class Params(val email: String, val password: String)
}
