package com.example.movies_mvvm.data.remote_db

import retrofit2.Response
import utils.Resource

abstract class BaseDataSource {

    protected suspend fun <T> getResults(call: suspend () -> Response<T>): Resource<T> {
        try {
            val result = call()
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) return Resource.success(body)
            }
            return Resource.error("error")
        } catch (e: Exception) {
            return Resource.error("Exception")
        }
    }
}