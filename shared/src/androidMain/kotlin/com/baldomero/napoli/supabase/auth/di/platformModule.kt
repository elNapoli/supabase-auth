package com.baldomero.napoli.supabase.auth.di

import com.baldomero.napoli.supabase.auth.presentation.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual fun platformModule() = module {
    viewModel { AuthViewModel(get(), get(), get(),get(), get()) }
}