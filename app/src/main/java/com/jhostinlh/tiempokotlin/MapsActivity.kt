package com.jhostinlh.tiempokotlin

import Dia
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.*
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import retrofit2.Callback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.*
import com.jhostinlh.tiempokotlin.Retrofit.MyApiAdapter
import com.jhostinlh.tiempokotlin.Retrofit.MyApiService
import com.jhostinlh.tiempokotlin.databinding.ActivityMapsBinding
import com.jhostinlh.tiempokotlin.location.levantarGPS.LocationUtils
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.lang.IllegalStateException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,LocationListener,GoogleMap.OnMyLocationButtonClickListener,GoogleMap.OnMyLocationClickListener,GoogleMap.OnMapClickListener


{
    // VAriables globales
    private lateinit var ubicacion: Location
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    protected var temperaturaDias: Dia? = null
    protected lateinit var myIntent: Intent

    //Oncreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        myIntent = Intent(this@MapsActivity,DetalleTiempo::class.java)

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val taskLocation = fusedLocationClient.lastLocation
        taskLocation.addOnFailureListener(object :OnFailureListener{
            override fun onFailure(p0: java.lang.Exception) {
                Log.i("tasklocation", "Fallo en conexion de fusedLocatioClient")

            }

        })
        taskLocation.addOnSuccessListener(object : OnSuccessListener<Location>{
            override fun onSuccess(p0: Location?) {
                Log.i("tasklocation", "tasklocation con exito!")
            }

        })

        try {
            ubicacion = taskLocation.result

        }catch (e: IllegalStateException){
            Toast.makeText(this,"Porfavor espera cargano ubicacion!",Toast.LENGTH_LONG).show()

        }

        mMap = googleMap
        with(googleMap) {

            // We will provide our own zoom controls.
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isMyLocationButtonEnabled = true

            // Show Sydney
            //moveCamera(CameraUpdateFactory.newLatLngZoom(sydneyLatLng, 10f))
        }
        mMap.setOnMapClickListener(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true

        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        checkPermisos()

        // Añado dialogos de informacion a la ubocacion
        /*
        val melbourne = mMap.addMarker(
            MarkerOptions()
                .position(coord)
                .title("Melbourne")
                .snippet("Population: 4,137,400")
        )

         */

    }

    fun habilitaUbicacion(googleMap: GoogleMap){
        val mMap = googleMap
        val locationRequest: LocationRequest = LocationRequest.create()

        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)




        val settingsClient: SettingsClient = LocationServices.getSettingsClient(this)
        val locationSettingsRequest: LocationSettingsRequest?


        locationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)



        settingsClient
            .checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener(this as Activity) {
                // GPS enabled already
                val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val locationRequest: LocationRequest = LocationRequest()
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this,"locationManager no funciona",Toast.LENGTH_LONG).show()

                }else{


                }


            }
            .addOnFailureListener(this@MapsActivity) { e ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        try {
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(this@MapsActivity,
                                LocationUtils.PRIORITY_HIGH_ACCURACY_REQ
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.i("prueba", "PendingIntent unable to execute request.")
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Log.e("prueba", "Enable location services from settings.")
                    }
                }
            }

    }
    fun checkPermisos() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        cargaUbicacion()


    }
    @SuppressLint("MissingPermission")
    fun cargaUbicacion(){
        val locationTask : Task<Location> = fusedLocationClient.lastLocation

        locationTask.addOnSuccessListener(object :OnSuccessListener<Location>{
            override fun onSuccess(location: Location?) {
                if (location != null) {
                    val coord = LatLng(location.latitude,location.longitude)
                    val cameraPosition = CameraPosition.builder()
                        .target(coord)
                        .zoom(3.0f)
                        .bearing(0.0f)
                        .tilt(25f).build()
                    mMap.addMarker(MarkerOptions().position(coord).title(""))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(coord))
                }else{
                    Toast.makeText(this@MapsActivity,"getLastLocation es null!!",Toast.LENGTH_LONG).show()
                }
            }

        })
        locationTask.addOnFailureListener(object :OnFailureListener{
            override fun onFailure(p0: Exception) {
                Log.i("fallo",p0.localizedMessage)
            }

        })
    }

    override fun onLocationChanged(location: Location) {
        Toast.makeText(this@MapsActivity,"Cambios en tu localizacion",Toast.LENGTH_LONG).show()
        Log.i("listener","Cambios en tu localizacion")
    }

    override fun onProviderEnabled(provider: String) {
        Toast.makeText(this@MapsActivity,"GPS activado",Toast.LENGTH_LONG).show()
        Log.i("listener","activo")
    }

    override fun onProviderDisabled(provider: String) {
        Toast.makeText(this@MapsActivity,"GPS inactivo",Toast.LENGTH_LONG).show()
        Log.i("listener","Inactivo")
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "mi actual ubicacion", Toast.LENGTH_SHORT).show()
    }

    override fun onMapClick(location: LatLng) {
        val geocoder = Geocoder(this)
        var pais:MutableList<Address> = ArrayList()
        try {
            pais = geocoder.getFromLocation(location.latitude,location.longitude,2)
        }catch (e: IOException){
            Log.i("geocoderOnclickMap","puede que tengas Error en la conexion")
        }

        val cameraPosition: CameraPosition = CameraPosition.Builder().
        target(location)
            .zoom(3.0f)
            .bearing(0.0f)
            .tilt(25.0f)
            .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        mMap.clear()


        val myApiService: MyApiService = MyApiAdapter.getApiService()!!
        val call = myApiService?.getPrediccionDias(location.latitude.toString(),location.longitude.toString(),"minutely,alerts","metric","es", API_KEY)
        if (call != null){
            call.enqueue(object: Callback<Dia>{

                override fun onResponse(call: Call<Dia>, response: Response<Dia>) {
                    if (!response.isSuccessful){
                        Log.i("responseMaps","ressponse Dias sin exito!")
                        return
                    }
                    val drawable = getDrawable(R.drawable.frio)
                    val bitmap = BitmapFactory.decodeResource(resources,R.drawable.frio)
                    val reducido = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                    temperaturaDias = response.body()
                    mMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(""+pais[0].countryName+pais[1].locality)
                            .snippet("Temperatura Actual: "+Math.round(temperaturaDias?.daily?.get(0)?.temp!!.day)+" ºC")
                            .icon(BitmapDescriptorFactory.fromBitmap(reducido))
                            .visible(true)
                    )
                    mMap.setOnInfoWindowClickListener { marker ->
                        Toast.makeText(this@MapsActivity,"Funcionaaa!!!",Toast.LENGTH_LONG).show()
                        val loc = Location(LocationManager.GPS_PROVIDER)
                        loc.latitude = marker.position.latitude
                        loc.longitude = marker.position.longitude
                        peticionApiService(loc)
                    }
                }


                override fun onFailure(call: Call<Dia>, t: Throwable) {
                    Log.i("responseMaps","Fallo en la conexion")

                }

            }

        )}else{
            mMap.addMarker(MarkerOptions().position(location))
        }
        mMap.moveCamera(cameraUpdate)

    }
    private fun peticionApiService(location: Location) {
        val myApiService = MyApiAdapter.getApiService()
        val call = myApiService?.getPrediccionDias(location.latitude.toString(),
            location.longitude.toString(),"current,minutely,alerts","metric","es", API_KEY)
        if (call != null) {
            call.enqueue(object : Callback<Dia>{
                override fun onResponse(call: Call<Dia>, response: Response<Dia>) {
                    if (!response.isSuccessful){
                        Log.i("responseretrofit","Respuesta retrofit sin exito")
                        return
                    }
                    val respuesta: Dia = response.body()!!
                    Log.i("apiService",respuesta.toString())
                    myIntent.putExtra(CIUDAD,respuesta)
                    startActivity(myIntent)


                }

                override fun onFailure(call: Call<Dia>, t: Throwable) {
                    Log.i("responseretrofit","Respuesta Fállida")
                }

            })
        }
    }


}