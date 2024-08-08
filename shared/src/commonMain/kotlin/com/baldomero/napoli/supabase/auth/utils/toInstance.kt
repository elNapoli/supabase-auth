package com.baldomero.napoli.supabase.auth.utils

import com.baldomero.napoli.eventplanner.core.domain.models.User
import io.github.jan.supabase.gotrue.user.UserInfo

fun UserInfo?.toInstance(): User? {
    return this?.let {
        User(
            id = id,
            email = email ?: "",
            name = "",
            picture = "",
        )
    }
}

