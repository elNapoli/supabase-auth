package com.baldomero.napoli.supabase.auth.presentation

import com.baldomero.napoli.common.randomUUID
import com.baldomero.napoli.eventplanner.core.presentation.effect.BaseUiIntent
import com.baldomero.napoli.eventplanner.core.presentation.intent.BaseEffect
import com.baldomero.napoli.eventplanner.core.presentation.models.FeedbackUI
import com.baldomero.napoli.eventplanner.core.presentation.models.UserUI
import com.baldomero.napoli.eventplanner.core.presentation.state.BaseUiState
import io.ktor.utils.io.core.toByteArray
import okio.ByteString

interface AuthContract {
    data class UiState(
        var passwordVisible: Boolean,
        var email: String,
        var user: UserUI?,
        var password: String,
        var repeatPassword: String,
        var loading: Boolean,
        val nonce: String,
        var feedbackUI: FeedbackUI?
    ) : BaseUiState() {
        // NOTE: de debe instanciar el constructor de esta forma para KMM
        constructor() : this(
            passwordVisible = false,
            email = "",
            user = null,
            password = "",
            nonce = randomUUID,
            repeatPassword = "",
            loading = false,
            feedbackUI = null
        )

        fun loading(value: Boolean): UiState = copy(loading = value)
        fun saveEmail(email: String): UiState = copy(email = email)
        fun savePassword(password: String): UiState = copy(password = password)
        fun saveRepeatPassword(repeatPassword: String): UiState =
            copy(repeatPassword = repeatPassword)

        fun togglePasswordVisible(): UiState = copy(passwordVisible = !passwordVisible)

        fun handleCreateUseWithEmailAndPassword(
            user: UserUI?,
            feedbackUI: FeedbackUI?
        ): UiState =
            copy(user = user, feedbackUI = feedbackUI)

        fun nonceHash(): String {
            val bytes = nonce.toByteArray()
            val hashedNonce = ByteString.of(*bytes).sha256().hex()
            return hashedNonce
        }

    }

    sealed interface UiIntent : BaseUiIntent {
        data object ToggleVisualTransformation : UiIntent
        data object ResetFeedbackUI : UiIntent
        data class SaveEmail(val email: String) : UiIntent
        data class SavePassword(val password: String) : UiIntent
        data class SaveRepeatPassword(val repeatPassword: String) : UiIntent
        data object CreateUseWithEmailAndPassword : UiIntent
        data object SignInWithEmailAndPassword : UiIntent
        data class LoginWithGoogle(val token: String) : UiIntent

    }

    sealed interface Effect : BaseEffect {
        data object GoToHome : Effect
        data object None : Effect
    }
}

