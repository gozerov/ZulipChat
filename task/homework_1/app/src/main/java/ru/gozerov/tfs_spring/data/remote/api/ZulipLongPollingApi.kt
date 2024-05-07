package ru.gozerov.tfs_spring.data.remote.api

import org.json.JSONArray
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.gozerov.tfs_spring.data.remote.api.models.GetEventsResponse
import ru.gozerov.tfs_spring.data.remote.api.models.RegisterEventQueueResponse

interface ZulipLongPollingApi {

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