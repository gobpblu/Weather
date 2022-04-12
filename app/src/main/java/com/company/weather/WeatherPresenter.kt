package com.company.weather

import android.util.Log
import com.company.api.WeatherApi
import com.company.model.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherPresenter(private val api: WeatherApi) {
    private var weatherContract: WeatherContract? = null

    fun attach(wc: WeatherContract) {
        weatherContract = wc
    }

    fun detach() {
        weatherContract = null
    }

    fun getDataFromApi(cityName: String) {
        val key = "efb7de6a666fd428f916effb6c64f231"
        api.getWeatherData(cityName, key, "metric", "ru")
            .enqueue(object : Callback<WeatherData> {

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    Log.i("OnResponse", weatherContract.toString())
                    weatherContract?.dataFailure(t)
                }

                override fun onResponse(
                    call: Call<WeatherData>,
                    response: Response<WeatherData>
                ) {
                    Log.i("OnResponse", "OnResponse34Line")
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!
                        weatherContract?.showWeatherData(data)
                    }
                }
            })
    }


}