package com.jsdisco.lilhelper.data.remote

import com.jsdisco.lilhelper.data.remote.models.RecipeRemote
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


const val BASE_URL = "https://recipes.jsdisco.dev/"

const val APITOKEN = Token.TOKEN
//const val APITOKEN = ExampleToken.TOKEN

private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
    val newRequest: Request = chain.request().newBuilder().addHeader("Authorization", "Bearer $APITOKEN").build()
    chain.proceed(newRequest)
}.build()

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RecipeApiService {

    @GET("/api/recipes")
    suspend fun getRecipes(): List<RecipeRemote>
}

object RecipeApi {
    val retrofitService: RecipeApiService by lazy {retrofit.create(RecipeApiService::class.java)}
}