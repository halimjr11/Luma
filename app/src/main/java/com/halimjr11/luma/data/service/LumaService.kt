package com.halimjr11.luma.data.service

import com.halimjr11.luma.core.base.BaseResponse
import com.halimjr11.luma.data.model.AuthRequest
import com.halimjr11.luma.data.model.AuthResponse
import com.halimjr11.luma.data.model.StoryResponse
import com.halimjr11.luma.data.model.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface LumaService {
    @POST("register")
    suspend fun register(
        @Body registerRequest: AuthRequest
    ): AuthResponse

    @POST("login")
    suspend fun login(
        @Body loginRequest: AuthRequest
    ): AuthResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): BaseResponse<List<StoryResponse>>

    @GET("stories")
    suspend fun getDetailStories(
        @Path("id") id: String = ""
    ): BaseResponse<StoryResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): UploadStoryResponse
}