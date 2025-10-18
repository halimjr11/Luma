package com.halimjr11.luma.data.repository.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.data.local.SharedPreferenceHelper
import com.halimjr11.luma.data.mapper.LumaDataMapper
import com.halimjr11.luma.data.model.AuthRequest
import com.halimjr11.luma.data.model.StoryResponse
import com.halimjr11.luma.data.repository.LumaRemoteRepository
import com.halimjr11.luma.data.service.LumaService
import com.halimjr11.luma.data.utils.safeApiCall
import com.halimjr11.luma.domain.model.AuthDomain
import com.halimjr11.luma.domain.model.StoryDomain
import com.halimjr11.luma.domain.model.UploadStoryDomain
import com.halimjr11.luma.utils.DomainResult
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class LumaRemoteRepositoryImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcherProvider,
    private val mapper: LumaDataMapper,
    private val lumaService: LumaService,
    private val sharedPreferenceHelper: SharedPreferenceHelper
) : LumaRemoteRepository {

    override suspend fun login(
        email: String,
        password: String
    ): DomainResult<AuthDomain> = safeApiCall {
        val result = lumaService.login(AuthRequest(email = email, password = password))
        result.loginResult?.let { login ->
            sharedPreferenceHelper.saveLoginResult(
                name = login.name.orEmpty(),
                token = login.token.orEmpty(),
                userId = login.userId.orEmpty()
            )
        }
        mapper.mapAuth(result)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): DomainResult<AuthDomain> = safeApiCall {
        val result = lumaService.register(AuthRequest(name, email, password))
        mapper.mapAuth(result)
    }

    override suspend fun getStories(size: Int): DomainResult<List<StoryDomain>> = safeApiCall {
        val result = lumaService.getStories(size = size)
        result.data?.mapNotNull {
            it?.let { response -> mapper.mapStory(response) }
        }.orEmpty()
    }

    override suspend fun uploadStory(
        fileName: String,
        description: String,
        fileBody: RequestBody,
        lat: Double?,
        lon: Double?
    ): DomainResult<UploadStoryDomain> = safeApiCall {
        val descBody = description.toRequestBody("text/plain".toMediaType())
        val filePart = MultipartBody.Part.createFormData("photo", fileName, fileBody)
        val latBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val lonBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())

        val result = lumaService.uploadStory(
            description = descBody,
            file = filePart,
            lat = latBody,
            lon = lonBody
        )
        mapper.mapUploadStory(result)
    }

    override suspend fun detailStory(id: String): DomainResult<StoryDomain> = safeApiCall {
        val result = lumaService.getDetailStories(id)
        mapper.mapStory(result.data ?: StoryResponse())
    }

    override suspend fun compressAndSave(uri: Uri): File = withContext(dispatcher.io) {
        val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
        val file = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")

        var quality = 90
        var compressedData: ByteArray

        do {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            compressedData = stream.toByteArray()
            quality -= 5
        } while (compressedData.size > 1_000_000 && quality > 10)

        FileOutputStream(file).use { fos ->
            fos.write(compressedData)
        }
        return@withContext file
    }
}