package laquay.jerome.services

import kotlinx.coroutines.Deferred
import laquay.doctestproject.FileInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface FileService{
    @GET("files")
    fun getFiles(@Query("path") path : String): Deferred<Response<List<FileInfo>>>

    @Multipart
    @POST("files")
    fun stockFile(@Query("path") path : String, @Part file : MultipartBody.Part) : Deferred<String>
}