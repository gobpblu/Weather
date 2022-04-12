package com.company.weather

import com.company.model.WeatherData

interface WeatherContract {
    fun showWeatherData(data: WeatherData)
    fun dataFailure(t: Throwable)
}