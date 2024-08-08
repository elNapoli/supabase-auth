package com.baldomero.napoli.supabase.auth.data.models

import com.baldomero.napoli.supabase.auth.domain.models.User
import com.baldomeronapoli.eventplanner.mappers.Mappable
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDTO(
    @SerialName("id")
    val id: String,

    @SerialName("email")
    val email: String,

    @SerialName("name")
    val name: String?,

    @SerialName("picture")
    val picture: String?,

    ) : Mappable<User> {
    override fun toInstance(): User = User(
        id = id,
        email = email,
        name = name,
        picture = picture,
    )
}


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

