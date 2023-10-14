package ru.dynamical_lr.vtb_atm_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.GsonBuilder
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dynamical_lr.vtb_atm_app.models.AtmModel
import ru.dynamical_lr.vtb_atm_app.network.api.AtmAPI

class MainActivity : AppCompatActivity() {

    lateinit var mapView: MapView

    lateinit var bottomSheet: View

    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject

    private lateinit var atms: List<AtmModel>
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

        performRequest()

        mapView = findViewById(R.id.map_view)
        mapView.map.isNightModeEnabled = true;
        mapView.map.move(CameraPosition(Point(55.751408, 37.621152), 11.0f, 0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 1.5f ), null)

    }

    private fun renderAtms() {
        val imageProvider: ImageProvider = ImageProvider.fromResource(this, R.drawable.marker_small);
        for (atm in atms) {
            mapObjectCollection = mapView.map.mapObjects
            placemarkMapObject = mapObjectCollection.addPlacemark().apply {
                geometry = Point(atm.latitude, atm.longitude)
                setIcon(imageProvider)
            }

        }

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

    private fun performRequest() {
        val url = "https://5c75-79-111-21-157.ngrok-free.app/";
        val api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AtmAPI::class.java)

        api.getAtms().enqueue(object: Callback<List<AtmModel>> {
            override fun onResponse(
                call: Call<List<AtmModel>>,
                response: Response<List<AtmModel>>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        atms = it
                        renderAtms()
                    }
                }
            }

            override fun onFailure(call: Call<List<AtmModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}