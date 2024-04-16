package ru.gozerov.tfs_spring.api

import org.json.JSONArray
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gozerov.tfs_spring.api.models.GetAllStreamsResponse
import ru.gozerov.tfs_spring.api.models.GetEventsResponse
import ru.gozerov.tfs_spring.api.models.GetMessagesResponse
import ru.gozerov.tfs_spring.api.models.GetSubscribedStreamsResponse
import ru.gozerov.tfs_spring.api.models.GetTopicsResponse
import ru.gozerov.tfs_spring.api.models.GetUserByIdResponse
import ru.gozerov.tfs_spring.api.models.RegisterEventQueueResponse
import ru.gozerov.tfs_spring.api.models.SendMessageResponse
import ru.gozerov.tfs_spring.api.models.User

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
        @Query("emoji_name") emojiName: String,
        @Query("reaction_type") reactionType: String
    )

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String,
        @Query("to") to: String,
        @Query("topic") topic: String,
        @Query("content") content: String
    ): SendMessageResponse

    @POST("register")
    suspend fun registerEventQueue(
        @Query("event_types") eventTypes: String = JSONArray(listOf("message")).toString(),
        @Query("narrow") narrow: String
    ): RegisterEventQueueResponse

    @GET("events")
    suspend fun getEvents(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Int
    ): GetEventsResponse

    @DELETE("events")
    suspend fun deleteEventQueue(
        @Query("queue_id") queueId: String
    )

}