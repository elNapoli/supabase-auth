package com.baldomero.napoli.supabase.auth.di

import com.baldomero.napoli.supabase.auth.presentation.AuthViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.dsl.module


actual fun platformModule() = module {
    factory { AuthViewModel(get(), get(), get(), get()) }
}

@Suppress("unused")
object ViewModels : KoinComponent {
    fun authViewModel() = get<AuthViewModel>()
}