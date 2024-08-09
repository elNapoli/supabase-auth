package com.baldomero.napoli.supabase.auth.di

import com.baldomero.napoli.supabase.auth.data.repositories.AuthRepositoryImpl
import com.baldomero.napoli.supabase.auth.domain.repository.AuthRepository
import com.baldomero.napoli.supabase.auth.domain.usecases.CheckIsLoggedUserUseCase
import com.baldomero.napoli.supabase.auth.domain.usecases.CreateUseWithEmailAndPasswordUseCase
import com.baldomero.napoli.supabase.auth.domain.usecases.LoginWithGoogleUseCase
import com.baldomero.napoli.supabase.auth.domain.usecases.SignInWithEmailAndPasswordUseCase
import org.koin.core.module.Module
import org.koin.dsl.module



@Suppress("unused")
object AuthModule {

    private fun repositoryModule() = module {
        single<AuthRepository> { AuthRepositoryImpl(get()) }
    }
    private fun useCaseModule() = module {
        single { CreateUseWithEmailAndPasswordUseCase(get()) }
        single { SignInWithEmailAndPasswordUseCase(get()) }
        single { CheckIsLoggedUserUseCase(get()) }
        single { LoginWithGoogleUseCase(get()) }
    }
    fun init(): Module = module {
        includes(platformModule(), repositoryModule(), useCaseModule())
    }
}