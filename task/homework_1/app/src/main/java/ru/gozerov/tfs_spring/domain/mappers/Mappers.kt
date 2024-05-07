package ru.gozerov.tfs_spring.domain.mappers

import ru.gozerov.tfs_spring.data.remote.api.models.User
import ru.gozerov.tfs_spring.data.remote.api.models.UserContact

fun User.toUserContact() = UserContact(
    user_id,
    avatar_url ?: "",
    full_name,
    delivery_email ?: "",
    is_active
)