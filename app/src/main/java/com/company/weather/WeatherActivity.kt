package com.company.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.company.api.RetrofitClient
import com.company.api.WeatherApi
import com.company.model.WeatherData
import com.company.weather.databinding.ActivityWeatherBinding
import kotlin.math.roundToInt

class WeatherActivity : AppCompatActivity(), WeatherContract {

    private val api: WeatherApi = RetrofitClient.getRetrofit().create(WeatherApi::class.java)

    private val weatherPresenter = WeatherPresenter(api)

    private val binding: ActivityWeatherBinding by lazy {
        ActivityWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        weatherPresenter.attach(this)

        with(binding) {
            etCity.doAfterTextChanged {
                if (etCity.length() > 0) etCity.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.clear,
                    0
                )
                else etCity.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)

                weatherPresenter.getDataFromApi(etCity.text.toString().trim())
                it?.filters = arrayOf(InputFilter.LengthFilter(20))
            }
        }

    }

    override fun showWeatherData(data: WeatherData) = with(binding) {
        val strBuilder = StringBuilder().append(data.name)
        tvCityName.text = strBuilder
        tvCityName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.city, 0, 0, 0)

        strBuilder.clear().append(data.main.temp.roundToInt())
            .append('\u00B0').append("C")
        tvTemp.text = strBuilder
        tvTemp.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.thermostat,
            0,
            0,
            0
        )

        strBuilder.clear().append(data.weather[0].description)
        strBuilder[0] = strBuilder[0].uppercaseChar()
        tvDescription.text = strBuilder
        tvDescription.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.weather_description,
            0,
            0,
            0
        )

        strBuilder.clear()
        when (data.visibility) {
            in 0..999 -> strBuilder.append(data.visibility).append(" м")
            else -> strBuilder.append(String.format("%.2f", data.visibility / 1000.0)).append(" км")
        }
        tvVisibility.text = strBuilder

        strBuilder.clear().append(data.main.pressure).append(" гПа")
        tvPressure.text = strBuilder

        strBuilder.clear().append(data.wind.speed).append(" м/с")
        tvWindSpeed.text = strBuilder

        ll1.visibility = View.VISIBLE

        strBuilder.clear().append(data.clouds.all).append("%")
        tvCloudiness.text = strBuilder

        strBuilder.clear().append(data.main.humidity).append("%")
        tvHumidity.text = strBuilder

        strBuilder.clear()
        when (data.wind.deg) {
            in 0..22, in 338..360 -> strBuilder.append("С,")
            in 23..67 -> strBuilder.append("СВ, ")
            in 68..112 -> strBuilder.append("В, ")
            in 113..157 -> strBuilder.append("ЮВ, ")
            in 158..202 -> strBuilder.append("Ю, ")
            in 203..247 -> strBuilder.append("ЮЗ, ")
            in 248..292 -> strBuilder.append("З, ")
            in 293..337 -> strBuilder.append("СЗ, ")
        }
        strBuilder.append(data.wind.deg).append('\u00B0')
        tvWindDirection.text = strBuilder

        ll2.visibility = View.VISIBLE

    }

    override fun dataFailure(t: Throwable) {
        Log.e("DataFailure", t.message.toString())
    }

    override fun onDestroy() {
        weatherPresenter.detach()
        super.onDestroy()
    }
}

