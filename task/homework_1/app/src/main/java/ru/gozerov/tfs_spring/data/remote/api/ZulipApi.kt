package ru.gozerov.tfs_spring.data.remote.api

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gozerov.tfs_spring.data.remote.api.models.GetAllStreamsResponse
import ru.gozerov.tfs_spring.data.remote.api.models.GetMessagesResponse
import ru.gozerov.tfs_spring.data.remote.api.models.GetSubscribedStreamsResponse
import ru.gozerov.tfs_spring.data.remote.api.models.GetTopicsResponse
import ru.gozerov.tfs_spring.data.remote.api.models.GetUserByIdResponse
import ru.gozerov.tfs_spring.data.remote.api.models.GetUsersResponse
import ru.gozerov.tfs_spring.data.remote.api.models.SendMessageResponse
import ru.gozerov.tfs_spring.data.remote.api.models.User

interface ZulipApi {

    @GET("users")
    suspend fun getUsers(): GetUsersResponse

    @GET("users/me")
    suspend fun getOwnUser(): User

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): GetUserByIdResponse

    @GET("streams")
    suspend fun getAllStreams(): GetAllStreamsResponse

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): GetSubscribedStreamsResponse

    @GET("users/me/{id}/topics")
    suspend fun getStreamTopics(@Path("id") streamId: Int): GetTopicsResponse

    @GET("messages")
    suspend fun getMessages(
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("anchor") anchor: String,
        @Query("narrow") narrow: String
    ): GetMessagesResponse

    @POST("messages/{id}/reactions")
    suspend fun addReaction(
        @Path("id") messageId: Int,
        @Query("emoji_name") emojiName: String
    )

    @DELETE("messages/{id}/reactions")
    suspend fun removeReaction(
        @Path("id") messageId: Int,
        @Query("emoji_name") emojiName: String
    )

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String,
        @Query("to") to: String,
        @Query("topic") topic: String,
        @Query("content") content: String
    ): SendMessageResponse

}