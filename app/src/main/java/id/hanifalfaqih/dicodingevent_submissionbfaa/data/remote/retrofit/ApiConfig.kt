package id.hanifalfaqih.dicodingevent_submissionbfaa.data.remote.retrofit

import id.hanifalfaqih.dicodingevent_submissionbfaa.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val BASE_URL = "https://event-api.dicoding.dev/"

    fun getApiService(): ApiService {
        val loggingLevel = when (BuildConfig.LOGGING_LEVEL) {
            "BODY" -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }

        val loggingInterceptor = HttpLoggingInterceptor().setLevel(loggingLevel)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
