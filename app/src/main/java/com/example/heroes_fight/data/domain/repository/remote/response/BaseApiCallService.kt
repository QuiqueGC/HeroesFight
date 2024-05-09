package com.example.heroes_fight.data.domain.repository.remote.response

import com.example.heroes_fight.data.domain.model.error.ErrorModel
import retrofit2.Response

abstract class BaseApiCallService {
    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): BaseResponse<T> {
        val response: Response<T>

        return try {
            response = call.invoke()
            if (response.isSuccessful) {

                response.body()?.let { body ->
                    BaseResponse.Success(body)
                } ?: BaseResponse.Error(ErrorModel())

            } else {

                BaseResponse.Error(ErrorModel())
            }

        } catch (throwable: Throwable) {

            BaseResponse.Error(ErrorModel())
        }
    }
}