package ru.gozerov.tfs_spring.domain.mappers

import ru.gozerov.tfs_spring.data.api.models.User
import ru.gozerov.tfs_spring.data.api.models.UserContact

fun ru.gozerov.tfs_spring.data.api.models.User.toUserContact() =
    ru.gozerov.tfs_spring.data.api.models.UserContact(
        user_id,
        avatar_url ?: "",
        full_name,
        delivery_email ?: "",
        is_active
    )