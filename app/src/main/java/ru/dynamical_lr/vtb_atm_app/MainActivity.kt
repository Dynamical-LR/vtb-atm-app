package ru.dynamical_lr.vtb_atm_app

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.gson.GsonBuilder
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dynamical_lr.vtb_atm_app.adapters.CardsAdapter
import ru.dynamical_lr.vtb_atm_app.models.AtmModel
import ru.dynamical_lr.vtb_atm_app.models.CardInfo
import ru.dynamical_lr.vtb_atm_app.models.OfficeModel
import ru.dynamical_lr.vtb_atm_app.models.RouteNode
import ru.dynamical_lr.vtb_atm_app.network.api.AtmAPI
import ru.dynamical_lr.vtb_atm_app.requests.RouteRequest

class MainActivity : AppCompatActivity() {

    lateinit var mapView: MapView

    lateinit var bottomSheet: View

    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject
    private lateinit var atmChip: Chip
    private lateinit var officeChip: Chip
    private var atmChipEnable = true
    private lateinit var chipExpander: Chip
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var recyclerView: RecyclerView

    private lateinit var cardsList: MutableList<CardInfo>

    private lateinit var atms: List<AtmModel>
    private lateinit var offices: List<OfficeModel>
    private lateinit var routeNodes: List<RouteNode>

    private lateinit var adapter: CardsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("5f714a8d-5610-4b5f-81ff-73c3a7d1126d")
        MapKitFactory.initialize(this)

        cardsList = mutableListOf()

        setContentView(R.layout.activity_main)
        bottomSheet = findViewById(R.id.bottom_sheet_layout)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.peekHeight = 425
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        chipExpander = findViewById(R.id.chipExpander)

        atmChip = findViewById(R.id.atmChip)
        atmChip.text = "банкоматы"
        atmChip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        atmChip.setTextColor(getColorStateList(R.color.dark_vtb_blue))

        atmChip.setOnClickListener {
            if (!atmChipEnable) {
                atmChip.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                atmChip.setTextColor(getColorStateList(R.color.dark_vtb_blue))

                officeChip.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_vtb_blue))
                officeChip.setTextColor(getColorStateList(R.color.white))
                atmChipEnable = true
            } else {
                atmChip.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_vtb_blue))
                atmChip.setTextColor(getColorStateList(R.color.white))

                officeChip.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                officeChip.setTextColor(getColorStateList(R.color.dark_vtb_blue))
                atmChipEnable = false
            }
            performRequest()
        }
        officeChip = findViewById(R.id.officeChip)
        officeChip.text = "отделения"
        officeChip.setOnClickListener {
            if (!atmChipEnable) {
                atmChip.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                atmChip.setTextColor(getColorStateList(R.color.dark_vtb_blue))

                officeChip.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_vtb_blue))
                officeChip.setTextColor(getColorStateList(R.color.white))
                atmChipEnable = true
            } else {
                atmChip.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_vtb_blue))
                atmChip.setTextColor(getColorStateList(R.color.white))

                officeChip.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                officeChip.setTextColor(getColorStateList(R.color.dark_vtb_blue))
                atmChipEnable = false
            }
            performRequest()
        }
        chipExpander.setOnClickListener {
            when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    chipExpander.chipBackgroundColor =
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                    chipExpander.setTextColor(getColorStateList(R.color.dark_vtb_blue))
                }

                BottomSheetBehavior.STATE_EXPANDED -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    chipExpander.chipBackgroundColor =
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_vtb_blue))
                    chipExpander.setTextColor(getColorStateList(R.color.white))
                }
            }
        }

        recyclerView = findViewById(R.id.recycler_view_cards)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CardsAdapter(cardsList)
        recyclerView.adapter = adapter
        performRequest()

        mapView = findViewById(R.id.map_view)
        mapView.map.isNightModeEnabled = true;
        mapView.map.move(
            CameraPosition(Point(55.751408, 37.621152), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1.5f),
            null
        )

        mapObjectCollection = mapView.map.mapObjects
        performRequestForRoute()
    }

    private fun renderAtms() {
        val imageProvider: ImageProvider =
            ImageProvider.fromResource(this, R.drawable.marker_small)
        mapObjectCollection = mapView.map.mapObjects
        mapObjectCollection.clear()
        if (atmChipEnable) {
            for (atm in atms) {
                placemarkMapObject = mapObjectCollection.addPlacemark().apply {
                    geometry = Point(atm.latitude, atm.longitude)
                    setIcon(imageProvider)
                }
            }
        } else {
            for (office in offices) {
                placemarkMapObject = mapObjectCollection.addPlacemark().apply {
                    geometry = Point(office.latitude, office.longitude)
                    setIcon(imageProvider)
                }
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

    private fun createCardsList() {
        var cards = mutableListOf<CardInfo>()
        if (atmChipEnable) {
            for (atm in atms) {
                cards.add(CardInfo("Банкомат ВТБ", "банкомат", atm.address))
            }

        } else {
            for (office in offices) {
                cards.add(CardInfo("Отделение ВТБ", "отделение", office.address))
            }
        }
        cardsList.clear()
        cardsList.addAll(cards)
        Log.i("1234", "${cardsList.size}")
        adapter.notifyDataSetChanged()
    }

    private fun renderRoute() {
        mapObjectCollection = mapView.map.mapObjects
        for (routeNode in routeNodes) {
            val polyline = mapObjectCollection.addPolyline(
                Polyline(
                    listOf(
                        Point(routeNode.fromLatitude, routeNode.fromLongitude),
                        Point(routeNode.toLatitude, routeNode.toLongitude)
                    )
                )
            )
            polyline.setStrokeColor(Color.argb(255, 255, 0, 0))
            polyline.strokeWidth = 6.0f
        }
    }

    private fun performRequestForRoute() {
        val url = "https://5c75-79-111-21-157.ngrok-free.app/";
        val api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AtmAPI::class.java)
        api.getRoute(RouteRequest(latitude =  55.7743705, longitude = 37.8428135))
            .enqueue(object : Callback<List<RouteNode>> {
                override fun onResponse(
                    call: Call<List<RouteNode>>,
                    response: Response<List<RouteNode>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            routeNodes = it
                            renderRoute()
                        }
                    }
                }

                override fun onFailure(call: Call<List<RouteNode>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun performRequest() {
        val url = "https://5c75-79-111-21-157.ngrok-free.app/";
        val api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AtmAPI::class.java)
        if (atmChipEnable) {
            api.getAtms().enqueue(object : Callback<List<AtmModel>> {
                override fun onResponse(
                    call: Call<List<AtmModel>>,
                    response: Response<List<AtmModel>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            atms = it
                            createCardsList()
                            renderAtms()
                        }
                    }
                }

                override fun onFailure(call: Call<List<AtmModel>>, t: Throwable) {
                    Log.i("123", "Failed!")
                }
            })
        } else {
            api.getOffices().enqueue(object : Callback<List<OfficeModel>> {
                override fun onResponse(
                    call: Call<List<OfficeModel>>,
                    response: Response<List<OfficeModel>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            offices = it
                            createCardsList()
                            renderAtms()
                        }
                    }
                }

                override fun onFailure(call: Call<List<OfficeModel>>, t: Throwable) {
                    Log.i("123", "Failed!")
                }
            })
        }

    }
}