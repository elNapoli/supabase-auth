package com.baldomero.napoli.supabase.auth.domain.usecases

import com.baldomero.napoli.eventplanner.core.data.utils.NetworkResult
import com.baldomero.napoli.eventplanner.core.domain.models.User
import com.baldomero.napoli.supabase.auth.domain.repository.AuthRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

class CheckIsLoggedUserUseCase(
    private val repository: AuthRepository,
) {
    @NativeCoroutines
    suspend operator fun invoke(): Flow<NetworkResult<User?>> =
        repository.checkIsLoggedUserUseCase()
}
