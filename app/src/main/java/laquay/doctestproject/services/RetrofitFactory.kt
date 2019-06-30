package laquay.jerome.services

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import laquay.doctestproject.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitFactory{
    private const val FILE_SYSTEM_URL = "http://192.168.43.234:8080/"
    fun makeRetrofitService() : FileService{
        val client = OkHttpClient.Builder()
        client.addInterceptor(LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .build())
        val okHttpClient = client.build()

        return Retrofit.Builder()
            .baseUrl(FILE_SYSTEM_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build().create(FileService::class.java)
    }
}