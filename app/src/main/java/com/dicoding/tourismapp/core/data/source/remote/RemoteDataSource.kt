package com.dicoding.tourismapp.core.data.source.remote


import com.dicoding.tourismapp.core.data.source.remote.network.ApiResponse
import com.dicoding.tourismapp.core.data.source.remote.network.ApiService
import com.dicoding.tourismapp.core.data.source.remote.response.TourismResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class RemoteDataSource private constructor(private val apiService: ApiService) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }

    fun getAllTourism() : Flow<ApiResponse<List<TourismResponse>>> =
        flow{
            try {
                val response = apiService.getList()
                val dataArray = response.places
                if(dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(dataArray))
                }else{
                    emit(ApiResponse.Empty)
                }
            }catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
//        {
//        val resultData = MutableLiveData<ApiResponse<List<TourismResponse>>>()
//
//        //get data from local json
//        val client = apiService.getList()
//
//        client.enqueue(object : retrofit2.Callback<ListTourismResponse> {
//            override fun onResponse(
//                call: Call<ListTourismResponse>,
//                response: Response<ListTourismResponse>
//            ) {
//                val dataArray = response.body()?.places
//                resultData.value = if(dataArray != null)ApiResponse.Success(dataArray) else ApiResponse.Empty
//            }
//
//            override fun onFailure(call: Call<ListTourismResponse>, t: Throwable) {
//                resultData.value = ApiResponse.Error(t.message.toString())
//                Log.e("Remote Data Source", t.message.toString())
//            }
//
//        })
//        return resultData
//    }
}

