package ru.gozerov.tfs_spring.use_cases

import ru.gozerov.tfs_spring.api.models.User
import ru.gozerov.tfs_spring.screens.contacts.list.models.UserContact

fun User.toUserContact() = UserContact(user_id, avatar_url ?: "", full_name, delivery_email ?: "", is_active)