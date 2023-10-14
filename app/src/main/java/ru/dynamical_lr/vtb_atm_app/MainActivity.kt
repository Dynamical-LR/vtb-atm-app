package ru.dynamical_lr.vtb_atm_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class MainActivity : AppCompatActivity() {

    lateinit var mapView: MapView

    lateinit var bottomSheet: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("5f714a8d-5610-4b5f-81ff-73c3a7d1126d")
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_main)

        bottomSheet = findViewById(R.id.bottom_sheet_layout);

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = 450
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        mapView = findViewById(R.id.map_view)
        mapView.map.isNightModeEnabled = true;
        mapView.map.move(CameraPosition(Point(55.751408, 37.621152), 11.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 1.5f ), null)

    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    fun performRequest() {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://curl https://5c75-79-111-21-157.ngrok-free.app/api/v1/atms?page=1&size=5")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("failed request")
                    }

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                }
            }
        })
    }
}