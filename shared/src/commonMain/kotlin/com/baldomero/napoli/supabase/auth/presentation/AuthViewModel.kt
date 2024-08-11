package com.baldomero.napoli.supabase.auth.presentation

import com.baldomero.napoli.eventplanner.core.presentation.models.FeedbackUI
import com.baldomero.napoli.eventplanner.core.presentation.models.FeedbackUIType
import com.baldomero.napoli.eventplanner.core.presentation.viewModel.BaseViewModel
import com.baldomero.napoli.eventplanner.core.utils.useCaseRunner
import com.baldomero.napoli.supabase.auth.domain.usecases.CheckIsLoggedUserUseCase
import com.baldomero.napoli.supabase.auth.domain.usecases.CreateUseWithEmailAndPasswordUseCase
import com.baldomero.napoli.supabase.auth.domain.usecases.LoginWithGoogleUseCase
import com.baldomero.napoli.supabase.auth.domain.usecases.SignInWithEmailAndPasswordUseCase
import com.baldomero.napoli.supabase.auth.presentation.AuthContract.Effect
import com.baldomero.napoli.supabase.auth.presentation.AuthContract.UiIntent
import com.baldomero.napoli.supabase.auth.presentation.AuthContract.UiState
import com.baldomero.napoli.supabase.auth.utils.RouteProvider

open class AuthViewModel(
    private val createUseWithEmailAndPasswordUseCase: CreateUseWithEmailAndPasswordUseCase,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val routeProvider: RouteProvider,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val checkIsLoggedUserUseCase: CheckIsLoggedUserUseCase,
) : BaseViewModel<UiState, UiIntent, Effect>(
    UiState(initialRoute = routeProvider.getRouteForLoggedOutUser())
) {

    override fun sendIntent(uiIntent: UiIntent) {
        when (uiIntent) {
            UiIntent.ToggleVisualTransformation -> updateUiState { togglePasswordVisible() }
            is UiIntent.SaveEmail -> {
                updateUiState {
                    saveEmail(uiIntent.email)
                }
            }

            is UiIntent.SavePassword -> {
                updateUiState {
                    savePassword(uiIntent.password)
                }
            }

            is UiIntent.SaveRepeatPassword -> updateUiState { saveRepeatPassword(uiIntent.repeatPassword) }

            UiIntent.CreateUseWithEmailAndPassword -> createUseWithEmailAndPassword()
            UiIntent.SignInWithEmailAndPassword -> signInWithEmailAndPassword()
            UiIntent.ResetFeedbackUI -> updateUiState { copy(feedbackUI = null) }
            UiIntent.GetInitialRoute -> checkIsLoggedUser()
            is UiIntent.LoginWithGoogle -> loginWithGoogle(uiIntent.token)
        }
    }

    private fun loginWithGoogle(token: String) = scope.useCaseRunner(
        loadingUpdater = { value -> updateUiState { loading(value) } },
        onError = {
            updateUiState {
                handleCreateUseWithEmailAndPassword(
                    user = null, feedbackUI = FeedbackUI(
                        title = "Error",
                        message = it.message,
                        type = FeedbackUIType.ERROR,
                        show = true
                    )
                )
            }
        },
        onSuccess = { data ->
            updateUiState {
                handleCreateUseWithEmailAndPassword(
                    user = data?.mapToUI(), feedbackUI = FeedbackUI(
                        title = "Cuenta creada",
                        message = "Tu cuenta ha sido creada exitosamente, el usuario es ${data?.email}",
                        type = FeedbackUIType.SUCCESS,
                        show = true
                    )
                )
            }
            sendEffect(Effect.GoToHome)
        },
        useCase = {
            loginWithGoogleUseCase(token = token, rawNonce = uiState.value.nonce)
        }
    )

    private fun createUseWithEmailAndPassword() = scope.useCaseRunner(
        loadingUpdater = { value -> updateUiState { loading(value) } },
        onError = {
            updateUiState {
                handleCreateUseWithEmailAndPassword(
                    user = null, feedbackUI = FeedbackUI(
                        title = "Error",
                        message = it.message,
                        type = FeedbackUIType.ERROR,
                        show = true
                    )
                )
            }
        },
        onSuccess = { data ->
            updateUiState {
                handleCreateUseWithEmailAndPassword(
                    user = data?.mapToUI(), feedbackUI = FeedbackUI(
                        title = "Cuenta creada",
                        message = "Tu cuenta ha sido creada exitosamente, el usuario es ${data}",
                        type = FeedbackUIType.SUCCESS,
                        show = true
                    )
                )
            }
        },
        useCase = {
            createUseWithEmailAndPasswordUseCase(
                CreateUseWithEmailAndPasswordUseCase.Params(
                    email = uiState.value.email,
                    password = uiState.value.password
                )
            )
        }
    )

    private fun signInWithEmailAndPassword() = scope.useCaseRunner(
        loadingUpdater = { value ->
            updateUiState { loading(value) }
        },
        onError = {
            updateUiState {
                handleCreateUseWithEmailAndPassword(
                    user = null, feedbackUI = FeedbackUI(
                        title = "Error",
                        message = it.message,
                        type = FeedbackUIType.ERROR,
                        show = true
                    )
                )
            }

        },
        onSuccess = { data ->
            sendEffect(Effect.GoToHome)
        },
        useCase = {
            signInWithEmailAndPasswordUseCase(
                SignInWithEmailAndPasswordUseCase.Params(
                    email = uiState.value.email,
                    password = uiState.value.password
                )
            )
        }
    )



    private fun checkIsLoggedUser() = scope.useCaseRunner(
        loadingUpdater = { value ->
            updateUiState { copy(loading = value) }
        },
        onError = {
            updateUiState {
                copy(
                    user = null, feedbackUI = FeedbackUI(
                        title = "Error",
                        message = it.message,
                        type = FeedbackUIType.ERROR,
                        show = true
                    )
                )
            }

        },
        onSuccess = { data ->
            if(data != null){
               updateUiState { copy(initialRoute =  routeProvider.getRouteForLoggedInUser()) }
            }
            else{
                updateUiState { copy(initialRoute =  routeProvider.getRouteForLoggedOutUser()) }

            }
        },
        useCase = {
            checkIsLoggedUserUseCase()
        }
    )
}