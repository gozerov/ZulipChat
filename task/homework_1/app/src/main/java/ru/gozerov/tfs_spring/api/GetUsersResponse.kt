package ru.gozerov.tfs_spring.api

import ru.gozerov.tfs_spring.api.models.User

data class GetUsersResponse(
    val members: List<User>
)
